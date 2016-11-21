/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * For demonstration purpose only.
 * IS2T PROPRIETARY. Use is subject to license terms.
 */
package ej.demo.smartwatch.style;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ej.demo.smartwatch.utils.Log;
import ej.microui.display.Image;

/**
 * The images used in the application.
 */
public interface Images {

	/**
	 * The Class ImageSequance.
	 */
	class ImageSequence {

		/** The images. */
		private final List<Image> images;

		/** The path seq. */
		private final String[] pathSeq;

		/**
		 * Instantiates a new image sequence.
		 *
		 * @param pathSeq
		 *            the path to the sequence.
		 */
		public ImageSequence(String[] pathSeq) {
			super();
			this.pathSeq = pathSeq;
			images = new ArrayList<Image>();
		}

		/**
		 * Instantiates a new image sequence.
		 *
		 * @param path
		 *            Path to the image. eg "/image/myImage.
		 * @param suffix
		 *            Suffix of the image eg ".png"
		 * @param count
		 *            Number of images.
		 */
		public ImageSequence(String path, String suffix, int count) {
			pathSeq = new String[count];
			for (int i = 0; i < count; i++) {
				pathSeq[i] = path + i + suffix;
			}
			images = new ArrayList<Image>();
		}

		/**
		 * Gets the image.
		 *
		 * @param idx
		 *            the image index.
		 * @return the image.
		 */
		public Image getImg(int idx) {
			if (idx < 0 || idx >= pathSeq.length) {
				return null;
			}

			while (images.size() <= idx) {
				images.add(null);
			}

			if (images.get(idx) == null) {
				try {
					Image img = Image.createImage(pathSeq[idx]);
					images.set(idx, img);
				} catch (IOException e) {
					Log.e("ImageSequance", e); //$NON-NLS-1$
				}
			}
			return images.get(idx);
		}

		/**
		 * Gets the img count.
		 *
		 * @return the img count
		 */
		public int getImgCount() {
			return pathSeq.length;
		}
	}

	/** The battery. */
	/** The battery path. */
	String BATTERY_PATH = "/images/battery."; //$NON-NLS-1$

	/** The battery suffix. */
	String BATTERY_SUFFIX = ".png"; //$NON-NLS-1$

	/** The battery count. */
	int BATTERY_COUNT = 10;

	/** The battery seq. */
	ImageSequence BATTERY_SEQ = new ImageSequence(BATTERY_PATH, BATTERY_SUFFIX, BATTERY_COUNT);

	/** The bell. */
	String BELL = "/images/bell.png"; //$NON-NLS-1$

	/** The clock paths. */
	/** The next. */
	String NEXT = "/images/next.png"; //$NON-NLS-1$

	/** The previous. */
	String PREVIOUS = "/images/previous.png"; //$NON-NLS-1$

	/** The runner paths. */
	String[] RUNNER_PATHS = new String[] { "/images/runner_1.png", "/images/runner_2.png", "/images/runner_3.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"/images/runner_4.png", "/images/runner_5.png", "/images/runner_6.png" //$NON-NLS-1$ //$NON-NLS-2$
	};
	/** The runner seq. */
	ImageSequence RUNNER_SEQ = new ImageSequence(RUNNER_PATHS);

	/** The sun. */
	String[] SUN_PATHS = new String[] { "/images/sun_1.png", "/images/sun_2.png", "/images/sun_3.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"/images/sun_4.png", //$NON-NLS-1$
			"/images/sun_5.png", "/images/sun_6.png", "/images/sun_7.png", "/images/sun_8.png", "/images/sun_9.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"/images/sun_10.png" };

	/** The rain. */
	String[] RAIN_PATHS = new String[] { "/images/rain_1.png", "/images/rain_2.png", "/images/rain_3.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"/images/rain_4.png", "/images/rain_5.png", "/images/rain_6.png", "/images/rain_7.png" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	/** The store. */
	String Strore = "/images/store.png"; //$NON-NLS-1$

	/** The cloudy. */
	String[] CLOUD_PATHS = new String[] { "/images/cloud_1.png", "/images/cloud_2.png", "/images/cloud_3.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"/images/cloud_4.png", "/images/cloud_5.png", "/images/cloud_6.png", "/images/cloud_7.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"/images/cloud_8.png", "/images/cloud_9.png", "/images/cloud_10.png" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	/**
	 * The cond sequences. NOTE this array should be kept in sync with {@link ej.demo.smartwatch.model.FakeDataProvider#forecast} values
	 */
	ImageSequence[] WEATHER_COND_SEQ = new ImageSequence[] { new ImageSequence(SUN_PATHS),
			new ImageSequence(RAIN_PATHS), new ImageSequence(CLOUD_PATHS) };
}
