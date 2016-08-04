/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.demo.smartwatch.component.Bubble.DatePosition;
import ej.demo.smartwatch.utils.Constants;
import ej.microui.display.GraphicsContext;
import ej.motion.linear.LinearMotion;
import ej.mwt.Widget;

/**
 * Watch's controller.
 */
public class WatchController implements Animation {

	private boolean isAnimated = false;

	/**
	 * Available actions.
	 *
	 */
	private enum Action {
		DoNothing, SwitchFace, Transition
	}

	/**
	 * Motion for the bubble transition.
	 */
	private class BubbleMotion extends LinearMotion {
		/**
		 * Current bubble.
		 */
		private Bubble bubble;

		/**
		 * Whether it is counting from start to stop or the contrary.
		 */
		private boolean countUp;

		/**
		 * Creates a motion for the bubble.
		 *
		 * @param start
		 *            The initial value.
		 * @param stop
		 *            The end value.
		 * @param duration
		 *            The duration.
		 */
		private BubbleMotion(int start, int stop, long duration) {
			super(start, stop, duration);
			this.countUp = true;
			this.bubble = null;
		}

		/**
		 * Set whether it is counting from start to stop or the contrary.
		 *
		 * @param countUp
		 *            whether it is counting from start to stop or the contrary.
		 */
		public void setCountDirection(boolean countUp) {
			this.countUp = countUp;
		}

		/**
		 * Get whether it is counting from start to stop or the contrary.
		 *
		 * @return true if it is counting from start to stop.
		 */
		public boolean getCountDirection() {
			return this.countUp;
		}

		/**
		 * Set the target bubble.
		 *
		 * @param bubble
		 *            The target bubble.
		 */
		public void setTargetBubble(Bubble bubble) {
			this.bubble = bubble;
		}

		@Override
		public int getStartValue() {
			return (this.countUp) ? super.getStartValue() : super.getStopValue();
		}

		@Override
		public int getStopValue() {
			return (this.countUp) ? super.getStopValue() : super.getStartValue();
		}

		@Override
		public int getCurrentValue() {
			int value = super.getCurrentValue();
			return (this.countUp) ? value : super.getStopValue() - value;
		}

		@Override
		public int getValue(long elapsed) {
			return (this.countUp) ? super.getValue(elapsed) : super.getValue(getDuration() - elapsed);
		}
	}

	/**
	 * Contrains all the bubbles.
	 */
	private final BubbleWidget[] bubbles;
	private Bubble activeBubble = null;
	private Bubble mainBubble = null;

	private boolean resetOnNext = false;
	private boolean freezeInput = false;
	private final Object mutex = new Object();

	private Action currentAction = Action.DoNothing;
	private DatePosition currentDatePosition = DatePosition.OUTSIDE;
	private DatePosition nextDatePosition = DatePosition.OUTSIDE;

	private SmartWatch watch = null;

	/**
	 * Current transition stage, initialized at "finished".
	 */
	private int transistionStage = Constants.TRANSITION_HIGH;

	private final BubbleMotion motion;

	/**
	 * Creates a watch controller.
	 *
	 * @param watch
	 *            Watch canvas.
	 */
	public WatchController(SmartWatch watch) {
		this.bubbles = new BubbleWidget[ScreenArea.values().length];
		this.watch = watch;

		this.motion = new BubbleMotion(Constants.TRANSITION_LOW, Constants.TRANSITION_HIGH, Constants.DURATION);
	}

	/**
	 * Add a bubble.
	 *
	 * @param bubble
	 *            The bubble to be added.
	 */
	public void add(BubbleWidget bubble) {
		ScreenArea area = bubble.getOriginalPosition();

		// Order the bubbles for the rendering order.
		this.bubbles[area.get()] = bubble;

		if (this.mainBubble == null) {
			this.mainBubble = bubble;
			this.currentDatePosition = this.mainBubble.getDatePosition();
			this.nextDatePosition = this.currentDatePosition;
		}

		if (this.activeBubble == null) {
			this.activeBubble = bubble;
		}
	}

