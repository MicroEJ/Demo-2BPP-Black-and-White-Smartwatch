/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ej.demo.smartwatch.rcommand;

import ej.date.DateListener;
import ej.date.rcommand.RemoteDateService;
import ej.rcommand.RemoteService;
import ej.rcommand.activators.RemoteServiceActivator;

/**
 *
 */
public class DateServiceActivator extends RemoteServiceActivator implements DateListener {

	private RemoteDateService remoteDateService;

	@Override
	public RemoteService newRemoteService() {
		this.remoteDateService = new RemoteDateService();
		this.remoteDateService.addDateListener(this);
		return this.remoteDateService;
	}

	@Override
	public void dateUpdated(long currentTimeMs) {
		ej.bon.Util.setCurrentTimeMillis(currentTimeMs);
	}

	@Override
	public void start() {
		super.start();
		this.remoteDateService.requestDate();
	}

	@Override
	public void stop() {
		this.remoteDateService.removeDateListener(this);
		super.stop();
	}
}
