package fr.beungoud.xbmcremote.browsing.database.typed.view;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import fr.beungoud.util.Base64;
import fr.beungoud.util.ConfigurationAccess;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowseItem;
import fr.beungoud.xbmcremote.browsing.database.typed.AbstractDbBrowseItem;
import fr.beungoud.xbmcremote.browsing.database.typed.TypedBrowsingListAdapter;

public class ViewFactory {

	/**
	 * The Id Used to identify the different view, and be sure, the updater
	 * thread has to update a view.
	 * 
	 */
	private static Long currentId = new Long(0);

	private static BlockingQueue<Runnable> QueuedEvent = new ArrayBlockingQueue<Runnable>(15);
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 10000, TimeUnit.MILLISECONDS, QueuedEvent);

	/**
	 * Permet la conversion des browseItem en vue correpondant pour les listes
	 * qui vont bien. Utilisé par le {@link TypedBrowsingListAdapter}
	 * 
	 * @param browseItem
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public static View createView(AbstractDbBrowseItem browseItem, View convertView, View parent) {
		View view = (View) convertView;
		RebuildableBrowseItemView browseItemView = null;

		if (view == null) {
			browseItemView = new RebuildableBrowseItemView(parent.getContext(), browseItem);
		} else {
			if (view instanceof RebuildableBrowseItemView) {
				browseItemView = (RebuildableBrowseItemView) view;

			} else {
				browseItemView = new RebuildableBrowseItemView(parent.getContext(), browseItem);
			}
		}

		long id = currentId++;
		browseItem.setBuildId(id);
		browseItemView.setBuildId(id);

		refreshView(browseItem, browseItemView);

		// BrowseItemRefresher refresher = new BrowseItemRefresher(browseItem,
		// browseItemView, id);
		//
		// try {
		// executor.execute(refresher);
		// } catch (Exception e) {
		// // Execution was rejected
		// }

		return browseItemView;
	}

	public static void refreshView(BrowseItem browseItem, RebuildableBrowseItemView refreshed) {

		refreshed.setName(browseItem.getName());
		refreshed.setMediaType(browseItem.getMediaType());
		refreshed.setPath(browseItem.getPath());
		refreshed.setFolder(browseItem.isFolder());
		refreshed.setSousTitre(browseItem.getComment());
		refreshed.setSourceItem(browseItem);
		refreshed.setViewImage(null);

		if (ConfigurationAccess.getInstance().isThumbnailEnabled()) {
			if (browseItem.getThumbPath() != null) {

				refreshed.setPath(browseItem.getThumbPath());

				ImageRefresher.getInstance().addRefreshable(refreshed);
			}
		}
	}

}
