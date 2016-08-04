/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch;

import ej.bon.Util;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.components.registry.BundleRegistry;
import ej.components.registry.util.BundleRegistryHelper;
import ej.demo.smartwatch.component.SmartWatch;
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

		// Initialise system.
		Util.setCurrentTimeMillis(Constants.START_TIME * Constants.MS_IN_SEC);
		BundleRegistry registry = ServiceLoaderFactory.getServiceLoader().getService(BundleRegistry.class);
		BundleRegistryHelper.startup(registry);

		// Initialise UI
		Display display = Display.getDefaultDisplay();
		Desktop desktop = new Desktop(display);
		SmartWatch smartWatch = new SmartWatch(Constants.WIDTH, Constants.HEIGHT);
		Panel mainPage = new Panel();
		mainPage.setWidget(smartWatch);

		// Start robot.
		SmartWatchRobot robot = new SmartWatchRobot(smartWatch);
		robot.start();

		// Start ARGB4444.
		mainPage.show(desktop, true);
		desktop.show();
	}
}