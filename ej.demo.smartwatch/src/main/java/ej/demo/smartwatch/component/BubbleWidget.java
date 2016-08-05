/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component;

import ej.demo.smartwatch.model.DataProvider;
import ej.demo.smartwatch.model.IDataProvider;
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
	protected static final IDataProvider PROVIDER = DataProvider.getInstance();

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
		X_PADDING = (int) (10 * Constants.DISPLAY_DEFAULT_WIDTH_RATIO);
		Y_PADDING = (int) (10 * Constants.DISPLAY_DEFAULT_HEIGHT_RATIO);
	}

	/**
	 * Diameter of the widget.
	 */
	protected int smallDiameter;

	/**
	 * Diameter when at the center.
	 */
	protected int largeDiameter;

	/**
	 * Current position of the widget.
	 */
	protected ScreenArea currentPosition;

	/**
	 * Initial position.
	 */
	protected ScreenArea originalPosition;

	/**
	 * Target position at the end of the animation.
	 */
	protected ScreenArea targetPosition;

	/**
	 * Direction of the animation.
	 */
	protected Direction direction;

	/**
	 * Whether the widget is in a face switch animation.
	 */
	protected boolean inFaceSwitchAnimation = false;

	/**
	 * Transition completion.
	 */
	protected int transitionCompletion;

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
		this.inFaceSwitchAnimation = false;
		setCompletion(Constants.COMPLETION_MAX);
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
	public ScreenArea getWidgetCurrentPosition() {
		return this.currentPosition;
	}

	@Override
	public ScreenArea getWidgetOriginalPosition() {
		return this.originalPosition;
	}

	@Override
	public ScreenArea getWidgetTargetPosition() {
		return this.targetPosition;
	}

	@Override
	public int countFaces() {
		return 1;
	}

	@Override
	public boolean boundingBoxContains(int x, int y) {
		final int radius = this.smallDiameter / 2;
		int left = getX() - radius;
		int top = getY() - radius;
		int right = getX() + radius;
		int bottom = getY() + radius;
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

		if (state == Constants.COMPLETION_MAX) {
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

		if (state != Constants.COMPLETION_MAX) {
			x2 = x1 + (x2 - x1) * state / Constants.COMPLETION_MAX;
			y2 = y1 + (y2 - y1) * state / Constants.COMPLETION_MAX;
		}
		setLocation(x2, y2);
	}

	@Override
	public void redraw(GraphicsContext g, int completion) {
		this.inFaceSwitchAnimation = false;
		setCompletion(completion);
		realignXY(completion);

		if (!this.inFaceSwitchAnimation) {
			redraw(g, this.direction, this.transitionCompletion, getX(), getY());
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
	public void moveOutThenIn(GraphicsContext g, int completion) {
		this.inFaceSwitchAnimation = true;

		if (completion > Constants.COMPLETION_MAX / 2) {
			completion = Constants.COMPLETION_MAX - completion + Constants.COMPLETION_MIN;
		}

		int x, y;
		int offset = this.smallDiameter * completion / Constants.COMPLETION_MAX;
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
		redraw(g, Direction.CornerStill, completion, x, y);
	}

	@Override
	public void setWidgetCurrentPosition(ScreenArea current) {
		this.currentPosition = current;
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		this.smallDiameter = Math.min(width, height) / 2;
		this.largeDiameter = (int) (this.smallDiameter * LARGE_DIAMETER_RATIO);
	}

	/**
	 * Set the current transition completion.
	 *
	 * @param completion
	 *            completion (in percent)
	 */
	public void setCompletion(int completion) {
		this.transitionCompletion = completion;
	}

	@Override
	public void setWidgetTargetPosition(ScreenArea originalPosition) {
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
				this.direction = Direction.CornerStill;
			}
		} else if (this.targetPosition == ScreenArea.Center) {
			this.direction = Direction.ToCenter;
		} else if (this.currentPosition == ScreenArea.Center) {
			this.direction = Direction.ToCorner;
		} else {
			this.direction = Direction.CornerSwitch;
		}
	}

	@Override
	public void switchFace(GraphicsContext g, int completion) {
		this.transitionCompletion = completion;
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
	public void drawDate(GraphicsContext g, DatePosition current, DatePosition next, int completion) {
		int y1 = current.getOffset();
		int y2 = next.getOffset();
		int y = y1;
		if (y1 != y2) {
			float ratio = completion / (float) Constants.COMPLETION_MAX;
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
	 * Draw the circle in the corner.
	 *
	 * @param g
	 *            the graphic context.
	 * @param direction
	 *            The direction.
	 * @param completion
	 *            The current completion.
	 * @param x
	 *            The X center.
	 * @param y
	 *            The Y center.
	 * @return Return the circle's center.
	 */
	protected Point drawBoundingCircle(GraphicsContext g, Direction direction, int completion, int x, int y) {
		// surrounding circle when placed in the corner
		if (direction != Direction.CenterStill) {
			int offset = 0;
			if (direction == Direction.ToCenter) {
				offset = completion;
			} else if (direction == Direction.ToCorner) {
				offset = Constants.COMPLETION_MAX - completion;
			}

			offset = this.smallDiameter * offset * 2 / Constants.COMPLETION_MAX;
			ScreenArea position = (direction == Direction.ToCenter) ? this.currentPosition : this.targetPosition;
			final int radius = this.smallDiameter / 2;
			int xCircle = x - radius + ((!position.isLeft()) ? offset : -offset);
			int yCircle = y - radius + ((!position.isTop()) ? offset : -offset);

			Utils.drawCircle(g, xCircle, yCircle, this.smallDiameter, Constants.DEFAULT_THICKNESS,
					Constants.DEFAULT_FADE);
			xCircle += radius - X_PADDING;
			yCircle += radius - Y_PADDING;
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
	 * @param completion
	 *            current completion
	 * @param x
	 *            x position of the center of the bubble.
	 * @param y
	 *            y position of the center of the bubble.
	 */
	public void redraw(GraphicsContext g, Direction direction, int completion, int x, int y) {
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
