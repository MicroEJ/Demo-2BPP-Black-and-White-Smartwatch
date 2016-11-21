/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package ej.demo.smartwatch.utils;

import ej.demo.smartwatch.component.ScreenArea;
import ej.microui.display.GraphicsContext;
import ej.microui.display.shape.AntiAliasedShapes;

/**
 * Utils functions.
 */
public class Utils {

	private Utils() {
		// do nothing
	}

	/**
	 * Converts a float to a string.
	 *
	 * @param value
	 *            value
	 * @param precision
	 *            precision
	 * @return a string
	 */
	public static String floatToString(float value, int precision) {
		String str = String.valueOf(value);
		int dot = str.indexOf('.');
		if (precision > 0) {
			precision++;
		}
		if (dot > 0 && str.length() > dot + precision) {
			str = str.substring(0, dot + precision);
		}
		return str;
	}

	/**
	 * Compute the mean of row values.
	 *
	 * @param val1
	 *            val1
	 * @param val2
	 *            val2
	 * @param ratio
	 *            ratio
	 * @return mean
	 */
	public static int computeMean(int val1, int val2, float ratio) {
		return (int) ((1.0f - ratio) * val2 + ratio * val1);
	}

	/**
	 * Draw a line.
	 *
	 * @param g
	 *            The graphic context to use
	 * @param x1
	 *            x1
	 * @param y1
	 *            y1
	 * @param x2
	 *            x2
	 * @param y2
	 *            y2
	 * @param thickness
	 *            thickness
	 * @param fade
	 *            fade
	 */
	public static void drawLine(GraphicsContext g, int x1, int y1, int x2, int y2, int thickness, int fade) {
		if (Constants.ENABLE_ANTIALIASED) {
			AntiAliasedShapes.Singleton.setFade(fade);
			AntiAliasedShapes.Singleton.setThickness(thickness);
			AntiAliasedShapes.Singleton.drawLine(g, x1, y1, x2, y2);
		} else {
			g.drawLine(x1, y1, x2, y2);
		}
	}

	/**
	 * @param g
	 *            The graphic context to use
	 * @param x
	 *            x
	 * @param y
	 *            y
	 * @param diameter
	 *            diameter
	 * @param thickness
	 *            thickness
	 * @param fade
	 *            fade
	 */
	public static void drawCircle(GraphicsContext g, int x, int y, int diameter, int thickness, int fade) {
		if (Constants.ENABLE_ANTIALIASED) {
			AntiAliasedShapes.Singleton.setFade(fade);
			AntiAliasedShapes.Singleton.setThickness(thickness);
			AntiAliasedShapes.Singleton.drawCircle(g, x, y, diameter);
		} else {
			g.drawCircle(x, y, diameter);
		}
	}

	/**
	 * @param g
	 *            The graphic context to use. g
	 * @param x
	 *            x
	 * @param y
	 *            y
	 * @param diameter
	 *            diameter
	 * @param startAngle
	 *            startAngle
	 * @param arcAngle
	 *            arcAngle
	 * @param thickness
	 *            thickness
	 * @param fade
	 *            fade
	 */
	public static void drawEllipseArc(GraphicsContext g, int x, int y, int diameter, int startAngle, int arcAngle,
			int thickness, int fade) {
		if (Constants.ENABLE_ANTIALIASED) {
			AntiAliasedShapes.Singleton.setFade(fade);
			AntiAliasedShapes.Singleton.setThickness(thickness);
			AntiAliasedShapes.Singleton.drawCircleArc(g, x, y, diameter, startAngle, arcAngle);
		} else {
			g.drawCircleArc(x, y, diameter, startAngle, arcAngle);
		}
	}

	/**
	 * Get the X offset of an image depending on the current position and the
	 * target position.
	 *
	 * @param centerOffset
	 *            The offset when the object is at the center
	 * @param leftOffset
	 *            The offset when the object is at the left
	 * @param rightOffset
	 *            The offset when the object is at the right
	 * @param currentPosition
	 *            The current position
	 * @param targetPosition
	 *            The targeted position.
	 * @param ratio
	 *            Ratio of the animation.
	 * @return The X offset.
	 */
	public static int getXOffset(int centerOffset, int leftOffset, int rightOffset, ScreenArea currentPosition,
			ScreenArea targetPosition, float ratio) {
		int x1 = 0, x2 = 0;

		if (currentPosition.isCenter()) {
			x1 = centerOffset;
		} else if (currentPosition.isLeft()) {
			x1 = leftOffset;
		} else {
			x1 = rightOffset;
		}

		if (targetPosition.isCenter()) {
			x2 = centerOffset;
		} else if (targetPosition.isLeft()) {
			x2 = leftOffset;
		} else {
			x2 = rightOffset;
		}

		return computeMean(x1, x2, ratio);
	}

	/**
	 * Get the y offset of an image depending on the current position and the
	 * target position.
	 *
	 * @param centerOffset
	 *            The offset when the object is at the center
	 * @param topOffset
	 *            The offset when the object is at the top
	 * @param bottomOffset
	 *            The offset when the object is at the bottom
	 * @param currentPosition
	 *            The current position
	 * @param targetPosition
	 *            The targeted position.
	 * @param ratio
	 *            Ratio of the animation.
	 * @return The Y offset.
	 */
	public static int getYOffset(int centerOffset, int topOffset, int bottomOffset, ScreenArea currentPosition,
			ScreenArea targetPosition, float ratio) {
		int y1 = 0, y2 = 0;

		if (currentPosition.isCenter()) {
			y1 = centerOffset;
		} else if (currentPosition.isTop()) {
			y1 = topOffset;
		} else {
			y1 = bottomOffset;
		}

		if (targetPosition.isCenter()) {
			y2 = centerOffset;
		} else if (targetPosition.isTop()) {
			y2 = topOffset;
		} else {
			y2 = bottomOffset;
		}

		return computeMean(y1, y2, ratio);
	}
}
