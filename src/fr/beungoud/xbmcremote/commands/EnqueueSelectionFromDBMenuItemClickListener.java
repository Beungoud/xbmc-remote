/**
 * 
 */
package fr.beungoud.xbmcremote.commands;

import java.util.List;

import android.content.Intent;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;

/**
 * @author Benoit
 *
 */
public class EnqueueSelectionFromDBMenuItemClickListener implements
		OnMenuItemClickListener {

	XbmcRequester requester;
	/**
	 * Constructeur par défaut
	 */
	public EnqueueSelectionFromDBMenuItemClickListener( ) {
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
			requester.sendCommand("AddToPlayListFromDB(" + type + "; " + statement + ")");
			if (type.equals("episodes"))
			{
				
			}
			
			List<String> stringList = requester.requestStringList("GetPlaylistContents(" + mediaType + ")");
			if (stringList.size() >0)
			{
				String fichier = stringList.get(0);
				requester.sendCommand("SetCurrentPlaylist(" + mediaType + ")");
				requester.sendCommand("PlayNext");
			}
		} catch (RequestException e) {
			return false;
		}
		return true;
	}

}
