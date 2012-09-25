/**
 * 
 */
package fr.beungoud.xbmcremote;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import fr.beungoud.util.Base64;
import fr.beungoud.util.ConfigurationAccess;
import fr.beungoud.util.FieldHandler;
import fr.beungoud.util.Server;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;

/**
 * @author Benoit
 * 
 */
public class XbmcRequester {

	private int port = 0;

	private String ipAddress;

	private String login;

	private String password;

	private Context context;

	private DefaultHttpClient httpClient;

	private static XbmcRequester _instance = new XbmcRequester();

	public static XbmcRequester getInstace() {
		return _instance;
	}

	private XbmcRequester() {

	}

	/**
	 * Initialise the XBMc requester (especialy the configuration and context)
	 * 
	 * @param context
	 */
	public void initRequester(Context context) {
		this.context = context;

		resetRequester();

	}

	public synchronized String request(String request) throws RequestException {
		InputStream istream = streamRequest(request);
		StringBuilder retour = new StringBuilder();
		if (istream != null) {

			try {
				byte[] array = new byte[1024];
				int length = 0;
				while ((length = istream.read(array)) >= 0) {
					retour.append(new String(array, 0, length, "UTF-8"));
				}
			} catch (IOException e) {

			}
		}
		return retour.toString();
	}

	public synchronized InputStream streamRequest(String request) throws RequestException {
		Formatter formater = new Formatter();
		String request2 = java.net.URLEncoder.encode(request);
		formater.format("http://%s:%d/xbmcCmds/xbmcHttp?command=%s", new Object[] { ipAddress, port, request2 });

		InputStream streamRetour;
		Log.i("XBMC2", formater.toString());
		try {
			URL url = new URL(formater.toString());
			// Build the string to be used for Basic Authentication
			// <username>:<password>
			String userPassword = login + ":" + password;

			// Base64 encode the authentication string
			String encoding = Base64.encodeBytes(userPassword.getBytes());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// Set Basic Authentication parameters
			conn.setRequestProperty("Authorization", "Basic " + encoding);
			conn.setRequestMethod("GET");
			// conn.setDoOutput(true);
			// conn.setDoInput(true);
			conn.setChunkedStreamingMode(2048);
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(200000);
			conn.connect();

			streamRetour = conn.getInputStream();

			// response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
		} catch (ClientProtocolException e) {
			throw new RequestException();
		} catch (IOException e) {
			throw new RequestException(e);
		} catch (IllegalArgumentException e) {
			throw new RequestException(e);
		}
		return streamRetour;

	}

	public void sendCommand(String command) throws RequestException {
		request(command);
	}

	public Bundle requestBundle(String request) throws RequestException {
		Bundle retour = new Bundle();
		String htmlString = request(request);

		htmlString = removeHtmlTags(htmlString);

		String[] items = htmlString.split("<li>");

		String key;
		String value;
		int pointer;
		for (String string : items) {
			pointer = string.indexOf(':');
			if (pointer != -1) {
				key = string.substring(0, pointer);
				value = string.substring(pointer + 1);

				retour.putString(key, value.trim());
			}
		}

		return retour;
	}

	public Bundle requestList(String request) throws RequestException {
		Bundle retour = new Bundle();
		String htmlString = request(request);

		htmlString = removeHtmlTags(htmlString);

		String[] items = htmlString.split("<li>");

		String key;
		String value;
		int pointer;
		int pointer2;
		for (String string : items) {
			pointer = string.indexOf(';');
			if (pointer != -1) {
				pointer2 = string.lastIndexOf(';');
				key = string.substring(0, pointer);
				if (pointer != pointer2) {
					value = string.substring(pointer + 1, pointer2);
				} else {
					value = string.substring(pointer + 1);
				}
				retour.putString(key, value);
			}
		}

		return retour;
	}

	public String removeHtmlTags(String htmlString) {
		htmlString = htmlString.trim();
		if (htmlString.length() > "<html>".length() + "</html>".length())
			// Removing the <html> </html> tags
			htmlString = htmlString.substring("<html>".length(), htmlString.length() - "</html>".length()).trim();
		return htmlString;
	}

	public List<String[]> requestQuery(MediaType mediaType, String query, int nbFieldsPerLine) throws RequestException {
		String queryStart;
		if (mediaType == mediaType.Music) {
			queryStart = "QueryMusicDatabase(";
		} else {
			queryStart = "QueryVideoDatabase(";
		}

		String htmlString = request(queryStart + query + ")");

		htmlString = removeHtmlTags(htmlString);

		Log.i("XBMC", htmlString);

		FieldHandler fh = new FieldHandler();
		fh.parse(htmlString, nbFieldsPerLine);

		return fh.getResult();
	}

	public List<String> requestStringList(String request) throws RequestException {
		List<String> retour = new ArrayList<String>();
		String htmlString = request(request);

		if (htmlString.length() > 0) {
			htmlString = removeHtmlTags(htmlString);

			String[] items = htmlString.split("<li>");

			for (String string : items) {
				if (string.trim().length() != 0)
					retour.add(string.trim());
			}
		}
		return retour;
	}

	/**
	 * Reset configuration based on SQLite informations.
	 */
	public void resetRequester() {

		Server server = ConfigurationAccess.getInstance().getCurrentServer();

		if (server != null) {
			ipAddress = server.getHostname();
			login = server.getLogin();
			password = server.getPassword();
			port = server.getPort();

			httpClient = new DefaultHttpClient();
			httpClient.getCredentialsProvider().setCredentials(new AuthScope(ipAddress, port),
					new UsernamePasswordCredentials(login, password));
		}

	}

}
