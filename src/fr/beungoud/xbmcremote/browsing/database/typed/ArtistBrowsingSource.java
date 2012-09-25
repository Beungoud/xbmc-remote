package fr.beungoud.xbmcremote.browsing.database.typed;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.BaseAdapter;
import fr.beungoud.util.Utils;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;

public class ArtistBrowsingSource extends BrowsingSource {
	
	TypedBrowsingListAdapter adapter;

	static String QUERY = "select strartist,idArtist from artist order by strartist";

	static String TITLE = "%1$s";

	static String SUBTITLE = "";
	
	public ArtistBrowsingSource(Intent intent, Activity activity, XbmcRequester requester) {
		super(intent, activity, requester);

		adapter = new TypedBrowsingListAdapter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.beungoud.xbmcremote.browsing.database.BrowsingThread#getAdapter()
	 */
	@Override
	public BaseAdapter getAdapter() {

		return adapter;
	}

	@Override
	public void getData() throws RequestException {
		List<AbstractDbBrowseItem> list = new ArrayList<AbstractDbBrowseItem>();

		Formatter formatter = new Formatter();

		String Query = QUERY;
		Log.i("XBMC", Query);

		List<String[]> result = requester.requestQuery(MediaType.Music, Query,2);

		for (String[] strings : result) {
			ArtistDbBrowseItem item = new ArtistDbBrowseItem();

			formatter = new Formatter();
			String title = (formatter.format(TITLE, strings).toString());
			item.setName(title);
			formatter = new Formatter();
			item.setComment(formatter.format(SUBTITLE, strings).toString());
			item.setMediaType(MediaType.Music);

			item.setFolder(true);
			
			item.setIdArtist(Integer.parseInt(strings[1]));
			
			// Set the hash for thumbnail
			String hash =  Utils.Hash("artist" + strings[0]);

			item.setThumbPath("special://masterprofile/Thumbnails/" + "Music" + "/" + "Artists/" + hash + ".tbn");
			
			
			Intent intent = new Intent();
			intent.setClass(getActivity(), TypedBrowsingActivity.class);
			intent.putExtra(BrowsingFactory.IntentSourceEnum.TypeBrowsing.toString(), 
					BrowsingFactory.TypeBrowsingEnum.ALBUM);
			intent.putExtra(BrowsingFactory.IntentSourceEnum.IdArtist.toString(), 
					new Long(item.getIdArtist()));
			intent.putExtra(BrowsingFactory.IntentSourceEnum.NomArtist.toString(),  strings[0]);
			intent.putExtra((String) activity.getText(R.string.key_media_type), MediaType.Music);
			item.setIntentAction(intent);
			//			
			// for (String string : path) {
			// pathString = pathString + string + "/";
			// }
			// formatter = new Formatter();
			// pathString = pathString + formatter.format(queryDesc.subPath,
			// strings).toString();
			//			
			// item.setPath(pathString);
			//
			// item.setType(queryDesc.getAddFromDbMedia());
			// formatter = new Formatter();
			// item.setStatement(formatter.format(queryDesc.getAddFromDbQuery(),
			// strings).toString());
			//			
			list.add(item);
		}
		adapter.setList(list);

	}

	@Override
	public String getTitle() {
		return "Artists";
	}
}
