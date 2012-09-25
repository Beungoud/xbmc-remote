package fr.beungoud.xbmcremote.musicstreamer;

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

public class MusicStreamerThread extends Thread {

	XbmcRequester requester;

	List<String> playlist;

	public List<String> getPlaylist() {
		return playlist;
	}

	public void setPlaylist(List<String> playlist) {
		this.playlist = playlist;
	}

	int playlistIndex = 0;

	boolean playing = false;
	
	MusicStreamWaiterInterface waiter = null;

	public MusicStreamWaiterInterface getWaiter() {
		return waiter;
	}

	public void setWaiter(MusicStreamWaiterInterface waiter) {
		this.waiter = waiter;
	}

	
	public MusicStreamerThread( List<String> playlist) {
		this.requester = XbmcRequester.getInstace();

		this.playlist = playlist;

		playlistIndex = 0;
	}

	@Override
	public void run() {
		playing = true;
		while (playing) {

			try {
				List<String> sizeString = requester.requestStringList("FileSize(" + playlist.get(playlistIndex) + ")");
				long fileSize = Long.parseLong(sizeString.get(0));
				Log.i("XBMC", "File size : " + fileSize);
				InputStream inputStream = requester.streamRequest("FileDownload(" + playlist.get(playlistIndex)
						+ ")");

				// byte[] sound = Base64.decode(result);
				Log.i("XBMC", "inputStream récupéré.");
				
				////// Nouvelle solution
				
				
				if (waiter != null)
				{
//					waiter.musicReady("http://localhost:12345/toto.mp3");
				}
				

				try {
					ServerSocket server = new ServerSocket(12345);
					Socket sock = server.accept();

					Log.i("XBMC", "Connexion acceptée sur le socket server HTTP");
					final DefaultHttpServerConnection serverConnexion = new DefaultHttpServerConnection();
					serverConnexion.bind(sock, new BasicHttpParams());

					HttpRequest header = serverConnexion.receiveRequestHeader();
					Log.i("XBMC2", header.getRequestLine().toString());

					final HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

					Base64InputStreamEntity myEntity = new Base64InputStreamEntity(inputStream, fileSize);
					
//					StringEntity myEntity = new StringEntity("Bonjour les amis comment ca va?");
					
					myEntity.setContentType("audio/mpeg");
//					myEntity.setContentEncoding("identity");
					response.setEntity(myEntity);
//					response.setHeader(myEntity.getContentEncoding());
					response.setHeader(myEntity.getContentType());
					Thread thread = new Thread(){
						public void run() {
							try {
								serverConnexion.sendResponseHeader(response);
								serverConnexion.sendResponseEntity(response);
								
//								serverConnexion.close();
							} catch (HttpException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					};
					thread.start();
			

				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (RequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			playlistIndex++;
			if (playlistIndex >= playlist.size()) {
				playing = false;
			}
		}

	}
}
