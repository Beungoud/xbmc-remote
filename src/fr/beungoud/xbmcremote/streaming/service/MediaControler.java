/**
 * 
 */
package fr.beungoud.xbmcremote.streaming.service;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;

/**
 * @author Benoit
 *
 */
public class MediaControler {
	StreamingService service = null;
	
	
	public MediaControler(StreamingService serviceSource)
	{
		service = serviceSource;
	}
	
	public void startPlaying()
	{
		service.getServiceHandler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				try {
					MediaPlayer mediaPlayer = service.getMediaplayer();
					String songPath = service.getPlayList().get(service.getCurrentSong()).getPath();
					songPath = java.net.URLEncoder.encode( MusicStreamerThread.URIHeader + songPath);
					
					String urlfichier = "http://localhost:12345/" + songPath ;
					
					mediaPlayer.setOnErrorListener(new OnErrorListener() {
						
						@Override
						public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
							Log.i("XBMC", "Erreur Media: " + arg1 + " - " + arg2);
							return false;
						}
					});
					
					mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer mp) {
							service.nextSong();
							service.play();
						}
					});
					
					mediaPlayer.reset();
					
					mediaPlayer.setDataSource(urlfichier);
					
					
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
		}, 500);
	}
}
