/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ej.demo.smartwatch.model;

import java.util.Calendar;
import java.util.Date;

import ej.demo.smartwatch.utils.Constants;

/**
 * A weather condition.
 */
public class WeatherCondition implements IWeatherCondition {

	/** The temp. */
	private int temp;

	/** The condition. */
	private int condition;

	/** The date. */
	private Date date;

	/**
	 * Instantiates a new weather condition.
	 *
	 * @param temp
	 *            the temp
	 * @param condition
	 *            the condition
	 * @param date
	 *            the date
	 */
	public WeatherCondition(int temp, int condition, Date date) {
		super();
		this.temp = temp;
		this.condition = condition;
		this.date = date;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	@Override
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date
	 *            the new date
	 */
	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gets the temp.
	 *
	 * @return the temp
	 */
	@Override
	public int getTemp() {
		return temp;
	}

	/**
	 * Sets the temp.
	 *
	 * @param temp
	 *            the new temp
	 */
	@Override
	public void setTemp(int temp) {
		this.temp = temp;
	}

	/**
	 * Gets the condition.
	 *
	 * @return the condition
	 */
	@Override
	public int getCondition() {
		return condition;
	}

	/**
	 * Sets the condition.
	 *
	 * @param condition
	 *            the new condition
	 */
	@Override
	public void setCondition(int condition) {
		this.condition = condition;
	}

	@Override
	public String getRelDateStr() {
		/* NOTE: maybe some relative time display could help */
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.date);

		return Constants.DAY_SHORT[calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY];

	}

	@Override
	public String getDateStr() {
		StringBuilder sb = new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.date);
		sb.append(calendar.get(Calendar.DAY_OF_MONTH));
		sb.append(" "); //$NON-NLS-1$
		sb.append(Constants.MONTH_SHORT[calendar.get(Calendar.MONTH)]);
		return sb.toString();

	}
}