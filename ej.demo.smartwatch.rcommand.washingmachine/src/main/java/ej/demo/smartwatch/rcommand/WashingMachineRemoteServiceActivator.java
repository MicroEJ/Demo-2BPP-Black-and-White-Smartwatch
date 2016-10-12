/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ej.demo.smartwatch.rcommand;

import ej.components.registry.BundleRegistry;
import ej.components.registry.util.BundleRegistryHelper;
import ej.rcommand.RemoteService;
import ej.rcommand.activators.RemoteServiceActivator;

/**
 * Activator for the Washing Machine Remote service
 *
 */
public class WashingMachineRemoteServiceActivator extends RemoteServiceActivator {

	private WashingMachineNotificationHandler notificationService;

	@Override
	public RemoteService newRemoteService() {
		this.notificationService = new WashingMachineNotificationHandler();
		BundleRegistry registry = BundleRegistryHelper.getRegistry();
		registry.register(WashingMachineNotificationHandler.class, this.notificationService);
		return this.notificationService;
	}

}
