/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component.widget;

import java.io.IOException;

import ej.demo.smartwatch.component.Direction;
import ej.demo.smartwatch.component.ScreenArea;
import ej.demo.smartwatch.dal.ISmDataProvider;
import ej.demo.smartwatch.dal.ISmDataProvider.Event;
import ej.demo.smartwatch.dal.SmDataPrivider;
import ej.demo.smartwatch.style.Images;
import ej.demo.smartwatch.utils.Constants;
import ej.demo.smartwatch.utils.Log;
import ej.demo.smartwatch.utils.Utils;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.style.text.TextHelper;

/**
 * Widget displaying notifications.
 */
public class NotificationsWidget extends MultipleViewWidget {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * Message display when no notification available.
	 */
	private static final String NONE = "None."; //$NON-NLS-1$

	/**
	 * Proportion available to display the message.
	 */
	private static final float MESSAGE_SCREEN_X_PORTION = 0.75f;

	/**
	 * Proportion available to display the message.
	 */
	private static final float MESSAGE_SCREEN_Y_PORTION = 0.4f;

	/**
	 * Data provider.
	 */
	private static final ISmDataProvider PROVIDER = SmDataPrivider.get();

	private static final String TAG = "NotificationsWidget"; //$NON-NLS-1$

	/**
	 * The ratio for width and height in only computed at runtime, after the
	 * static variable had been init. This boolean is used to know if the had
	 * been init.
	 */
	private static boolean STATIC_VARIABLE_INITIALIZED = false;

	/**
	 * Font for the message.
	 */
	private static Font FONT_MESSAGE;

	/**
	 * Font for the count on the edge.
	 */
	private static Font FONT_NOTIFICATION_COUNT;

	/**
	 * Number of lines to display.
	 */
	private static int MESSAGE_SCREEN_Y_LINES;

	/**
	 * Bell image.
	 */
	private Image imgIcon;

	/**
	 * Current message lines.
	 */
	private int messageLinesToDisplay = 0;

	/**
	 * Message split in lines.
	 */
	private String[] message = { EMPTY_STRING };

	/**
	 * Date string.
	 */
	private String date = EMPTY_STRING;

	/**
	 * Time string.
	 */
	private String time = EMPTY_STRING;

	/**
	 * Current event.
	 */
	private Event event;

	/**
	 * Widget displaying notification.
	 *
	 * @param width
	 *            the width.
	 * @param height
	 *            the height.
	 * @param position
	 *            The initial position.
	 */
	public NotificationsWidget(int width, int height, ScreenArea position) {
		super(width, height, position);
		if (!STATIC_VARIABLE_INITIALIZED) {
			FONT_MESSAGE = Constants.FONT_36;
			FONT_NOTIFICATION_COUNT = Constants.FONT_24;
			MESSAGE_SCREEN_Y_LINES = (int) ((Constants.WIDTH * MESSAGE_SCREEN_Y_PORTION) / FONT_MESSAGE.getHeight());
			STATIC_VARIABLE_INITIALIZED = true;
		}
		try {
			this.imgIcon = Image.createImage(Images.BELL);
		} catch (IOException e) {
			Log.e(TAG, e);
		}

		setupMessage(true);
	}

	/**
	 * Draw the notification.
	 *
	 * @param g
	 *            Graphic context.
	 * @param ratio
	 *            Transition ratio.
	 */
	private void drawText(GraphicsContext g, float ratio) {
		int y = (getHeight() - FONT_MESSAGE.getHeight()) / 2;
		// No message available.
		if (this.messageLinesToDisplay == 0) {
			// Load the message. Return true if a new message available.
			if (!setupMessage(true)) {
				g.setFont(FONT_MESSAGE);
				String str = NONE;
				int stringWidth = FONT_MESSAGE.stringWidth(str);
				int x = (getWidth() - stringWidth) / 2;
				if (this.direction == Direction.ToEdge) {
					x = Utils.computeMean(-stringWidth, x, ratio);
				} else if (this.direction != Direction.CenterStill) {
					x = Utils.computeMean(x, getWidth(), ratio);
				}
				g.drawString(str, x, y, 0);
				return;
			}
		}

		// Draw the notification count.
		g.setFont(FONT_NOTIFICATION_COUNT);
		int yCount = y - FONT_NOTIFICATION_COUNT.getHeight();
		int xCount = (getWidth() - FONT_NOTIFICATION_COUNT.stringWidth(this.time)) / 2;
		if (this.direction == Direction.ToEdge) {
			int xEndNotification = -FONT_NOTIFICATION_COUNT.stringWidth(this.time);
			xCount = Utils.computeMean(xEndNotification, xCount, ratio);
		} else if (this.direction != Direction.CenterStill) {
			int xEndNotification = -FONT_NOTIFICATION_COUNT.stringWidth(this.time);
			xCount = Utils.computeMean(xCount, xEndNotification, ratio);
		}
		g.drawString(this.time, xCount, yCount, 0);

		// Draw the message
		g.setFont(FONT_MESSAGE);
		int lineId = 0;
		for (String line : this.message) {
			if (lineId++ >= this.messageLinesToDisplay) {
				// Truncate message.
				break;
			}
			int x = (getWidth() - FONT_MESSAGE.stringWidth(line)) / 2;

			if (this.direction == Direction.ToEdge) {
				int xEndMessage = -FONT_MESSAGE.stringWidth(line);
				x = Utils.computeMean(xEndMessage, x, ratio);
			} else if (this.direction != Direction.CenterStill) {
				int xEndMessage = -FONT_MESSAGE.stringWidth(line);
				x = Utils.computeMean(x, xEndMessage, ratio);
			}

			g.drawString(line, x, y, 0);
			// Goes to next line.
			y += g.getFont().getHeight();
		}
	}

