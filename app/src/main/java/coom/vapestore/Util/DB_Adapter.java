package coom.vapestore.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.sql.SQLException;

import zambelz.sqlite.helper.library.ZafDBManager;

public class DB_Adapter extends SQLiteOpenHelper {
    protected ZafDBManager db;
    private SQLiteDatabase sqlDB;

    protected DB_Adapter(Context context) throws IOException {
        super(context, Config.DB_NAME, null, Config.DB_VERSION);
        db = new ZafDBManager(context, this, sqlDB, Config.DB_PATH, Config.DB_NAME);
        db.setDBProfile(Config.DB_PATH, Config.DB_VERSION, Config.DB_NAME);
        db.registerDB();
    }

    public void open() throws SQLException {
        db.open();
    }

    @Override
    public synchronized void close() {
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
