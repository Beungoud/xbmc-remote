/**
 * 
 */
package fr.beungoud.util;

import org.apache.http.impl.conn.DefaultClientConnection;

import fr.beungoud.xbmcremote.XBMCRemote;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Benoit
 * 
 */
public class ConfigurationAccess {
	private static final String PREFS_NAME = "GlobalPrefs";

	private static ConfigurationAccess instance = new ConfigurationAccess();

	/**
	 * GetInstance
	 * 
	 * @return
	 */
	public static ConfigurationAccess getInstance() {
		return instance;
	}

	/**
	 * The application context (for database);
	 */
	private Context context;
	
	private Boolean thumbnailEnabled = null;

	/**
	 * The Database accessor
	 */
	private ConfigurationDbOpenHelper helper;

	/**
	 * Private constructor?
	 */
	private ConfigurationAccess() {
	}
	
	/**
	 * Tells if thumbnail is enabled
	 */
	public boolean isThumbnailEnabled(){
		if (thumbnailEnabled == null)
		{
		       // Restore preferences
		       SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		       thumbnailEnabled = settings.getBoolean("thumbnailEnabled", false);
		}
		return thumbnailEnabled;
	}
	
	/**
	 * Sets if thumbnail should be enabled or not.
	 * @param enable
	 */
	public void setThumbnailEnabled(boolean enable ) {
	       // Restore preferences
	       SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
	       SharedPreferences.Editor editor = settings.edit();
	       
	       editor.putBoolean("thumbnailEnabled", enable);
	       
	       editor.commit();       
	       
	       thumbnailEnabled = enable;
	       
	}

	/**
	 * To retrieve the current configured server
	 * 
	 * @return The current server
	 */
	public String getCurrentServerName() {
		String serverName = null;
		
		Server[] servers = getServers();
		if (servers.length > 0)
		{
			for (Server server : servers) {
				if (server.isCurrent())
				{
					serverName = server.getServerName();
				}
			}
		} else {
			return "null";
		}

		return serverName;
	}

	/**
	 * To retrieve the current configured server
	 * 
	 * @return The current server
	 */
	public Server getCurrentServer() {
		Server serverRetour = null;
		
		Server[] servers = getServers();
		if (servers.length > 0)
		{
			for (Server server : servers) {
				if (server.isCurrent())
				{
					serverRetour = server;
				}
			}
		} 

		return serverRetour;
	}
	
	/**
	 * REturns the list of configured servers
	 * @return
	 */
	public Server[] getServers()
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query(ConfigurationDbOpenHelper.DEFAUTLT_SERVER_TABLE,
				new String[] {ConfigurationDbOpenHelper.COL_ID, ConfigurationDbOpenHelper.COL_SERVER_NAME, ConfigurationDbOpenHelper.COL_SERVER_HOST, 
				ConfigurationDbOpenHelper.COL_SERVER_PORT, ConfigurationDbOpenHelper.COL_USERNAME, ConfigurationDbOpenHelper.COL_PASSWORD, ConfigurationDbOpenHelper.COL_IS_DEFAULT},  
				null, null, null, null, null);

		cursor.moveToFirst();
		
		Server[] retour = new Server[cursor.getCount()];
		
		while (!cursor.isAfterLast())
		{
			retour[cursor.getPosition()] = createServer(cursor);
			cursor.moveToNext();
		}
		
		cursor.close();
		db.close();
		
		return retour;
	}
	
	public Server createServer(Cursor cursor)
	{
		Server retour = new Server(cursor.getInt(cursor.getColumnIndex(ConfigurationDbOpenHelper.COL_ID)),
				cursor.getString(cursor.getColumnIndex(ConfigurationDbOpenHelper.COL_SERVER_NAME)),
				cursor.getString(cursor.getColumnIndex(ConfigurationDbOpenHelper.COL_SERVER_HOST)),
				cursor.getInt(cursor.getColumnIndex(ConfigurationDbOpenHelper.COL_SERVER_PORT)), 
				cursor.getString(cursor.getColumnIndex(ConfigurationDbOpenHelper.COL_USERNAME)), 
				cursor.getString(cursor.getColumnIndex(ConfigurationDbOpenHelper.COL_PASSWORD)),
				cursor.getInt(cursor.getColumnIndex(ConfigurationDbOpenHelper.COL_IS_DEFAULT)) == 1? true: false);
		
		return retour;
	}
	
	/**
	 * Add a server to the database
	 * @param server the server to add
	 */
	public void addServer(Server server)
	{
		SQLiteDatabase db = helper.getWritableDatabase();

		if (server.isCurrent())
		{
			db.execSQL("update " + ConfigurationDbOpenHelper.DEFAUTLT_SERVER_TABLE + " set " + 
					ConfigurationDbOpenHelper.COL_IS_DEFAULT + " = 0;");
		}
		
		db.execSQL("insert into " + ConfigurationDbOpenHelper.DEFAUTLT_SERVER_TABLE + "(" +
				ConfigurationDbOpenHelper.COL_SERVER_NAME + ", " + 
				ConfigurationDbOpenHelper.COL_SERVER_HOST + ", " + 
				ConfigurationDbOpenHelper.COL_SERVER_PORT + ", " + 
				ConfigurationDbOpenHelper.COL_USERNAME + ", " + 
				ConfigurationDbOpenHelper.COL_PASSWORD + ", " + 
				ConfigurationDbOpenHelper.COL_IS_DEFAULT + ") " + 
				" values ( " + 
				"\"" + server.getServerName() + "\"" + ", " + 
				"\"" + server.getHostname() + "\"" + ", " + 
				"\"" + server.getPort() + "\"" + ", " + 
				"\"" + server.getLogin() +"\"" +  ", " + 
				"\"" + server.getPassword() +"\"" +  ", " + 
				"\"" +( server.isCurrent()?"1":"0") + "\"" + "); ");
		
		db.close();

	}

	public void initConfiguration(Context context) {
		this.context = context;
		helper = new ConfigurationDbOpenHelper(context);
	}

	public void deleteServer(Server server) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from " + ConfigurationDbOpenHelper.DEFAUTLT_SERVER_TABLE + 
				" where " + ConfigurationDbOpenHelper.COL_ID + " = " + server.getId());
		db.close();
	}

	public void updateServer(Server server) {
		SQLiteDatabase db = helper.getWritableDatabase();

		if (server.isCurrent())
		{
			db.execSQL("update " + ConfigurationDbOpenHelper.DEFAUTLT_SERVER_TABLE + " set " + 
					ConfigurationDbOpenHelper.COL_IS_DEFAULT + " = 0;");
		}
		
		db.execSQL("update " + ConfigurationDbOpenHelper.DEFAUTLT_SERVER_TABLE + " set " +
				ConfigurationDbOpenHelper.COL_SERVER_NAME + "=\"" + server.getServerName() +"\", "+  
				ConfigurationDbOpenHelper.COL_SERVER_HOST + "=\"" + server.getHostname() + "\", "+
				ConfigurationDbOpenHelper.COL_SERVER_PORT + "=\"" + server.getPort() + "\", "+
				ConfigurationDbOpenHelper.COL_USERNAME + "=\"" + server.getLogin() + "\", "+
				ConfigurationDbOpenHelper.COL_PASSWORD + "=\"" + server.getPassword() + "\", "+
				ConfigurationDbOpenHelper.COL_IS_DEFAULT + "=\"" + ( server.isCurrent()?"1":"0") +"\" "+ 
				"where " + ConfigurationDbOpenHelper.COL_ID + " = \"" + server.getId() +"\""); 
		
		db.close();
		
	}
}
