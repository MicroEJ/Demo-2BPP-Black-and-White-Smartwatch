/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component.widget;

import java.util.ArrayList;

import ej.demo.smartwatch.component.BubbleWidget;
import ej.demo.smartwatch.component.Direction;
import ej.demo.smartwatch.component.ScreenArea;
import ej.demo.smartwatch.component.widget.clock.BlackClock;
import ej.demo.smartwatch.component.widget.clock.Digital;
import ej.demo.smartwatch.component.widget.clock.IClock;
import ej.demo.smartwatch.component.widget.clock.WhiteClock;
import ej.demo.smartwatch.model.DataProvider;
import ej.demo.smartwatch.utils.Constants;
import ej.microui.display.GraphicsContext;

/**
 * Widget for date/time.
 */
public class DateTimeWidget extends BubbleWidget {

	/**
	 * All clock faces.
	 */
	private final ArrayList<IClock> clocks = new ArrayList<>();

	/**
	 * Current clock face position.
	 */
	private int currentClock = 0;

	/**
	 * Previous clock when switching faces.
	 */
	private int previousClock;

	/**
	 * Default clock.
	 */
	private final IClock defaultClock;

	/**
	 * True if is switching face.
	 */
	private boolean faceSwitch = false;

	/**
	 * True if the switch animation is going up.
	 */
	private boolean switchUp;

	private final Object mutex = new Object();

	/**
	 * A Widget displaying a clock.
	 *
	 * @param width
	 *            the clock width.
	 * @param height
	 *            the clock height.
	 * @param position
	 *            the clock position.
	 */
	public DateTimeWidget(int width, int height, ScreenArea position) {
		super(width, height, position);

		this.defaultClock = new WhiteClock(this.largeDiameter, this.smallDiameter);
		this.clocks.add(this.defaultClock);
		this.clocks.add(new BlackClock(this.largeDiameter, this.smallDiameter));
		this.clocks.add(new Digital(height, width));

		// Set the first clock as the default one.
		this.previousClock = 1;
	}

	/**
	 * Draw the clock.
	 *
	 * @param g
	 *            The graphic context.
	 * @param x
	 *            The center x.
	 * @param y
	 *            The center y.
	 */
	private void drawClock(GraphicsContext g, int x, int y) {
		this.getCurrentClock().draw(g, this.direction, DataProvider.getInstance(), x, y, this.transitionCompletion);
	}


	@Override
	public boolean isSwitchAnimated() {
		return true;
	}

	@Override
	public void redraw(GraphicsContext g, Direction direction, int completion, int x, int y) {
		if (!this.getCurrentClock().hasCornerFace()
				&& (direction == Direction.CornerStill || direction == Direction.CornerSwitch)) {
			// IF we are at corner, and the current face can't be displayed in corner.
			this.defaultClock.draw(g, direction, DataProvider.getInstance(), x, y, this.transitionCompletion);
		} else {
			drawClock(g, x, y);
		}
	}

	@Override
	public void render(GraphicsContext g) {
		super.render(g);

		if (this.direction == Direction.CenterStill) {
			g.setColor(Constants.COLOR_BACKGROUND);
			int x = getX();
			int y = getY();
			if (this.faceSwitch) {
				x = getWidth() / 2;
				int centerY = getHeight() / 2;
				int left = -this.smallDiameter;
				int right = getHeight() + this.smallDiameter;

				int yOtherClock = this.transitionCompletion * left / Constants.COMPLETION_MAX
						+ (Constants.COMPLETION_MAX - this.transitionCompletion) * centerY / Constants.COMPLETION_MAX;

				IClock topClock = (!this.switchUp) ? getPreviousClock() : getCurrentClock();
				IClock bottomClock = (this.switchUp) ? getPreviousClock() : getCurrentClock();

				y = this.transitionCompletion * centerY / Constants.COMPLETION_MAX
						+ (Constants.COMPLETION_MAX - this.transitionCompletion) * right / Constants.COMPLETION_MAX;
				topClock.draw(g, this.direction, DataProvider.getInstance(), x, yOtherClock, this.transitionCompletion);
				bottomClock.draw(g, this.direction, DataProvider.getInstance(), x, y, this.transitionCompletion);
			} else {
				this.getCurrentClock().draw(g, this.direction, DataProvider.getInstance(), x, y, this.transitionCompletion);
			}

		}
	}

	@Override
	public void startSwitchFace(boolean up) {
		synchronized (this.mutex) {
			this.switchUp = up;
			this.faceSwitch = true;

			// Switch to the next clock.
			this.previousClock = this.currentClock;
			if (!this.switchUp) {
				this.currentClock++;
				this.currentClock %= this.clocks.size();
			} else {
				this.currentClock--;
				this.currentClock += this.clocks.size();
				this.currentClock %= this.clocks.size();
			}
		}
	}

	@Override
	public void stopSwitchFace() {
		synchronized (this.mutex) {
			this.faceSwitch = false;
		}
	}

	@Override
	public void switchFace(GraphicsContext g, int completion) {
		super.switchFace(g, completion);
		render(g);
	}

	@Override
	public DatePosition getDatePosition() {
		return this.getCurrentClock().getDatePosition();
	}

	@Override
	public int countFaces() {
		return this.clocks.size();
	}

	/**
	 * Gets the current Clock.
	 *
	 * @return the current clock.
	 */
	private IClock getCurrentClock() {
		synchronized (this.mutex) {
			return this.clocks.get(this.currentClock);
		}
	}

	/**
	 *
	 * Gets the previous clock.
	 *
	 * @return Previous clock.
	 */
	private IClock getPreviousClock() {
		synchronized (this.mutex) {
			return this.clocks.get(this.previousClock);
		}
	}
}
