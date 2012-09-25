/**
 * 
 */
package fr.beungoud.util;

import fr.beungoud.xbmcremote.R;
import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * @author Benoit
 *
 */
public class ConfigurationDbOpenHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "Configuration.db";
	private final static int DATABASE_VERSION = 1;
	
	public final static  String DEFAUTLT_SERVER_TABLE = "Servers";
	
	public final static String COL_ID = "Id";
	public final static String COL_SERVER_NAME = "ServerName";
	public final static String COL_SERVER_HOST = "Hostname";
	public final static String COL_USERNAME = "UserName";
	public final static String COL_PASSWORD = "Password";
	public final static String COL_SERVER_PORT = "Port";
	public final static String COL_IS_DEFAULT = "IsCurrent";
	
	/**
	 * The working context
	 */
	private Context context;

	public ConfigurationDbOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("create table " + DEFAUTLT_SERVER_TABLE + " (" +
				COL_ID + " INTEGER PRIMARY KEY, "+
				COL_SERVER_NAME+ " String, " +
				COL_SERVER_HOST + " String, " +
				COL_SERVER_PORT + " Integer, " +
				COL_USERNAME + " String, " + 
				COL_PASSWORD + " String, " +
				COL_IS_DEFAULT + " bool" + 
				");");
//		database.execSQL("insert into " + DEFAUTLT_SERVER_TABLE + " ("+DEFAUTLT_SERVER_COL + ") values (\"Default\");");
		
		
		
		initDefaultServer(database);
		
		
	}
	
	public void initDefaultServer(SQLiteDatabase database)
	{
		SharedPreferences mPrefs = context.getSharedPreferences((String) context.getText(R.string.shared_preference_name), Context.MODE_PRIVATE);

		String ipAddress = mPrefs.getString((String) context.getText(R.string.key_ip_address), "0.0.0.0");
		String login = mPrefs.getString((String) context.getText(R.string.key_login), "xbmc");
		String password = mPrefs.getString((String) context.getText(R.string.key_password), "xbmc");
		int port = mPrefs.getInt((String) context.getText(R.string.key_port), 8080);
		
		database.execSQL("insert into " + DEFAUTLT_SERVER_TABLE + "(" +
				COL_SERVER_NAME + ", " + 
				COL_SERVER_HOST + ", " + 
				COL_SERVER_PORT + ", " + 
				COL_USERNAME + ", " + 
				COL_PASSWORD + ", " + 
				COL_IS_DEFAULT + ") " + 
				" values ( " + 
				"\"Server1\"" + ", " + 
				"\"" + ipAddress + "\"" + ", " + 
				"\"" + port + "\"" + ", " + 
				"\"" + login +"\"" +  ", " + 
				"\"" + password +"\"" +  ", " + 
				"\"1\"" + "); ");
	}
	
	public void insertServer()
	{
	
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

	}

}

