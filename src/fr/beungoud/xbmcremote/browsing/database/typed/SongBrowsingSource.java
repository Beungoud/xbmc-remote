package fr.beungoud.xbmcremote.browsing.database.typed;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.BaseAdapter;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory.IntentSourceEnum;

public class SongBrowsingSource extends BrowsingSource {
	
	TypedBrowsingListAdapter adapter;

	private Long idAlbum;

	private String nomAlbum;

	private String nomArtist;

	static String QUERY = "select strTitle,idSong, strPath, strFileName,strThumb from songview where idAlbum='%1$s'";

	static String TITLE = "%1$s";

	static String SUBTITLE = "";
	
	public SongBrowsingSource(Intent intent, Activity activity, XbmcRequester requester) {
		super(intent, activity, requester);

		adapter = new TypedBrowsingListAdapter();
		
		idAlbum = (Long)intent.getExtras().get(IntentSourceEnum.IdAlbum.toString());
		
		nomAlbum = intent.getExtras().getString(IntentSourceEnum.NomAlbum.toString());
		nomArtist = intent.getExtras().getString(IntentSourceEnum.NomArtist.toString());
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

		String Query = formatter.format(QUERY, idAlbum.toString()).toString();
		Log.i("XBMC", Query);

		List<String[]> result = requester.requestQuery(MediaType.Music, Query,5);

		for (String[] strings : result) {
			SongDbBrowseItem item = new SongDbBrowseItem();

			formatter = new Formatter();
			String title = (formatter.format(TITLE, strings).toString());
			item.setName(title);
			formatter = new Formatter();
			item.setComment(formatter.format(SUBTITLE, strings).toString());
			item.setMediaType(MediaType.Music);

			item.setFolder(false);
			
			item.setIdSong(Long.parseLong(strings[1]));
			item.setPath(strings[2] + strings[3]);
			
			item.setThumbPath(strings[4]);
			
			list.add(item);
		}
		adapter.setList(list);

	}

	@Override
	public String getTitle() {
		return nomArtist + " : " + nomAlbum;
	}
}
