/**
 * 
 */
package fr.beungoud.xbmcremote.browsing.database.typed;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import fr.beungoud.xbmcremote.ConnexionErrorActivity;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.VolumeControlingActivity;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowseItem;
import fr.beungoud.xbmcremote.browsing.BrowseItemView;
import fr.beungoud.xbmcremote.browsing.BrowsingActivity;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;
import fr.beungoud.xbmcremote.browsing.database.typed.view.ImageRefresher;
import fr.beungoud.xbmcremote.directcommand.DirectCommandActivity;
import fr.beungoud.xbmcremote.nowplaying.NowPlayingActivity;

/**
 * @author Benoit
 * 
 */
public class TypedBrowsingActivity extends VolumeControlingActivity {

	public static final int INVALIDATE_LIST = 4;

	private static final int MENU_NOWPLAYING = 0;

	private static final int MENU_REMOTE = 1;
//	String browsingPath = null;
	MediaType mediaType = null;
	// int playListId = 0;

	private ListView liste;

	BaseAdapter adapter;

	Handler browsingHandler = null;

	private XbmcRequester requester;

	private String browsingType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.browsing);

		// L'intent source
		Intent sourceIntent = getIntent();
		// On en extrait le bundle
		Bundle bundle = sourceIntent.getExtras();

//		browsingPath = bundle.getString((String) getText(R.string.key_browsing_path));
		mediaType = (MediaType) bundle.getSerializable((String) getText(R.string.key_media_type));

		browsingType = bundle.getString((String) getText(R.string.key_browsing_type));

		// On met a jour l'image media type
		ImageView mediaTypeImage = (ImageView) findViewById(R.id.MediaTypeImage);

		if (mediaType== MediaType.Music) {
			mediaTypeImage.setImageResource(R.drawable.icon_60x60_filetype_music);
		} else if (mediaType== MediaType.Pictures) {
			mediaTypeImage.setImageResource(R.drawable.icon_60x60_filetype_images);
		} else if (mediaType==MediaType.Video) {
			mediaTypeImage.setImageResource(R.drawable.icon_60x60_filetype_video);
		}
		
//		browsingPath.replace('\\', '/');

//		String[] tabString = browsingPath.split("/");
//
//		if (tabString.length > 0) {
//			textPath.setText(tabString[tabString.length - 1]);
//		} else {
//			textPath.setText(browsingPath);
//		}
		// Fin construction du titre

		liste = (ListView) findViewById(R.id.BrowsingList);

		requester = XbmcRequester.getInstace();

		final BrowsingSource browseSource = BrowsingFactory.getBrowsingSource(sourceIntent, this, requester);

		// / Construction du titre
		TextView textPath = (TextView) findViewById(R.id.BrowsingTitle);
		textPath.setText(browseSource.getTitle());

		liste.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				BrowseItemView browseItemView = (BrowseItemView) arg1;

				BrowseItem browseItem = browseItemView.getSourceItem();

				browseItem.execCommand(requester, arg1.getContext());

			}
		});

		liste.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				AdapterView.AdapterContextMenuInfo info;
				try {
					info = (AdapterView.AdapterContextMenuInfo) menuInfo;
				} catch (ClassCastException e) {
					// Log.e("XBMC", "bad menuInfo", e);
					return;
				}

				BrowseItemView browseItem = (BrowseItemView) info.targetView;

				browseItem.getSourceItem().buildContextMenu(menu, requester);

			}
		});

		browsingHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				if (msg.what == INVALIDATE_LIST) {
					adapter.notifyDataSetChanged();
				}
			}
		};

		updateThread th = new updateThread(this, browseSource);
		th.start();

		liste.setAdapter(browseSource.getAdapter());
		adapter = browseSource.getAdapter();

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		return super.onContextItemSelected(item);
	}

	public Handler getHandler() {
		return browsingHandler;
	}

	@Override
	protected void onPause() {
		ImageRefresher.getInstance().stop();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		ImageRefresher.getInstance().start();
		super.onResume();
	}
	

	@Override
	/**
	 * Creation des menus
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_NOWPLAYING, Menu.NONE, R.string.menu_nowplaying).setIcon(R.drawable.nowplaying_mini);
		menu.add(Menu.NONE, MENU_REMOTE, Menu.NONE, R.string.menu_remote).setIcon(R.drawable.telecommande_mini);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		Intent intent;
		switch (item.getItemId()) {
		case MENU_NOWPLAYING:
			intent = new Intent(this, NowPlayingActivity.class);
			startActivity(intent);
			return true;
		case MENU_REMOTE:
			intent = new Intent(this, DirectCommandActivity.class);
			startActivity(intent);
			return true;
		}
		return false;
	}
	
	private class updateThread extends Thread {
		private BrowsingSource browseSource;
		private TypedBrowsingActivity activity;

		public updateThread(TypedBrowsingActivity activity, BrowsingSource browseSource) {
			this.browseSource = browseSource;
			this.activity = activity;
		}

		public void run() {
			try {
				browseSource.getData();
			} catch (RequestException e) {

				Intent intent = new Intent(activity, ConnexionErrorActivity.class);

				activity.startActivity(intent);

				activity.finish();
			}

			activity.getHandler().sendEmptyMessage(BrowsingActivity.INVALIDATE_LIST);

		}
	};
}
