/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smartwatch;

import com.microej.demo.smartwatch.component.Bubble;
import com.microej.demo.smartwatch.component.SmartWatch;
import com.microej.demo.smartwatch.component.WatchController;
import com.microej.demo.smartwatch.component.widget.BatteryWidget;
import com.microej.demo.smartwatch.component.widget.DateTimeWidget;
import com.microej.demo.smartwatch.component.widget.DistanceWidget;
import com.microej.demo.smartwatch.component.widget.NotificationsWidget;
import com.microej.demo.smartwatch.model.DataProvider;
import com.microej.demo.smartwatch.utils.Constants;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.microui.event.EventGenerator;
import ej.microui.event.generator.Pointer;
import ej.microui.util.EventHandler;

/**
 *  A robot to automatically browse through a watch
 */
public class SmartWatchRobot implements EventHandler {

	/**
	 * Delay before the robot starts.
	 */
	private static final int DELAY = 10_000;

	/**
	 * Delay to wait for a short action to happen.
	 */
	private static final long SHORT_ACTION_DELAY = 1_000;

	/**
	 * Delay to wait for a long action to happen.
	 */
	private static final long LONG_ACTION_DELAY = 3_000;

	/**
	 * Delay to wait in between bubble switch.
	 */
	private static final long BUBBLE_DELAY = LONG_ACTION_DELAY;

	public static void initialize() {
		RobotTask.initialize();
	}

	/**
	 * Base pointer event handler.
	 */
	private EventHandler pointerEventHandler;

	/**
	 * Main task.
	 */
	private RobotTask task;

	/**
	 * The watch's controller.
	 */
	private final WatchController controller;

	/**
	 * Timer (separate to avoid conflict).
	 */
	private final Timer timer;

