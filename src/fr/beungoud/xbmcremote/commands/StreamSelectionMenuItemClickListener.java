/**
 * 
 */
package fr.beungoud.xbmcremote.commands;

import java.util.List;

import android.content.Intent;
import android.os.RemoteException;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.streaming.StreamingServiceAccess;

/**
 * @author Benoit
 * 
 */
public class StreamSelectionMenuItemClickListener implements OnMenuItemClickListener {

	XbmcRequester requester;

	/**
	 * Constructeur par défaut
	 */
	public StreamSelectionMenuItemClickListener() {
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
		String folder = (String) intent.getExtras().get("FOLDER");
		Integer mediaType = (Integer) intent.getExtras().get("MEDIA_TYPE");

		try {
			// requester.sendCommand("AddToPlayList(" + folder + "; " +
			// mediaType + ")");

			List<String> stringList = requester.requestStringList("GetPlaylistContents(" + mediaType + ")");
			
			StreamingServiceAccess.getInstace().getMusicStreamerService().setPlaylist(stringList);

//			MusicStreamerThread streamerThread = new MusicStreamerThread(stringList);
//			MusicStreamer streamer = new MusicStreamer();
//			streamerThread.setWaiter(streamer);
//			streamerThread.start();
//			streamer.start();
		} catch (RequestException e) {

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
	
}
