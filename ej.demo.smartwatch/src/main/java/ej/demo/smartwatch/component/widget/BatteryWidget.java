/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component.widget;

import ej.demo.smartwatch.component.BubbleWidget;
import ej.demo.smartwatch.component.Direction;
import ej.demo.smartwatch.component.ScreenArea;
import ej.demo.smartwatch.dal.ISmDataProvider;
import ej.demo.smartwatch.dal.SmDataPrivider;
import ej.demo.smartwatch.style.Images;
import ej.demo.smartwatch.utils.Constants;
import ej.demo.smartwatch.utils.Utils;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.style.container.Rectangle;

/**
 * Widget of a battery.
 */
public class BatteryWidget extends BubbleWidget {
	private static final String TAG = "BatteryWidget"; //$NON-NLS-1$

	/**
	 * Outline to draw the progress into the bigest battery.
	 */
	private static final Rectangle BIG_BATTERY_PROGRESS = new Rectangle(13, 7, 60, 34);

	/**
	 * Outline to draw the progress into the smallest battery.
	 */
	private static final Rectangle SMALL_BATTERY_PROGRESS = new Rectangle(6, 3, 29, 16);

	/**
	 * Data provider.
	 */
	private static final ISmDataProvider PROVIDER = SmDataPrivider.get();

	/**
	 * The ratio for width and height in only computed at runtime, after the
	 * static variable had been init. This boolean is used to know if the had
	 * been init.
	 */
	private static boolean STATIC_VARIABLE_INITIALIZED = false;

	/**
	 * Y Offset for the battery when in the edge.
	 */
	private static int EDGE_Y_OFFSET = -22;

	/**
	 * Y Offset for the battery when in the edge.
	 */
	private static int EDGE_X_OFFSET = -6;

	/**
	 * Offset between two lines.
	 */
	private static int TEXT_OFFSET = 5;

	/**
	 * Font to draw the available time line.
	 */
	private Font fontAvailableTime = null;

	/**
	 * Font to draw the battery percent.
	 */
	private Font fontBatteryLevel = null;

	/**
	 * @see BubbleWidget
	 * @param width
	 *            width
	 * @param height
	 *            height
	 * @param position
	 *            position
	 */
	public BatteryWidget(int width, int height, ScreenArea position) {
		super(width, height, position);
		if (!STATIC_VARIABLE_INITIALIZED) {
			EDGE_Y_OFFSET *= Constants.WIDTH_RATIO;
			EDGE_X_OFFSET *= Constants.HEIGHT_RATIO;
			TEXT_OFFSET *= Constants.HEIGHT_RATIO;
			STATIC_VARIABLE_INITIALIZED = true;
		}
		this.fontBatteryLevel = Constants.FONT_36;
		this.fontAvailableTime = Constants.FONT_24;
	}

	private int computeMean(int val1, int val2, float ratio) {
		return (int) ((1.0f - ratio) * val2 + ratio * val1);
	}

	/**
	 * Get the X offset of an image depending on the current position and the
	 * target position.
	 *
	 * @param image
	 *            The image.
	 * @param ratio
	 *            The ratio of the animation.
	 * @return The X offset.
	 */
	private int getImageXOffset(final Image image, final float ratio) {
		int centerOffset = -image.getWidth() / 2;
		int leftOffset = this.smallDiameter / 4 + EDGE_X_OFFSET;
		int rightOffset = -this.smallDiameter / 4 + EDGE_X_OFFSET;

		return Utils.getXOffset(centerOffset, leftOffset, rightOffset, this.currentPosition, this.targetPosition,
				ratio);
	}

	/**
	 * Get the y offset of an image depending on the current position and the
	 * target position.
	 *
	 * @param image
	 *            The image.
	 * @param ratio
	 *            The ratio of the animation.
	 * @return The Y offset.
	 */
	private int getImageYOffset(final Image image, final float ratio) {
		int centerOffset = -image.getHeight();
		int topOffset = this.smallDiameter / 4 + EDGE_Y_OFFSET;
		int bottomOffset = -this.smallDiameter / 4 + EDGE_Y_OFFSET;

		return Utils.getYOffset(centerOffset, topOffset, bottomOffset, this.currentPosition, this.targetPosition,
				ratio);
	}

	@Override
	public String getTag() {
		return TAG;
	}

	/**
	 * Draw the remaining percent and the remaining time.
	 *
	 * @param g
	 *            The graphic context.
	 * @param direction
	 *            Direction of the animation.
	 * @param imageY
	 *            Image top Y.
	 * @param img
	 *            Battery image.
	 * @param ratio
	 *            Ratio of the animation.
	 */
	private void drawText(GraphicsContext g, Direction direction, int imageY, Image img, float ratio) {

		// draw text when centered
		if (direction == Direction.ToCenter || direction == Direction.CenterStill || direction == Direction.ToEdge) {
			float stepRatio = (Direction.ToCenter == direction || direction == Direction.CenterStill) ? ratio
					: (1 - ratio);

			// horizontally, it shall be placed in the center
			int xCoordinate = getWidth() / 2;
			// coordinate when not in view
			int y1 = getHeight();
			// coordinate when centered
			int y2 = imageY + img.getHeight() + TEXT_OFFSET;
			// battery level
			g.setFont(this.fontBatteryLevel);
			String text = Integer.toString(PROVIDER.getBatteryLevel()) + "%"; //$NON-NLS-1$
			g.drawString(text, xCoordinate - this.fontBatteryLevel.stringWidth(text) / 2,
					(direction == Direction.ToEdge) ? computeMean(y1, y2, stepRatio) : computeMean(y2, y1, stepRatio),
					0);

			// time left - start position is further so they(level/time left)
			// don't appear with the same speed.
			y1 *= 2;
			// placed below battery level
			y2 += this.fontBatteryLevel.getHeight() + TEXT_OFFSET;
			text = PROVIDER.getBatteryLevelStr();
			g.setFont(this.fontAvailableTime);
			g.drawString(text, xCoordinate - this.fontAvailableTime.stringWidth(text) / 2,
					(direction == Direction.ToEdge) ? computeMean(y1, y2, stepRatio) : computeMean(y2, y1, stepRatio),
					0);
		}
	}

