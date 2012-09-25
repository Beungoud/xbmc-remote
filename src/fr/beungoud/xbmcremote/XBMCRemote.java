package fr.beungoud.xbmcremote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import fr.beungoud.util.ConfigurationAccess;
import fr.beungoud.util.Server;
import fr.beungoud.xbmcremote.browsing.BrowsingActivity;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;
import fr.beungoud.xbmcremote.browsing.database.typed.TypedBrowsingActivity;
import fr.beungoud.xbmcremote.directcommand.DirectCommandActivity;
import fr.beungoud.xbmcremote.nowplaying.NowPlayingActivity;
import fr.beungoud.xbmcremote.streaming.LecteurStreaming;
import fr.beungoud.xbmcremote.streaming.StreamingServiceAccess;

public class XBMCRemote extends VolumeControlingActivity {

	private static final int MENU_SETTINGS = 03;
	private static final int MENU_WHATSNEW = 04;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		// StreamingServiceAccess.getInstace().init(this);

		if (shouldIShowWhatsNew()) {
			showWhatsNew();
		}

		// Initialises the configuration
		ConfigurationAccess.getInstance().initConfiguration(this);
		XbmcRequester.getInstace().initRequester(this);
		// Log.v("XBMC", "Ip address : " + ipAddress);

		// ListView list = (ListView)findViewById(R.id.MainMenuList);
//		ArrayAdapter<String> MainMenuArrayAdapter = new ArrayAdapter<String>(this, R.layout.menuview, R.id.ListText);
//		MainMenuArrayAdapter.add(getString(R.string.main_menu_now_playing));
//		MainMenuArrayAdapter.add(getString(R.string.main_menu_music));
//		MainMenuArrayAdapter.add(getString(R.string.main_menu_videos));
		// list.setAdapter(MainMenuArrayAdapter);
		// list.setTextFilterEnabled(true);
		//
		// MenuOnClickListener clickListener = new MenuOnClickListener(this);
		// list.setOnItemClickListener(clickListener);

		ViewOnClickListener itemClickListener = new ViewOnClickListener(this);

		View nowPlayingImage = findViewById(R.id.now_playing_image);
		nowPlayingImage.setTag("NOW_PLAYING");
		nowPlayingImage.setOnClickListener(itemClickListener);

		View musicImage = findViewById(R.id.music_image);
		musicImage.setTag("MUSIC");
		musicImage.setOnClickListener(itemClickListener);

		View videoImage =  findViewById(R.id.video_image);
		videoImage.setTag("VIDEO");
		videoImage.setOnClickListener(itemClickListener);

		View pictureImage =  findViewById(R.id.image_image);
		pictureImage.setTag("PICTURE");
		pictureImage.setOnClickListener(itemClickListener);

		View configureImage =  findViewById(R.id.configure_image);
		configureImage.setTag("CONFIGURE");
		configureImage.setOnClickListener(itemClickListener);

		View dataBase2 =  findViewById(R.id.database_image);
		dataBase2.setTag("DATABASE2");
		dataBase2.setOnClickListener(itemClickListener);

