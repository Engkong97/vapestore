package coom.vapestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.EncryptDecrypt;
import coom.vapestore.Util.SharedPref;
import coom.vapestore.admin.AdminActivity;

public class LoginActivity extends AppCompatActivity {

    TextView btnRegis;
    EditText txtUsername, txtPassword;
    CardView btnLogin;
    SharedPref pref;
    DB_VAPE db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegis = findViewById(R.id.btnRegis);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Loading...");

        try {
            db = new DB_VAPE(this);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        pref = new SharedPref(this);
        pref.saveSPInt(SharedPref.SP_IS_LOGIN, 0);

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                if (TextUtils.isEmpty(txtUsername.getText().toString())) {
                    pd.dismiss();
                    txtUsername.setError("Username Tidak Boleh Kosong");
                } else if (TextUtils.isEmpty(txtPassword.getText().toString())) {
                    pd.dismiss();
                    txtPassword.setError("Password Tidak Boleh Kosong");
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (txtUsername.getText().toString().equals("admin") && txtPassword.getText().toString().equals("admin1234")) {
                                pd.dismiss();
                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                finish();
                                pref.saveSPInt(SharedPref.SP_IS_LOGIN, 1);
                                pref.saveSPString(SharedPref.SP_USER_NAME, "admin");
                            } else {
                                pd.dismiss();
                                try{
                                    String secKey = EncryptDecrypt.encrypt(txtUsername.getText().toString()+ txtPassword.getText().toString());
                                    db.open();
                                    int count = db.login(secKey);
                                    Log.i("seckey", secKey);
                                    Log.i("count", String.valueOf(count));
                                    if (count == 1){
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                        Cursor csr =db.getUserDetail(secKey);
//                                        Log.i("idUser", String.valueOf(csr.getString(0)));
                                        pref.saveSPInt(SharedPref.SP_IS_LOGIN, 1);
                                        for (int i= 0; i<csr.getCount(); i++) {
                                            csr.moveToPosition(i);
                                            pref.saveSPInt(SharedPref.SP_ID_USER, Integer.parseInt(csr.getString(0)));
                                            pref.saveSPString(SharedPref.SP_USER_NAME, csr.getString(4));
                                            Log.i("idUser", String.valueOf(csr.getString(0)));
                                            Log.i("userName", String.valueOf(csr.getString(4)));
                                        }
//                                        playSound();
                                    } else {
                                        Toast.makeText(LoginActivity.this,"Username/Password Anda Salah", Toast.LENGTH_SHORT).show();
                                    }
                                    db.close();
                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }
                    },3000L);

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}
