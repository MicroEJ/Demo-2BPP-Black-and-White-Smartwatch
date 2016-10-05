/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component.widget.clock;

import ej.demo.smartwatch.component.Bubble.DatePosition;
import ej.demo.smartwatch.component.Direction;
import ej.demo.smartwatch.model.IDataProvider;
import ej.demo.smartwatch.utils.Constants;
import ej.demo.smartwatch.utils.Utils;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;

/**
 *
 */
public class Digital implements IClock {

	/**
	 * Font for the hours.
	 */
	private static final Font FONT_HOUR;

	/**
	 * Font for the time.
	 */
	private static final Font FONT_TIME;

	/**
	 * Padding.
	 */
	private static final int PADDING;

	/**
	 * x coordinate of center.
	 */
	private final int xCenter;

	/**
	 * y coordinate of center.
	 */
	private final int yCenter;

	static {
		FONT_HOUR = Constants.FONT_36;
		FONT_TIME = Constants.FONT_24;
		PADDING = FONT_TIME.getHeight();
	}

	/**
	 * Instantiate digital clock.
	 *
	 * @param height
	 *            Height of the clock.
	 * @param width
	 *            Width of the clock.
	 *
	 */
	public Digital(int height, int width) {
		this.xCenter = width / 2;
		this.yCenter = height / 2;
	}

	@Override
	public void draw(GraphicsContext g, Direction direction, IDataProvider provider, int x, int y, int completion) {
		int foregroundColor = Constants.COLOR_FOREGROUND;
		int backgroundColor = Constants.COLOR_BACKGROUND;
		int originalY = y;
		int hour = provider.getHour();
		boolean am = provider.getAm();
		int minute = provider.getMinute();

		StringBuilder sb = new StringBuilder();
		sb.append(hour);
		int second = provider.getSecond();
		sb.append((second & 1) != 1 ? ":" : " "); //$NON-NLS-1$//$NON-NLS-2$
		if (minute < Constants.DOUBLE_DIGIT_THRESHOLD) {
			sb.append("0" + minute); //$NON-NLS-1$
		} else {
			sb.append(minute);
		}

		int hourWidth = FONT_HOUR.stringWidth("88:88"); //$NON-NLS-1$
		g.setColor(backgroundColor);
		x -= (hourWidth >> 1);

		// Draw the hour.
		g.setColor(foregroundColor);
		g.setFont(FONT_HOUR);
		String formattedHour = sb.toString();
		g.drawString(formattedHour, x, y, GraphicsContext.LEFT | GraphicsContext.TOP);

		// Draw AM or PM
		int spaceWidth = FONT_TIME.charWidth(' ');
		String period = (am) ? Constants.AM : Constants.PM;
		x += hourWidth + spaceWidth;
		int timeOfDayFontHeight = FONT_TIME.getHeight();
		g.setFont(FONT_TIME);
		g.drawString(period, x, y, GraphicsContext.TOP | GraphicsContext.LEFT);

		// Draw the date.
		int offset = originalY - this.yCenter;
		offset = (offset < 0) ? -offset : offset;
		String text = provider.getDayStr();
		x = this.xCenter - FONT_TIME.stringWidth(text) / 2;
		y = Utils.computeMean(-PADDING, PADDING, (float) offset / this.yCenter);
		y -= (timeOfDayFontHeight >> 1);
		g.drawString(text, x, y, GraphicsContext.TOP | GraphicsContext.LEFT);
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public DatePosition getDatePosition() {
		return DatePosition.POSITION2;
	}

	@Override
	public boolean hasCornerFace() {
		return false;
	}
}
