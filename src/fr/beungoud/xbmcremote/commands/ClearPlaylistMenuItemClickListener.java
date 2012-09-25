/**
 * 
 */
package fr.beungoud.xbmcremote.commands;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;

/**
 * @author Benoit
 *
 */
public class ClearPlaylistMenuItemClickListener implements
		OnMenuItemClickListener {

	XbmcRequester requester;
	/**
	 * Constructeur par défaut
	 */
	public ClearPlaylistMenuItemClickListener( ) {
		this.requester = XbmcRequester.getInstace();
	}
	
	/* (non-Javadoc)
	 * @see android.view.MenuItem.OnMenuItemClickListener#onMenuItemClick(android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		try {
			requester.sendCommand("ClearPlayList()");
			requester.sendCommand("SetPlaylistSong(0)");
		} catch (RequestException e) {

		}
		return true;
	}

}
