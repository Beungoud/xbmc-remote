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
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory.IntentSourceEnum;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory.TypeBrowsingEnum;

public class SerieSaisonBrowsingSource extends BrowsingSource {
	
	TypedBrowsingListAdapter adapter;

	private Long idShow;

	private String nomSerie;

	static String QUERY = "select DISTINCT  ev.C12,ev.idShow,show.strPath from episodeview ev,tvshowview show where ev.idShow='%1$s' and show.idShow='%1$s' order by ev.C12";

	static String TITLE = "Season %1$s";

	static String SUBTITLE = "";
	
	public SerieSaisonBrowsingSource(Intent intent, Activity activity, XbmcRequester requester) {
		super(intent, activity, requester);

		adapter = new TypedBrowsingListAdapter();
		
		idShow = (Long)intent.getExtras().get(IntentSourceEnum.IdSerie.toString());

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

		String Query = formatter.format(QUERY, idShow.toString()).toString();
		Log.i("XBMC", Query);

		List<String[]> result = requester.requestQuery(MediaType.Video, Query,3);

		for (String[] strings : result) {
			SerieSeasonDbBrowseItem item = new SerieSeasonDbBrowseItem();

			formatter = new Formatter();
			String title = (formatter.format(TITLE, strings).toString());
			item.setName(title);
			formatter = new Formatter();
			item.setComment(formatter.format(SUBTITLE, strings).toString());
			item.setMediaType(MediaType.Video);

			item.setFolder(true);
			
			item.setIdShow(idShow);
			
			item.setNumeroSaison(new Long(strings[0]));
			
			item.setNomSerie(nomSerie);
			item.setPath("season" + strings[2] + "Season " + item.getNumeroSaison());
			
			item.setHash("Video", Utils.Hash(item.getPath()));

			
			Intent intent = new Intent();
			intent.setClass(getActivity(), TypedBrowsingActivity.class);
			intent.putExtra(BrowsingFactory.IntentSourceEnum.TypeBrowsing.toString(), 
					BrowsingFactory.TypeBrowsingEnum.SERIE_EPISODES);
			intent.putExtra(BrowsingFactory.IntentSourceEnum.NomSerie.toString(), 
					item.getNomSerie());
			
			intent.putExtra(BrowsingFactory.IntentSourceEnum.IdSerie.toString(),  item.getIdShow());
			intent.putExtra(BrowsingFactory.IntentSourceEnum.NumeroSaison.toString(), item.getNumeroSaison());
			intent.putExtra((String) activity.getText(R.string.key_media_type), MediaType.Video);
			item.setIntentAction(intent);
			list.add(item);
		}
		

		Collections.sort(list, new Comparator<AbstractDbBrowseItem>(){
			@Override
			public int compare(AbstractDbBrowseItem object1, AbstractDbBrowseItem object2) {
				if (object1 instanceof SerieSeasonDbBrowseItem && object2 instanceof SerieSeasonDbBrowseItem )
				{
					SerieSeasonDbBrowseItem o1 = (SerieSeasonDbBrowseItem)object1;
					SerieSeasonDbBrowseItem o2 = (SerieSeasonDbBrowseItem)object2;
					return (int) (o1.getNumeroSaison() - o2.getNumeroSaison());
				}
				return 0;
			}
			
		});
		
		adapter.setList(list);

	}


	@Override
	public String getTitle() {
		return nomSerie;
	}
}
