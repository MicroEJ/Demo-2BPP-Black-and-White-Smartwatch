/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.dal;

/**
 * The Class SmDataPrivider.
 */
public class SmDataPrivider {

	/** The provider. */
	private static ISmDataProvider Provider = null;

	private SmDataPrivider() {

	}

	/**
	 * Gets the.
	 *
	 * @return the sm data provider to be used.
	 */
	public static ISmDataProvider get() {
		if (Provider == null) {
			Provider = new SmFakeData(); /* make it a real one */
		}
		return Provider;
	}
}
