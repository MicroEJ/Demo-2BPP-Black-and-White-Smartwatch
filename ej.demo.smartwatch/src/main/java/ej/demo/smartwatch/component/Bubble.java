/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component;

import ej.demo.smartwatch.utils.Constants;
import ej.microui.display.GraphicsContext;

/**
 * Bubble interface
 */
public interface Bubble {

	/**
	 * Position of the date.
	 */
	public enum DatePosition {
		/**
		 * Outside.
		 */
		OUTSIDE(-50),

		/**
		 * First line.
		 */
		POSITION1(4),

		/**
		 * Second line.
		 */
		POSITION2(45);

		private int offset;

		DatePosition(int offset) {
			this.offset = offset;
		}

		/**
		 * Get the position's y offset.
		 *
		 * @return the y offset.
		 */
		public int getOffset() {
			return (int) (this.offset * Constants.DISPLAY_DEFAULT_HEIGHT_RATIO);
		}

		/**
		 * Set the position's y offset.
		 *
		 * @param offset
		 *            the y offset to set.
		 */
		public void setOffset(int offset) {
			this.offset = offset;
		}
	}

	/**
	 *
	 * Get the targeted position of the date.
	 *
	 * @return the targeted position of the date.
	 */
	DatePosition getDatePosition();

	/**
	 * Draw the date.
	 *
	 * @param g
	 *            The graphic context to use.
	 * @param current
	 *            the current position
	 * @param next
	 *            the next position
	 * @param completion
	 *            the completion
	 */
	void drawDate(GraphicsContext g, DatePosition current, DatePosition next, int completion);

	/**
	 * Get the current position.
	 *
	 * @return Current position
	 */
	ScreenArea getWidgetCurrentPosition();

	/**
	 * Get the original position.
	 *
	 * @return original position
	 */
	ScreenArea getWidgetOriginalPosition();

	/**
	 * Get the target widget's position.
	 *
	 * @return widget's target position
	 */
	ScreenArea getWidgetTargetPosition();

	/**
	 * Count the number of faces.
	 *
	 * @return The number of faces.
	 */
	int countFaces();

	/**
	 * Get whether or not a point is in this bubble's bounding box (when in a corner).
	 *
	 * @param x
	 *            x
	 * @param y
	 *            y
	 * @return whether or not a point is in range.
	 */
	boolean boundingBoxContains(int x, int y);

	/**
	 * Get whether or not a transition should be performed on a switch.
	 *
	 * @return if a transition should be performed on a switch.
	 */
	boolean isSwitchAnimated();

	/**
	 * Redraw the full bubble.
	 *
	 * @param g
	 *            The graphic context to use. g
	 * @param completion
	 *            completion
	 */
	void redraw(GraphicsContext g, int completion);

	/**
	 *
	 * Retreat the bubble behind the corners.
	 *
	 * @param g
	 *            The graphic context to use. g
	 * @param completion
	 *            completion
	 */
	void moveOutThenIn(GraphicsContext g, int completion);

	/**
	 * Set the current position.
	 *
	 * @param current
	 *            position
	 */
	void setWidgetCurrentPosition(ScreenArea current);

	/**
	 * Set the target position.
	 *
	 * @param targetPosition
	 *            original position
	 */
	void setWidgetTargetPosition(ScreenArea targetPosition);

	/**
	 * @param right
	 *            Is a left to right transition
	 */
	void switchView(boolean right);

	/**
	 * Redraw the bubble during a face switch.
	 *
	 * @param g
	 *            The graphic context to use. g
	 * @param completion
	 *            completion
	 */
	void switchFace(GraphicsContext g, int completion);

	/**
	 * Prepare for face switch.
	 *
	 * @param up
	 *            upwards
	 */
	void startSwitchFace(boolean up);

	/**
	 * Clean the bubble after a face switch.
	 */
	void stopSwitchFace();

	/**
	 * Start the animation for the bubble.
	 */
	void startAnimation();

	/**
	 * Stop the animation.
	 */
	void stopAnimation();

}