	@Override
	public void redraw(GraphicsContext g, Direction direction, int stage, int x, int y) {
		g.setColor(Constants.COLOR_FOREGROUND);

		// keep the same direction
		if (direction == Direction.CenterStill) {
			stage = Constants.TRANSITION_HIGH;
		}

		// surrounding circle when placed on the edge
		if (this.originalPosition == ScreenArea.TopRight && direction != Direction.CenterStill) {
			int xCircle = x, yCircle = y;

			int offset = 0;
			if (direction == Direction.ToCenter) {
				offset = stage;
			} else if (direction == Direction.ToEdge) {
				offset = Constants.TRANSITION_HIGH - stage;
			}

			xCircle = xCircle - this.smallDiameter / 2 + this.smallDiameter * offset * 2 / Constants.TRANSITION_HIGH;
			yCircle = yCircle - this.smallDiameter / 2 - this.smallDiameter * offset * 2 / Constants.TRANSITION_HIGH;

			Utils.drawCircle(g, xCircle, yCircle, this.smallDiameter, Constants.DEFAULT_THICKNES,
					Constants.DEFAULT_FADE);
		}

		// draw battery icon
		int imageX, imageY, index;
		if (direction == Direction.ToCenter || direction == Direction.ToEdge) {
			if (direction == Direction.ToEdge) {
				stage = Constants.TRANSITION_HIGH - stage;
			}
			index = Images.BATTERY_SEQ.getImgCount() - 1
					- stage * (Images.BATTERY_SEQ.getImgCount() - 1) / Constants.TRANSITION_HIGH;
		} else if (direction == Direction.CenterStill) {
			index = 0;
		} else {
			index = Images.BATTERY_SEQ.getImgCount() - 1;
		}

		float ratio = (float) stage / Constants.TRANSITION_HIGH;
		float stepRatio = (Direction.ToCenter != direction) ? ratio : (1 - ratio);
		int level = PROVIDER.getBatteryLevel();
		Image img = Images.BATTERY_SEQ.getImg(index);
		imageX = x + getImageXOffset(img, stepRatio);
		int imageYOffset = getImageYOffset(img, stepRatio);
		imageY = y + imageYOffset;

		stepRatio = ratio;
		if (direction == Direction.EdgeStill || direction == Direction.EdgeSwitch) {
			stepRatio = 0;
		}

		// Draws the background.
		int bprogressWidth = (int) ((BIG_BATTERY_PROGRESS.getWidth() - SMALL_BATTERY_PROGRESS.getWidth()) * stepRatio
				+ SMALL_BATTERY_PROGRESS.getWidth());
		int bprogressHeight = (int) ((BIG_BATTERY_PROGRESS.getHeight() - SMALL_BATTERY_PROGRESS.getHeight()) * stepRatio
				+ SMALL_BATTERY_PROGRESS.getHeight());
		int bprogressX = (int) (imageX + (BIG_BATTERY_PROGRESS.getX() - SMALL_BATTERY_PROGRESS.getX()) * stepRatio
				+ SMALL_BATTERY_PROGRESS.getX());
		int bprogressY = (int) (imageY + (BIG_BATTERY_PROGRESS.getY() - SMALL_BATTERY_PROGRESS.getY()) * stepRatio
				+ SMALL_BATTERY_PROGRESS.getY());

		g.setColor(Constants.COLOR_BACKGROUND);
		g.fillRect(bprogressX, bprogressY, bprogressWidth, bprogressHeight);

		// Draw the progress
		int progressWidth = bprogressWidth * level / PROVIDER.getBatteryMax();
		int progressHeight = bprogressHeight;
		int progressX = bprogressX + (bprogressWidth - progressWidth);
		int progressY = bprogressY;
		g.setColor(Constants.COLOR_FOREGROUND);
		g.fillRect(progressX, progressY, progressWidth, progressHeight);

		// Draw battery icon.
		g.drawImage(img, imageX, imageY, 0);
		drawText(g, direction, imageY, img, ratio);
	}

	@Override
	public void switchFace(GraphicsContext g, int stage) {
		// no extra face, therefore it is redrawn
		redraw(g, this.direction, Constants.TRANSITION_HIGH, getX(), getY());
	}

	@Override
	public DatePosition getDatePosition() {
		return DatePosition.OUTSIDE;
	}
}
