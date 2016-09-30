/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.component.widget;

import ej.animation.Animation;
import ej.animation.Animator;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.demo.smartwatch.component.BubbleWidget;
import ej.demo.smartwatch.component.Direction;
import ej.demo.smartwatch.component.ScreenArea;
import ej.demo.smartwatch.style.Images;
import ej.demo.smartwatch.utils.Constants;
import ej.demo.smartwatch.utils.Utils;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;

/**
 * Widget for running distance
 */
public class DistanceWidget extends BubbleWidget implements Animation {

	/**
	 * Angle of a full circle.
	 */
	private static final int ANGLE_FULL_CIRCLE = 360;

	/**
	 * Angle of the full ellipse.
	 */
	private static final int ELIPSE_ARCANGLE = 270;

	/**
	 * Angle to start.
	 */
	private static final int ELIPSE_START_ANGLE = 225;

	/**
	 * Thickness for the small circle.
	 */
	private static final int ELIPSE_THICKNESS = Constants.DEFAULT_THICKNESS;

	/**
	 * Thickness for the completed progress.
	 */
	private static final int ELIPSE_PROGRESS_THICKNESS = 5;

	/**
	 * Relative position of the running man.
	 */
	private static final float IMAGE_Y_OFFSET = 0.38f;

	/**
	 * Padding used for the text.
	 */
	private static int TEXT_X_PADDING;

	/**
	 * Padding used for the text.
	 */
	private static int TEXT_Y_PADDING;

	/**
	 * Refresh rate of the running man.
	 */
	private static final int REFRESH_RATE = 50;

	/**
	 * Y ratio to place the text.
	 */
	private static final int TEXT_Y_RATIO = 4;

	private static void inizialize() {
		TEXT_X_PADDING = (int) (-7 * Constants.DISPLAY_DEFAULT_WIDTH_RATIO);
		TEXT_Y_PADDING = (int) (1 * Constants.DISPLAY_DEFAULT_HEIGHT_RATIO);
	}

	/**
	 * Full unit for distance.
	 */
	private static final String LONG_UNIT = "miles"; //$NON-NLS-1$

	/**
	 * Short unit for ditance.
	 */
	private static final String SHORT_UNIT = "mi"; //$NON-NLS-1$

	/**
	 * Set the speed to increment the progress.
	 */
	private static final int PROGRESS_ANIM_INCREMENT = 3;

	/**
	 * The string of the current distance. eg "9.9"
	 */
	private String currentDistanceStr = null;

	/**
	 * The string of the short distance. eg "9"
	 */
	private String shortDistanceStr;

	/**
	 * Current value run.
	 */
	private int currentVal = 0;

	/**
	 * Current image index.
	 */
	private int imgIndex;

	/**
	 * Last tick of the animation. -1 when not animated.
	 */
	private long lastTick = -1;

	/**
	 * Current displayed progress.
	 */
	private int visibleProgress = 0;

	/**
	 * A widget displaying the distance.
	 *
	 * @param width
	 *            Width of the widget.
	 * @param height
	 *            Height of the widget.
	 * @param position
	 *            Initial position.
	 */
	public DistanceWidget(int width, int height, ScreenArea position) {
		super(width, height, position);
		inizialize();
		updateCurrentVal();
	}

	/**
	 * Gets the x offset of a string.
	 *
	 * @param width
	 *            Width of the string.
	 * @param ratio
	 *            Ratio of the animation.
	 * @return The x offset.
	 */
	private int getStringXOffset(int width, final float ratio) {
		int centerOffset = -width / 2;
		int leftOffset = TEXT_X_PADDING;
		int rightOffset = -(width + TEXT_X_PADDING);

		return Utils.getXOffset(centerOffset, leftOffset, rightOffset, this.currentPosition, this.targetPosition,
				(1 - ratio));
	}

	/**
	 * Get the Y offset of a text.
	 *
	 * @param ratio
	 *            Ratio of the animation.
	 * @param diameter
	 *            Diameter of the bubble.
	 * @return The Y offset.
	 */
	private int getStringYOffset(float ratio, int diameter) {
		int centerOffset = diameter / TEXT_Y_RATIO;
		int topOffset = TEXT_Y_PADDING;
		int bottomOffset = -(TEXT_Y_PADDING + this.font.getHeight());

		int tmp = Utils.getYOffset(centerOffset, topOffset, bottomOffset, this.currentPosition, this.targetPosition,
				(1 - ratio));
		return tmp;
	}

