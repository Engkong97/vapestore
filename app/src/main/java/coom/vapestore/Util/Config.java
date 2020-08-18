package coom.vapestore.Util;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.File;

@SuppressLint("SdCardPath")
public class Config {

    //Database configuration
    public static final String APP_PACKAGE 	= "coom.vapestore/";
    public static final String DB_PATH 		= "/data/data/"+ APP_PACKAGE +"databases/";
    public static final String DB_NAME 		= "DB_VAPE.db";
    public static final int DB_VERSION 		= 3;

    public static final String BASE_DATA_DIR 	= "/VapeStore/";
    public static final String[] REQUIRE_DIR 	= {"Backup_db", "Data", "Log"};
    public static final String BACKUP_DB_DIR 	= BASE_DATA_DIR + REQUIRE_DIR[0];
    public static final String MAIN_DATA_DIR 	= BASE_DATA_DIR + REQUIRE_DIR[1];
    public static final String LOG_DIR 			= BASE_DATA_DIR + REQUIRE_DIR[2];
    public static final String IMG_APPRAISAL	= BASE_DATA_DIR + REQUIRE_DIR[1] +"/Image/";

    public static final boolean WRITE_LOG_ON_STORAGE = true;

    public static void createRequireDir() {
        File baseDir = new File(Environment.getExternalStorageDirectory(), BASE_DATA_DIR);
        File reqDir = null;

        if (!baseDir.exists()){
            baseDir.mkdirs();
        }

        for(int i = 0; i < Config.REQUIRE_DIR.length; i++) {
            reqDir = new File(Environment.getExternalStorageDirectory(), BASE_DATA_DIR +"/"+ REQUIRE_DIR[i]);
            if (!reqDir.exists()){
                reqDir.mkdirs();
            }
        }
    }

    public static void createPackageName() {
        File baseDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + APP_PACKAGE+"databases/");
        File reqDir = null;

        if (!baseDir.exists()){
            baseDir.mkdirs();
        }


    }
}

