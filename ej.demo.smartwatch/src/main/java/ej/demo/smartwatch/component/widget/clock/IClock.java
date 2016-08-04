/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component.widget.clock;

import ej.demo.smartwatch.component.Bubble.DatePosition;
import ej.demo.smartwatch.component.Direction;
import ej.demo.smartwatch.dal.ISmDataProvider;
import ej.microui.display.GraphicsContext;

/**
 *
 */
public interface IClock {

	/**
	 * Get the targeted position of the date.
	 *
	 * @return the targeted position of the date.
	 */
	DatePosition getDatePosition();

	/**
	 * @return class name
	 */
	String getName();

	/**
	 *
	 * Draw the clock.
	 *
	 * @param g
	 *            The graphic context to use.
	 * @param direction
	 *            Animation direction.
	 * @param provider
	 *            Data provider.
	 * @param x
	 *            Center x.
	 * @param y
	 *            Center Y.
	 * @param stage
	 *            Animation stage.
	 *
	 */
	void draw(GraphicsContext g, Direction direction, ISmDataProvider provider, int x, int y, int stage);

	/**
	 * Has edge representation.
	 *
	 * @return true if it has edge representation.
	 */
	boolean hasEdgeFace();
}
