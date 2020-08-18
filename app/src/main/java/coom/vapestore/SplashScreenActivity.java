package coom.vapestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import java.io.IOException;

import coom.vapestore.Util.Config;
import coom.vapestore.Util.SharedPref;
import coom.vapestore.admin.AdminActivity;
import coom.vapestore.admin.InventoryActivity;
import coom.vapestore.admin.ListUserActivity;
import coom.vapestore.admin.OrderActivity;

public class SplashScreenActivity extends AppCompatActivity {
    SharedPref pref;
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        final Handler handler = new Handler();
        pref = new SharedPref(this);
        Config.createPackageName();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println(getExternalCacheDir());
                if (pref.getUserName().equals("admin")) {
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    playSound();
                    finish();
                }
            }
        }, 3000L);
    }
    public void playSound(){
        mPlayer = MediaPlayer.create(this,R.raw.welcomesound);
        try {
            mPlayer.prepare();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        mPlayer.start();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion (MediaPlayer mediaPlayer){
//                stateAwal();
            }
        });
    }
}
