/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smartwatch.component.widget.clock;

import com.microej.demo.smartwatch.component.Direction;
import com.microej.demo.smartwatch.component.Bubble.DatePosition;
import com.microej.demo.smartwatch.model.IDataProvider;
import com.microej.demo.smartwatch.utils.Constants;

import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;

/**
 * The Class WhiteClock.
 */
public class WhiteClock extends AbstractAnalogClock {

	/**
	 * Instantiates a new white clock.
	 *
	 * @param maxDiameter
	 *            The maximum diameter.
	 * @param minDiameter
	 *            The minimum diameter.
	 */
	public WhiteClock(int maxDiameter, int minDiameter) {
		super(maxDiameter, minDiameter);
	}

	@Override
	public void draw(GraphicsContext g, Direction direction, IDataProvider provider, int x, int y, int completion) {

		float ratio = ((float) completion) / Constants.COMPLETION_MAX;
		int diameter = getDiameter(direction, ratio);

		int width = diameter;
		int height = diameter;
		int centerX = x - width / 2;
		int centerY = y - height / 2;
		g.setBackgroundColor(Constants.COLOR_BACKGROUND);
		g.setColor(Constants.COLOR_FOREGROUND);
		// Draw white clock background
		if (completion == Constants.COMPLETION_MAX || completion == Constants.COMPLETION_MIN) {
			AntiAliasedShapes.Singleton.setFade(1);
			AntiAliasedShapes.Singleton.setThickness(2);
			AntiAliasedShapes.Singleton.drawCircle(g, centerX, centerY, width);
		}
		g.fillCircle(centerX, centerY, width);
		g.setBackgroundColor(Constants.COLOR_FOREGROUND);
		g.setColor(Constants.COLOR_BACKGROUND);
		final int padding = 5;
		final int length = 10;
		int halfWidth = width >> 1;
		// Draw clock marks.
		g.drawVerticalLine(centerX + halfWidth, centerY + padding, length);
		g.drawVerticalLine(centerX + halfWidth, centerY + width - (padding + length), length);
		g.drawHorizontalLine(centerX + padding, centerY + halfWidth, length);
		g.drawHorizontalLine(centerX + width - (padding + length), centerY + halfWidth, length);

		// Draw hands.
		drawClock(g, direction, provider, x, y, completion, diameter);
		g.setBackgroundColor(Constants.COLOR_BACKGROUND);
		g.setColor(Constants.COLOR_FOREGROUND);

	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public DatePosition getDatePosition() {
		return DatePosition.POSITION1;
	}
}
