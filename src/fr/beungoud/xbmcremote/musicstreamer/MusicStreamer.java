package fr.beungoud.xbmcremote.musicstreamer;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;

public class MusicStreamer extends Thread  implements MusicStreamWaiterInterface {
	String url = null;
	
	/**
	 * Running, waiting for data to be available.
	 */
	public void run()
	{
		while (this.url == null)
		{
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Log.i("XBMC", "Music Ready");
		try {
			MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(url);
			mediaPlayer.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					Log.e("XBMC", "Erreur MP : " + what + " : " + extra);
					return false;
				}
			});
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void musicReady(String url) {
		this.url = url;
	}

}
