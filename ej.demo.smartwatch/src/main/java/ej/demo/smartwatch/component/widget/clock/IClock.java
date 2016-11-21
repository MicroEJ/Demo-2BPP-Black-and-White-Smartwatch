/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package ej.demo.smartwatch.component.widget.clock;

import ej.demo.smartwatch.component.Bubble.DatePosition;
import ej.demo.smartwatch.component.Direction;
import ej.demo.smartwatch.model.IDataProvider;
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
	 * @param completion
	 *            Animation completion.
	 *
	 */
	void draw(GraphicsContext g, Direction direction, IDataProvider provider, int x, int y, int completion);

	/**
	 * Has corner representation.
	 *
	 * @return true if it has corner representation.
	 */
	boolean hasCornerFace();
}
