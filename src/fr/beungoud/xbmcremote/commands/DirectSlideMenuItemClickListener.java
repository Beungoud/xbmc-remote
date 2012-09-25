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
public class DirectSlideMenuItemClickListener implements
		OnMenuItemClickListener {

	XbmcRequester requester;
	/**
	 * Constructeur par défaut
	 */
	public DirectSlideMenuItemClickListener( ) {
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
		String folder = (String)intent.getExtras().get("FOLDER");
		

		try {
			
			requester.sendCommand("ClearSlideShow()");

			requester.sendCommand("AddToSlideshow(" + folder + ")");
			requester.sendCommand("SlideshowSelect(" + folder + ")");
			requester.sendCommand("PlaySlideshow()");
		} catch (RequestException e) {
			return false;
		}
		return true;
	}

}
