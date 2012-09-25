/**
 * 
 */
package fr.beungoud.xbmcremote.nowplaying;

import android.view.View;
import android.view.View.OnClickListener;
import fr.beungoud.xbmcremote.RequestException;

/**
 * @author Benoit
 *
 */
public class NowPlayinOnClickListener implements OnClickListener {
	
	NowPlayingActivity activity;
	
	public NowPlayinOnClickListener(NowPlayingActivity activity)
	{
		this.activity = activity;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		try {
			if (activity.boutonPlayPause.equals(v))
			{
				activity.getRequester().sendCommand("Pause");
			}
			if (activity.boutonPrecedent.equals(v))
			{
				activity.getRequester().sendCommand("PlayPrev");
				
			}
			if (activity.boutonSuivant.equals(v))
			{
				activity.getRequester().sendCommand("PlayNext");
			}
		} catch (RequestException e) {


		}
	}

}
