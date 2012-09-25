package fr.beungoud.xbmcremote.streaming.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.XBMCRemote;

public class StreamingService extends Service {
	private static final String LOG_TAG = "XBMC_SERV";

	private List<Song> playList;
	private int currentSong;

	MusicStreamerServerSocket serverThread = null;;

	private Handler serviceHandler = null;

	private MediaControler mediaControl;

	private MediaPlayer mediaplayer = new MediaPlayer();

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d(LOG_TAG, "onStart");
		setServiceHandler(new Handler());

		mediaControl = new MediaControler(this);

		playList = new ArrayList<Song>();

		if (serverThread == null) {
			serverThread = new MusicStreamerServerSocket();
		}
		if (!serverThread.isServiceRunning()) {
			serverThread.setServiceRunning(true);
			Thread th = new Thread(serverThread);
			th.start();
		}

		showNotification(true);

	}

	@Override
	public IBinder onBind(Intent intent) {
		return new XBMCMusicStreamer(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "onDestroy");
		showNotification(false);
		super.onDestroy();

	}

	/**
	 * Permet de définir la liste de lecture du player
	 * 
	 * @param playlist
	 *            liste de lecture à définir. Path vers chacun des fichiers MP3.
	 */
	public void setPlayList(List<String> playlist) {
		Toast.makeText(this, "Changement de la playlist", 2);
		currentSong = 0;

		synchronized (playList) {
			playList.clear();
			for (String path : playlist) {
				playList.add(new Song(path));
			}
		}
	}

	/**
	 * Recupere la lsite des éléments de la playList par leur chemins d'accès
	 * 
	 * @return La liste
	 */
	public List<Song> getPlayList() {
		ArrayList<Song> retour = new ArrayList<Song>();
		synchronized (playList) {
			for (Song song : playList) {
				retour.add(song);
			}
		}
		return retour;
	}

	/**
	 * Vide totalement la playListe
	 */
	public void clearPlayList() {
		currentSong = 0;
		synchronized (playList) {
			playList.clear();
		}
	}

	public void play() {
		mediaControl.startPlaying();
	}

	/**
	 * Stop the service
	 */
	public void stop() {
		showNotification(false);
		serverThread.setServiceRunning(false);
		stopSelf();
	}

	/**
	 * Affichage ou masquage de la notification dans la barre
	 * 
	 * @param on
	 */
	protected void showNotification(Boolean on) {
		NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		if (on) {
			Notification n = new Notification(R.drawable.service_image, "Service streaming XBMC", System
					.currentTimeMillis());

			Intent intent = new Intent(this, XBMCRemote.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
					Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			n.setLatestEventInfo(this, "XBMC Streaming", "Streaming music from XBMC", pendingIntent);
			nm.notify(22, n);
		} else {
			nm.cancel(22);
		}
	}

	/**
	 * Retourne le numéro de la chanson en cours.
	 * 
	 * @return
	 */
	public int getCurrentSong() {
		return currentSong;
	}

	public void setCurrentSong(int song) {
		currentSong = song;
	}

	public void nextSong() {
		currentSong = (currentSong + 1) % playList.size();
	}

	/**
	 * @param serviceHandler
	 *            the serviceHandler to set
	 */
	public void setServiceHandler(Handler serviceHandler) {
		this.serviceHandler = serviceHandler;
	}

	/**
	 * @return the serviceHandler
	 */
	public Handler getServiceHandler() {
		return serviceHandler;
	}

	/**
	 * @param mediaplayer
	 *            the mediaplayer to set
	 */
	public void setMediaplayer(MediaPlayer mediaplayer) {
		this.mediaplayer = mediaplayer;
	}

	/**
	 * @return the mediaplayer
	 */
	public MediaPlayer getMediaplayer() {
		return mediaplayer;
	}
}