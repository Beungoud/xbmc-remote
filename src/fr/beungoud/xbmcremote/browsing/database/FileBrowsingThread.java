package fr.beungoud.xbmcremote.browsing.database;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Intent;
import android.widget.BaseAdapter;
import fr.beungoud.util.Utils;
import fr.beungoud.xbmcremote.ConnexionErrorActivity;
import fr.beungoud.xbmcremote.Constants;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowseItem;
import fr.beungoud.xbmcremote.browsing.BrowsingActivity;
import fr.beungoud.xbmcremote.browsing.BrowsingListAdapter;
import fr.beungoud.xbmcremote.browsing.FileBrowseItem;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;

public class FileBrowsingThread extends BrowsingThread {

	BrowsingListAdapter adapter;

	public FileBrowsingThread(String browsePath, BrowsingActivity activity, MediaType mediaType, XbmcRequester requester) {
		super(browsePath, activity, mediaType, requester);
		adapter = new BrowsingListAdapter();
	}

	public void run() {
		String request;
		boolean isRoot = false;
		if (path.equals("/")) {
			request = "GetShares(" + mediaType + ")";
			isRoot = true;
		} else {
			request = "GetMediaLocation(" + mediaType + ";" + path + ")";
			isRoot = false;
		}

		try {
			List<String> stringList = requester.requestStringList(request);

			List<BrowseItem> browseList = new ArrayList<BrowseItem>();
			for (String line : stringList) {
				BrowseItem item = new FileBrowseItem();
				StringTokenizer tockenizer = new StringTokenizer(line, ";");
				if (isRoot) {
					if (tockenizer.countTokens() == 2) {
						item.setMediaType(mediaType);

						item.setFolder(true);

						item.setName(tockenizer.nextToken());

						item.setPath(tockenizer.nextToken());
						browseList.add(item);
					}
				} else {
					if (tockenizer.countTokens() == 3) {
						item.setName(tockenizer.nextToken());

						item.setPath(tockenizer.nextToken());
						
						String mediaExt = tockenizer.nextToken();

						if (mediaExt.equals("1")) {
							item.setFolder(true);
						} else {
							item.setFolder(false);
						}
						if (mediaType==MediaType.Music)
						{
							String path = item.getPath();
							if (path.endsWith("/"))
							{
								path = item.getPath().substring(0, path.length()-1);
							}
							item.setHash("Music", Utils.Hash(path));
						} else if (MediaType.Video==mediaType)
						{
							item.setHash("Video", Utils.Hash(item.getPath()));
							
						}
						
						item.setMediaType(mediaType);
						browseList.add(item);
					}
				}
			}

			adapter.setList(browseList);
		} catch (RequestException e) {

			Intent intent = new Intent(activity, ConnexionErrorActivity.class);
			
			activity.startActivity(intent);
			

			activity.finish();
		}

		activity.getHandler().sendEmptyMessage(BrowsingActivity.INVALIDATE_LIST);
	}

	@Override
	public BaseAdapter getAdapter() {
		return adapter;
	}

	@Override
	public int getPlaylistId() {
		int playListId = -1;
		if (mediaType == MediaType.Music) {
			playListId = Constants.ID_PLAYLIST_MUSIC;
		} else if (mediaType == MediaType.Pictures) {
			playListId = -1;
		} else if (mediaType == MediaType.Video) {
			playListId = Constants.ID_PLAYLIST_VIDEO;
		}
		return playListId;
	}
}
