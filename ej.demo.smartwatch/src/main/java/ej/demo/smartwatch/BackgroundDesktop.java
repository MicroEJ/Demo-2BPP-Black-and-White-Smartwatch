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
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.display.shape.AntiAliasedShapes;
import ej.microui.event.Event;
import ej.microui.event.EventGenerator;
import ej.microui.event.generator.Pointer;
import ej.mwt.Desktop;

/**
 * A panel drawing the background and a browser button.
 */
public class BackgroundDesktop extends Desktop {
	private Image browser;
	private static final int browserTop = 6;
	private static final int browserLeft = 6;
	private final int xOffset;
	private final int yOffset;
	private static final int BACKGROUND_COLOR = 0x4B5357;
	private static final int BORDER_COLOR = 0x717D83;

	/**
	 * Instantiate the background panel.
	 *
	 * @param yOffset
	 * @param xOffset
	 */
	public BackgroundDesktop(Display display, int xOffset, int yOffset) {
		super(display);
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		try {
			browser = Image.createImage(Images.Strore);
		} catch (IOException e) {
		}
	}

	@Override
	public void render(GraphicsContext g) {
		// Draw background.
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, getDisplay().getWidth(), getDisplay().getHeight());

		// Draw separation lines.
		g.setColor(BORDER_COLOR);
		AntiAliasedShapes aliasedShapes = AntiAliasedShapes.Singleton;
		aliasedShapes.setFade(2);
		aliasedShapes.setThickness(1);

		int top = yOffset - 1;
		int left = xOffset - 1;
		int right = xOffset + Constants.DISPLAY_WIDTH;
		int bottom = yOffset + Constants.DISPLAY_HEIGHT;
		aliasedShapes.drawLine(g, left, top, right, top);
		aliasedShapes.drawLine(g, left, top, left, bottom);
		aliasedShapes.drawLine(g, right, top, right, bottom);
		aliasedShapes.drawLine(g, left, bottom, right, bottom);

		g.setColor(Colors.WHITE);
		// Draw browser icon.
		g.drawImage(browser, browserLeft, browserTop, GraphicsContext.TOP | GraphicsContext.LEFT);
		super.render(g);
	}

	@Override
	public boolean handleEvent(int event) {
		// Handles only pointer release event on browser button.
		if (Event.getType(event) == Event.POINTER) {
			Pointer p = (Pointer) EventGenerator.get(Event.getGeneratorID(event));
			if (Pointer.isPressed(event) && browserPressed(p.getX(), p.getY())) {
				ExitHandler exitHandler = ServiceLoaderFactory.getServiceLoader().getService(ExitHandler.class);
				if (exitHandler != null) {
					exitHandler.exit();
				}
				return true;
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
