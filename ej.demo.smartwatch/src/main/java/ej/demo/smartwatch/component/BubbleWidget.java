/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component;

import ej.demo.smartwatch.dal.ISmDataProvider;
import ej.demo.smartwatch.dal.SmDataPrivider;
import ej.demo.smartwatch.utils.Constants;
import ej.demo.smartwatch.utils.Utils;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.mwt.Widget;

/**
 * The base class for the bubble.
 */
public class BubbleWidget extends Widget implements Bubble {

	/**
	 * Data provider.
	 */
	private static final ISmDataProvider PROVIDER = SmDataPrivider.get();

	/**
	 * Ratio to compute large diameter.
	 */
	private static final float LARGE_DIAMETER_RATIO = 153f / 120;

	/**
	 * Default horizontal padding.
	 */
	private static final int X_PADDING;

	/**
	 * Default vertical padding.
	 */
	private static final int Y_PADDING;

	static {
		X_PADDING = (int) (10 * Constants.WIDTH_RATIO);
		Y_PADDING = (int) (10 * Constants.HEIGHT_RATIO);
	}

	/**
	 * Current position of the widget.
	 */
	protected ScreenArea currentPosition;

	/**
	 * Diameter of the widget.
	 */
	protected int smallDiameter;

	/**
	 * Diameter when at the center.
	 */
	protected int largeDiameter;

	/**
	 * Direction of the animation.
	 */
	protected Direction direction;

	/**
	 * Whether the widget is in a face switch animation.
	 */
	protected boolean faceSwitchAnimation = false;

	/**
	 * Initial position.
	 */
	protected ScreenArea originalPosition;

	/**
	 * Transition state.
	 */
	protected int transitionState;

	/**
	 * Target position at the end of the animation.
	 */
	protected ScreenArea targetPosition;

	/**
	 * The main font of the bubble.
	 */
	protected Font font;

	/**
	 * Creates a new Bubble widget.
	 *
	 * @param width
	 *            width
	 * @param height
	 *            height
	 * @param position
	 *            position
	 */
	protected BubbleWidget(int width, int height, ScreenArea position) {
		this.originalPosition = position;
		this.targetPosition = position;
		this.currentPosition = position;
		setSize(width, height);
		setLocation(0, 0);
		this.faceSwitchAnimation = false;
		setState(Constants.TRANSITION_HIGH);
		this.font = Constants.FONT_24;
		repaint();
	}

	/**
	 * A point.
	 *
	 */
	protected class Point {
		/**
		 * X coordinate.
		 */
		private int x;

		/**
		 * Y coordinate.
		 */
		private int y;

		/**
		 * Point constructor.
		 *
		 * @param x
		 *            X coordinate.
		 * @param y
		 *            Y coordinate.
		 */
		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		/**
		 * Gets the x.
		 *
		 * @return the x.
		 */
		public int getX() {
			return this.x;
		}

		/**
		 * Sets the x.
		 *
		 * @param x
		 *            the x to set.
		 */
		public void setX(int x) {
			this.x = x;
		}

		/**
		 * Gets the y.
		 *
		 * @return the y.
		 */
		public int getY() {
			return this.y;
		}

		/**
		 * Sets the y.
		 *
		 * @param y
		 *            the y to set.
		 */
		public void setY(int y) {
			this.y = y;
		}

	}

	@Override
	public ScreenArea getCurrentPosition() {
		return this.currentPosition;
	}

	@Override
	public ScreenArea getOriginalPosition() {
		return this.originalPosition;
	}

	@Override
	public String getTag() {
		return "left: " + this.originalPosition.isLeft() + " top: " + this.originalPosition.isTop() + " center: " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ this.originalPosition.isCenter();
	}

	@Override
	public ScreenArea getTargetPosition() {
		return this.targetPosition;
	}

	@Override
	public int countFaces() {
		return 1;
	}

