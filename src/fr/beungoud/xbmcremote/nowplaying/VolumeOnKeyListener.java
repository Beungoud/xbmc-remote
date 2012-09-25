/**
 * 
 */
package fr.beungoud.xbmcremote.nowplaying;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

/**
 * @author Benoit
 *
 */
public class VolumeOnKeyListener implements OnKeyListener {

	/* (non-Javadoc)
	 * @see android.view.View.OnKeyListener#onKey(android.view.View, int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		Log.i("XBMC", "KeyCode = " + keyCode);
		return true;
	}

}
