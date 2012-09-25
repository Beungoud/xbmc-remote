/**
 * 
 */
package fr.beungoud.xbmcremote.nowplaying;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import fr.beungoud.util.Base64;
import fr.beungoud.xbmcremote.ConnexionErrorActivity;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.VolumeControlingActivity;
import fr.beungoud.xbmcremote.XbmcRequester;

/**
 * @author Benoit
 * 
 */
public class NowPlayingActivity extends VolumeControlingActivity {
	private static final int UPDATE_GUI = 4;
	private static final int UPDATE_VOL = 5;

	private int port = 0;

	private String ipAddress;

	private String login;

	private String password;

	private NowPlayingRefreshThread refreshThread;

	private NowPlayingHandler handler;

	private XbmcRequester requester;

	private int currentVolume = 50;

	public XbmcRequester getRequester() {
		return requester;
	}

	private boolean notplaying;

	public ImageButton boutonPrecedent;

	public ImageButton boutonSuivant;

	public ImageButton boutonPlayPause;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

//		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//			setContentView(R.layout.nowplaying_landscape);
//		} else {
			setContentView(R.layout.nowplaying);
//		}

		SharedPreferences mPrefs = getSharedPreferences((String) getText(R.string.shared_preference_name), MODE_PRIVATE);

		ipAddress = mPrefs.getString((String) getText(R.string.key_ip_address), "0.0.0.0");
		login = mPrefs.getString((String) getText(R.string.key_login), "xbmc");
		password = mPrefs.getString((String) getText(R.string.key_password), "xbmc");
		port = mPrefs.getInt((String) getText(R.string.key_port), 8080);

		boutonPrecedent = (ImageButton) findViewById(R.id.BoutonPrecedent);
		boutonSuivant = (ImageButton) findViewById(R.id.BoutonSuivant);
		boutonPlayPause = (ImageButton) findViewById(R.id.BoutonPlayPause);

		requester = XbmcRequester.getInstace();
		try {
			Bundle response = requester.requestBundle("GetCurrentlyPlaying");
			fillData(response, true);
		} catch (RequestException e) {
			Intent intent = new Intent(this, ConnexionErrorActivity.class);

			startActivity(intent);
			finish();
		}
		handler = new NowPlayingHandler();

		NowPlayinOnClickListener listener = new NowPlayinOnClickListener(this);

		boutonPlayPause.setOnClickListener(listener);
		boutonPrecedent.setOnClickListener(listener);
		boutonSuivant.setOnClickListener(listener);

		SeekBar seek = (SeekBar) findViewById(R.id.CurrentPositionSlider);

		SeekBarChangeListener seekbarListener = new SeekBarChangeListener(this);
//		seek.setProgressDrawable(new BitmapDrawable (R.drawable.seekbar));
		