	/**
	 * Handle a touch event.
	 *
	 * @param x
	 *            horizontal coordinate
	 * @param y
	 *            vertical coordinate
	 */
	public void input(int x, int y) {
		synchronized (this.mutex) {
			// If a transition is ongoing.
			if (this.freezeInput) {
				return;
			}
			this.freezeInput = true;
		}

		if (this.activeBubble.inRange(x, y)) {
			synchronized (this.mutex) {
				this.freezeInput = false;
			}
			return;
		}

		boolean found = false;
		// Find if it is in range of a corner bubble.
		for (final Bubble bubble : this.bubbles) {
			if (bubble != null && bubble.inRange(x, y)) {
				transitionTo(bubble);
				this.nextDatePosition = bubble.getDatePosition();
				found = true;
				break;
			}
		}
		if (!found) {
			synchronized (this.mutex) {
				this.freezeInput = false;
			}
		}
	}

	/**
	 * Handle a swipe event.
	 *
	 * @param x1
	 *            coordinate
	 * @param y1
	 *            coordinate
	 * @param x2
	 *            coordinate
	 * @param y2
	 *            coordinate
	 */
	public void swipe(final int x1, final int y1, final int x2, final int y2) {
		synchronized (this.mutex) {
			if (this.freezeInput) {
				return;
			}
			this.freezeInput = true;
		}

		int xRange = x2 - x1;
		int yRange = y2 - y1;

		xRange = (xRange < 0) ? -xRange : xRange;
		yRange = (yRange < 0) ? -yRange : yRange;

		if (xRange > Constants.MINIMUM_SWIPE || yRange > Constants.MINIMUM_SWIPE) {
			if (yRange > xRange) {
				// vertical swipe
				switchFace((y2 - y1) > 0);
			} else {
				// horizontal swipe
				switchView((x2 - x1) > 0);
			}
		} else {
			synchronized (this.mutex) {
				this.freezeInput = false;
			}
			input(x2, y2);
		}
	}

	/**
	 * Position and redraw the bubbles during a transition.
	 *
	 * @param g
	 *            The graphic context to use. graphics context
	 */
	public void realign(GraphicsContext g) {
		// Draw the date.
		if (this.motion.getCountDirection()) {
			this.activeBubble.drawDate(g, this.currentDatePosition, this.nextDatePosition, this.transistionStage);
		} else {
			this.activeBubble.drawDate(g, this.nextDatePosition, this.currentDatePosition, this.transistionStage);
		}

		// Draw the bubbles.
		if (this.currentAction == Action.SwitchFace) {
			switchFace(g);
		} else {
			redraw(g);
		}

		if (this.resetOnNext) {
			// Reset
			this.currentDatePosition = this.nextDatePosition;
			this.currentAction = Action.DoNothing;
			this.resetOnNext = false;
			synchronized (this.mutex) {
				this.freezeInput = false;
			}
			if (this.motion.bubble != null) {
				this.activeBubble = this.motion.bubble;
				this.activeBubble.startAnimation();
			}
			this.watch.startAnimation();
		}
	}

	/**
	 * Redraw all the bubbles.
	 *
	 * @param g
	 *            The graphic context to use.
	 */
	private void redraw(GraphicsContext g) {
		for (Bubble bubble : this.bubbles) {
			if (bubble != null) {
				bubble.redraw(g, this.transistionStage);
			}
		}
	}

	/**
	 * Starts a switch face animation.
	 *
	 * @param up
	 *            is going up.
	 */
	private void switchFace(boolean up) {
		this.currentAction = Action.SwitchFace;

		this.activeBubble.startSwitchFace(up);
		this.nextDatePosition = this.activeBubble.getDatePosition();
		if (!this.activeBubble.isSwitchAnimated()) {
			// notify active bubble of the swipe.
			this.activeBubble.stopSwitchFace();
			this.resetOnNext = true;
			return;
		}

		this.motion.setCountDirection(!up);
		this.motion.setTargetBubble(null);
		startAnimation();
	}

