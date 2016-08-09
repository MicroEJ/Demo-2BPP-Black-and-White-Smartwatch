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
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.display.transform.ImageRotation;

/**
 * Clock displaying number with angles.
 */
public class DisksClock implements IClock {
	/**
	 * Angle in between two number.
	 */
	private static final int ANGLE_SPACING = 17;

	/**
	 * Base angle.
	 */
	private static final int REFERENCE_ANGLE = 90;

	/**
	 * Reference angle for bottom line.
	 */
	private static final int REFERENCE_ANGLE_ROT = 270;

	/**
	 * Reference angle for top line.
	 */
	private static final int REFERENCE_ANGLE_ROT2 = 90;

	/**
	 * Offset between lines.
	 */
	private static final float CENTER_OFFSET_PERC = 0.1f;

	/**
	 * Degree offset for 2nd digit.
	 */
	private static final int OFFSET2 = 2;

	/**
	 * Degree offset for 3rd digit.
	 */
	private static final int OFFSET3 = 3;

	/**
	 * Degree offset for 4th digit.
	 */
	private static final int OFFSET4 = 4;

	/**
	 * Font for side digits.
	 */
	private final Font fontPrimary = Constants.FONT_24;

	/**
	 * Font for central digit.
	 */
	private final Font fontSecondary = Constants.FONT_36;

	/**
	 * Width.
	 */
	private final int width;

	/**
	 * Height.
	 */
	private final int height;

	/**
	 * X coordinate for center.
	 */
	private int centerX;

	/**
	 * Y coordinate for center.
	 */
	private int centerY;

	/**
	 * Clock displaying number with angles.
	 *
	 * @param height
	 *            Height.
	 * @param width
	 *            Width.
	 */
	public DisksClock(int height, int width) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Get the minute as a string.
	 *
	 * @param minute
	 *            the minute to convert.
	 * @return The minute.
	 */
	private String getMinute(int minute) {
		if (minute < 0) {
			minute += Constants.MINUTES_IN_HOUR;
		}
		return Integer.toString(minute);
	}

	/**
	 * Get the hour as a string.
	 *
	 * @param hour
	 *            the hour to convert.
	 * @return The hour.
	 */
	private String getHour(int hour) {
		if (hour < 0) {
			hour += Constants.HOURS_IN_DAY;
		}
		return Integer.toString(hour);
	}

	@Override
	public void draw(GraphicsContext g, Direction direction, IDataProvider provider, int x, int y, int completion) {
		if (direction != Direction.CenterStill) {
			return;
		}

		g.setColor(Constants.COLOR_FOREGROUND);

		this.centerX = x;
		this.centerY = (int) (y + this.height / 2 + this.height * CENTER_OFFSET_PERC);
		int degree = ANGLE_SPACING;
		int startangle = -REFERENCE_ANGLE + degree * 2;
		int startangle2 = REFERENCE_ANGLE - degree * 2;

		int minute = provider.getMinute();
		int hour = provider.getHour();

		int firstMinute = minute + 2;
		int firstHour = hour + 2;

		// Draw bottom line.
		drawAt(getMinute(firstMinute--), startangle, g, this.fontPrimary, REFERENCE_ANGLE_ROT);
		drawAt(getMinute(firstMinute--), startangle - degree, g, this.fontPrimary, REFERENCE_ANGLE_ROT);
		drawAt(getMinute(firstMinute--), startangle - OFFSET2 * degree, g, this.fontSecondary, REFERENCE_ANGLE_ROT);
		drawAt(getMinute(firstMinute--), startangle - OFFSET3 * degree, g, this.fontPrimary, REFERENCE_ANGLE_ROT);
		drawAt(getMinute(firstMinute--), startangle - OFFSET4 * degree, g, this.fontPrimary, REFERENCE_ANGLE_ROT);

		// Draw top line.
		this.centerY = (int) (y - this.height / 2 - this.height * CENTER_OFFSET_PERC);
		drawAt(getHour(firstHour--), startangle2, g, this.fontPrimary, REFERENCE_ANGLE_ROT2);
		drawAt(getHour(firstHour--), startangle2 + degree, g, this.fontPrimary, REFERENCE_ANGLE_ROT2);
		drawAt(getHour(firstHour--), startangle2 + OFFSET2 * degree, g, this.fontSecondary, REFERENCE_ANGLE_ROT2);
		drawAt(getHour(firstHour--), startangle2 + OFFSET3 * degree, g, this.fontPrimary, REFERENCE_ANGLE_ROT2);
		drawAt(getHour(firstHour--), startangle2 + OFFSET4 * degree, g, this.fontPrimary, REFERENCE_ANGLE_ROT2);
	}

	/**
	 * Draw a number.
	 *
	 * @param text
	 *            Number to draw.
	 *
	 * @param degrees
	 *            Degree to rotate.
	 * @param g
	 *            Graphic context
	 * @param font
	 *            Font for the text.
	 * @param referenceAngle
	 *            Base angle.
	 */
	private void drawAt(String text, int degrees, GraphicsContext g, Font font, int referenceAngle) {
		int offsetX = this.width / 2;
		int offsetY = this.height / 2;
		offsetX *= Math.cos(Math.toRadians(degrees));
		offsetY *= Math.sin(Math.toRadians(degrees));

		offsetX += this.centerX;
		offsetY += this.centerY;

		// Draw string in image.
		Image img = Image.createImage(font.stringWidth(text), font.getHeight());
		GraphicsContext gc = img.getGraphicsContext();
		gc.setFont(font);
		gc.setColor(Constants.COLOR_BACKGROUND);
		gc.fillRect(0, 0, img.getWidth(), img.getHeight());
		gc.setColor(Constants.COLOR_FOREGROUND);
		gc.drawString(text, 0, 0, 0);

		// Rotate image.
		ImageRotation rotation = new ImageRotation();
		rotation.setRotationCenter(offsetX, offsetY);
		rotation.setAngle(referenceAngle - degrees);
		rotation.drawNearestNeighbor(g, img, offsetX, offsetY, GraphicsContext.HCENTER | GraphicsContext.VCENTER);
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
		return false;
	}
}