	@Override
	public boolean inRange(int x, int y) {
		int left = getX() - this.smallDiameter / 2;
		int top = getY() - this.smallDiameter / 2;
		int right = getX() + this.smallDiameter / 2;
		int bottom = getY() + this.smallDiameter / 2;
		if (left < x && top < y && right > x && bottom > y) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isSwitchAnimated() {
		return false;
	}

	/**
	 * Set the position of the widget depending on the state.
	 *
	 * @param state
	 *            current state.
	 */
	protected void realignXY(int state) {
		int x1, y1, x2, y2;

		if (state == Constants.TRANSITION_HIGH) {
			this.currentPosition = this.targetPosition;
		}

		setupDirection();

		// based on current/targetposition we can determine the direction
		if (this.targetPosition.isCenter()) {
			x2 = getWidth() / 2;
			y2 = getHeight() / 2;
		} else {
			x2 = (this.targetPosition.isLeft()) ? X_PADDING : getWidth() - X_PADDING;
			y2 = (this.targetPosition.isTop()) ? Y_PADDING : getHeight() - Y_PADDING;
		}

		if (this.currentPosition.isCenter()) {
			x1 = getWidth() / 2;
			y1 = getHeight() / 2;
		} else {
			x1 = (this.currentPosition.isLeft()) ? X_PADDING : getWidth() - X_PADDING;
			y1 = (this.currentPosition.isTop()) ? Y_PADDING : getHeight() - Y_PADDING;
		}

		if (state != Constants.TRANSITION_HIGH) {
			x2 = x1 + (x2 - x1) * state / Constants.TRANSITION_HIGH;
			y2 = y1 + (y2 - y1) * state / Constants.TRANSITION_HIGH;
		}
		setLocation(x2, y2);
	}

	@Override
	public void redraw(GraphicsContext g, int stage) {
		this.faceSwitchAnimation = false;
		setState(stage);
		realignXY(stage);

		if (!this.faceSwitchAnimation) {
			redraw(g, this.direction, this.transitionState, getX(), getY());
		}
	}

	@Override
	public void render(GraphicsContext g) {
		// do nothing

	}

	@Override
	public void validate(int widthHint, int heightHint) {
		// do nothing

	}

	@Override
	public void retreat(GraphicsContext g, int stage) {
		this.faceSwitchAnimation = true;

		if (stage > Constants.TRANSITION_HIGH / 2) {
			stage = Constants.TRANSITION_HIGH - stage + Constants.TRANSITION_LOW;
		}
		int x, y;
		int offset = this.smallDiameter * stage / Constants.TRANSITION_HIGH;
		if (this.currentPosition.isLeft()) {
			x = X_PADDING - offset;
		} else {
			x = getWidth() + offset - X_PADDING;
		}

		if (this.currentPosition.isTop()) {
			y = Y_PADDING - offset;
		} else {
			y = getHeight() + offset - Y_PADDING;
		}
		redraw(g, Direction.EdgeStill, stage, x, y);
	}

	@Override
	public void setCurrentPosition(ScreenArea current) {
		this.currentPosition = current;
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		this.smallDiameter = Math.min(width, height) / 2;
		this.largeDiameter = (int) (this.smallDiameter * LARGE_DIAMETER_RATIO);
	}

	/**
	 * Set the current transition state.
	 *
	 * @param state
	 *            state
	 */
	public void setState(int state) {
		this.transitionState = state;
	}

	@Override
	public void setTargetPosition(ScreenArea originalPosition) {
		this.targetPosition = originalPosition;
	}

	/**
	 * Set the direction depending on the target and current position.
	 */
	private void setupDirection() {
		if (this.targetPosition == this.currentPosition) {
			if (this.currentPosition == ScreenArea.Center) {
				this.direction = Direction.CenterStill;
			} else {
				this.direction = Direction.EdgeStill;
			}
		} else if (this.targetPosition == ScreenArea.Center) {
			this.direction = Direction.ToCenter;
		} else if (this.currentPosition == ScreenArea.Center) {
			this.direction = Direction.ToEdge;
		} else {
			this.direction = Direction.EdgeSwitch;
		}
	}

	@Override
	public void switchFace(GraphicsContext g, int stage) {
		this.transitionState = stage;
	}

	@Override
	public DatePosition getDatePosition() {
		return DatePosition.OUTSIDE;
	}

	/**
	 * @return the date in a string format
	 */
	protected String getDate() {
		return PROVIDER.getDateStr();
	}

	@Override
	public void drawDate(GraphicsContext g, DatePosition current, DatePosition next, int stage) {
		int y1 = current.getOffset();
		int y2 = next.getOffset();
		int y = y1;
		if (y1 != y2) {
			float ratio = stage / (float) Constants.TRANSITION_HIGH;
			y = (int) ((1.0f - ratio) * y1 + ratio * y2);
		}

		String date = getDate();

		// Draw the date.
		int x = (getWidth() - this.font.stringWidth(date)) / 2;
		g.setColor(Constants.COLOR_FOREGROUND);
		g.setFont(this.font);
		g.drawString(date, x, y, 0);
	}

	/**
	 * Draw the circle on the edge.
	 *
	 * @param g
	 *            the graphic context.
	 * @param direction
	 *            The direction.
	 * @param stage
	 *            The current stage.
	 * @param x
	 *            The X center.
	 * @param y
	 *            The Y center.
	 * @return Return the circle's center.
	 */
	protected Point drawEncapsulatingCircle(GraphicsContext g, Direction direction, int stage, int x, int y) {
		// surrounding circle when placed on the edge
		if (direction != Direction.CenterStill) {
			int offset = 0;
			if (direction == Direction.ToCenter) {
				offset = stage;
			} else if (direction == Direction.ToEdge) {
				offset = Constants.TRANSITION_HIGH - stage;
			}

			offset = this.smallDiameter * offset * 2 / Constants.TRANSITION_HIGH;
			ScreenArea position = (direction == Direction.ToCenter) ? this.currentPosition : this.targetPosition;
			int xCircle = x - this.smallDiameter / 2 + ((!position.isLeft()) ? offset : -offset);
			int yCircle = y - this.smallDiameter / 2 + ((!position.isTop()) ? offset : -offset);

			Utils.drawCircle(g, xCircle, yCircle, this.smallDiameter, Constants.DEFAULT_THICKNES,
					Constants.DEFAULT_FADE);
			xCircle += this.smallDiameter / 2 - X_PADDING;
			yCircle += this.smallDiameter / 2 - Y_PADDING;
			return new Point(xCircle, yCircle);
		}

		return new Point(x, y);

	}

	/**
	 *
	 * Redraw the bubble.
	 *
	 * @param g
	 *            The graphic context to use.
	 * @param direction
	 *            direction
	 * @param stage
	 *            current stage
	 * @param x
	 *            x position of the center of the bubble.
	 * @param y
	 *            y position of the center of the bubble.
	 */
	public void redraw(GraphicsContext g, Direction direction, int stage, int x, int y) {
		// do nothing by default
	}

	@Override
	public void switchView(boolean right) {
		// do nothing by default

	}

	@Override
	public void startSwitchFace(boolean up) {
		// do nothing by default

	}

	@Override
	public void startAnimation() {
		// do nothing by default

	}

	@Override
	public void stopAnimation() {
		// do nothing by default

	}

	@Override
	public void stopSwitchFace() {
		// do nothing by default

	}
}
