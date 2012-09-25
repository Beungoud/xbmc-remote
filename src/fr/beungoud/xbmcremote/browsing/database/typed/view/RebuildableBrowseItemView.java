/**
 * 
 */
package fr.beungoud.xbmcremote.browsing.database.typed.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import fr.beungoud.xbmcremote.browsing.BrowseItem;
import fr.beungoud.xbmcremote.browsing.BrowseItemView;
import fr.beungoud.xbmcremote.browsing.database.typed.AbstractDbBrowseItem;

/**
 * @author Benoit
 * 
 */
public class RebuildableBrowseItemView extends BrowseItemView implements ImageRefreshableInterface {

	private Long buildId = null;

	/**
	 * The browsze item used in case of a rebuild
	 */
	private BrowseItem browseItem;

	private Handler handler;

	public RebuildableBrowseItemView(Context context, BrowseItem browseItem) {
		super(context);

		setBrowseItem(browseItem);

		handler = new Handler();

	}

	@Override
	public void setThumbnail(Bitmap bmp, String path) {
		// Pass through the handler to be sure we are in the correct thread.
		handler.post(new RunnableHandler(bmp, path));
	}

	/**
	 * @param browseItem
	 *            the browseItem to set
	 */
	public void setBrowseItem(BrowseItem browseItem) {
		this.browseItem = browseItem;
	}

	/**
	 * @return the browseItem
	 */
	public BrowseItem getBrowseItem() {
		return browseItem;
	}

	/**
	 * @param buildId
	 *            the buildId to set
	 */
	public void setBuildId(Long buildId) {
		this.buildId = buildId;
	}

	/**
	 * @return the buildId
	 */
	public String getThumbPath() {
		return getPath();
	}

	public void setViewImage(Bitmap bm) {
		if (bm != null) {
			if (bm.getWidth() > bm.getHeight() * 3) {
				viewImageBack.setImageBitmap(bm);
				viewImage.setImageBitmap(null);
			} else {
				viewImage.setImageBitmap(bm);
				viewImageBack.setImageBitmap(null);
			}
		} else {
			updateImage();
		}
	}

	/**
	 * Le runnable utilisé dans le handler.
	 * 
	 * @author Benoit
	 * 
	 */
	private class RunnableHandler implements Runnable {
		Bitmap bmp = null;
		String path = null;

		public RunnableHandler(Bitmap bmp, String path) {
			this.bmp = bmp;
			this.path = path;
		}

		@Override
		public void run() {

			if (this.path == getThumbPath()) {
				setViewImage(bmp);
			}
		}
	}
}
