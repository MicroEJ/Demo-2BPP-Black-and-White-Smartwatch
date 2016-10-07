/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package ej.demo.smartwatch;

import java.io.IOException;

import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.demo.smartwatch.style.Images;
import ej.demo.smartwatch.utils.Constants;
import ej.exit.ExitHandler;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.event.Event;
import ej.microui.event.EventGenerator;
import ej.microui.event.generator.Pointer;
import ej.mwt.Panel;

/**
 * A panel drawing the background and a browser button.
 */
public class BackgroundPanel extends Panel {
	private Image browser;
	private int browserTop;
	private int browserLeft;

	/**
	 * Instantiate the background panel.
	 */
	public BackgroundPanel() {
		try {
			browser = Image.createImage(Images.Strore);

		} catch (IOException e) {
		}
	}

	@Override
	public void validate(int widthHint, int heightHint) {
		browserLeft = (Constants.BROWSER_WIDTH - browser.getWidth()) / 2;
		browserTop = (getHeight() - browser.getHeight()) / 2;
		super.validate(widthHint, heightHint);
	}

	@Override
	public void render(GraphicsContext g) {
		// Paint background
		g.setColor(Constants.COLOR_BACKGROUND);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());

		// Draw separation line.
		g.setColor(Constants.COLOR_FOREGROUND);
		AntiAliasedShapes aliasedShapes = AntiAliasedShapes.Singleton;
		aliasedShapes.setFade(2);
		aliasedShapes.setThickness(1);
		aliasedShapes.drawLine(g, Constants.BROWSER_WIDTH - 1, 0, Constants.BROWSER_WIDTH - 1, g.getClipHeight());

		// Draw browser icon.
		g.drawImage(browser, browserLeft, browserTop, GraphicsContext.TOP | GraphicsContext.LEFT);
		super.render(g);
	}

	@Override
	public boolean handleEvent(int event) {
		// Handles only pointer release event on browser button.
		if (Event.getType(event) == Event.POINTER) {
			Pointer p = (Pointer) EventGenerator.get(Event.getGeneratorID(event));
			if (Pointer.isReleased(event) && browserPressed(p.getX(), p.getY())) {
				ExitHandler exitHandler = ServiceLoaderFactory.getServiceLoader().getService(ExitHandler.class);
				if (exitHandler != null) {
					exitHandler.exit();
				}
				return false;
			}
		}
		return false;
	}


	/**
	 * Check if a position is within the browser button.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @return True if the position is within the browser button.
	 */
	private boolean browserPressed(int x, int y) {
		return x >= browserLeft && x <= browserLeft + browser.getWidth() && y >= browserTop
				&& x <= browserTop + browser.getHeight();
	}
}
