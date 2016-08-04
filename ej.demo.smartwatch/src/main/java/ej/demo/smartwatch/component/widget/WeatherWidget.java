/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component.widget;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.demo.smartwatch.component.Direction;
import ej.demo.smartwatch.component.ScreenArea;
import ej.demo.smartwatch.dal.ISmDataProvider;
import ej.demo.smartwatch.dal.ISmDataProvider.WeatherCondition;
import ej.demo.smartwatch.dal.SmDataPrivider;
import ej.demo.smartwatch.style.Images;
import ej.demo.smartwatch.utils.Constants;
import ej.demo.smartwatch.utils.Utils;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;

/**
 *
 */
public class WeatherWidget extends MultipleViewWidget implements Animation {

	/**
	 * Rate to refresh the icons.
	 */
	private static final int REFRESH_RATE = 75;

	/**
	 * Data provider.
	 */
	private static final ISmDataProvider PROVIDER = SmDataPrivider.get();

	private static final String TAG = "WeatherWidget"; //$NON-NLS-1$

	/**
	 * The ratio for width and height in only computed at runtime, after the
	 * static variable had been init. This boolean is used to know if the had
	 * been init.
	 */
	private static boolean STATIC_VARIABLE_INITIALIZED = false;

	/**
	 * Font used for the temperature.
	 */
	private static Font FONT_36 = Constants.FONT_36;

	/**
	 * Font used for the text.
	 */
	private static Font FONT_24 = Constants.FONT_24;

	/**
	 * Line spacing.
	 */
	private static int LINE_HEIGHT;

	/**
	 * Current displayed forecast.
	 */
	private WeatherCondition currentForecast;

	/**
	 * Current weather image displayed.
	 */
	private Image currentImage;

	/**
	 * Index of the current displayed image.
	 */
	private int imgIndex;

	/**
	 * Last tick during the animation, -1 when not animated.
	 */
	private long lastTick = -1;

	/**
	 * Height of the biggest image.
	 */
	private int maxImageHeight;

	/**
	 * Width of the biggest image.
	 */
	private int maxImageWidth;

	/**
	 * Weather widget.
	 *
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @param position
	 *            The position.
	 */
	public WeatherWidget(int width, int height, ScreenArea position) {
		super(width, height, position);
		if (!STATIC_VARIABLE_INITIALIZED) {
			FONT_36 = Constants.FONT_36;
			FONT_24 = Constants.FONT_24;
			LINE_HEIGHT = FONT_24.getHeight();
			STATIC_VARIABLE_INITIALIZED = true;
		}
		// get the max animation area
		this.maxImageHeight = this.maxImageWidth = 0;
		for (

		int i = 0; i < Images.WEATHER_COND_SEQ.length; i++) {
			// all images within an sequence should have the same size
			Image cImg = Images.WEATHER_COND_SEQ[i].getImg(0);
			if (this.maxImageHeight < cImg.getHeight()) {
				this.maxImageHeight = cImg.getHeight();
			}
			if (this.maxImageWidth < cImg.getWidth()) {
				this.maxImageWidth = cImg.getWidth();
			}
		}

		switchForecast(true);
		this.currentImage = Images.WEATHER_COND_SEQ[this.currentForecast.getCondition()].getImg(0);
	}

	@Override
	public String getTag() {
		return TAG;
	}

