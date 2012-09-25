/**
 * 
 */
package fr.beungoud.xbmcremote.browsing.database.typed.view;

import java.util.HashMap;
import java.util.Stack;

import fr.beungoud.util.Base64;
import fr.beungoud.util.ConfigurationAccess;
import fr.beungoud.xbmcremote.XbmcRequester;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * This class is responsible for refreshing all the thumbnails while browsing.
 * It runs in a background thread.
 * 
 * @author Benoit
 * 
 */
public class ImageRefresher {
	private static ImageRefresher instance = new ImageRefresher();

	public static ImageRefresher getInstance() {
		return instance;
	}

	protected boolean finished = true;

	private ImageRefresher() {
		dataCache = new HashMap<String, Bitmap>();
		toBeChecked = new Stack<ImageRefreshableInterface>();
		toBeDownloaded = new Stack<ImageRefreshableInterface>();
	}

	private HashMap<String, Bitmap> dataCache;

	private Stack<ImageRefreshableInterface> toBeChecked;

	private Stack<ImageRefreshableInterface> toBeDownloaded;

	private CheckingThread checkThread;

	private DownloadingThread downloadThread;

	public void addRefreshable(ImageRefreshableInterface refreshable) {
		toBeChecked.add(refreshable);
		synchronized (toBeChecked) {
			toBeChecked.notify();
		}
	}

	public void start() {
		if (finished && ConfigurationAccess.getInstance().isThumbnailEnabled()) {
			finished = false;
			checkThread = new CheckingThread();
			checkThread.start();

			downloadThread = new DownloadingThread();
			downloadThread.start();
		}
	}

	public void stop() {
		if (!finished) {
			finished = true;
			checkThread.interrupt();
			downloadThread.interrupt();
		}
	}

	public class DownloadingThread extends Thread {
		public void run() {
			while (!finished && !Thread.interrupted()) {
				// retreaving the latest item
				ImageRefreshableInterface refreshable = null;
				if (!toBeDownloaded.isEmpty())
					refreshable = toBeDownloaded.pop();
				if (refreshable != null) {
					try {
						if(((RebuildableBrowseItemView)refreshable).getName().equals("clarika"))
						{
							Log.i("XBMC", "Clarika");
						}
						String downloadedPath = refreshable.getThumbPath();
						// Look in the cache
						if (dataCache.containsKey(downloadedPath)) {
							refreshable.setThumbnail(dataCache.get(downloadedPath), downloadedPath);
						} else // not in the cache
						{
							XbmcRequester requester = XbmcRequester.getInstace();
							String result = requester.request("FileDownload(" + downloadedPath + ")");

							result =requester.removeHtmlTags(result);

							byte[] thumbnail = Base64.decode(result);
							Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
							
							int desiredHeight = 90;
							
							Bitmap petit = Bitmap.createScaledBitmap(bitmap, (int)((bitmap.getWidth() *desiredHeight)/(double)bitmap.getHeight()), desiredHeight, true);

							refreshable.setThumbnail(petit, downloadedPath);
							dataCache.put(downloadedPath, petit);
						}
					} catch (Exception e) {
						Log.w("XBMC", "Impossible de récupérer " + refreshable.getThumbPath());
					}
				} else {
					try {
						synchronized (toBeDownloaded) {
							toBeDownloaded.wait(300);
						}

					} catch (InterruptedException e) {

					}
				}
			}
		}
	}

	public class CheckingThread extends Thread {
		public void run() {
			while (!finished && !Thread.interrupted()) {
				// retreaving the latest item
				ImageRefreshableInterface refreshable = null;
				if (!toBeChecked.isEmpty())
					refreshable = toBeChecked.pop();
				if (refreshable != null) {
					if(((RebuildableBrowseItemView)refreshable).getName().equals("clarika"))
					{
						Log.i("XBMC", "Clarika");
					}
					try {
						String downloadedPath = refreshable.getThumbPath();

						// Look in the cache
						if (dataCache.containsKey(downloadedPath)) {
							refreshable.setThumbnail(dataCache.get(downloadedPath), downloadedPath);
						} // not in the cache
						else {
							toBeDownloaded.add(refreshable);
							synchronized (toBeDownloaded) {
								toBeDownloaded.notify();
							}
						}
					} catch (Exception e) {
						Log.w("XBMC", "Impossible de récupérer " + refreshable.getThumbPath());
					}
				} else {
					try {
						synchronized (toBeChecked) {
							toBeChecked.wait(300);
						}

					} catch (InterruptedException e) {

					}
				}
			}
		}
	}
}
