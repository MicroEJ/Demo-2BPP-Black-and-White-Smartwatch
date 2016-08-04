/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component;

/**
 * The area of the screen.
 */
public enum ScreenArea {
	/**
	 * BottomLeft.
	 */
	BottomLeft(0, false, true, false),
	/**
	 * BottomRight.
	 */
	BottomRight(1, false, false, false),
	/**
	 * Center.
	 */
	Center(2, false, true, true),

	/**
	 * TopLeft.
	 */
	TopLeft(3, true, true, false),

	/**
	 * TopRight.
	 */
	TopRight(4, true, false, false);

	private boolean center, left, top;
	private int index;

	ScreenArea(int i, boolean top, boolean left, boolean center) {
		this.index = i;
		this.top = top;
		this.left = left;
		this.center = center;
	}

	/**
	 * @return current index.
	 */
	public int get() {
		return this.index;
	}

	/**
	 * @return true if center.
	 */
	public boolean isCenter() {
		return this.center;
	}

	/**
	 * @return true if left.
	 */
	public boolean isLeft() {
		return this.left;
	}

	/**
	 * @return true if top.
	 */
	public boolean isTop() {
		return this.top;
	}
}