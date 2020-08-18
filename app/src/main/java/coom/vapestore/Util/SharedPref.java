package coom.vapestore.Util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref extends Application {
    public static final String SP_APP = "spApp";
    public static final String SP_IS_LOGIN = "spIsLogin";
    public static final String SP_ID_USER = "spIdUser";
    public static final String SP_USER_NAME = "spUserName";

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public SharedPref(Context context){
        sp = context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        editor.putString(keySP, value);
        editor.apply();
    }

    public void saveSPInt(String keySP, int value){
        editor.putInt(keySP, value);
        editor.apply();
    }

    public void saveSPBoolean(String keySP, boolean value){
        editor.putBoolean(keySP, value);
        editor.apply();
    }

    public int getIslogin(){
        return sp.getInt(SP_IS_LOGIN, 0);
    }
    public int getIdUser(){return sp.getInt(SP_ID_USER, 0);}
    public String getUserName(){return sp.getString(SP_USER_NAME, "");}
}
