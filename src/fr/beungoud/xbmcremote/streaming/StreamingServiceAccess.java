package fr.beungoud.xbmcremote.streaming;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import fr.beungoud.xbmcremote.streaming.service.IXBMCMusicStreamer;
import fr.beungoud.xbmcremote.streaming.service.StreamingService;

public class StreamingServiceAccess {
	
	private static StreamingServiceAccess _instance = new StreamingServiceAccess();
	
	StreamingServiceConnexion conn = null;
	
	Context myContext;
	
	public static StreamingServiceAccess getInstace()
	{
		return _instance;
	}
	
	private StreamingServiceAccess() {
	}
	
	public void init(Context context)
	{
		myContext = context;
		if (conn == null)
		{
			conn = new StreamingServiceConnexion();
		}
		if ((conn != null) && !conn.isBound())
		{
			Intent i = new Intent();
			i.setClass(myContext, StreamingService.class);
		
			myContext.startService(i);
			myContext.bindService(i, conn, Context.BIND_AUTO_CREATE);
		}
		Log.i("XBMC_SVC_M", "Manager initialisé");
	}
	
	public void release()
	{
		if (myContext!= null && conn != null && conn.isBound())
			
		{
			myContext.unbindService(conn);
		}
		Log.i("XBMC_SVC_M", "Manager libéré");
	}
	
	IXBMCMusicStreamer serviceInstance = null;
	
	public IXBMCMusicStreamer getMusicStreamerService()
	{
		Log.i("XBMC_SVC_M", "Demande de service");
		if (myContext== null){
			throw new RuntimeException("No context given to service manager. Please init service maanger first.");
		}
		if (conn == null)
		{
			conn = new StreamingServiceConnexion();
		}
		if ((conn != null) && !conn.isBound())
		{
			Intent i = new Intent();
			i.setClass(myContext, StreamingService.class);
		
			myContext.bindService(i, conn, Context.BIND_AUTO_CREATE);

			// wait for the service to bind.
			int cpt=0;
			while (!conn.isBound && cpt < 20)
			{
				cpt++;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
		}
		return serviceInstance;
	}
	

	/**
	 * Manager de la connexion avec le service
	 * @author Benoit
	 *
	 */
	class StreamingServiceConnexion implements ServiceConnection {
		private boolean isBound = false;

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			Log.i("XBMC_SVC_M", "Service connecté");
			serviceInstance = IXBMCMusicStreamer.Stub.asInterface(arg1);
			isBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.i("XBMC_SVC_M", "Service déconnecté");
			serviceInstance = null;
			isBound = false;
		}

		/**
		 * @return the isBound
		 */
		public boolean isBound() {
			return isBound;
		}
	}
}
