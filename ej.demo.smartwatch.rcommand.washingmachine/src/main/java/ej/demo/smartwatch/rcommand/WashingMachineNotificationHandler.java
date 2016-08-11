package ej.demo.smartwatch.rcommand;
/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.util.Calendar;

import ej.demo.smartwatch.model.DataProvider;
import ej.washingmachine.remote.Category;
import ej.washingmachine.remote.RemoteWashingMachineService;
import ej.washingmachine.remote.rcommand.RemoteWashingMachineServiceImpl;

/**
 * Notification Handler.
 *
 * <p>
 * Sends notification for every action that is performed by the washing machine.
 * </p>
 */
public class WashingMachineNotificationHandler extends RemoteWashingMachineServiceImpl
implements RemoteWashingMachineService {

	/**
	 *
	 */
	public WashingMachineNotificationHandler() {
		super();
	}

	@Override
	public void newNotification(int category, String message, long timestamp) {
		String notificationString = getNotificationString(category, message);
		if (notificationString != null) {
			DataProvider.getInstance().addEvent(notificationString, Calendar.getInstance().getTime());
		}
	}

	/**
	 * @param category
	 * @param message
	 * @return
	 */
	private String getNotificationString(int category, String message) {
		switch (category) {
		case Category.STATUS:
			if (message.equalsIgnoreCase("true")) {
				return "Laundry started.";
			} else {
				return "Laundry finished.";
			}
		case Category.SPIN:
			return "Spin " + message;
		case Category.TEMPERATURE:
			return "Temprature " + message;
		case Category.TYPE:
			return "Type " + message;
		case Category.STARTING_TIME:
		default:
			return null;
		}
	}

}