	@Override
	public void redraw(GraphicsContext g, Direction direction, int completion, int x, int y) {
		super.redraw(g, direction, completion, x, y);
		g.setColor(Constants.COLOR_FOREGROUND);

		int startAngle = ELIPSE_START_ANGLE;
		int arcangle = -ELIPSE_ARCANGLE;
		int currentProgress = ((this.visibleProgress > 0) && (this.visibleProgress < this.currentVal))
				? -this.visibleProgress : -this.currentVal;

		// Distance text.
		StringBuffer text = new StringBuffer();
		if (direction == Direction.CenterStill) {
			text.append(this.currentDistanceStr);
		} else {
			text.append(this.shortDistanceStr);
		}
		text.append(" "); //$NON-NLS-1$

		float ratio = (float) completion / Constants.COMPLETION_MAX;

		// Compute diameter.
		int diameter = this.smallDiameter;
		if (direction != Direction.CornerStill && direction != Direction.CornerSwitch) {
			diameter = (int) ((this.largeDiameter - this.smallDiameter)
					* ((direction == Direction.ToCorner) ? (1 - ratio) : ratio) + this.smallDiameter);
		}

		if (direction == Direction.ToCorner) {
			currentProgress *= (1.0f - ratio);
			// Stop the running man.
			stopAnimation();
		} else if (direction == Direction.ToCenter) {
			currentProgress *= ratio;
		} else if (direction == Direction.CornerStill) {
			updateCurrentVal();
			// Draw full circle
			startAngle = 0;
			arcangle = ANGLE_FULL_CIRCLE;
		} else if (direction == Direction.CornerSwitch) {
			// Full circle
			startAngle = 0;
			arcangle = ANGLE_FULL_CIRCLE;
		} else if (direction == Direction.CenterStill) {
			startAnimation();
			redawRunner(g);
		}

		if (direction == Direction.CenterStill) {
			text.append(LONG_UNIT);
			g.setFont(this.font);
			String string = text.toString();
			// Draw the full distance.
			g.drawString(string, x + getStringXOffset(this.font.stringWidth(string), ratio),
					y + getStringYOffset(ratio, diameter), 0);
		} else {
			String unit = text.toString();
			int valueWidth = Constants.FONT_36.stringWidth(unit);
			int stringWidth = valueWidth + this.font.stringWidth(SHORT_UNIT);
			int getHeightDiff = this.font.getHeight() - Constants.FONT_36.getHeight();
			g.setFont(Constants.FONT_36);
			// Draw the distance value.
			g.drawString(unit, x + getStringXOffset(stringWidth, ratio),
					y + getStringYOffset(ratio, diameter) + getHeightDiff, 0);
			g.setFont(this.font);
			// Drax the unit.
			g.drawString(SHORT_UNIT, valueWidth + x + getStringXOffset(stringWidth, ratio),
					y + getStringYOffset(ratio, diameter), 0);
		}

		// Center x and y.
		x -= diameter / 2;
		y -= diameter / 2;

		// Draw the progress circle.
		Utils.drawEllipseArc(g, x, y, diameter, startAngle, arcangle, ELIPSE_THICKNESS, Constants.DEFAULT_FADE);
		if (direction != Direction.CornerSwitch && direction != Direction.CornerStill && currentProgress != 0) {
			Utils.drawEllipseArc(g, x, y, diameter + ELIPSE_PROGRESS_THICKNESS / 2, startAngle, currentProgress,
					ELIPSE_PROGRESS_THICKNESS, Constants.DEFAULT_FADE);
		}
	}

	@Override
	public void render(GraphicsContext g) {
		super.render(g);
		redraw(g, this.direction, this.transitionCompletion, getX(), getY());
	}

	/**
	 * Draw the running man.
	 *
	 * @param g
	 *            The graphic context.
	 */
	private void redawRunner(GraphicsContext g) {
		Image img = Images.RUNNER_SEQ.getImg(this.imgIndex);
		if (img != null) {
			g.drawImage(img, getX() - img.getWidth() / 2, getY() - (int) (this.smallDiameter * IMAGE_Y_OFFSET), 0);
		}
	}

	/**
	 * Update the runner image.
	 */
	private void updateAnimationData() {
		this.imgIndex++;
		this.imgIndex = this.imgIndex % Images.RUNNER_SEQ.getImgCount();
		if (this.visibleProgress > 0) {
			this.visibleProgress += PROGRESS_ANIM_INCREMENT;
			if (this.visibleProgress > this.currentVal) {
				this.visibleProgress = 0;
			}
		}
	}

	/**
	 * Update the progress value.
	 */
	private void updateCurrentVal() {
		float currentDistance = PROVIDER.getDistance();
		float totalDistance = PROVIDER.getTargetDistance();
		currentDistance = (currentDistance < 0) ? -currentDistance : currentDistance;

		if (currentDistance >= totalDistance) {
			this.currentVal = ELIPSE_ARCANGLE;
		} else {
			this.currentVal = (int) (ELIPSE_ARCANGLE * currentDistance / totalDistance);
		}
		String temp = Utils.floatToString(currentDistance, 1);
		this.shortDistanceStr = String.valueOf(Math.round(currentDistance));
		this.currentDistanceStr = temp;
	}

	@Override
	public DatePosition getDatePosition() {
		return DatePosition.POSITION1;
	}

	@Override
	public boolean tick(long currentTimeMillis) {
		if (this.lastTick == -1) {
			return false;
		}
		updateCurrentVal();
		if (currentTimeMillis - this.lastTick >= REFRESH_RATE) {
			this.lastTick = currentTimeMillis;
			updateAnimationData();
		}
		return true;
	}

	@Override
	public void startAnimation() {
		if (this.lastTick == -1) {
			this.lastTick = 0;
			Animator animator = ServiceLoaderFactory.getServiceLoader().getService(Animator.class);
			animator.startAnimation(this);
		}
	}

	@Override
	public void stopAnimation() {
		this.lastTick = -1;
	}
}
