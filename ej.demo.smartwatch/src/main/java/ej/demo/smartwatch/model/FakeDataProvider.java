/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ej.demo.smartwatch.utils.Constants;

/**
 * The Class SmFakeData.
 */
public class FakeDataProvider extends DataProvider implements IDataProvider {

	private static final int BATTERY_DIVISION = 23;
	private static final int NOTIFICATION_LIKELINESS = 1;
	private static final int SECONDS_IN_A_MINUTE = 60;
	private static final int ONE_HUNDRED = 100;
	private static final int ONE_THOUSAND = 1000;
	private static Random Rand = new Random();
	private static final int TARGET_DISTANCE = 9;
	private static final int TEMPERATURE1 = 15;
	private static final int TEMPERATURE2 = 5;
	private static final int DISTANCE_TIME = 2;
	private static final int SEC_IN_DAY = (24 * 60 * 60);
	private static final long MS_IN_SEC = 1000;
	private static final int MAX_NOTIF = 10;

	/** The events. */
	private final List<IDataProvider.Event> events;

	/** The forecast. */
	private final List<IWeatherCondition> forecast;

	/**
	 * Instantiates a new Smartwatch fake data provider.
	 */
	public FakeDataProvider() {
		this.events = new ArrayList<IDataProvider.Event>();
		this.events.add(new Event(getDate(), "It's John's birthday!", "22 May", "08:00")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.events.add(new Event(getDate(), "Some other longer message, check multiline", "24 June", "08:01")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// $NON-NLS-3$

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(Constants.START_TIME * MS_IN_SEC));
		long sec = Constants.START_TIME;
		this.forecast = new ArrayList<IWeatherCondition>();
		this.forecast.add(new WeatherCondition(TEMPERATURE1, IWeatherCondition.COND_SUNNY, new Date(sec * MS_IN_SEC)));
		sec += SEC_IN_DAY;
		this.forecast.add(new WeatherCondition(TEMPERATURE2, IWeatherCondition.COND_RAIN, new Date(sec * MS_IN_SEC)));
		sec += SEC_IN_DAY;
		this.forecast.add(new WeatherCondition(TEMPERATURE1, IWeatherCondition.COND_CLOUDY, new Date(sec * MS_IN_SEC)));
	}

	@Override
	public int getActivityProgress() {
		return (int) ((getDistance() * ONE_HUNDRED) / getTargetDistance());
	}

	@Override
	public int getBatteryLevel() {
		return ONE_HUNDRED - (int) (getDate().getTime() / ONE_THOUSAND) % ONE_HUNDRED;
	}

	@Override
	public int getBatteryMax() {
		return ONE_HUNDRED;
	}

	@Override
	public String getBatteryLevelStr() {
		int temp = getBatteryLevel();
		StringBuffer str = new StringBuffer();

		if (temp / BATTERY_DIVISION > 0) {
			str.append(temp / BATTERY_DIVISION);
			str.append("d "); //$NON-NLS-1$
		}

		str.append(temp % BATTERY_DIVISION);
		str.append("h left"); //$NON-NLS-1$
		return str.toString();
	}

	@Override
	public Date getDate() {
		return new Date();
	}

	@Override
	public int getDay() {

		Calendar calendar = Calendar.getInstance();

		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public int getDayOfWeek() {

		Calendar calendar = Calendar.getInstance();

		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	@Override
	public float getDistance() {
		return ((getMinute() % DISTANCE_TIME) * SECONDS_IN_A_MINUTE + getSecond()) * getTargetDistance()
				/ (DISTANCE_TIME * SECONDS_IN_A_MINUTE);
	}

	@Override
	public Event getEvent(int id) {
		return this.events.get(id);
	}

	@Override
	public int getEventsCount() {
		/*
		 * String[] text = { "Wild", "Nice", "Happy", "Fierce", "Red", "Random"
		 * }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		 * //$NON-NLS-5$//$NON-NLS-6$ // $NON-NLS-5$ //$NON-NLS-6$ String[]
		 * text2 = { "vase", "duck", "frame", "birthday", "flour", "table",
		 * "screen", "colibri" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		 * //$NON-NLS-4$ //$NON-NLS-5$//$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		 * // $NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		 * //$NON-NLS-7$ // $NON-NLS-8$ if (this.events.size() < MAX_NOTIF &&
		 * Rand.nextInt(1000) <= NOTIFICATION_LIKELINESS) { int offset =
		 * Rand.nextInt() % text.length; offset = (offset < 0) ? -offset :
		 * offset; StringBuffer temp = new StringBuffer(text[offset]);
		 * temp.append(' '); offset = Rand.nextInt() % text2.length; offset =
		 * (offset < 0) ? -offset : offset; temp.append(text2[offset]);
		 * 
		 * this.events.add(this.events.size(), new Event(getDate(),
		 * temp.toString(), "Oct. 6", "08:0" + this.events.size()));
		 * //$NON-NLS-1$ //$NON-NLS-2$ }
		 */

		return this.events.size();
	}

	@Override
	public IWeatherCondition getForecast(int id) {
		return this.forecast.get(id);
	}

	@Override
	public int getForecastCount() {
		return this.forecast.size();
	}

	@Override
	public int getHour() {

		Calendar calendar = Calendar.getInstance();

		return calendar.get(Calendar.HOUR);
	}

	@Override
	public int getMinute() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MINUTE);
	}

	@Override
	public int getMonth() {

		Calendar calendar = Calendar.getInstance();

		return calendar.get(Calendar.MONTH);
	}

	@Override
	public int getSecond() {

		Calendar calendar = Calendar.getInstance();

		return calendar.get(Calendar.SECOND);
	}

	@Override
	public float getTargetDistance() {
		return TARGET_DISTANCE;
	}

	@Override
	public int getYear() {

		Calendar calendar = Calendar.getInstance();

		return calendar.get(Calendar.YEAR);
	}

	@Override
	public String getDateStr() {
		return Integer.toString(getDay()) + Constants.MONTH_SHORT[getMonth()];
	}

	@Override
	public String getDayStr() {
		return Constants.DAY_SHORT[getDayOfWeek() - Calendar.SUNDAY];
	}

	@Override
	public boolean getAm() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.AM_PM) == Calendar.AM;
	}

	/**
	 * @param notificationString
	 *            Event string.
	 * @param time
	 *            Event time.
	 */
	@Override
	public void addEvent(String notificationString, Date time) {
		int hour = getHour();
		int min = getMinute();
		StringBuilder dateStr = new StringBuilder();
		dateStr.append(hour);
		dateStr.append(":"); //$NON-NLS-1$
		if (min < 10) {
			dateStr.append("0"); //$NON-NLS-1$
		}
		dateStr.append(min);
		while (this.events.size() >= MAX_NOTIF) {
			this.events.remove(0);
		}
		this.events.add(new Event(time, notificationString, "", dateStr.toString())); //$NON-NLS-1$

	}

	@Override
	public void removeEvent(Event event) {
		this.events.remove(event);

	}

}
