package fr.beungoud.xbmcremote.browsing.database;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.beungoud.util.ConfigurationAccess;
import fr.beungoud.xbmcremote.ConnexionErrorActivity;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingActivity;
import fr.beungoud.xbmcremote.browsing.DbBrowseItem;
import fr.beungoud.xbmcremote.browsing.DbBrowsingListAdapter;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;

public class DatabaseBrowsingThread extends BrowsingThread {
	DbBrowsingListAdapter adapter;

	private static final String TV_SHOW_PATH = "TV Shows";
	private static final String MOVIE_PATH = "Movies";
	private static final String MUSIC_PATH = "Music";

	private static HashMap<String, List<QueryDesc>> listQueries = new HashMap<String, List<QueryDesc>>();


	// Construciton de la hashMap avec les informations pour tous les query
	static {
		/************************************
		 ** TV Shows Queries ****************
		 ************************************/
		ArrayList<QueryDesc> listTVShow = new ArrayList<QueryDesc>();
		QueryDesc qd;
		qd=new QueryDesc(
				"select C00,totalcount,watchedcount,idShow from Tvshowview order by C00", 4, 
				"%1$s", "%3$s seen, %2$s total ");
		qd.setAddFromDb("episodes", "idShow=%4$s");
		listTVShow.add(qd);
		
		qd = new QueryDesc("select DISTINCT  C12,idShow from episodeview where strTitle='%1$s' order by C12", 2,
				"Season %1$s", "");
		qd.setSubPath("%1$s");
		qd.setAddFromDb("episodes", "C12=%1$s and idShow=%2$s");
		listTVShow.add(qd);
		
		qd=new QueryDesc("select C00,c12,c13,coalesce(playcount, '0'),idEpisode from episodeview where strTitle='%1$s' and c12='%2$s' order by c13", 5,
				"%1$s", "s%2$s e%3$s seen %4$s time(s)");
		qd.setAddFromDb("episodes", "idEpisode=%5$s ");
		listTVShow.add(qd);
		
		
		listQueries.put(TV_SHOW_PATH, listTVShow);

		/************************************
		 ** Movies Queries ******************
		 ************************************/
		ArrayList<QueryDesc> listMovies = new ArrayList<QueryDesc>();
		
		qd=new QueryDesc("select C00,coalesce(playcount, '0'),idMovie from Movieview order by C00", 3,
				"%1$s", "seen %2$s time(s)");
		qd.setAddFromDb("movies", "idMovie=%3$s");
		listMovies.add(qd);
		
		listQueries.put(MOVIE_PATH, listMovies);

		/************************************
		 ** Music Queries *******************
		 ************************************/
		ArrayList<QueryDesc> listMusic = new ArrayList<QueryDesc>();
		
		qd=new QueryDesc("select strartist,idArtist from artist order by strartist", 2,
				"%1$s", "");
		qd.setAddFromDb("songs", "idArtist=%2$s");
		listMusic.add(qd);
		
		qd = new QueryDesc("select strAlbum,strGenre,idAlbum from albumview where strartist='%1$s' order by strAlbum", 3,
				"%1$s", "%2$s");
		qd.setSubPath("%3$s");
		qd.setAddFromDb("songs", "idAlbum=%3$s");
	
		listMusic.add(qd);
		
		qd=new QueryDesc("select strTitle,idSong from songview where idAlbum='%2$s'", 2,
				"%1$s", "");
		qd.setAddFromDb("songs", "idSong=%2$s order by idAlbum,iTrack");
		listMusic.add(qd);
		
		listQueries.put(MUSIC_PATH, listMusic);
		
		

	}

	public DatabaseBrowsingThread(String browsePath, BrowsingActivity activity, MediaType mediaType,
			XbmcRequester requester) {
		super(browsePath, activity, mediaType, requester);
		adapter = new DbBrowsingListAdapter();
	}

	public void run() {
		String[] items = path.split("/");
		try {
			List<DbBrowseItem> list = null;

			if (items.length == 0 || items[0].length() == 0) {
				list = buildRootList();
			} else {
				list = buildList(items);
			}
			adapter.setList(list);
		} catch (RequestException e) {

//				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//				builder.setCancelable(true);
//				builder.setTitle("Error");
//				builder.setInverseBackgroundForced(false);
//
//
//				builder.setView(LayoutInflater.from(activity).inflate(R.layout.connexion_error, null));
//				AlertDialog alert = builder.create();
//				alert.show();
			
			Intent intent = new Intent(activity, ConnexionErrorActivity.class);

			activity.startActivity(intent);

			activity.finish();
		}

		 activity.getHandler().sendEmptyMessage(BrowsingActivity.INVALIDATE_LIST);
	}

	private List<DbBrowseItem> buildList(String[] path) throws RequestException {
		List<DbBrowseItem> list = new ArrayList<DbBrowseItem>();

		Object[] parameters = new Object[path.length - 1];

		for (int i = 0; i < path.length - 1; i++) {
			parameters[i] = path[i + 1];
		}

		Formatter formatter = new Formatter();
		QueryDesc queryDesc =  listQueries.get(path[0]).get(path.length - 1);
		String Query = formatter.format(queryDesc.getQuery(), parameters).toString();
		Log.i("XBMC", Query);
		List<String[]> result = requester.requestQuery(getMediaType(), Query, queryDesc.getReturnCount());
		for (String[] strings : result) {
			DbBrowseItem item = new DbBrowseItem();
			
			formatter = new Formatter();
			String title = (formatter.format(queryDesc.title, strings).toString());
			item.setName(title);
			formatter = new Formatter();
			item.setComment(formatter.format(queryDesc.subTitle, strings).toString());
			item.setMediaType(mediaType);
			if (listQueries.get(path[0]).size() == path.length)
			{
				item.setFolder(false);
			}
			else
			{
				item.setFolder(true);
			}
			
			String pathString="" ;
			
			for (String string : path) {
				pathString = pathString + string + "/";
			}
			formatter = new Formatter();
			pathString = pathString + formatter.format(queryDesc.subPath, strings).toString();
			
			item.setPath(pathString);

			item.setType(queryDesc.getAddFromDbMedia());
			formatter = new Formatter();
			item.setStatement(formatter.format(queryDesc.getAddFromDbQuery(), strings).toString());
			
			list.add(item);
		}
		
		return list;
	}

	private List<DbBrowseItem> buildRootList() {
		List<DbBrowseItem> list = new ArrayList<DbBrowseItem>();
		// Ajout de l'item Musique
		DbBrowseItem item1 = new DbBrowseItem();
		item1.setFolder(true);
		item1.setMediaType(MediaType.Music);
		item1.setName("Music");
		item1.setPath(MUSIC_PATH + "/");
		list.add(item1);

		// Ajout de l'Item TV show
		DbBrowseItem item2 = new DbBrowseItem();
		item2.setFolder(true);
		item2.setMediaType(MediaType.Video);
		item2.setName("TV Shows");
		item2.setPath(TV_SHOW_PATH + "/");
		list.add(item2);

		// Ajout de l'item Movies
		DbBrowseItem item3 = new DbBrowseItem();
		item3.setFolder(true);
		item3.setMediaType(MediaType.Video);
		item3.setName("Movies");
		item3.setPath(MOVIE_PATH + "/");
		list.add(item3);

		return list;
	}

	@Override
	public BaseAdapter getAdapter() {
		return adapter;
	}

	@Override
	public int getPlaylistId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
