/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package universalimageloader.core;

import android.graphics.Bitmap;

import universalimageloader.core.display.BitmapDisplayer;
import universalimageloader.utils.L;

/**
 * Displays bitmap in {@link universalimageloader.core.imageaware.ImageAware}. Must be called on UI thread.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see universalimageloader.core.listener.ImageLoadingListener
 * @see BitmapDisplayer
 * @since 1.3.1
 */
final class DisplayBitmapTask implements Runnable {

	private static final String LOG_DISPLAY_IMAGE_IN_IMAGEAWARE = "Display image in ImageAware (loaded from %1$s) [%2$s]";
	private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
	private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ImageAware was collected by GC. Task is cancelled. [%s]";

	private final Bitmap bitmap;
	private final String imageUri;
	private final universalimageloader.core.imageaware.ImageAware imageAware;
	private final String memoryCacheKey;
	private final BitmapDisplayer displayer;
	private final universalimageloader.core.listener.ImageLoadingListener listener;
	private final universalimageloader.core.ImageLoaderEngine engine;
	private final universalimageloader.core.assist.LoadedFrom loadedFrom;
	private final Bitmap bit;
	public DisplayBitmapTask(Bitmap bitmap, ImageLoadingInfo imageLoadingInfo, universalimageloader.core.ImageLoaderEngine engine,
                             universalimageloader.core.assist.LoadedFrom loadedFrom, Bitmap bit) {
		this.bitmap = bitmap;
		this.bit = bit;
		imageUri = imageLoadingInfo.uri;
		imageAware = imageLoadingInfo.imageAware;
		memoryCacheKey = imageLoadingInfo.memoryCacheKey;
		displayer = imageLoadingInfo.options.getDisplayer();
		listener = imageLoadingInfo.listener;
		this.engine = engine;
		this.loadedFrom = loadedFrom;
	}

	@Override
	public void run() {
		if (imageAware.isCollected()) {
			L.d(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED, memoryCacheKey);
			listener.onLoadingCancelled(imageUri, imageAware.getWrappedView());
		} else if (isViewWasReused()) {
			L.d(LOG_TASK_CANCELLED_IMAGEAWARE_REUSED, memoryCacheKey);
			listener.onLoadingCancelled(imageUri, imageAware.getWrappedView());
		} else {
			L.d(LOG_DISPLAY_IMAGE_IN_IMAGEAWARE, loadedFrom, memoryCacheKey);
			displayer.display(bitmap, imageAware, loadedFrom);
			engine.cancelDisplayTaskFor(imageAware);
			if(null ==bit)
			{
				listener.onLoadingComplete(imageUri, imageAware.getWrappedView(), bitmap);
			}
			else
			{
				listener.onLoadingComplete1(imageUri, imageAware.getWrappedView(),bitmap,bit);
			}
		}
	}

	/** Checks whether memory cache key (image URI) for current ImageAware is actual */
	private boolean isViewWasReused() {
		String currentCacheKey = engine.getLoadingUriForView(imageAware);
		return !memoryCacheKey.equals(currentCacheKey);
	}
}