		seek.setOnSeekBarChangeListener(seekbarListener);
	}

	public void fillData(Bundle currentStatus, boolean eraseUnknown) {
		HashMap<String, Integer> mapping = new HashMap<String, Integer>();
		mapping.put("Title", R.id.now_playing_title);
		mapping.put("Artist", R.id.now_playing_nom_artiste);
		mapping.put("Album", R.id.now_playing_nom_album);
		mapping.put("Time", R.id.now_playing_song_position);
		mapping.put("Duration", R.id.now_playing_song_duration);
		mapping.put("Volume", R.id.now_playing_volume);

		Bundle status = currentStatus;

		if (status.containsKey("Filename") && status.getString("Filename").equals("[Nothing Playing]")) {
			notplaying = true;
		} else {
			notplaying = false;
		}

		for (String key : mapping.keySet()) {
			if (status.containsKey(key)) {
				TextView tv = (TextView) findViewById(mapping.get(key));
				if (status.getString(key) != null)
					tv.setText(status.getString(key));
			} else {
				if (eraseUnknown) {
					TextView tv = (TextView) findViewById(mapping.get(key));
					tv.setText("---");
				}
			}
		}

		if (status.containsKey("PlayStatus")) {
			if (status.getString("PlayStatus").equals("Playing")) {
				boutonPlayPause.setImageResource(R.drawable.img_button_pause_nowplaying);
			} else {
				boutonPlayPause.setImageResource(R.drawable.img_button_play_nowplaying);
			}
		}

		if (status.containsKey("Percentage")) {
			SeekBar seekBar = (SeekBar) findViewById(R.id.CurrentPositionSlider);
			if (seekBar != null) {
				seekBar.setMax(100);
				try {
					seekBar.setProgress(Integer.parseInt(status.getString("Percentage")));
				} catch (NumberFormatException e) {
					// We don't do anything.
				}
			}
		}
	}

	/**
	 * Set the bitmap to render
	 * 
	 * @param bitmap
	 *            the bitmap
	 */
	public void drawBitmap(Bitmap bitmap) {
		ImageView imageView = (ImageView) findViewById(R.id.AlbumArt);
		if (notplaying) {
//			imageView.setImageBitmap(null);
			imageView.setImageResource(R.drawable.dvd);
		} else {
			imageView.setImageBitmap(bitmap);
	//		imageView.setBackgroundDrawable(null);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Log.v("XBMC", "Arret Thread");
		refreshThread.setFinished(true);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshThread = new NowPlayingRefreshThread(handler, requester);
		refreshThread.start();
		// Log.v("XBMC", "Démarrage Thread");
	}

	public class NowPlayingHandler extends Handler {
		Bitmap bitmap = null;

		/**
		 * Set the bitmap to render
		 * 
		 * @param bitmap
		 *            the bitmap
		 */
		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_GUI: {
				Bundle response = msg.getData();
				fillData(response, true);
				if (bitmap != null && bitmap.getHeight() > 10 && bitmap.getWidth() > 10) {
					// Update the bitmap
					drawBitmap(bitmap);
				}
			}
				break;
			case UPDATE_VOL: {
				Bundle response = msg.getData();
				fillData(response, false);

			}
			}

			// TODO Auto-generated method stub
			super.handleMessage(msg);

		}
	}

	public class NowPlayingRefreshThread extends Thread {
		NowPlayingActivity activity;

		private boolean finish = false;

		XbmcRequester requester;

		NowPlayingHandler handler;

		private String lastLoadedThumb = "";

		public void setFinished(boolean fin) {
			finish = fin;
		}

		/**
		 * Constructeur
		 */
		public NowPlayingRefreshThread(NowPlayingHandler handler, XbmcRequester requester) {
			this.handler = handler;
			this.requester = requester;
		}

		public void run() {
			while (!finish) {

				try {
					Bundle response = requester.requestBundle("GetCurrentlyPlaying");

					// Trying to download thumbnail
					if (response.containsKey("Thumb") && !lastLoadedThumb.equals(response.getString("Thumb"))) {
						lastLoadedThumb = (String) response.getString("Thumb");
						String result = requester.request("FileDownload(" + response.getString("Thumb") + ")");

						result = result.substring("<html>".length(), result.length() - "</html>".length()).trim();
						try {
							byte[] thumbnail = Base64.decode(result);
							Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);

							handler.setBitmap(bitmap);
						} catch (IOException e) {

						} catch (Exception e) {

						}

					}
					// Récupération du volume
					List<String> volumeResponse = requester.requestStringList("getVolume");
					if (volumeResponse != null && volumeResponse.size() == 1) {
						response.putString("Volume", volumeResponse.get(0));
						try {
							currentVolume = Integer.parseInt(volumeResponse.get(0));
						} catch (NumberFormatException e) {
							Log.e("XBMC", "Faillure while retreiving volume");
						}
					}

					Message message = new Message();
					message.setData(response);
					message.what = UPDATE_GUI;
					handler.sendMessage(message);
					Log.v("XBMC", "Update GUI");
				} catch (RequestException e1) {

				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
