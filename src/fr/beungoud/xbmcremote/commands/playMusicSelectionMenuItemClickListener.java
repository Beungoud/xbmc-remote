/**
 * 
 */
package fr.beungoud.xbmcremote.commands;

import java.util.List;

import android.content.Intent;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import fr.beungoud.xbmcremote.Constants;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;

/**
 * @author Benoit
 * 
 */
public class playMusicSelectionMenuItemClickListener implements OnMenuItemClickListener {

	XbmcRequester requester;

	/**
	 * Constructeur par défaut
	 */
	public playMusicSelectionMenuItemClickListener() {
		this.requester = XbmcRequester.getInstace();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.MenuItem.OnMenuItemClickListener#onMenuItemClick(android
	 * .view.MenuItem)
	 */
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Intent intent = item.getIntent();
		exec(intent);
		return true;
	}

	public enum execModeEnum {
		Play, Enqueue
	}

	public boolean exec(Intent intent) {
		String whereClause = (String) intent.getExtras().get("WHERECLAUSE");
		execModeEnum execMode = (execModeEnum) intent.getExtras().get("EXEC_MODE");
		Integer mediaType = Constants.ID_PLAYLIST_MUSIC;

		try {
			if (execMode == execModeEnum.Play) {
				requester.sendCommand("ClearPlayList(" + mediaType + ")");
				requester.sendCommand("SetCurrentPlaylist(" + mediaType + ")");
			}

			String Query = "select strPath, StrFileName from songview where " + whereClause;

			List<String[]> results = requester.requestQuery(MediaType.Music, Query, 2);

			
			
			for (String[] result : results) {
				requester.sendCommand("AddToPlayList(" + result[0] + result[1] + "; " + mediaType + ")");

			}

			if (execMode == execModeEnum.Play) {
				List<String> stringList = requester.requestStringList("GetPlaylistContents(" + mediaType + ")");
				if (stringList.size() > 0) {

					requester.sendCommand("SetPlaylistSong(0)");

				}
			}
		} catch (RequestException e) {
			return false;
		}
		return true;
	}

}
