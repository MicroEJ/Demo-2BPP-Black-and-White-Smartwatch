/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch;

import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.demo.smartwatch.component.BubbleWidget;
import ej.demo.smartwatch.component.SmartWatch;
import ej.demo.smartwatch.component.widget.BatteryWidget;
import ej.demo.smartwatch.component.widget.DistanceWidget;
import ej.demo.smartwatch.component.widget.MultipleViewWidget;
import ej.demo.smartwatch.component.widget.NotificationsWidget;
import ej.demo.smartwatch.component.widget.WeatherWidget;
import ej.demo.smartwatch.component.widget.clock.Digital;
import ej.demo.smartwatch.utils.Constants;
import ej.microui.MicroUI;
import ej.microui.display.Display;
import ej.mwt.Desktop;
import ej.mwt.Panel;

/**
 * Main application entry point.
 *
 */
public class SmartWatchApp {

	// initialize
	static {
		MicroUI.start();

		Display display = Display.getDefaultDisplay();
		Constants.initialize(display.getWidth() - Constants.STORE_WIDTH, display.getHeight());
		Digital.initialize();
		BatteryWidget.initialize();
		DistanceWidget.initialize();
		MultipleViewWidget.initialize();
		NotificationsWidget.initialize();
		WeatherWidget.initialize();
		BubbleWidget.initialize();
		SmartWatchRobot.initialize();

	}
	// Prevents initialization.
	private SmartWatchApp() {
	}

	/**
	 * Application entry point..
	 *
	 * @param args
	 *            not used.
	 */
	public static void main(String[] args) {
		// Start MicroUI.
		MicroUI.start();

		// Initialize UI
		ServiceLoaderFactory.getServiceLoader().getService(Animator.class).setPeriod(80);
		Display display = Display.getDefaultDisplay();
		Desktop desktop = new Desktop(display);
		SmartWatch smartWatch = new SmartWatch(Constants.DISPLAY_WIDTH, Constants.DISPLAY_HEIGHT);
		Panel mainPage = new Panel();
		mainPage.setWidget(smartWatch);

		// Start robot.
		SmartWatchRobot robot = new SmartWatchRobot(smartWatch);
		robot.start();

		// Start Display.
		Panel backgroundPanel = new BackgroundPanel();
		backgroundPanel.setBounds(0, 0, Constants.STORE_WIDTH, Constants.DISPLAY_HEIGHT);
		backgroundPanel.setPacked(false);
		backgroundPanel.show(desktop);
		mainPage.setPacked(false);
		mainPage.setBounds(Constants.STORE_WIDTH, 0, Constants.DISPLAY_WIDTH, Constants.DISPLAY_HEIGHT);
		mainPage.show(desktop);
		desktop.show();
	}
}