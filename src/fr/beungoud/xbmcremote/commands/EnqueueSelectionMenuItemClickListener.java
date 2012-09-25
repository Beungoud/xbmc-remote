/**
 * 
 */
package fr.beungoud.xbmcremote.commands;

import android.content.Intent;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;

/**
 * @author Benoit
 *
 */
public class EnqueueSelectionMenuItemClickListener implements
		OnMenuItemClickListener {

	XbmcRequester requester;
	/**
	 * Constructeur par défaut
	 */
	public EnqueueSelectionMenuItemClickListener( ) {
		this.requester = XbmcRequester.getInstace();
	}
	
	/* (non-Javadoc)
	 * @see android.view.MenuItem.OnMenuItemClickListener#onMenuItemClick(android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Intent intent = item.getIntent();
		String folder = (String)intent.getExtras().get("FOLDER");
		Integer mediaType = (Integer)intent.getExtras().get("MEDIA_TYPE");
		
		try {
			requester.sendCommand("AddToPlayList(" + folder + "; " + mediaType + ")");
		} catch (RequestException e) {

		}
		
		return true;
	}

}