	@Override
	protected String getDate() {
		return this.date;
	}

	/**
	 * Get the notification count string.
	 *
	 * @return the notification count
	 */
	private String getNotificationCount() {
		return Integer.toString(PROVIDER.getEventsCount());
	}

	/**
	 * Get the notification message.
	 *
	 * @return the notification message.
	 */
	private String getNotificationsMessage() {
		StringBuffer notifs = new StringBuffer();
		if (this.currentPosition == this.targetPosition && this.currentPosition == ScreenArea.Center) {
			notifs.append(this.viewIndex + 1);
			notifs.append("/"); //$NON-NLS-1$
			notifs.append(getNotificationCount());
		} else {
			notifs.append(getNotificationCount());
		}
		return notifs.toString();
	}

	@Override
	public String getTag() {
		return TAG;
	}

	@Override
	public void redraw(GraphicsContext g, Direction direction, int stage, int x, int y) {
		g.setColor(Constants.COLOR_FOREGROUND);
		Point center = drawEncapsulatingCircle(g, direction, stage, x, y);
		// draw icon
		if (direction != Direction.CenterStill) {
			g.drawImage(this.imgIcon, center.x, center.y, 0);

			// draw notification number
			if (direction != Direction.ToCenter && direction != Direction.ToEdge) {
				String notifs = getNotificationCount();
				int xString = center.x + this.imgIcon.getWidth() / 2 - FONT_NOTIFICATION_COUNT.stringWidth(notifs) / 2;
				int y11 = center.y + Y_PADDING;
				g.setFont(FONT_NOTIFICATION_COUNT);
				g.drawString(notifs, xString, y11, 0);
			}
		}

		float ratio = (float) stage / Constants.TRANSITION_HIGH;
		Image icon = this.imgIcon;

		if (direction != Direction.ToCenter && direction != Direction.ToEdge && direction != Direction.CenterStill) {
			return;
		}

		// Draw notification.
		g.setFont(FONT_MESSAGE);
		String notifs = getNotificationsMessage();
		int xNotification = (getWidth() - FONT_NOTIFICATION_COUNT.stringWidth(notifs)) / 2;
		int yNotification = Y_PADDING * 2;
		int yEndNotification = getHeight() - FONT_NOTIFICATION_COUNT.getHeight() - Y_PADDING;
		int xEndNotification = X_PADDING + icon.getWidth() / 2 - FONT_NOTIFICATION_COUNT.stringWidth(notifs) / 2;

		int xString, yString;
		if (direction == Direction.ToEdge) {
			xString = Utils.computeMean(xEndNotification, xNotification, ratio) - 2;
			yString = Utils.computeMean(yNotification, yEndNotification, ratio);
		} else if (direction == Direction.CenterStill) {
			xString = xNotification;
			yString = yEndNotification;
		} else {
			xString = Utils.computeMean(xNotification, xEndNotification, ratio);
			yString = Utils.computeMean(yEndNotification, yNotification, ratio);
		}

		drawNextPrevious(g, direction, stage);
		drawText(g, ratio);

		g.setFont(FONT_NOTIFICATION_COUNT);
		g.drawString(notifs, xString, yString, 0);
	}

	@Override
	public void render(GraphicsContext g) {
		if (this.direction == Direction.CenterStill) {
			redraw(g, this.direction, this.transitionState, getX(), getY());
		}
	}

	/**
	 * Setup all the message variables.
	 *
	 * @param newer
	 *            Goes to the newer notifications.
	 * @return True if notifications available.
	 */
	private boolean setupMessage(boolean newer) {
		if (PROVIDER.getEventsCount() == 0) {
			this.messageLinesToDisplay = 0;
			this.event = null;
			this.date = EMPTY_STRING;
			this.time = EMPTY_STRING;
			return false;
		}

		// Find the message.

		nextView(newer);
		this.event = PROVIDER.getEvent(this.viewIndex);

		// Setup the variables.
		this.date = this.event.getDateStr();
		this.time = this.event.getTimeStr();
		this.message = TextHelper.splitStringInLines(this.event.getMessage(), FONT_MESSAGE,
				(int) (getWidth() * MESSAGE_SCREEN_X_PORTION));

		this.messageLinesToDisplay = this.message.length;
		if (this.messageLinesToDisplay > MESSAGE_SCREEN_Y_LINES) {
			this.messageLinesToDisplay = MESSAGE_SCREEN_Y_LINES;
		}

		return true;
	}

	@Override
	public DatePosition getDatePosition() {
		return DatePosition.OUTSIDE;
	}

	@Override
	public void switchView(boolean right) {
		setupMessage(!right);
	}

	@Override
	public void startSwitchFace(boolean up) {
		if (this.event != null) {
			PROVIDER.removeEvent(this.event);
			nextView(true);
			setupMessage(false);
		}
	}

	@Override
	protected int viewCount() {
		return PROVIDER.getEventsCount();
	}
}