	/**
	 * Get the temperature string.
	 *
	 * @return The temperature as a string.
	 */
	private String getTemperature() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.currentForecast.getTemp());
		sb.append('º');
		return sb.toString();
	}

	@Override
	public void redraw(GraphicsContext g, Direction direction, int stage, int x, int y) {
		g.setColor(Constants.COLOR_FOREGROUND);
		drawEncapsulatingCircle(g, direction, stage, x, y);

		float ratio = (float) stage / Constants.TRANSITION_HIGH;
		Image img = this.currentImage;
		if (direction == Direction.ToEdge) {
			stopAnimation();
		}
		drawNextPrevious(g, direction, stage);

		if (direction == Direction.ToCenter || direction == Direction.ToEdge || direction == Direction.CenterStill) {
			int yCenter = (getHeight() - img.getHeight()) / 2;
			int xCenter = (getWidth() - img.getWidth()) / 2;
			g.setFont(FONT_36);
			String temperature = getTemperature();
			int xText = (getWidth() - FONT_36.stringWidth(temperature)) / 2;
			int yText = getHeight() - FONT_36.getHeight() - Y_PADDING;

			int imageWidth = -img.getWidth();

			// Draw the image.
			int xImg;
			int xString;
			if (direction == Direction.ToEdge) {
				xImg = (int) ((1.0f - ratio) * xCenter + ratio * imageWidth);
				xString = (int) ((1.0f - ratio) * xText + ratio * X_PADDING);
			} else if (direction == Direction.CenterStill) {
				xImg = xCenter;
				xString = xText;
			} else {
				xImg = (int) ((1.0f - ratio) * imageWidth + ratio * xCenter);
				xString = (int) ((1.0f - ratio) * X_PADDING + ratio * xText);
			}
			g.drawImage(this.currentImage, xImg, yCenter, 0);

			// Draw the temperature.
			g.drawString(temperature, xString, yText, 0);

			// Draw the date.
			String text = this.currentForecast.getRelDateStr();
			g.setFont(FONT_24);
			if (direction == Direction.ToEdge) {
				ratio = (float) (Constants.TRANSITION_HIGH - stage) / Constants.TRANSITION_HIGH;
				g.drawString(text, getWidth() / 2 - FONT_24.stringWidth(text) / 2,
						Utils.computeMean(LINE_HEIGHT, -LINE_HEIGHT, ratio),
						GraphicsContext.VCENTER | GraphicsContext.LEFT);
			} else if (direction == Direction.ToCenter) {
				ratio = (float) stage / Constants.TRANSITION_HIGH;
				g.drawString(text, getWidth() / 2 - FONT_24.stringWidth(text) / 2,
						Utils.computeMean(LINE_HEIGHT, -LINE_HEIGHT, ratio),
						GraphicsContext.VCENTER | GraphicsContext.LEFT);
			} else {
				g.drawString(text, getWidth() / 2 - FONT_24.stringWidth(text) / 2, LINE_HEIGHT,
						GraphicsContext.VCENTER | GraphicsContext.LEFT);
			}
		} else {
			// Draw the temperature.
			String temperature = getTemperature();
			int xString = x - X_PADDING;
			int yString = getHeight() - FONT_36.getHeight() - Y_PADDING;
			g.setFont(FONT_36);
			g.drawString(temperature, xString, yString, 0);
		}
	}

	@Override
	public void render(GraphicsContext g) {
		super.render(g);

		if (this.direction == Direction.CenterStill) {
			g.setColor(Constants.COLOR_BACKGROUND);
			g.fillRect(getX() - this.maxImageWidth / 2, (getHeight() - this.maxImageHeight) / 2, this.maxImageWidth,
					this.maxImageHeight);

			if (this.currentImage != null) {
				g.drawImage(this.currentImage, getX() - this.currentImage.getWidth() / 2,
						(getHeight() - this.currentImage.getHeight()) / 2, 0);
			}
		}
	}

	private void switchForecast(boolean next) {
		nextView(next);

		this.currentForecast = PROVIDER.getForecast(this.viewIndex);
		this.currentImage = Images.WEATHER_COND_SEQ[this.currentForecast.getCondition()].getImg(0);
	}

	@Override
	public DatePosition getDatePosition() {
		return DatePosition.POSITION2;
	}

	@Override
	public void switchView(boolean right) {
		switchForecast(!right);
	}

	@Override
	protected String getDate() {
		return this.currentForecast.getDateStr();
	}

	@Override
	public boolean tick(long currentTimeMillis) {
		if (this.lastTick == -1) {
			return false;
		}
		if (currentTimeMillis - this.lastTick >= REFRESH_RATE) {
			this.lastTick = currentTimeMillis;
			this.currentImage = Images.WEATHER_COND_SEQ[this.currentForecast.getCondition()].getImg(
					this.imgIndex++ % Images.WEATHER_COND_SEQ[this.currentForecast.getCondition()].getImgCount());
		}
		return true;
	}

	@Override
	public void startAnimation() {
		if (this.lastTick == -1) {
			this.lastTick = 0;
			Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
			animator.startAnimation(this);
		}
	}

	@Override
	public void stopAnimation() {
		this.lastTick = -1;
	}

	@Override
	protected int viewCount() {
		return PROVIDER.getForecastCount();
	}
}
