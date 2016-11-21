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

	/**
	 * Padding.
	 */
	protected static final int X_PADDING;

	/**
	 * Padding.
	 */
	protected static final int Y_PADDING;

	private static final String TAG = "MultipleViewWidget"; //$NON-NLS-1$

	/**
	 * Next arrow.
	 */
	private static final Image NEXT;

	/**
	 * Previous arrow.
	 */
	private static final Image PREVIOUS;

	/**
	 * Offset for the next previous arrows.
	 */
	private static final int NEXT_PREVIOUS_OFFSET = 4;

	static {
		X_PADDING = (int) (5 * Constants.DISPLAY_DEFAULT_WIDTH_RATIO);
		Y_PADDING = (int) (5 * Constants.DISPLAY_DEFAULT_HEIGHT_RATIO);
		Image nextImage = null;
		Image previousImage = null;
		try {
			nextImage = Image.createImage(Images.NEXT);
			previousImage = Image.createImage(Images.PREVIOUS);
		} catch (IOException e) {
			Log.e(TAG, e);
		}
		NEXT = nextImage;
		PREVIOUS = previousImage;
	}

	/**
	 * Message index.
	 */
	protected int viewIndex = -1;

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
	}

	/**
	 *
	 *
	 * /** Draw the next and previous arrows.
	 *
	 * @param g
	 *            The graphic context.
	 * @param direction
	 *            Transition direction.
	 * @param completion
	 *            Transition completion.
	 */
	protected void drawNextPrevious(GraphicsContext g, Direction direction, int completion) {
		if (direction != Direction.CenterStill
				|| (completion != Constants.COMPLETION_MIN && completion != Constants.COMPLETION_MAX)) {
			return;
		}

		// Center
		int y = getHeight() / 2;
		if (this.viewIndex > 0) {
			g.drawImage(PREVIOUS, X_PADDING + NEXT_PREVIOUS_OFFSET, y - PREVIOUS.getHeight() / 2, 0);
		}

		if (this.viewIndex != (viewCount() - 1)) {
			g.drawImage(NEXT, getWidth() - NEXT.getWidth() - X_PADDING - NEXT_PREVIOUS_OFFSET, y - NEXT.getHeight() / 2,
					0);
		}
	}

	/**
	 * Update the view index.
	 *
	 * @param forward
	 *            Whether to go to next or previous view.
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
	 * Get the available views count.
	 *
	 * @return available views count.
	 */
	protected abstract int viewCount();
}
