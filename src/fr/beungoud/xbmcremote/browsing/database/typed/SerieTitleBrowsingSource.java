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

public class SerieTitleBrowsingSource extends BrowsingSource {
	
	TypedBrowsingListAdapter adapter;

	static String QUERY = "select C00,totalcount,watchedcount,idShow, strPath from Tvshowview order by C00";

	static String TITLE = "%1$s";

	static String SUBTITLE = "%3$s seen, %2$s total";
	
	public SerieTitleBrowsingSource(Intent intent, Activity activity, XbmcRequester requester) {
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

		List<String[]> result = requester.requestQuery(MediaType.Video, Query,5);

		for (String[] strings : result) {
			SerieTitleDbBrowseItem item = new SerieTitleDbBrowseItem();

			formatter = new Formatter();
			String title = (formatter.format(TITLE, strings).toString());
			item.setName(title);
			formatter = new Formatter();
			item.setComment(formatter.format(SUBTITLE, strings).toString());
			item.setMediaType(MediaType.Video);

			item.setFolder(false);
			
			item.setIdSerie(new Long(strings[3]));
			
			
			item.setPath(strings[4]);
			
			item.setHash("Video", Utils.Hash(item.getPath()));
			
			Intent intent = new Intent();
			intent.setClass(getActivity(), TypedBrowsingActivity.class);
			intent.putExtra(BrowsingFactory.IntentSourceEnum.TypeBrowsing.toString(), 
					BrowsingFactory.TypeBrowsingEnum.SERIE_SEASONS);
			intent.putExtra(BrowsingFactory.IntentSourceEnum.NomSerie.toString(), strings[0]);
			intent.putExtra(BrowsingFactory.IntentSourceEnum.IdSerie.toString(), item.getIdSerie());
			intent.putExtra((String) activity.getText(R.string.key_media_type), MediaType.Video);
			item.setIntentAction(intent);
		
			list.add(item);
		}
		adapter.setList(list);

	}

	@Override
	public String getTitle() {
		return "TV Shows";
	}
}