		View streamImage =  findViewById(R.id.telecommande_image);
		streamImage.setTag("DIRECTCOMMAND");
		streamImage.setOnClickListener(itemClickListener);

	}

	@Override
	protected void onResume() {
		super.onResume();

		Spinner serverSpinner = (Spinner) findViewById(R.id.ServerList);

		Server[] serverList = ConfigurationAccess.getInstance().getServers();
		ArrayAdapter<Server> adapter;
		if (serverList.length > 0) {
			adapter = new ArrayAdapter<Server>(this, R.layout.spiner_text, serverList);
		} else {
			Server s1 = new Server();
			s1.setServerName("No server");
			Server s2 = new Server();
			s2.setServerName("Create one in Configuration");

			adapter = new ArrayAdapter<Server>(this,  R.layout.spiner_text, new Server[] { s1, s2 });
		}

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		serverSpinner.setAdapter(adapter);

		for (Server server : serverList) {
			if (server.isCurrent()) {
				serverSpinner.setSelection(adapter.getPosition(server));
			}
		}

		serverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<? extends Adapter> adapterView, View view, int arg2, long arg3) {
				Spinner spinner = (Spinner) adapterView;
				if (spinner.getSelectedItem() instanceof Server) {
					Server server = (Server) ((spinner.getSelectedItem()));
					server.setCurrent(true);

					ConfigurationAccess.getInstance().updateServer(server);

					// Update the XBMC requester that might be running
					XbmcRequester.getInstace().resetRequester();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	@Override
	/**
	 * Creation des menus
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, R.string.menu_settings);
		menu.add(Menu.NONE, MENU_WHATSNEW, Menu.NONE, R.string.menu_whatsnew);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_SETTINGS:
			Intent intent = new Intent(this, SettingsActivity.class);

			startActivityForResult(intent, Constants.REQ_ID_SETTINGS);
			return true;
		case MENU_WHATSNEW:
			showWhatsNew();
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.REQ_ID_SETTINGS) {
			if (resultCode == RESULT_OK) {
				Bundle parameters = data.getExtras();
				if (parameters != null) {

					// Update the XBMC requester that might be running
					XbmcRequester.getInstace().initRequester(this);
				}
			}
		}

	}

	private class ViewOnClickListener implements View.OnClickListener {
		private Context cont;

		public ViewOnClickListener(Context context) {
			cont = context;
		}

		@Override
		public void onClick(View v) {
			if ("NOW_PLAYING".equals(v.getTag())) {
				Intent intent = new Intent(cont, NowPlayingActivity.class);
				startActivity(intent);
			} else if ("MUSIC".equals(v.getTag())) {
				Intent intent = new Intent(cont, BrowsingActivity.class);
				intent.putExtra((String) getText(R.string.key_browsing_path), "/");
				intent.putExtra((String) getText(R.string.key_browsing_type),
						(String) getText(R.string.browsing_type_file));
				intent.putExtra((String) getText(R.string.key_media_type), MediaType.Music);
				startActivity(intent);
			} else if ("VIDEO".equals(v.getTag())) {
				Intent intent = new Intent(cont, BrowsingActivity.class);
				intent.putExtra((String) getText(R.string.key_browsing_path), "/");
				intent.putExtra((String) getText(R.string.key_browsing_type),
						(String) getText(R.string.browsing_type_file));
				intent.putExtra((String) getText(R.string.key_media_type), MediaType.Video);
				startActivity(intent);
			} else if ("PICTURE".equals(v.getTag())) {
				Intent intent = new Intent(cont, BrowsingActivity.class);
				intent.putExtra((String) getText(R.string.key_browsing_path), "/");
				intent.putExtra((String) getText(R.string.key_browsing_type),
						(String) getText(R.string.browsing_type_file));
				intent.putExtra((String) getText(R.string.key_media_type), MediaType.Pictures);
				startActivity(intent);
			} else if ("CONFIGURE".equals(v.getTag())) {
				Intent intent = new Intent(cont, SettingsActivity.class);

				startActivityForResult(intent, Constants.REQ_ID_SETTINGS);
			} else if ("DATABASE".equals(v.getTag())) {
				Intent intent = new Intent(cont, BrowsingActivity.class);
				intent.putExtra((String) getText(R.string.key_browsing_path), "/");
				intent.putExtra((String) getText(R.string.key_browsing_type),
						(String) getText(R.string.browsing_type_database));
				
				intent.putExtra((String) getText(R.string.key_media_type), MediaType.Video);
				startActivity(intent);
			} else if ("STREAM".equals(v.getTag())) {
				Intent intent = new Intent(cont, LecteurStreaming.class);
				startActivity(intent);
			} else if ("DATABASE2".equals(v.getTag())) {
				Intent intent = new Intent(cont, TypedBrowsingActivity.class);
				intent.putExtra(BrowsingFactory.IntentSourceEnum.TypeBrowsing.toString(),
						BrowsingFactory.TypeBrowsingEnum.MAIN_MENU);
				intent.putExtra((String) getText(R.string.key_media_type), MediaType.Video);
				startActivity(intent);
			} else if ("DIRECTCOMMAND".equals(v.getTag())) {
				Intent intent = new Intent(cont, DirectCommandActivity.class);
				startActivity(intent);

			}
		}

	}

	public static final int WHATSNEW = 0;

	/**
	 * Checks weather it is the first run in a new version.
	 * 
	 * @return
	 */
	public boolean shouldIShowWhatsNew() {
		boolean retour = false;
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo("fr.beungoud.xbmcremote",
					PackageManager.GET_META_DATA);
			Log.d("XBMC", pInfo.versionName);

			SharedPreferences mPrefs = getSharedPreferences((String) getText(R.string.shared_preference_name),
					Context.MODE_PRIVATE);
			String lastVersion = mPrefs.getString((String) getText(R.string.key_version), "0.0");
			
			if (!lastVersion.equals(pInfo.versionName))
			{
				retour = true;
				Editor prefEditor = mPrefs.edit();
				prefEditor.putString((String) getText(R.string.key_version), pInfo.versionName);
				prefEditor.commit();
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return retour;
	}

	public void showWhatsNew() {
		showDialog(WHATSNEW);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case WHATSNEW:
			LayoutInflater factory = LayoutInflater.from(this);
			final View diagView = factory.inflate(R.layout.whatsnewdialog, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("What's New");
			builder.setView(diagView).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			return builder.create();
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case WHATSNEW:

			WebView webView = (WebView) dialog.findViewById(R.id.whatsNew);

			// les définitions de type mime et de l'encodage
			final String mimeType = Messages.getString("help_activity.mime_type"); //$NON-NLS-1$
			final String encoding = Messages.getString("help_activity.encoding"); //$NON-NLS-1$
			WebView objetview;

			// mon code html
			String mapage = Messages.getString("what_s_new.text"); //$NON-NLS-1$

			webView.loadData(mapage, mimeType, encoding);
			break;

		default:
			break;
		}

		super.onPrepareDialog(id, dialog);
	}
}