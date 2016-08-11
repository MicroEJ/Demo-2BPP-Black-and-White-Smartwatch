/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.model;

/**
 * The Class DataProvider.
 */
public abstract class DataProvider {

	/** The provider. */
	private static IDataProvider Provider = null;

	protected DataProvider() {

	}

	/**
	 * Gets the singleton instance.
	 *
	 * @return the data provider instance.
	 */
	public static IDataProvider getInstance() {
		return Provider;
	}

	public static void setInstance(IDataProvider provider) {
		Provider = provider;
	}
}
