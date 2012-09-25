package fr.beungoud.xbmcremote.streaming.service;

import java.io.IOException;
import java.io.InputStream;
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

import fr.beungoud.xbmcremote.RequestException;
import fr.beungoud.xbmcremote.XbmcRequester;

import android.net.Uri;
import android.util.Log;

public class MusicStreamerThread extends Thread{
	
	public static final String URIHeader = "GetFile.mp3?File=";
	
	Socket socket;
	String filename;
	
	public MusicStreamerThread (Socket socket)
	{
		this.socket = socket;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run()
	{

		final DefaultHttpServerConnection serverConnexion;
		final HttpResponse response;
		Base64InputStreamEntity myEntity;
		try {
			Log.i("XBMC", "Connexion acceptée sur le socket server HTTP");
			serverConnexion = new DefaultHttpServerConnection();
			serverConnexion.bind(socket, new BasicHttpParams());

			HttpRequest header = serverConnexion.receiveRequestHeader();
			Log.i("XBMC2", header.getRequestLine().toString());
			
			filename = header.getRequestLine().getUri();
			filename = java.net.URLDecoder.decode(filename);
			
			if (filename.startsWith("/" + URIHeader))
			{
				filename = filename.substring(URIHeader.length()+1);
				XbmcRequester requester= XbmcRequester.getInstace();
				
				List<String> sizeString = requester.requestStringList("FileSize(" + filename + ")");
				long fileSize = Long.parseLong(sizeString.get(0));
				Log.i("XBMC", "File size : " + fileSize);
				InputStream inputStream = requester.streamRequest("FileDownload(" + filename + ")");

				// byte[] sound = Base64.decode(result);
				Log.i("XBMC", "inputStream récupéré.");
				response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

				myEntity = new Base64InputStreamEntity(inputStream, fileSize);

				// StringEntity myEntity = new
				// StringEntity("Bonjour les amis comment ca va?");
				
				long leng = myEntity.getContentLength();
				
				myEntity.setContentType("audio/mpeg");
				// myEntity.setContentEncoding("identity");
				response.setEntity(myEntity);
				// response.setHeader(myEntity.getContentEncoding());
				response.setHeader(myEntity.getContentType());
				response.setHeader("Content-Length", "" + myEntity.getContentLength());
				Thread thread = new Thread() {
					public void run() {
						try {
							serverConnexion.sendResponseHeader(response);
							serverConnexion.sendResponseEntity(response);

							// serverConnexion.close();
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
				
			} 

			
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (HttpException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RequestException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
