/**
 * 
 */
package fr.beungoud.xbmcremote;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;

/**
 * @author Benoit
 *
 */
public class VolumeControlingActivity extends Activity {
	/**
	 * Prise en charge des bouttons de volume;
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("XBMC", "KeyCode = " + keyCode);
		boolean volumeUpdated = false;

		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			sendActionToXBMC(88);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			sendActionToXBMC(89);
			return true;
		}
		return super.onKeyDown(keyCode, event);
		}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	public void sendActionToXBMC(Object value)
	{
		try {
			XbmcRequester.getInstace().sendCommand("Action(" + value + ")");
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
