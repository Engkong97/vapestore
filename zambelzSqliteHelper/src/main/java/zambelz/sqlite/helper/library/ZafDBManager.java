package zambelz.sqlite.helper.library;

/**
 * ZAMBELZ FRAMEWORK Open Source Project
 * Database Manager
 * @author Nanda . J . A 
 * @version 03.05.2014 build 14.06
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ZafDBManager extends ZafDBAdapter {

	private String DB_PATH;
	private int DB_VERSION;
	private String DB_NAME;

	private Context context;
	private SQLiteOpenHelper sHelper;
	private SQLiteDatabase db;

	public ZafDBManager(Context context, SQLiteOpenHelper sHelper, SQLiteDatabase db, String DB_PATH, String DB_NAME) {
		super(db, DB_PATH + DB_NAME);
		
		this.context = context;
		this.sHelper = sHelper;
		this.db		 = db;
	}

	/**
	 * Set database path, version and name
	 */
	public void setDBProfile(String DB_PATH, int DB_VERSION, String DB_NAME) {
		this.DB_PATH 	= DB_PATH;
		this.DB_VERSION	= DB_VERSION;
		this.DB_NAME 	= DB_NAME;
	}
	
	public SQLiteDatabase getDB() {
		return db;
	}
	
	public String getDBPath() {
		return DB_PATH;
	}
	
	public int getDBVersion() {
		return DB_VERSION;
	}
	
	public String getDBName() {
		return DB_NAME;
	}
	
	/**
	 * Register database
	 */
	public void registerDB() throws IOException {
		if(!isDBExists()) {
			copyDatabase();
		}
	}
	
	private boolean isDBExists() {
		try {
			File DBFile = new File(DB_PATH + DB_NAME);
			return DBFile.exists();
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		return false;
	}

	/**
	 * Copying database
	 */
	public void copyDatabase() throws IOException {
		SQLiteDatabase db = sHelper.getReadableDatabase();
		db.close();

		InputStream input 	= context.getAssets().open(DB_NAME);
		OutputStream output = new FileOutputStream(DB_PATH + DB_NAME);
		byte[] buffer 		= new byte[1024];
		int length;
		
		while((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}

		output.flush();
		output.close();
		input.close();
	}
	
}
