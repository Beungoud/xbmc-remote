package fr.beungoud.xbmcremote.streaming.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;

import android.util.Log;
import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;

public class MusicStreamerServerSocket implements Runnable {

	XbmcRequester requester;


	boolean serviceRunning = false;

	public MusicStreamerServerSocket() {
		this.requester = XbmcRequester.getInstace();

	}

	@Override
	public void run() {
		serviceRunning = true;
		while (serviceRunning) {

			try {

				ServerSocket server = new ServerSocket(12345);
				Socket sock = server.accept();
				Log.i("XBMC", "Connexion acceptée sur port 12345");
				MusicStreamerThread thread = new MusicStreamerThread(sock);
				
				thread.start();

			} catch (IOException e) {

			}

		}

	}

	public boolean isServiceRunning() {
		return serviceRunning;
	}

	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}
}
