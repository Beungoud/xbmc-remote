package fr.beungoud.xbmcremote.streaming;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import fr.beungoud.xbmcremote.R;

public class CopyOfLecteurStreaming extends Activity {

	private MediaPlayer mediaPlayer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.streaming_layout);
			mediaPlayer = new MediaPlayer();

			String urlfichier = "http://localhost:12345/test.mp3";
			mediaPlayer.setDataSource(urlfichier);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepare();
			final TextView texthaut = (TextView) findViewById(R.id.text);

			// calcul de la durer du morceau
			int iduration = mediaPlayer.getDuration();
			int minute = iduration / 60000;
			int intreste = iduration - minute * 60000;
			int iseconde = intreste / 1000;
			String sMetminutezero = "";
			String sMetminuteseconde = "";

			if (minute < 10)
				sMetminutezero = "0";

			if (iseconde < 10)
				sMetminuteseconde = "0";
			// j'affiche le titre suivi du temp de la chanson
			String stitre = "titre " + sMetminutezero + minute + ":" + sMetminuteseconde + iseconde;
			texthaut.setText(stitre);

			// mediaPlayer.start();

			Button btStart = (Button) findViewById(R.id.start);
			Button btStop = (Button) findViewById(R.id.stop);
			Button btPause = (Button) findViewById(R.id.pause);

			btStart.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					try {
						mediaPlayer.start();
					} catch (Exception e) {
						texthaut.setText("erreur " + e.getMessage());
					}

				}
			});

			btStop.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					try {
						mediaPlayer.stop();
						mediaPlayer.prepare();
					} catch (Exception e) {
						texthaut.setText("erreur " + e.getMessage());
					}

				}
			});

			btPause.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					try {
						mediaPlayer.pause();
					} catch (Exception e) {
						texthaut.setText("erreur " + e.getMessage());
					}

				}
			});

		} catch (Exception ex) {
			final TextView texthaut = (TextView) findViewById(R.id.text);
			texthaut.setText(ex.getMessage());
		}
	}
}
