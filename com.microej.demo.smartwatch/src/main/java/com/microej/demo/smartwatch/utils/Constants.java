/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smartwatch.utils;

import ej.microui.MicroUI;
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.microui.display.Font;

/**
 * Various Constants.
 */
public class Constants {

	/**********************************************************/
	/**                   TIME CONSTANTS                    ***/
	/**********************************************************/

	/**
	 * Hours in one day.
	 */
	public static final int HOURS_IN_DAY = 24;

	/**
	 * Hours in half a day.
	 */
	public static final int HOURS_IN_HALF_DAY = 12;

	/**
	 * Minutes in an hour.
	 */
	public static final int MINUTES_IN_HOUR = 60;

	/**
	 * Milliseconds in one second.
	 */
	public static final long MS_IN_SEC = 1000;

	/**
	 * Short names for months.
	 */
	public static final String[] MONTH_SHORT = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
			"Nov", "Dec" }; //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * Short names for days.
	 */
	public static final String[] DAY_SHORT = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

	/**
	 * START_TIME - initial date and time (seconds since epoch).
	 */
	public static final long START_TIME = 1467324061;

	/**
	 * P.M. String.
	 */
	public static final String PM = "PM"; //$NON-NLS-1$

	/**
	 * P.M. String.
	 */
	public static final String AM = "AM"; //$NON-NLS-1$

	/**
	 * Double digit threshold.
	 */
	public static final int DOUBLE_DIGIT_THRESHOLD = 10;

	/**********************************************************/
	/**                   UX CONSTANTS                      ***/
	/**********************************************************/

	/**
	 * Angle (in degrees) for a full circle.
	 */
	public static final int ANGLE_FULL_CIRCLE = 360;

	/**
	 * Angle (in degrees) for half a circle.
	 */
	public static final int ANGLE_HALF_CIRCLE = 180;

	/**
	 * COLOR_BACKGROUND.
	 */
	public static final int COLOR_BACKGROUND = 0x262A2C;
	/**
	 * COLOR_FOREGROUND.
	 */
	public static final int COLOR_FOREGROUND = Colors.WHITE;

	/**
	 * Small Font.
	 */
	public static Font FONT_24;

	/**
	 * Medium font.
	 */
	public static Font FONT_36;

	/**
	 * Large font.
	 */
	public static Font FONT_60;

	/**
	 * ENABLE_ANTIALIASED.
	 */
	public static final boolean ENABLE_ANTIALIASED = true;

	/** Indicates whether or not LOGGING is ENABLED. */
	public static final boolean LOGGING_ENABLED = true;

	/**
	 * Browser space width.
	 */
	public static final int MIN_BROWSER_WIDTH = 50;

	/**
	 * MIN_SWIPE.
	 */
	public static final int MINIMUM_SWIPE = 10;

	/**
	 * COMPLETION_MAX.
	 */
	public static final int COMPLETION_MAX = 100;

	/**
	 * COMPLETION_MIN.
	 */
	public static final int COMPLETION_MIN = 0;

	/**
	 * Duration for transition.
	 */
	public static final int DURATION = 350;

	/**
	 * Display Width.
	 */
	public static int DISPLAY_WIDTH;

	/**
	 * Default display width ratio (based on a 240px width).
	 */
	public static float DISPLAY_DEFAULT_WIDTH_RATIO;

	/**
	 * Display Height.
	 */
	public static int DISPLAY_HEIGHT;

	/**
	 * Default display height ration (based on a 240px height).
	 */
	public static float DISPLAY_DEFAULT_HEIGHT_RATIO;

	/**
	 * Default fade value.
	 */
	public static final int DEFAULT_FADE = 1;

	/**
	 * Default Thickness value.
	 */
	public static final int DEFAULT_THICKNESS = 1;

	public static void initialize(int newWidth, int newHeight) {
		FONT_24 = Font.getFont(Font.LATIN, 30, Font.STYLE_PLAIN);
		FONT_36 = Font.getFont(Font.LATIN, 44, Font.STYLE_PLAIN);
		FONT_60 = Font.getFont(Font.LATIN, 81, Font.STYLE_PLAIN);

		float oldWidth = 240;
		DISPLAY_WIDTH = newWidth;
		DISPLAY_DEFAULT_WIDTH_RATIO = oldWidth / DISPLAY_WIDTH;

		float oldHeight = 240;
		DISPLAY_HEIGHT = newHeight;
		DISPLAY_DEFAULT_HEIGHT_RATIO = oldHeight / DISPLAY_HEIGHT;
	}

	/**
	 * Color used for the background borders.
	 */
	public static final int BACKGROUND_BORDER_COLOR = 0x717D83;

	/**
	 * Color used for the border.
	 */
	public static final int BACKGROUND_COLOR = 0x4B5357;

	/**
	 * Instantiates the Constants.
	 */
	private Constants() {
	}

}
