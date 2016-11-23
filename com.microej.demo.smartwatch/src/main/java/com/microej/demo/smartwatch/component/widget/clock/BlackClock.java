/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smartwatch.component.widget.clock;

import com.microej.demo.smartwatch.component.Direction;
import com.microej.demo.smartwatch.model.IDataProvider;
import com.microej.demo.smartwatch.utils.Constants;
import com.microej.demo.smartwatch.utils.Utils;

import ej.microui.display.GraphicsContext;

/**
 * The Class BlackClock.
 */
public class BlackClock extends AbstractAnalogClock {

	/** The Constant CORNER_START_ANGLE. */
	private static final int CORNER_START_ANGLE = 90;

	private static final int SEC_ANGLE_MULT = -Constants.ANGLE_FULL_CIRCLE / 60;
	private static final int SECONDS_FADE = 1;
	private static final int SECONDS_THICKNESS = 1;

	/**
	 * Instantiates a new black clock.
	 *
	 * @param maxDiameter
	 *            The maximum diameter.
	 * @param minDiameter
	 *            The minimum diameter.
	 */
	public BlackClock(int maxDiameter, int minDiameter) {
		super(maxDiameter, minDiameter);
	}

	@Override
	public void draw(GraphicsContext g, Direction direction, IDataProvider provider, int x, int y, int completion) {
		float ratio = ((float) completion) / Constants.COMPLETION_MAX;
		int diameter = getDiameter(direction, ratio);

		int second = provider.getSecond();

		int centerX = x - diameter / 2;
		int centerY = y - diameter / 2;
		g.setColor(Constants.COLOR_FOREGROUND);

		if (direction == Direction.CornerStill || direction == Direction.CornerSwitch) {
			// Draw a full circle.
			Utils.drawCircle(g, centerX, centerY, diameter, Constants.DEFAULT_THICKNESS, Constants.DEFAULT_FADE);
		} else if (direction == Direction.CenterStill) {
			// Draw the seconds circle.
			Utils.drawEllipseArc(g, centerX, centerY, diameter, CORNER_START_ANGLE, second * SEC_ANGLE_MULT,
					SECONDS_THICKNESS, SECONDS_FADE);
		} else if (direction == Direction.ToCorner) {
			int currentAngle = second * SEC_ANGLE_MULT;
			int startAngle = CORNER_START_ANGLE + currentAngle;
			int angle = (int) ((Constants.ANGLE_FULL_CIRCLE + currentAngle) * ratio) - currentAngle;

			// Draw animation from second to full.
			Utils.drawEllipseArc(g, centerX, centerY, diameter, startAngle, angle, Constants.DEFAULT_THICKNESS,
					Constants.DEFAULT_FADE);
		} else if (direction == Direction.ToCenter) {
			int currentAngle = second * SEC_ANGLE_MULT;
			int angle = (int) ((Constants.ANGLE_FULL_CIRCLE + currentAngle) * (1 - ratio)) - currentAngle;
			int startAngle = CORNER_START_ANGLE + currentAngle;

			// Draw animation from full to second.
			Utils.drawEllipseArc(g, centerX, centerY, diameter, startAngle, angle, Constants.DEFAULT_THICKNESS,
					Constants.DEFAULT_FADE);
		}

		// Draw the hands.
		drawClock(g, direction, provider, x, y, completion, diameter);
	}
}
