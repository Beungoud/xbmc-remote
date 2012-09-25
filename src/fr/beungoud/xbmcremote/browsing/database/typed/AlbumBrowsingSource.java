package fr.beungoud.xbmcremote.browsing.database.typed;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.BaseAdapter;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory.IntentSourceEnum;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory.TypeBrowsingEnum;

public class AlbumBrowsingSource extends BrowsingSource {
	
	TypedBrowsingListAdapter adapter;

	private Long idArtist;

	private String nomArtist;

	static String QUERY = "select strAlbum,strGenre,idAlbum,strThumb from albumview where idArtist='%1$s' order by strAlbum";

	static String TITLE = "%1$s";

	static String SUBTITLE = "%2$s";
	
	public AlbumBrowsingSource(Intent intent, Activity activity, XbmcRequester requester) {
		super(intent, activity, requester);

		adapter = new TypedBrowsingListAdapter();
		
		idArtist = (Long)intent.getExtras().get(IntentSourceEnum.IdArtist.toString());

		nomArtist = (String)intent.getExtras().get(IntentSourceEnum.NomArtist.toString());
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

		String Query = formatter.format(QUERY, idArtist.toString()).toString();
		Log.i("XBMC", Query);

		List<String[]> result = requester.requestQuery(MediaType.Music, Query,4);

		for (String[] strings : result) {
			AlbumDbBrowseItem item = new AlbumDbBrowseItem();

			formatter = new Formatter();
			String title = (formatter.format(TITLE, strings).toString());
			item.setName(title);
			formatter = new Formatter();
			item.setComment(formatter.format(SUBTITLE, strings).toString());
			item.setMediaType(MediaType.Music);

			item.setFolder(true);
			
			item.setIdAlbum(Long.parseLong(strings[2]));
			
			item.setThumbPath(strings[3]);
			

			Intent intent = new Intent();
			intent.setClass(getActivity(), TypedBrowsingActivity.class);
			intent.putExtra(BrowsingFactory.IntentSourceEnum.TypeBrowsing.toString(), 
					BrowsingFactory.TypeBrowsingEnum.SONGS);
			intent.putExtra(BrowsingFactory.IntentSourceEnum.IdAlbum.toString(), 
					item.getIdAlbum());
			intent.putExtra(BrowsingFactory.IntentSourceEnum.NomAlbum.toString(),  strings[0]);
			intent.putExtra(BrowsingFactory.IntentSourceEnum.NomArtist.toString(),  nomArtist);
			
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
		return nomArtist;
	}
}
