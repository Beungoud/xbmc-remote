package fr.beungoud.xbmcremote.browsing;

import java.util.ArrayList;
import java.util.List;

import fr.beungoud.util.Base64;
import fr.beungoud.util.ConfigurationAccess;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.database.typed.view.ImageRefresher;
import fr.beungoud.xbmcremote.browsing.database.typed.view.RebuildableBrowseItemView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class BrowsingListAdapter extends BaseAdapter implements ListAdapter{
	
	List<BrowseItem> listItems = new ArrayList<BrowseItem>()	;
	
	public void setList(List<BrowseItem> list)
	{
		listItems = list;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return listItems.get(position).getPath().hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RebuildableBrowseItemView rebuildateBrowseItemView = (RebuildableBrowseItemView)convertView;
		BrowseItem item = listItems.get(position);
		if (rebuildateBrowseItemView== null)
		{
			rebuildateBrowseItemView = new RebuildableBrowseItemView(parent.getContext(), item);
		}
		rebuildateBrowseItemView.setName(item.getName());
		rebuildateBrowseItemView.setMediaType(item.getMediaType());
		rebuildateBrowseItemView.setPath(item.getPath());
		rebuildateBrowseItemView.setFolder(item.isFolder());
		rebuildateBrowseItemView.setSousTitre(item.getComment());
		rebuildateBrowseItemView.setSourceItem(item);
		

		if (rebuildateBrowseItemView.getThumbPath() != null &&	ConfigurationAccess.getInstance().isThumbnailEnabled()) {
			
			rebuildateBrowseItemView.setPath(item.getThumbPath());


			ImageRefresher.getInstance().addRefreshable(rebuildateBrowseItemView);
		}
		//TV.setText(item.getName());
//		try {
//			String mediaType = "Video";
//			switch (item.getMediaType()) {
//			case Music:
//				mediaType = "Music";
//				break;
//			case Picture:
//				mediaType = "Pictures";
//			case Video:
//				mediaType = "Video";
//			default:
//				break;
//			}
//			XbmcRequester requester = XbmcRequester.getInstace();
//			String result = requester.request("FileDownload(special://masterprofile/Thumbnails/" + mediaType
//					+ "/"+ item.getHash().substring(0, 1) + "/" + item.getHash() + ".tbn)");
//
//			result = result.substring("<html>".length(), result.length() - "</html>".length()).trim();
//
//			byte[] thumbnail = Base64.decode(result);
//			Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
//
//			TV.setViewImage(bitmap);
//		} catch (Exception e) {
//			Log.w("XBMC", "Impossible de récupérer " + item.getHash());
//		}

		return rebuildateBrowseItemView;
	}
	
}
