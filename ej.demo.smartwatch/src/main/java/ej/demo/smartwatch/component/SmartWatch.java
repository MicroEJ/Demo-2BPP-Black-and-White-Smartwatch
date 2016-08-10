/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component;

import java.io.IOException;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.demo.smartwatch.component.widget.BatteryWidget;
import ej.demo.smartwatch.component.widget.DateTimeWidget;
import ej.demo.smartwatch.component.widget.DistanceWidget;
import ej.demo.smartwatch.component.widget.NotificationsWidget;
import ej.demo.smartwatch.component.widget.WeatherWidget;
import ej.demo.smartwatch.style.Images;
import ej.demo.smartwatch.utils.Constants;
import ej.demo.smartwatch.utils.Log;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.event.Event;
import ej.microui.event.EventGenerator;
import ej.microui.event.generator.Buttons;
import ej.microui.event.generator.Pointer;
import ej.mwt.Composite;

/**
 * Composite of a smartwatch.
 */
public class SmartWatch extends Composite implements Animation {
	private static final String TAG = "MainWidget"; //$NON-NLS-1$

	/**
	 * Main controller.
	 */
	final WatchController controler = new WatchController(this);

	/**
	 * Whether a drag is ongoing.
	 */
	private boolean dragged = false;

	private int oldX, oldY;
	private boolean isAnimated = false;

	private Image store;

	private int storeLeft;
	private int storeTop;

	/**
	 * @param width
	 *            width
	 * @param height
	 *            height
	 */
	public SmartWatch(int width, int height) {
		super();
		Log.d(TAG, "New  widget: " + width + " " + height); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			store = Image.createImage(Images.Strore);
			storeLeft = width / 2 - store.getWidth() / 2;
			storeTop = height - store.getHeight();
		} catch (IOException e) {
		}
		// Add all the bubbles.
		DateTimeWidget dateTimeWidget = new DateTimeWidget(width, height, ScreenArea.Center);
		this.controler.add(dateTimeWidget);

		NotificationsWidget notificationsWidget = new NotificationsWidget(width, height, ScreenArea.TopLeft);
		this.controler.add(notificationsWidget);

		WeatherWidget weatherWidget = new WeatherWidget(width, height, ScreenArea.BottomLeft);
		this.controler.add(weatherWidget);

		BatteryWidget batteryWidget = new BatteryWidget(width, height, ScreenArea.TopRight);
		this.controler.add(batteryWidget);

		DistanceWidget distanceWidget = new DistanceWidget(width, height, ScreenArea.BottomRight);
		this.controler.add(distanceWidget);

		startAnimation();
	}

	@Override
	public boolean handleEvent(int event) {
		if (Event.getType(event) == Event.POINTER) {
			Pointer p = (Pointer) EventGenerator.get(Event.getGeneratorID(event));
			if (Buttons.getAction(event) == Pointer.DRAGGED) {
				this.dragged = true;
			} else if (Buttons.getAction(event) == Buttons.PRESSED) {
				this.oldX = p.getX();
				this.oldY = p.getY();
			} else if (Buttons.getAction(event) == Buttons.RELEASED) {
				final int x = p.getX();
				final int y = p.getY();

				if (this.dragged) {
					this.dragged = false;
					this.controler.swipe(this.oldX, this.oldY, x, y);
				} else {
					this.controler.input(x, y);
				}
			}

			return true;
		}
		return false;
	}

	/**
	 * Start the animation.
	 */
	public synchronized void startAnimation() {
		if (!this.isAnimated) {
			this.isAnimated = true;
			Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
			animator.startAnimation(this);
		}
	}

	/**
	 * Stop the animation.
	 */
	public synchronized void stopAnimation() {
		this.isAnimated = false;
	}

	@Override
	public void render(GraphicsContext g) {
		g.setBackgroundColor(Constants.COLOR_BACKGROUND);
		g.setColor(Constants.COLOR_BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Constants.COLOR_FOREGROUND);
		g.drawImage(store, storeLeft, storeTop,
				GraphicsContext.TOP | GraphicsContext.LEFT);
		this.controler.realign(g);
	}

	@Override
	public void validate(int widthHint, int heightHint) {
		setPreferredSize(widthHint, heightHint);
		this.controler.validate(widthHint, heightHint);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		this.controler.setBounds(x, y, width, height);
	}

	@Override
	public boolean tick(long currentTimeMillis) {
		if (!this.isAnimated) {
			return false;
		}
		repaint();
		return true;
	}

	/**
	 * Get the watch's controller.
	 *
	 * @return the watch's controller.
	 */
	public WatchController getController() {
		return this.controler;
	}

	public boolean boundingBoxContains(int x, int y) {
		return (x >= storeLeft) && (x <= storeLeft + store.getWidth()) && (y >= storeTop)
				&& (y <= storeTop + store.getHeight());
	}
}
