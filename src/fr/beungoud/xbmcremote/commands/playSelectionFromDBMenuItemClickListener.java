/**
 * 
 */
package fr.beungoud.xbmcremote.commands;

import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;

/**
 * @author Benoit
 *
 */
public class playSelectionFromDBMenuItemClickListener implements
		OnMenuItemClickListener {

	XbmcRequester requester;
	/**
	 * Constructeur par défaut
	 */
	public playSelectionFromDBMenuItemClickListener( ) {
		this.requester = XbmcRequester.getInstace();
	}
	
	/* (non-Javadoc)
	 * @see android.view.MenuItem.OnMenuItemClickListener#onMenuItemClick(android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Intent intent = item.getIntent();
		exec (intent);
		return true;
	}
	
	public boolean exec(Intent intent)
	{
		String type = (String)intent.getExtras().get("TYPE");
		String statement = (String) intent.getExtras().get("STATEMENT");
		Integer mediaType = (Integer)intent.getExtras().get("MEDIA_TYPE");

		try {
			requester.sendCommand("Stop()");
			requester.sendCommand("ClearPlayList(" + mediaType +")");
			requester.sendCommand("SetCurrentPlaylist(" + mediaType + ")");
			
			requester.sendCommand("AddToPlayListFromDB(" + type + "; " + statement + ")");
//			
//			List<String> stringList = requester.requestStringList("GetPlaylistContents(" + mediaType + ")");
//			for (String string : stringList) {
//				Log.i("StringL", string);
//			}
//			if (stringList.size() >0)
//			{
//				String fichier = stringList.get(0);
//			}
			requester.sendCommand("SetPlaylistSong(0)");

//			requester.sendCommand("PlayNext");
		} catch (RequestException e) {
			return false;
		}
		return true;
	}

}