	/**
	 * SmartWatchRobot constructor
	 *
	 * @param watch
	 *            the watch.
	 */
	public SmartWatchRobot(SmartWatch watch) {
		this.timer = new Timer();

		// Name the timer thread.
		this.timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Thread currentThread = Thread.currentThread();
				currentThread.setName("Robot Timer"); //$NON-NLS-1$
				// currentThread.setPriority(3);
			}
		}, 0);
		this.controller = watch.getController();
	}

	/**
	 * Start the robot.
	 */
	public void start() {
		// Stop the robot if it has already been started.
		if (this.pointerEventHandler != null) {
			stop();
		}
		Pointer pointer = EventGenerator.get(Pointer.class, 0);
		this.pointerEventHandler = pointer.getEventHandler();
		pointer.setEventHandler(this);
		resetTimer();
	}

	/**
	 * Stop the robot.
	 */
	public void stop() {
		if (this.task != null) {
			this.task.cancel();
		}

		Pointer pointer = EventGenerator.get(Pointer.class, 0);
		pointer.setEventHandler(this.pointerEventHandler);
		this.pointerEventHandler = null;
	}

	@Override
	public boolean handleEvent(int event) {
		resetTimer();
		return this.pointerEventHandler.handleEvent(event);
	}

	/**
	 * Restart the timer.
	 */
	public void resetTimer() {
		if (this.task != null) {
			this.task.cancel();
		}
		this.task = new RobotTask(this.timer, this.controller);
		this.timer.schedule(this.task, DELAY);
	}

	/**
	 * Task to browse through the watch.
	 */
	private static class RobotTask extends TimerTask {
		/**
		 * Offset for the inputs (to avoid corner cases).
		 */
		private static int OFFSET = 10;

		/**
		 * Y position of the top.
		 */
		private static int TOP = OFFSET;

		/**
		 * Y position of the BOTTOM.
		 */
		private static int BOTTOM;

		/**
		 * X position of the left.
		 */
		private static int LEFT = OFFSET;

		/**
		 * X position of the right.
		 */
		private static int RIGHT;

		/**
		 * X position of the center.
		 */
		private static int HCENTER;

		/**
		 * Y position of the center.
		 */
		private static int VCENTER;

		public static void initialize() {
			BOTTOM = Constants.DISPLAY_HEIGHT - OFFSET;
			RIGHT = Constants.DISPLAY_WIDTH - OFFSET;
			HCENTER = Constants.DISPLAY_WIDTH / 2;
			VCENTER = Constants.DISPLAY_HEIGHT / 2;
		}

		private long currentActionDelay = LONG_ACTION_DELAY;

		/**
		 * List of the available bubbles.
		 *
		 */
		private enum BubbleKind {
			DATE, BATTERY, DISTANCE, NOTIFICATION, WEATHER
		}

		private BubbleKind state = BubbleKind.DATE;
		private int currentFace = 0;
		private int currentAction = 0;

		private final WatchController controller;
		private TimerTask tick;
		private final Timer timer;

		/**
		 *
		 * A robot task.
		 *
		 * @param timer
		 *            timer to use for scheduling tasks.
		 * @param controller
		 *            The watch's controller.
		 */
		public RobotTask(Timer timer, WatchController controller) {
			this.timer = timer;
			this.controller = controller;
		}

		@Override
		public void run() {
			scheduleNextTick(0);
		}

		/**
		 * Do the action and schedule next task.
		 */
		public void tick() {
			if (action()) {
				// Has more action.
				scheduleNextTick(this.currentActionDelay);
			} else {
				// No more action for this bubble.
				switchBubble();
				scheduleNextTick(BUBBLE_DELAY);
			}
		}

		/**
		 * Schedule the next tick.
		 *
		 * @param delay
		 */
		private void scheduleNextTick(long delay) {
			this.tick = new TimerTask() {
				@Override
				public void run() {
					updateState();
					tick();

				}

			};
			this.timer.schedule(this.tick, delay);
		}

		/**
		 * Looks at the current watch's state, set the delay.
		 */
		private void updateState() {
			Bubble activeBubble = this.controller.getActiveBubble();
			if (activeBubble instanceof BatteryWidget) {
				this.currentActionDelay = SHORT_ACTION_DELAY;
				this.state = BubbleKind.BATTERY;
			} else if (activeBubble instanceof DateTimeWidget) {
				this.currentActionDelay = LONG_ACTION_DELAY;
				this.state = BubbleKind.DATE;
			} else if (activeBubble instanceof DistanceWidget) {
				this.currentActionDelay = SHORT_ACTION_DELAY;
				this.state = BubbleKind.DISTANCE;
			} else if (activeBubble instanceof NotificationsWidget) {
				this.state = BubbleKind.NOTIFICATION;
			} else { // if (activeBubble instanceof WeatherWidget) {
				this.currentActionDelay = LONG_ACTION_DELAY;
				this.state = BubbleKind.WEATHER;
			}
		}

		/**
		 * Do the current action.
		 *
		 * @return whether more actions are available.
		 */
		private boolean action() {
			switch (this.state) {
			case BATTERY:
				// Nothing to do.
				return false;
			case DATE:
				// If it is the last face.
				if (this.currentFace >= this.controller.getActiveBubble().countFaces()) {
					this.currentFace = 0;
					return false;
				}
				// Swipe up to the next face.
				this.controller.swipe(HCENTER, BOTTOM - 30, HCENTER, TOP + 30);
				this.currentFace++;
				return true;
			case WEATHER:
				// If more weather forecasts are available.
				if (DataProvider.getInstance().getForecastCount() > this.currentAction) {
					this.currentAction++;
					// Swipe left for next forecast.
					this.controller.swipe(RIGHT, VCENTER, LEFT, VCENTER);
					return true;
				}
				return false;
			case NOTIFICATION:
				int eventsCount = DataProvider.getInstance().getEventsCount();
				// Doesn't remove all the notifications (only one out of two).
				if (eventsCount > this.currentAction) {
					this.currentAction++;
					if (this.currentAction % 2 == 1) {
						// Switch down to remove notification
						this.controller.swipe(HCENTER, TOP, HCENTER, BOTTOM);
						this.currentActionDelay = SHORT_ACTION_DELAY;
					} else {
						// Switch left for next notification.
						this.controller.swipe(RIGHT, VCENTER, LEFT, VCENTER);
						this.currentActionDelay = LONG_ACTION_DELAY;
					}
					return true;
				}
				return false;
			case DISTANCE:
			default:
				// Nothing to do.
				return false;
			}
		}

		/**
		 * Switch to the next bubble.
		 */
		private void switchBubble() {
			this.currentAction = 0;
			switch (this.state) {
			case BATTERY:
				// Go to weather
				this.controller.input(LEFT, BOTTOM);
				break;
			case DATE:
				// Go to battery
				this.controller.input(RIGHT, TOP);
				break;
			case DISTANCE:
				// Go to notification
				this.controller.input(LEFT, TOP);
				break;
			case NOTIFICATION:
				// Go to Date
				this.controller.input(LEFT, TOP);
				break;
			case WEATHER:
				// Go to distance
				this.controller.input(RIGHT, BOTTOM);
				break;
			}
		}

		@Override
		public boolean cancel() {
			if (this.tick != null) {
				this.tick.cancel();
			}
			return super.cancel();
		}
	}
}
