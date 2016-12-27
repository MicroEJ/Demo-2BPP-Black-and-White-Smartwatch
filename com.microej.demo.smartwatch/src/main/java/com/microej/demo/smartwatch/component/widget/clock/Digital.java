/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smartwatch.component.widget.clock;

import com.microej.demo.smartwatch.component.Bubble.DatePosition;
import com.microej.demo.smartwatch.component.Direction;
import com.microej.demo.smartwatch.model.IDataProvider;
import com.microej.demo.smartwatch.utils.Constants;
import com.microej.demo.smartwatch.utils.Utils;

import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;

/**
 *
 */
public class Digital implements IClock {

	/**
	 * Font for the hours.
	 */
	private static Font FONT_HOUR;

	/**
	 * Font for the time.
	 */
	private static Font FONT_TIME;

	/**
	 * Padding.
	 */
	private static int PADDING;

	/**
	 * x coordinate of center.
	 */
	private final int xCenter;

	/**
	 * y coordinate of center.
	 */
	private final int yCenter;

	public static void initialize() {
		FONT_HOUR = Constants.FONT_60;
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
		y -= (FONT_HOUR.getHeight() - PADDING) / 2;

		int hour = provider.getHour();
		boolean am = provider.getAm();
		int minute = provider.getMinute();

		StringBuilder sb = new StringBuilder();
		sb.append(hour);
		int second = provider.getSecond();
		sb.append((second & 1) != 1 ? ":" : Constants.SPACE); //$NON-NLS-1$
		if (minute < Constants.DOUBLE_DIGIT_THRESHOLD) {
			sb.append("0" + minute); //$NON-NLS-1$
		} else {
			sb.append(minute);
		}

		int hourWidth = FONT_HOUR.stringWidth("88:88"); //$NON-NLS-1$
		int spaceWidth = FONT_HOUR.charWidth(' ');
		String period = (am) ? Constants.AM : Constants.PM;
		int periodWidth = Constants.FONT_36.stringWidth(period);
		int fullWidth = hourWidth + spaceWidth + periodWidth;
		g.setColor(backgroundColor);
		x -= (fullWidth >> 1);

		// Draw the hour.
		g.setColor(foregroundColor);
		g.setFont(FONT_HOUR);
		String formattedHour = sb.toString();
		g.drawString(formattedHour, x, y, GraphicsContext.LEFT | GraphicsContext.TOP);

		// Draw AM or PM
		x += hourWidth + spaceWidth;
		int timeOfDayFontHeight = Constants.FONT_36.getHeight();
		g.setFont(Constants.FONT_36);
		g.drawString(period, x, y + FONT_HOUR.getBaselinePosition(),
				GraphicsContext.BASELINE | GraphicsContext.LEFT);

		// Draw the date.
		g.setFont(FONT_TIME);
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