	/**
	 * Redraw the watches during a face switch. Retreat the corner bubbles and
	 * swipe the switch the active.
	 *
	 * @param g
	 *            The graphic context to use.
	 */
	private void switchFace(GraphicsContext g) {
		for (Bubble bubble : this.bubbles) {
			if (bubble != null) {
				if (bubble == this.activeBubble) {
					bubble.switchFace(g, this.transistionStage);
				} else {
					bubble.retreat(g, this.transistionStage);
				}
			}
		}
	}

	/**
	 * Switch the view (horizontal swipe).
	 *
	 * @param right
	 */
	private void switchView(boolean right) {
		this.activeBubble.switchView(right);
		synchronized (this.mutex) {
			this.freezeInput = false;
		}
	}

	/**
	 * Start the transition to a bubble.
	 *
	 * @param targetBubble
	 */
	private void transitionTo(Bubble targetBubble) {
		if (targetBubble == this.activeBubble) {
			synchronized (this.mutex) {
				this.freezeInput = false;
			}
			return;
		}

		// the main bubble takes the place of the target one.
		this.mainBubble.setTargetPosition(targetBubble.getOriginalPosition());
		if (this.mainBubble != this.activeBubble) {
			// Active bubble goes back to its original position.
			this.activeBubble.setTargetPosition(this.activeBubble.getOriginalPosition());
		}

		targetBubble.setTargetPosition(ScreenArea.Center);

		this.currentAction = Action.Transition;

		this.motion.setCountDirection(true);
		this.motion.setTargetBubble(targetBubble);
		startAnimation();
	}

	/**
	 * Set the bounds of the bubbles.
	 *
	 * @param x
	 *            position.
	 * @param y
	 *            position.
	 * @param width
	 *            bound's width.
	 * @param height
	 *            bound's height
	 */
	public void setBounds(int x, int y, int width, int height) {
		for (final BubbleWidget bubble : this.bubbles) {
			if (bubble != null) {
				bubble.setBounds(x, y, width, height);
			}
		}
	}

	/**
	 * Validate all the bubbles.
	 *
	 * @param widthHint
	 *            the width hint.
	 * @param heightHint
	 *            the height hint.
	 */
	public void validate(int widthHint, int heightHint) {
		for (Widget widget : this.bubbles) {
			if (widget != null) {
				widget.validate(widthHint, heightHint);
			}
		}
	}

	@Override
	public boolean tick(long currentTimeMillis) {
		this.transistionStage = this.motion.getCurrentValue();
		if (this.transistionStage == this.motion.getStopValue() || !this.isAnimated) {
			stopAnimation(this.transistionStage);
			return false;
		}
		return true;
	}

	/**
	 * Start the animation.
	 */
	private void startAnimation() {
		if (!this.isAnimated) {
			this.watch.stopAnimation();
			if (this.activeBubble != null) {
				this.activeBubble.stopAnimation();
			}
			this.isAnimated = true;
			this.transistionStage = this.motion.getStartValue();
			Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
			animator.startAnimation(this);
			this.motion.start();
		}
	}

	/**
	 * Stop the animation.
	 *
	 * @param stage
	 *            transition stage.
	 */
	private void stopAnimation(int stage) {
		if (this.isAnimated) {
			if (this.currentAction == Action.SwitchFace) {
				this.activeBubble.stopSwitchFace();
			}
			this.isAnimated = false;
			this.resetOnNext = true;
			// System.out.println("BubbleController.stopAnimation() stage" +
			// stage);

			this.transistionStage = stage;

		}
	}

	/**
	 * Get the active bubbles.
	 *
	 * @return the active bubble.
	 */
	public Bubble getActiveBubble() {
		return this.activeBubble;
	}
}
