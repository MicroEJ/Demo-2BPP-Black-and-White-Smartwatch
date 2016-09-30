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
 *
 */
public class BackgroundPanel extends Panel {
	private Image store;
	private int storeTop;
	private int storeLeft;

	/**
	 *
	 */
	public BackgroundPanel() {
		try {
			store = Image.createImage(Images.Strore);

		} catch (IOException e) {
		}
	}

	@Override
	public void validate(int widthHint, int heightHint) {
		storeLeft = (Constants.STORE_WIDTH - store.getWidth()) / 2;
		storeTop = (getHeight() - store.getHeight()) / 2;
		super.validate(widthHint, heightHint);
	}

	@Override
	public void render(GraphicsContext g) {
		g.setColor(Constants.COLOR_BACKGROUND);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
		g.setColor(Constants.COLOR_FOREGROUND);
		AntiAliasedShapes aliasedShapes = AntiAliasedShapes.Singleton;
		aliasedShapes.setFade(2);
		aliasedShapes.setThickness(1);
		aliasedShapes.drawLine(g, Constants.STORE_WIDTH - 1, 0, Constants.STORE_WIDTH - 1, g.getClipHeight());
		g.drawImage(store, storeLeft, storeTop, GraphicsContext.TOP | GraphicsContext.LEFT);
		super.render(g);
	}

	@Override
	public boolean handleEvent(int event) {
		if (Event.getType(event) == Event.POINTER) {
			Pointer p = (Pointer) EventGenerator.get(Event.getGeneratorID(event));
			if (Pointer.isReleased(event) && storePressed(p.getX(), p.getY())) {
				System.out.println("BackgroundPanel.handleEvent()");
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
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean storePressed(int x, int y) {
		return x >= storeLeft && x <= storeLeft + store.getWidth() && y >= storeTop
				&& x <= storeTop + store.getHeight();
	}
}
