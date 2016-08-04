/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
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
	 * Stay at the edge.
	 */
	EdgeStill,

	/**
	 * Switch from one edge to the other.
	 */
	EdgeSwitch,

	/**
	 * Go from the edge to the center.
	 */
	ToCenter,

	/**
	 * Go from center to edge.
	 */
	ToEdge;
}
