/**
 * 
 */
package fr.beungoud.xbmcremote.browsing.database.typed;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.BaseAdapter;
import fr.beungoud.util.Utils;
import fr.beungoud.xbmcremote.ConnexionErrorActivity;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingActivity;
import fr.beungoud.xbmcremote.browsing.DbBrowseItem;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;
import fr.beungoud.xbmcremote.browsing.database.QueryDesc;

/**
 * La browsing thread utilisé pour visualiser les Films
 * 
 * @author Benoit
 * 
 */
public class MovieBrowsingSource extends BrowsingSource {
	TypedBrowsingListAdapter adapter;

	static String QUERY = "select C00,coalesce(playcount, '0'),idMovie, strPath, strFileName from Movieview order by C00";

	static String TITLE = "%1$s";

	static String SUBTITLE = "seen %2$s time(s)";

	public MovieBrowsingSource(Intent intent, Activity activity, XbmcRequester requester) {
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

		List<String[]> result = requester.requestQuery(MediaType.Video, Query, 5);

		for (String[] strings : result) {
			MovieDbBrowseItem item = new MovieDbBrowseItem();

			formatter = new Formatter();
			String title = (formatter.format(TITLE, strings).toString());
			item.setName(title);
			formatter = new Formatter();
			item.setComment(formatter.format(SUBTITLE, strings).toString());
			item.setMediaType(MediaType.Video);

			item.setFolder(false);
			
			item.setFilePath(strings[3] + strings[4]);
			
			item.setPath(strings[3] + strings[4]);
			
			item.setHash("Video", Utils.Hash(item.getPath()));
		
			list.add(item);
		}
		adapter.setList(list);

	}

	@Override
	public String getTitle() {
		return "Movies";
	}

}
