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
 *
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
		POSITION1(10),

		/**
		 * Second line.
		 */
		POSITION2(50);

		private int offset;

		DatePosition(int offset) {
			this.offset = offset;
		}

		/**
		 * Get the y offset the position.
		 *
		 * @return the y offset.
		 */
		public int getOffset() {
			return (int) (this.offset * Constants.HEIGHT_RATIO);
		}

		/**
		 * Set the y offset the position.
		 *
		 * @param offset
		 *            the y offset to set.
		 */
		public void setOffset(int offset) {
			this.offset = offset;
		}
	}

	/**
	 * Draw the date.
	 *
	 * @param g
	 *            The graphic context to use. the context
	 * @param current
	 *            the current position
	 * @param next
	 *            the next position
	 * @param stage
	 *            the stage
	 */
	void drawDate(GraphicsContext g, DatePosition current, DatePosition next, int stage);

	/**
	 * Get the current position.
	 *
	 * @return Current position
	 */
	ScreenArea getCurrentPosition();

	/**
	 *
	 * Get the targeted position of the date.
	 *
	 * @return the targeted position of the date.
	 */
	DatePosition getDatePosition();

	/**
	 * Get the original position.
	 *
	 * @return original position
	 */
	ScreenArea getOriginalPosition();

	/**
	 * Get the tag.
	 *
	 * @return tag
	 */
	String getTag();

	/**
	 * Get the target position.
	 *
	 * @return target position
	 */
	ScreenArea getTargetPosition();

	/**
	 * Count the number of faces.
	 *
	 * @return The number of faces.
	 */
	int countFaces();

	/**
	 * Get whether or not a point is in range.
	 *
	 * @param x
	 *            x
	 * @param y
	 *            y
	 * @return whether or not a point is in range.
	 */
	boolean inRange(int x, int y);

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
	 * @param stage
	 *            stage
	 */
	void redraw(GraphicsContext g, int stage);

	/**
	 *
	 * Retreat the bubble behind the corners.
	 *
	 * @param g
	 *            The graphic context to use. g
	 * @param stage
	 *            stage
	 */
	void retreat(GraphicsContext g, int stage);

	/**
	 * Set the current position.
	 *
	 * @param current
	 *            position
	 */
	void setCurrentPosition(ScreenArea current);

	/**
	 * Set the target position.
	 *
	 * @param targetPosition
	 *            original position
	 */
	void setTargetPosition(ScreenArea targetPosition);

	/**
	 * Redraw the bubble during a face switch.
	 *
	 * @param g
	 *            The graphic context to use. g
	 * @param stage
	 *            stage
	 */
	void switchFace(GraphicsContext g, int stage);

	/**
	 * Prepare for face switch.
	 *
	 * @param up
	 *            upwards
	 */
	void startSwitchFace(boolean up);

	/**
	 * Clean the bubble after a switch face.
	 */
	void stopSwitchFace();

	/**
	 * @param right
	 *            Is a left to right transition
	 */
	void switchView(boolean right);

	/**
	 * Start the animation for the bubble.
	 */
	void startAnimation();

	/**
	 * Stop the animation.
	 */
	void stopAnimation();

}
