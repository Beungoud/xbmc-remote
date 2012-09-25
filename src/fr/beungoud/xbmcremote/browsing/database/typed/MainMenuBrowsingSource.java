package fr.beungoud.xbmcremote.browsing.database.typed;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.widget.BaseAdapter;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory;
import fr.beungoud.xbmcremote.browsing.DbBrowseItem;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;

public class MainMenuBrowsingSource extends BrowsingSource {

	private TypedBrowsingListAdapter adapter;

	public MainMenuBrowsingSource(Intent intent, Activity activity, XbmcRequester requester) {
		super(intent, activity, requester);
		
		adapter = new TypedBrowsingListAdapter();
	}

	@Override
	public BaseAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void getData() throws RequestException {
		List<AbstractDbBrowseItem> list = new ArrayList<AbstractDbBrowseItem>();
		// Ajout de l'item Musique
		MainMenuDbBrowseItem item1 = new MainMenuDbBrowseItem("Music");
		item1.setFolder(true);
		item1.setMediaType(MediaType.Music);
		item1.setName("Music");
		Intent intent = new Intent();
		intent.setClass(getActivity(), TypedBrowsingActivity.class);
		intent.putExtra(BrowsingFactory.IntentSourceEnum.TypeBrowsing.toString(), 
				BrowsingFactory.TypeBrowsingEnum.ARTIST);
		intent.putExtra((String) activity.getText(R.string.key_media_type), MediaType.Music);
		item1.setIntentAction(intent);
		list.add(item1);

		// Ajout de l'Item TV show
		item1 = new MainMenuDbBrowseItem("TV Shows");
		item1.setFolder(true);
		item1.setMediaType(MediaType.Video);
		item1.setName("TV Shows");
		intent = new Intent();
		intent.setClass(getActivity(), TypedBrowsingActivity.class);
		intent.putExtra(BrowsingFactory.IntentSourceEnum.TypeBrowsing.toString(), 
				BrowsingFactory.TypeBrowsingEnum.SERIE_TITLE);
		intent.putExtra((String) activity.getText(R.string.key_media_type), MediaType.Video);
		item1.setIntentAction(intent);
		list.add(item1);


		// Ajout de l'item Movies
		item1 = new MainMenuDbBrowseItem("Movies");
		item1.setFolder(true);
		item1.setMediaType(MediaType.Video);
		item1.setName("Movies");
		intent = new Intent();
		intent.setClass(getActivity(), TypedBrowsingActivity.class);
		intent.putExtra(BrowsingFactory.IntentSourceEnum.TypeBrowsing.toString(), 
				BrowsingFactory.TypeBrowsingEnum.MOVIES);
		intent.putExtra((String) activity.getText(R.string.key_media_type), MediaType.Video);
		item1.setIntentAction(intent);
		list.add(item1);

		adapter.setList(list);

	}

	@Override
	public String getTitle() {
		
		return "Database:";
	}

	
}
