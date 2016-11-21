/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package ej.demo.smartwatch.component;

/**
 * Direction of the bubble.
 */
public enum Direction {
	/**
	 * Stay at the center.
	 */
	CenterStill,

	/**
	 * Stay at the corner.
	 */
	CornerStill,

	/**
	 * Switch from one corner to the other.
	 */
	CornerSwitch,

	/**
	 * Go from corner to center.
	 */
	ToCenter,

	/**
	 * Go from center to corner.
	 */
	ToCorner;
}
