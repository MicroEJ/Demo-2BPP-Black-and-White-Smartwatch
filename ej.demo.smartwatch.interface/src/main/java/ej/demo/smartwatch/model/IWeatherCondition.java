/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ej.demo.smartwatch.model;

import java.util.Date;

/**
 *
 */
public interface IWeatherCondition {
	/** The Constant COND_SUNNY. */
	int COND_SUNNY = 0;

	/** The Constant COND_RAIN. */
	int COND_RAIN = 1;

	/** The Constant COND_CLOUDY. */
	int COND_CLOUDY = 2;

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	Date getDate();

	/**
	 * Sets the date.
	 *
	 * @param date
	 *            the new date
	 */
	void setDate(Date date);

	/**
	 * Gets the temp.
	 *
	 * @return the temp
	 */
	int getTemp();

	/**
	 * Sets the temp.
	 *
	 * @param temp
	 *            the new temp
	 */
	void setTemp(int temp);

	/**
	 * Gets the condition.
	 *
	 * @return the condition
	 */
	int getCondition();

	/**
	 * Sets the condition.
	 *
	 * @param condition
	 *            the new condition
	 */
	void setCondition(int condition);

	String getRelDateStr();

	String getDateStr();
}
