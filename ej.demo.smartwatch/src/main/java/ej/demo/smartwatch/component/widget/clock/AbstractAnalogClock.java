/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package ej.demo.smartwatch.component.widget.clock;

import ej.demo.smartwatch.component.Bubble.DatePosition;
import ej.demo.smartwatch.component.Direction;
import ej.demo.smartwatch.model.IDataProvider;
import ej.demo.smartwatch.utils.Constants;
import ej.demo.smartwatch.utils.Utils;
import ej.microui.display.GraphicsContext;

/**
 * Base class for analog clocks
 */
public abstract class AbstractAnalogClock implements IClock {

	/** The Constant DEGREES_PER_UNIT. */
	protected static final int DEGREES_PER_UNIT = 6;

	/** The Constant HAND_PADDING. */
	protected static final int HAND_PADDING = 5;

	/** The Constant HAND_RATIO. */
	protected static final float HAND_RATIO = 0.75f;

	/**
	 * Thickness for the hour hand.
	 */
	protected static final int HAND_THICKNESS_H = 3;

	/**
	 * Thickness for the minute hand.
	 */
	protected static final int HAND_THICKNESS_M = 1;

	/**
	 * Fade for the hour hand.
	 */
	protected static final int HAND_FADE_H = 1;

	/**
	 * Fade for the minute hand.
	 */
	protected static final int HAND_FADE_M = 1;

	/**
	 * Threshold to start to grow the hands.
	 */
	protected static final float SWITCH_THRESHOLD = 20;

	/**
	 * Diameter at the maximum size.
	 */
	protected int maxDiameter;

	/**
	 * Diameter at the minimum size.
	 */
	protected int minDiameter;

	/**
	 *
	 * Instantiate an analog clock.
	 *
	 * @param maxDiameter
	 *            Maximum diameter.
	 * @param minDiameter
	 *            Minimum diameter.
	 */
	public AbstractAnalogClock(int maxDiameter, int minDiameter) {
		super();
		this.maxDiameter = maxDiameter;
		this.minDiameter = minDiameter;
	}

	/**
	 * Draw the clock.
	 *
	 * @param g
	 *            The graphic context.
	 * @param direction
	 *            The animation direction.
	 * @param provider
	 *            The data provider.
	 * @param x
	 *            X coordinate of the center.
	 * @param y
	 *            Y coordinate of the center.
	 * @param completion
	 *            Animation completion.
	 * @param diameter
	 *            Current diameter.
	 */
	protected void drawClock(GraphicsContext g, Direction direction, IDataProvider provider, int x, int y, int completion,
			int diameter) {
		int hour = provider.getHour() % Constants.HOURS_IN_DAY;
		int minute = provider.getMinute();

		int centerX = x;
		int centerY = y;

		x = centerX - diameter / 2;
		y = centerY - diameter / 2;

		int handLength;
		float angle;
		if (direction == Direction.CenterStill) {
			if (completion > (Constants.COMPLETION_MAX / 2)) {
				completion = Constants.COMPLETION_MAX - completion;
			}

			float ratio = 0;
			if (completion < SWITCH_THRESHOLD) {
				ratio = (SWITCH_THRESHOLD - completion) / SWITCH_THRESHOLD;
			}

			handLength = (int) (ratio * diameter / 2);
		} else {
			handLength = diameter / 2;
		}

		minute *= DEGREES_PER_UNIT;
		minute = Constants.ANGLE_HALF_CIRCLE - minute;
		handLength = (int) (handLength * HAND_RATIO);
		angle = (float) Math.toRadians(minute);
		float sin = (float) Math.sin(angle);
		float cos = (float) Math.cos(angle);
		float padding_y = cos * HAND_PADDING;
		float padding_x = sin * HAND_PADDING;
		// Draw the minute hand.
		Utils.drawLine(g, centerX + (int) padding_x, (int) (centerY + padding_y), centerX + (int) (sin * handLength),
				(int) (centerY + cos * handLength), HAND_THICKNESS_M, HAND_FADE_M);

		hour = Constants.ANGLE_HALF_CIRCLE - hour * Constants.ANGLE_FULL_CIRCLE / Constants.HOURS_IN_HALF_DAY
				- provider.getMinute() / 2;
		handLength = (int) (handLength * HAND_RATIO);
		angle = (float) Math.toRadians(hour);
		sin = (float) Math.sin(angle);
		cos = (float) Math.cos(angle);
		padding_y = cos * HAND_PADDING;
		padding_x = sin * HAND_PADDING;

		// Draw the hour hand.
		Utils.drawLine(g, centerX + (int) padding_x, (int) (centerY + padding_y), centerX + (int) (sin * handLength),
				(int) (centerY + cos * handLength), HAND_THICKNESS_H, HAND_FADE_H);

		int circleDiameter = HAND_PADDING + 1;
		centerX -= circleDiameter / 2;
		centerY -= circleDiameter / 2;

		// Draw the inside circle.
		Utils.drawCircle(g, centerX, centerY, circleDiameter, Constants.DEFAULT_THICKNESS, Constants.DEFAULT_FADE);
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public DatePosition getDatePosition() {
		return DatePosition.POSITION1;
	}

	@Override
	public boolean hasCornerFace() {
		return true;
	}

	/**
	 * Get the diameter.
	 *
	 * @param direction
	 *            Direction of the animation.
	 * @param ratio
	 *            Ratio of the animation.
	 * @return The clock diameter.
	 */
	protected int getDiameter(Direction direction, float ratio) {
		int diameter = this.maxDiameter;
		if (direction == Direction.ToCorner || direction == Direction.ToCenter) {
			ratio = (direction == Direction.ToCenter) ? ratio : (1 - ratio);
			diameter = (int) ((this.maxDiameter - this.minDiameter) * ratio + this.minDiameter);
		} else if (direction == Direction.CornerStill || direction == Direction.CornerSwitch) {
			diameter = this.minDiameter;
		}
		return diameter;
	}
}
