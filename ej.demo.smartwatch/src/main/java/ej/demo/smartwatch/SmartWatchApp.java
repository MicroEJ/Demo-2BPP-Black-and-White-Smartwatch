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
import ej.components.registry.BundleRegistry;
import ej.components.registry.util.BundleRegistryHelper;
import ej.demo.smartwatch.component.SmartWatch;
import ej.demo.smartwatch.model.DataProvider;
import ej.demo.smartwatch.model.FakeDataProvider;
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
	// initialize data provider.
	static {
		DataProvider.setInstance(new FakeDataProvider());
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

		BundleRegistry registry = ServiceLoaderFactory.getServiceLoader().getService(BundleRegistry.class);
		BundleRegistryHelper.startup(registry);

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

		// Start ARGB4444.
		mainPage.show(desktop, true);
		desktop.show();
	}
}