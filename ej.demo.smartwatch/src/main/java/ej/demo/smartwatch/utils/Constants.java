/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.utils;

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
	 * Hour in one day.
	 */
	public static final int HOURS_IN_DAY = 24;

	/**
	 * Hour in half a day.
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
	 * Short name for month.
	 */
	public static final String[] MONTH_SHORT = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
			"Nov", "Dec" }; //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * Short name for day.
	 */
	public static final String[] DAY_SHORT = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

	/**
	 * START_TIME - initial date and time (seconds since epoch).
	 */
	public static final long START_TIME = 1467324061;

	/**
	 * Pm String.
	 */
	public static final String PM = "pm"; //$NON-NLS-1$

	/**
	 * Am String.
	 */
	public static final String AM = "am"; //$NON-NLS-1$

	/**
	 * Double digit threshold.
	 */
	public static final int DOUBLE_DIGIT_THRESHOLD = 10;

	/**********************************************************/
	/**                   UX CONSTANTS                      ***/
	/**********************************************************/
	
	/**
	 * Angle for a full circle.
	 */
	public static final int ANGLE_FULL_CIRCLE = 360;

	/**
	 * Angle for half a circle.
	 */
	public static final int ANGLE_HALF_CIRCLE = 180;

	/**
	 * COLOR_BACKGROUND.
	 */
	public static final int COLOR_BACKGROUND = 2501164;
	/**
	 * COLOR_FOREGROUND.
	 */
	public static final int COLOR_FOREGROUND = Colors.WHITE;

	/**
	 * Small Font.
	 */
	public static final Font FONT_24;

	/**
	 * Medium font.
	 */
	public static final Font FONT_36;

	/**
	 * Big font.
	 */
	public static final Font FONT_60;

	/**
	 * ENABLE_ANTIALIASED.
	 */
	public static final boolean ENABLE_ANTIALIASED = true;

	/** Indicates whether or not LOGGING is ENABLED. */
	public static final boolean LOGGING_ENABLED = true;

	/**
	 * MIN_SWIPE.
	 */
	public static final int MINIMUM_SWIPE = 10;

	/**
	 * TRANSITION_HIGH.
	 */
	public static final int TRANSITION_HIGH = 100;

	/**
	 * TRANSITION_LOW.
	 */
	public static final int TRANSITION_LOW = 0;

	/**
	 * Duration for transition.
	 */
	public static final int DURATION = 350;

	/**
	 * WIDTH.
	 */
	public static final int WIDTH;

	/**
	 * Ratio with the default with (240).
	 */
	public static final float WIDTH_RATIO;

	/**
	 * HEIGHT.
	 */
	public static final int HEIGHT;

	/**
	 * Ratio with the default height (240).
	 */
	public static final float HEIGHT_RATIO;

	/**
	 * default fade value.
	 */
	public static final int DEFAULT_FADE = 1;

	/**
	 * default Thickness value.
	 */
	public static final int DEFAULT_THICKNES = 1;

	static {

		MicroUI.start();

		FONT_24 = Font.getFont(Font.LATIN, 30, Font.STYLE_PLAIN);
		FONT_36 = Font.getFont(Font.LATIN, 44, Font.STYLE_PLAIN);
		FONT_60 = Font.getFont(Font.LATIN, 81, Font.STYLE_PLAIN);

		float oldWidth = 240;
		WIDTH = Display.getDefaultDisplay().getWidth();
		WIDTH_RATIO = WIDTH / oldWidth;

		float oldHeight = 240;
		HEIGHT = Display.getDefaultDisplay().getHeight();
		HEIGHT_RATIO = HEIGHT / oldHeight;
	}

	/**
	 * Instantiates the Constants.
	 */
	private Constants() {
	}

}
