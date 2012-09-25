package fr.beungoud.xbmcremote.browsing.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.BaseAdapter;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingActivity;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;

/**
 * Interface représentant un thread de browsin
 * 
 * @author Benoit
 * 
 */
public abstract class BrowsingThread extends Thread {

	String path;

	int port = 0;

	String ipAddress;

	String login;

	String password;

	XbmcRequester requester;

	MediaType mediaType;

	BrowsingActivity activity;

	public BrowsingThread(String browsePath, BrowsingActivity activity, MediaType mediaType, XbmcRequester requester) {
		this.activity = activity;
		this.mediaType = mediaType;
		this.path = browsePath;
		this.requester = requester;

		// Récupération des informations de configuration
		SharedPreferences sharedPrefs = activity.getSharedPreferences((String) activity
				.getText(R.string.shared_preference_name), Context.MODE_PRIVATE);

		ipAddress = sharedPrefs.getString((String) activity.getText(R.string.key_ip_address), "0.0.0.0");
		login = sharedPrefs.getString((String) activity.getText(R.string.key_login), "xbmc");
		password = sharedPrefs.getString((String) activity.getText(R.string.key_password), "xbmc");
		port = sharedPrefs.getInt((String) activity.getText(R.string.key_port), 8080);

	}

	protected MediaType getMediaType() {
		return mediaType;
	}

	public abstract BaseAdapter getAdapter();

	public abstract int getPlaylistId();
}
