package fr.beungoud.xbmcremote.browsing.database.typed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.BaseAdapter;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingActivity;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;

/**
 * Interface représentant un thread de browsin
 * 
 * @author Benoit
 * 
 */
public abstract class BrowsingSource {

	Intent intent;

	int port = 0;

	String ipAddress;

	String login;

	String password;

	XbmcRequester requester;

	Activity activity;

	public BrowsingSource(Intent intent, Activity activity, XbmcRequester requester) {
		this.activity = activity;
		this.intent = intent;
		this.requester = requester;

		// Récupération des informations de configuration
		SharedPreferences sharedPrefs = activity.getSharedPreferences((String) activity
				.getText(R.string.shared_preference_name), Context.MODE_PRIVATE);

		ipAddress = sharedPrefs.getString((String) activity.getText(R.string.key_ip_address), "0.0.0.0");
		login = sharedPrefs.getString((String) activity.getText(R.string.key_login), "xbmc");
		password = sharedPrefs.getString((String) activity.getText(R.string.key_password), "xbmc");
		port = sharedPrefs.getInt((String) activity.getText(R.string.key_port), 8080);

	}

	public abstract BaseAdapter getAdapter();
	
	public abstract void getData() throws RequestException;

	protected Activity getActivity() {
		return activity;
	}

	protected void setActivity(Activity activity) {
		this.activity = activity;
	}


	public String getTitle() {
		return "Set Title?";
	}

	public XbmcRequester getRequester() {
		return requester;
	}
}
