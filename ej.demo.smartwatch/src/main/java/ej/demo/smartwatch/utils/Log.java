/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class Log.
 */
public class Log {

	/** The Constant FILTER. */
	private static final String[] FILTER = null;

	/** The Constant SEPARATOR. */
	private static final String SEPARATOR = ": "; //$NON-NLS-1$

	/** The G logger. */
	private static Logger GLogger = Logger.getLogger("SmartWatch"); //$NON-NLS-1$

	/**
	 * Instantiates a new log.
	 */
	/* Hide this */
	private Log() {
	}

	/**
	 * Filtered print.
	 *
	 * @param level
	 *            the level
	 * @param tag
	 *            the tag
	 * @param s
	 *            the s
	 */
	private static void filteredPrint(Level level, String tag, String s) {
		if (!Constants.LOGGING_ENABLED) {
			return;
		}

		if (FILTER != null) {
			for (String str : FILTER) {
				if (str.equals(tag)) {
					return;
				}
			}
		}
		print(level, tag, s);
	}

	/**
	 * Prints the.
	 *
	 * @param level
	 *            the level
	 * @param tag
	 *            the tag
	 * @param s
	 *            the s
	 */
	private static void print(Level level, String tag, String s) {
		GLogger.log(level, tag + SEPARATOR + s);
	}

	/**
	 * D. Print debug message
	 *
	 * @param tag
	 *            the tag
	 * @param s
	 *            the log content
	 */
	public static void d(String tag, String s) {
		filteredPrint(Level.FINER, tag, s);
	}

	/**
	 * I. Print info message
	 *
	 * @param tag
	 *            the tag
	 * @param s
	 *            the log content
	 */
	public static void i(String tag, String s) {
		filteredPrint(Level.INFO, tag, s);
	}

	/**
	 * E. Print error message
	 *
	 * @param tag
	 *            the tag
	 * @param s
	 *            the log content
	 */
	public static void e(String tag, String s) {
		if (!Constants.LOGGING_ENABLED) {
			return;
		}
		print(Level.SEVERE, tag, s);
	}

	/**
	 * E.
	 *
	 * @param tag
	 *            the tag
	 * @param e
	 *            the e
	 */
	public static void e(String tag, Exception e) {
		if (!Constants.LOGGING_ENABLED) {
			return;
		}
		GLogger.log(Level.SEVERE, tag + SEPARATOR + e.getMessage(), e);
	}

	/**
	 * W. Print warning message
	 *
	 * @param tag
	 *            the tag
	 * @param s
	 *            the log content
	 */
	public static void w(String tag, String s) {
		filteredPrint(Level.WARNING, tag, s);
	}
}
