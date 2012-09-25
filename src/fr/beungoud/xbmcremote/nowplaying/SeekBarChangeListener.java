/**
 * 
 */
package fr.beungoud.xbmcremote.nowplaying;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import fr.beungoud.xbmcremote.RequestException;

/**
 * @author Benoit
 *
 */
public class SeekBarChangeListener implements OnSeekBarChangeListener {
	
	NowPlayingActivity activity;
	
	public SeekBarChangeListener(NowPlayingActivity activity) {
		this.activity = activity;
	}

	/* (non-Javadoc)
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
	 */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
	 */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		try {
			activity.getRequester().sendCommand("SeekPercentage(" + seekBar.getProgress() + ")");
		} catch (RequestException e) {
			
		}

	}

}
