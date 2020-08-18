package zambelz.sqlite.helper.library;

/**
 * ZAMBELZ FRAMEWORK Open Source Project
 * SQL Query Mapper for simplify database operation
 * @author Nanda . J . A 
 * @version 03.05.2014 build 14.06 
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class ZafDBAdapter {

	private SQLiteDatabase db;
	private String dbPath;
	
	/**
	 * Constructor
	 * @param SQLiteDatabase db
	 * @param String dbPath = Database path
	 */
	protected ZafDBAdapter(SQLiteDatabase db, String dbPath) {
		this.db = db;
		this.dbPath = dbPath;
	}
	
	/**
	 * Opening access to database 
	 */
	public void open() throws SQLException {
		db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	/**
	 * Closing access to database 
	 */
	public synchronized void close() {
		if(db != null) {
			db.close();
		}
	}
	
	public void beginTransacation() {
		db.beginTransaction();
	}
	
	public void endTransaction() {
		db.endTransaction();
	}
	
	public void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}
	
	public SQLiteStatement SQLStatement(String sql) {
		return db.compileStatement(sql);
	}
	
	/**
	 * Insert operation
	 * @param String dbTable = your db table
	 * @param ContentValues cVal = your value 
	 */
	public long insert(String dbTable, ContentValues cVal) {
		if(db.insert(dbTable, null, cVal) > 0) {
			cVal.clear();
			return 1;
		}
		
		return 0;
	}
	
	/**
	 * Update operation 
	 * @param String dbTable = your db table
	 * @param ContentValues cVal = your value
	 * @param String whereClause = where condition 
	 */
	public boolean update(String dbTable, ContentValues cVal, String whereClause) {
		if(db.update(dbTable, cVal, whereClause, null) > 0) {
			cVal.clear();
			return true;
		}
		
		return false;
	}
	
	/**
	 * Delete operation
	 * @param String dbTable = your db table
	 * @param ContentValues cVal = your value 
	 */
	public boolean delete(String dbTable, String whereClause) {
		return db.delete(dbTable, whereClause, null) > 0;
	}
	
	public void deleteAndReindex(String table) {
		execSQL("DELETE FROM "+ table);
		execSQL("REINDEX "+ table);
		execSQL("VACUUM");
	}
	
	public void dropAndReindex(String table) {
		execSQL("DROP TABLE "+ table);
		execSQL("REINDEX "+ table);
		execSQL("VACUUM");
	}
	
	/**
	 * Fetch by selecting your selection field
	 * @param String dbTable = your db table
	 * @param String[] columns = your table field
	 * @param String whereClause = where condition 
	 */
	public Cursor fetch(String dbTable, String[] columns, String whereClause) {
		return db.query(dbTable, columns, whereClause, null, null, null, null);
	}
	
	/**
	 * Fetch by selecting all field
	 * @param String dbTable = your db table
	 * @param String whereClause = where condition 
	 */
	public Cursor fetch(String dbTable, String whereClause) {
		return rawQuery("SELECT * FROM "+ dbTable +" WHERE "+ whereClause);
	}
	
	/**
	 * Fetch by selecting all field
	 * @param String dbTable = your db table
	 */
	public Cursor fetch(String dbTable) {
		return rawQuery("SELECT * FROM "+ dbTable);
	}
	
	/**
	 * Check if data is exists
	 * @param String query = your query
	 * @return boolean
	 */
	public boolean isDataExists(String query) {
		Cursor csr = null;
		
		try {
			csr = db.rawQuery(query, null);
			
			if(csr.moveToFirst()) {
				if(csr.getCount() > 0) {
					return true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(csr != null) {
				csr.close();
			}
		}
		
		return false;
	}
	
	public void execSQL(String query) {
		db.execSQL(query);
	}
	
	/**
	 * Execute custom query
	 * @param String query = your query
	 */
	public Cursor rawQuery(String query) {
		return db.rawQuery(query, null);
	}
	
	/**
	 * Execute custom query
	 * @param String query = your query
	 * @param String[] selectionArgs = selection arguments
	 */
	public Cursor rawQuery(String query, String[] selectionArgs) {
		return db.rawQuery(query, selectionArgs);
	}
	
	/**
	 * Get the total count
	 * @param String query = your query
	 * @return Integer
	 */
	public int getCount(String query) {
		return rawQuery(query).getCount();
	}
	
	/**
	 * Get the total selection columns count
	 * @param String query = your query
	 * @return Integer
	 */
	public int getColumnCount(String query) {
		return rawQuery(query).getColumnCount();
	}
	
	public int getIntColumnsValue(String query, String refColumn) {
		Cursor csr = null;
		int column = 0;
		
		try {
			csr = rawQuery(query);
			
			if(csr.moveToFirst()) {
				if(csr.getCount() > 0) {
					column = csr.getInt(csr.getColumnIndex(refColumn));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			csr.close();
		}
		
		return column;
	}
	
	public String getStringColumnsValue(String query, String refColumn) {
		Cursor csr = null;
		String column = null;
		
		try {
			csr = rawQuery(query);
			
			if(csr.moveToFirst()) {
				if(csr.getCount() > 0) {
					column = csr.getString(csr.getColumnIndex(refColumn));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			csr.close();
		}
		
		return column;
	}
	
}