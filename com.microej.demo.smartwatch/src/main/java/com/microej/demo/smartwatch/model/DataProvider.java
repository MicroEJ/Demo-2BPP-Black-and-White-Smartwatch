/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smartwatch.model;

/**
 * The Class DataProvider.
 */
public class DataProvider {

	/** The provider. */
	private static IDataProvider Provider = null;

	private DataProvider() {

	}

	/**
	 * Gets the singleton instance.
	 *
	 * @return the data provider instance.
	 */
	public static IDataProvider getInstance() {
		if (Provider == null) {
			Provider = new FakeDataProvider(); /* make it a real one */
		}
		return Provider;
	}
}
