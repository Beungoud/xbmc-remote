package fr.beungoud.xbmcremote.browsing.database.typed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.BaseAdapter;
import fr.beungoud.util.Utils;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory.IntentSourceEnum;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory.TypeBrowsingEnum;

public class SerieEpisodeBrowsingSource extends BrowsingSource {
	
	TypedBrowsingListAdapter adapter;

	private Long idShow;

	private Long numeroSaison;

	private String nomSerie;
	
	static String QUERY = "select C00,c12,c13,coalesce(playcount, '0'),idEpisode, strPath, strFileName from episodeview where idShow='%1$s' and c12='%2$s' order by c13";

	static String TITLE = "%1$s";

	static String SUBTITLE = "s%2$s e%3$s seen %4$s time(s)";
	
	public SerieEpisodeBrowsingSource(Intent intent, Activity activity, XbmcRequester requester) {
		super(intent, activity, requester);

		adapter = new TypedBrowsingListAdapter();
		
		idShow = (Long)intent.getExtras().get(IntentSourceEnum.IdSerie.toString());

		numeroSaison = (Long)intent.getExtras().get(IntentSourceEnum.NumeroSaison.toString());

		nomSerie = (String)intent.getExtras().get(IntentSourceEnum.NomSerie.toString());
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

		String Query = formatter.format(QUERY, idShow.toString(), numeroSaison.toString()).toString();
		Log.i("XBMC", Query);

		List<String[]> result = requester.requestQuery(MediaType.Video, Query,7);

		for (String[] strings : result) {
			SerieEpisodeDbBrowseItem item = new SerieEpisodeDbBrowseItem();

			formatter = new Formatter();
			String title = (formatter.format(TITLE, strings).toString());
			item.setName(title);
			formatter = new Formatter();
			item.setComment(formatter.format(SUBTITLE, strings).toString());
			item.setMediaType(MediaType.Video);

			item.setFolder(false);
			
			item.setIdEpisode(new Long(strings[4]));
			item.setEpisodeName(strings[0]);
			item.setNumeroEpisode(new Integer(strings[2]));
			
			item.setPath(strings[5] + strings[6]);
			
			item.setHash("Video", Utils.Hash(item.getPath()));

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
		
		Collections.sort(list, new Comparator<AbstractDbBrowseItem>(){
			@Override
			public int compare(AbstractDbBrowseItem object1, AbstractDbBrowseItem object2) {
				if (object1 instanceof SerieEpisodeDbBrowseItem && object2 instanceof SerieEpisodeDbBrowseItem )
				{
					SerieEpisodeDbBrowseItem o1 = (SerieEpisodeDbBrowseItem)object1;
					SerieEpisodeDbBrowseItem o2 = (SerieEpisodeDbBrowseItem)object2;
					return o1.getNumeroEpisode() - o2.getNumeroEpisode();
				}
				return 0;
			}
			
		});
		
		adapter.setList(list);

	}

	@Override
	public String getTitle() {
		return nomSerie + " s" + numeroSaison;
	}
}
