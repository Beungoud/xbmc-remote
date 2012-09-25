package fr.beungoud.xbmcremote.streaming;

import java.io.IOException;
import java.util.List;

import android.R.string;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.streaming.service.IXBMCMusicStreamer;
import fr.beungoud.xbmcremote.streaming.service.Song;

public class LecteurStreaming extends Activity {

	private MediaPlayer mediaPlayer;

	IXBMCMusicStreamer musicStreamer = null;
	
	PlaylistAdapter adapter = new PlaylistAdapter();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.streaming_layout);


		Button btStart = (Button) findViewById(R.id.start);
		Button btStop = (Button) findViewById(R.id.stop);
		Button btPause = (Button) findViewById(R.id.pause);
		
		ListView listView = (ListView) findViewById(R.id.playlist);
		listView.setAdapter(adapter);
		
		btStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IXBMCMusicStreamer service = StreamingServiceAccess.getInstace().getMusicStreamerService();
				try {
					service.stop();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});
		
		btStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IXBMCMusicStreamer service = StreamingServiceAccess.getInstace().getMusicStreamerService();
				try {
					service.playFile(0);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			
			}
		});
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		refreshPlaylist();
	}
	
	/**
	 * Methode de rafraichissement de la playlist
	 */
	protected void refreshPlaylist()
	{
		List<Song> playList;
		IXBMCMusicStreamer service = StreamingServiceAccess.getInstace().getMusicStreamerService();
		try {
			playList = service.getPlaylist();
			adapter.setPlaylist(playList);
			
			int currentSong = service.getCurrentSong();
			adapter.setNowPlaying(currentSong);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		
	}

	@Override
	protected void onStart() {
		super.onStart();

		
	}

	@Override
	protected void onStop() {
		super.onStop();
	}


}