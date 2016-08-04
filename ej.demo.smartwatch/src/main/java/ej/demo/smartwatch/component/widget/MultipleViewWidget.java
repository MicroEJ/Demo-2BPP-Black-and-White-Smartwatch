package ej.demo.smartwatch.component.widget;

import java.io.IOException;

import ej.demo.smartwatch.component.BubbleWidget;
import ej.demo.smartwatch.component.Direction;
import ej.demo.smartwatch.component.ScreenArea;
import ej.demo.smartwatch.style.Images;
import ej.demo.smartwatch.utils.Constants;
import ej.demo.smartwatch.utils.Log;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;

/**
 *
 * A widget with view switch.
 *
 */
public abstract class MultipleViewWidget extends BubbleWidget {

	private static final String TAG = "MultipleViewWidget"; //$NON-NLS-1$

	/**
	 * The ratio for width and height in only computed at runtime, after the
	 * static variable had been init. This boolean is used to know if the had
	 * been init.
	 */
	private static boolean STATIC_VARIABLE_INITIALIZED = false;

	/**
	 * Next arrow.
	 */
	private static Image NEXT;

	/**
	 * Previous arrow.
	 */
	private static Image PREVIOUS;

	/**
	 * Message index.
	 */
	protected int viewIndex = -1;

	/**
	 * Padding.
	 */
	protected static int X_PADDING = 5;

	/**
	 * Padding.
	 */
	protected static int Y_PADDING = X_PADDING;

	/**
	 * A widget with view switch.
	 *
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @param position
	 *            The initial position.
	 */
	public MultipleViewWidget(int width, int height, ScreenArea position) {
		super(width, height, position);
		if (!STATIC_VARIABLE_INITIALIZED) {
			X_PADDING *= Constants.WIDTH_RATIO;
			Y_PADDING *= Constants.HEIGHT_RATIO;
			STATIC_VARIABLE_INITIALIZED = true;
		}
		try {
			NEXT = Image.createImage(Images.NEXT);
			PREVIOUS = Image.createImage(Images.PREVIOUS);
		} catch (IOException e) {
			Log.e(TAG, e);
		}
	}

	/**
	 * Draw the next and previous arrows.
	 *
	 * @param g
	 *            The graphic context.
	 * @param direction
	 *            Transition direction.
	 * @param stage
	 *            Transition stage.
	 */
	protected void drawNextPrevious(GraphicsContext g, Direction direction, int stage) {
		if (direction != Direction.CenterStill
				|| (stage != Constants.TRANSITION_LOW && stage != Constants.TRANSITION_HIGH)) {
			return;
		}

		// Center
		int y = getHeight() / 2;
		if (this.viewIndex > 0) {
			g.drawImage(PREVIOUS, X_PADDING, y - PREVIOUS.getHeight() / 2, 0);
		}

		if (this.viewIndex != (viewCount() - 1)) {
			g.drawImage(NEXT, getWidth() - NEXT.getWidth() - X_PADDING, y - NEXT.getHeight() / 2, 0);
		}
	}

	/**
	 * Update the view index.
	 *
	 * @param forward
	 *            Whether goe to next or previous view.
	 */
	protected void nextView(boolean forward) {
		if (viewCount() == 0) {
			this.viewIndex = -1;
			return;
		}
		this.viewIndex += ((forward) ? 1 : -1);
		if (this.viewIndex < 0) {
			this.viewIndex = viewCount() - 1;
		} else if (this.viewIndex >= viewCount()) {
			this.viewIndex = 0;
		}
	}

	/**
	 * Get the amount of available views.
	 *
	 * @return Amount of available views.
	 */
	protected abstract int viewCount();
}
