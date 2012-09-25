/**
 * 
 */
package fr.beungoud.xbmcremote.directcommand;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.VolumeControlingActivity;
import fr.beungoud.xbmcremote.XbmcRequester;

/**
 * @author Benoit
 * 
 */
public class DirectCommandActivity extends VolumeControlingActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.direct_command2);

		ButtonsClickListener listenerAction = new ButtonsClickListener(ListenerType.ACTION);
		ButtonsClickListener listenerKey = new ButtonsClickListener(ListenerType.COMMAND);
		ButtonsClickListener listenerRequest = new ButtonsClickListener(ListenerType.REQUEST);

		ImageButton boutonOK = (ImageButton) findViewById(R.id.BoutonOk);
		boutonOK.setTag(0xF000 + 0x0D);
		boutonOK.setOnClickListener(listenerKey);

		ImageButton boutonHaut = (ImageButton) findViewById(R.id.BoutonHaut);
		boutonHaut.setTag(0xF000 + 0x26);
		boutonHaut.setOnClickListener(listenerKey);

		ImageButton boutonBas = (ImageButton) findViewById(R.id.BoutonBas);
		boutonBas.setTag(0xF000 + 0x28);
		boutonBas.setOnClickListener(listenerKey);

		ImageButton boutonGauche = (ImageButton) findViewById(R.id.BoutonGauche);
		boutonGauche.setTag(0xF000 + 0x25);
		boutonGauche.setOnClickListener(listenerKey);

		ImageButton boutonDroite = (ImageButton) findViewById(R.id.BoutonDroite);
		boutonDroite.setTag(0xF000 + 0x27);
		boutonDroite.setOnClickListener(listenerKey);

		ImageButton boutonPrecedent = (ImageButton) findViewById(R.id.BoutonBack);
		boutonPrecedent.setTag(0xF000 + 0x08);
		boutonPrecedent.setOnClickListener(listenerKey);

		ImageButton boutonMenu = (ImageButton) findViewById(R.id.BoutonMenu);
		boutonMenu.setTag(0xF000 + 0x1B);
		boutonMenu.setOnClickListener(listenerKey);

		ImageButton boutonInfo = (ImageButton) findViewById(R.id.BoutonInfo);
		boutonInfo.setTag(0xF000 + 0x49);
		boutonInfo.setOnClickListener(listenerKey);

		ImageButton boutonContexte = (ImageButton) findViewById(R.id.BoutonTitle);
		boutonContexte.setTag(0xF000 + 0x5D);
		boutonContexte.setOnClickListener(listenerKey);

		ImageButton bouton = (ImageButton) findViewById(R.id.ButtonDisplay);
		 bouton.setTag(0xF000+ 0x09);
		 bouton.setOnClickListener(listenerKey);

		bouton = (ImageButton) findViewById(R.id.ButtonPlay);
//		bouton.setTag(0xF000 + 0xB3);
//		bouton.setTag(0xF000 + 0x50);
//		bouton.setOnClickListener(listenerKey);
		bouton.setTag(79);
		bouton.setOnClickListener(listenerAction);

		bouton = (ImageButton) findViewById(R.id.ButtonPause);
//		bouton.setTag(0xF000 + 0xB3);
//		bouton.setOnClickListener(listenerKey);
		bouton.setTag(12);
		bouton.setOnClickListener(listenerAction);

		bouton = (ImageButton) findViewById(R.id.ButtonStop);
//		bouton.setTag(0xF000 + 0xB2);
//		bouton.setOnClickListener(listenerKey);
		bouton.setTag(13);
		bouton.setOnClickListener(listenerAction);

		bouton = (ImageButton) findViewById(R.id.ButtonNext);
//		bouton.setTag(0xF000 + 0xB0);
//		bouton.setOnClickListener(listenerKey);
		bouton.setTag(14);
		bouton.setOnClickListener(listenerAction);

		bouton = (ImageButton) findViewById(R.id.ButtonPrev);
//		bouton.setTag(0xF000 + 0xB1);
//		bouton.setOnClickListener(listenerKey);
		bouton.setTag(15);
		bouton.setOnClickListener(listenerAction);

		bouton = (ImageButton) findViewById(R.id.ButtonFfw);
		bouton.setTag(77);
		bouton.setOnClickListener(listenerAction);

		bouton = (ImageButton) findViewById(R.id.ButtonRew);
		bouton.setTag(78);
		bouton.setOnClickListener(listenerAction);

		bouton = (ImageButton) findViewById(R.id.BoutonMusic);
		bouton.setTag("ExecBuiltIn(ActivateWindow(music))");
		bouton.setOnClickListener(listenerRequest);

		bouton = (ImageButton) findViewById(R.id.BoutonVideo);
		bouton.setTag("ExecBuiltIn(ActivateWindow(video))");
		bouton.setOnClickListener(listenerRequest);

		bouton = (ImageButton) findViewById(R.id.BoutonPicture);
		bouton.setTag("ExecBuiltIn(ActivateWindow(pictures))");
		bouton.setOnClickListener(listenerRequest);

	}

	/**
	 * Prise en charge des bouttons de volume;
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
	// {
	// sendCommand(0xF000+ 0x26);
	// return true;
	// }
	// if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
	// {
	// sendCommand(0xF000+ 0x28);
	// return true;
	// }
	// if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
	// {
	// sendCommand(0xF000+ 0x25);
	// return true;
	// }
	// if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
	// {
	// sendCommand(0xF000+ 0x27);
	// return true;
	// }
	// if (keyCode == KeyEvent.KEYCODE_ENTER ||
	// keyCode==KeyEvent.KEYCODE_DPAD_CENTER)
	// {
	// sendCommand(0xF000+ 0x0D);
	// return true;
	// }
	// return false;
	// }
	//	
	//	

	public void execRequest(Object value) {
		try {
			XbmcRequester.getInstace().sendCommand(value.toString());
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendCommand(Object value) {
		try {
			XbmcRequester.getInstace().sendCommand("SendKey(" + value + ")");
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendAction(Object value) {
		try {
			XbmcRequester.getInstace().sendCommand("Action(" + value + ")");
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public enum ListenerType {
		COMMAND, ACTION, REQUEST
	}

	/**
	 * Le listener de boutons :)
	 */
	public class ButtonsClickListener implements OnClickListener {

		ListenerType listenerType;

		public ButtonsClickListener(ListenerType listType) {
			listenerType = listType;
		}

		/**
		 * Commands are sent threaded because some action don't return directly
		 */
		@Override
		public void onClick(View v) {
			switch (listenerType) {
			case ACTION:
				sendAction(v.getTag());
				break;
			case COMMAND:
				sendCommand(v.getTag());
				break;
			case REQUEST:
				execRequest(v.getTag());
				break;
			default:
				break;
			}
			//			
			// Thread th = new Thread(new runningThread(v.getTag().toString()));
			// th.start();
		}

	}

	public class runningThread implements Runnable {
		String tag = null;

		public runningThread(String tag) {
			this.tag = tag;
		}

		public void run() {
			try {
				XbmcRequester.getInstace().sendCommand("Action(" + tag + ")");
			} catch (RequestException e) {
				// Don't do any thing, it might work on next press.

			}
		}
	}

}
