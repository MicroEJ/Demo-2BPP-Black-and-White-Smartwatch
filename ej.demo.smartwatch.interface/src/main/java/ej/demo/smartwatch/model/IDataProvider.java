/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.model;

import java.util.Date;

/**
 * The Interface ISmDataProvider.
 */
public interface IDataProvider {

	/* Date and time */
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	Date getDate();

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	String getDateStr();

	/**
	 * Gets the second.
	 *
	 * @return the second
	 */
	int getSecond();

	/**
	 * Gets the minute.
	 *
	 * @return the minute
	 */
	int getMinute();

	/**
	 * Gets the hour.
	 *
	 * @return the hour
	 */
	int getHour();

	/**
	 * Gets the day.
	 *
	 * @return the day
	 */
	int getDay();

	/**
	 * Gets the day.
	 *
	 * @return the day as string
	 */
	String getDayStr();

	/**
	 * Gets the day of week.
	 *
	 * @return the day of week
	 */
	int getDayOfWeek();

	/**
	 * Gets the month.
	 *
	 * @return the month
	 */
	int getMonth();

	/**
	 * Gets the year.
	 *
	 * @return the year
	 */
	int getYear();

	/* Activity */
	/**
	 * Gets the distance.
	 *
	 * @return the distance
	 */
	float getDistance();

	/**
	 * Gets the target distance.
	 *
	 * @return the target distance
	 */
	float getTargetDistance();

	/**
	 * Gets the activity progress.
	 *
	 * @return the activity progress
	 */
	int getActivityProgress();

	/* Battery */
	/**
	 * Gets the battery level.
	 *
	 * @return the battery level
	 */
	int getBatteryLevel();

	/**
	 * Gets a message that is printed below the battery.
	 *
	 * @return the battery message
	 */
	String getBatteryLevelStr();

	/**
	 * Gets the max battery value.
	 *
	 * @return The battery max value.
	 */
	int getBatteryMax();

	/* Events */
	/**
	 * The Class Event.
	 */
	class Event {

		/** The date. */
		private Date date;

		/** The date. */
		private final String dateStr;

		/** The message. */
		private String message;

		/** The time. */
		private final String timeStr;

		/**
		 * Instantiates a new event.
		 *
		 * @param date
		 *            the date
		 * @param message
		 *            the message
		 * @param dateStr
		 *            dateStr
		 * @param timeStr
		 *            timeStr
		 */
		public Event(Date date, String message, String dateStr, String timeStr) {
			super();
			this.date = date;
			this.dateStr = dateStr;
			this.message = message;
			this.timeStr = timeStr;
		}

		/**
		 * Gets the date.
		 *
		 * @return the date
		 */
		public Date getDate() {
			return date;
		}

		/**
		 * Sets the date.
		 *
		 * @param date
		 *            the new date
		 */
		public void setDate(Date date) {
			this.date = date;
		}

		/**
		 * Gets the message.
		 *
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * Sets the message.
		 *
		 * @param message
		 *            the new message
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		public String getDateStr() {
			return dateStr;
		}

		public String getTimeStr() {
			return timeStr;
		}
	}

	/**
	 * Gets the events count.
	 *
	 * @return the events count
	 */
	int getEventsCount();

	/**
	 * Gets the event.
	 *
	 * @param id
	 *            the id
	 * @return the event
	 */
	Event getEvent(int id);

	/**
	 * Gets the forecast count.
	 *
	 * @return the forecast count
	 */
	int getForecastCount();

	/**
	 * Gets the forecast.
	 *
	 * @param id
	 *            the id
	 * @return the forecast
	 */
	IWeatherCondition getForecast(int id);

	/**
	 * @return true if the current time is AM.
	 */
	boolean getAm();

	/**
	 * @param event
	 *            the event to remove.
	 */
	void removeEvent(Event event);

	/**
	 * @param notificationString
	 *            Event string.
	 * @param time
	 *            Event time.
	 */
	void addEvent(String notificationString, Date time);

}
