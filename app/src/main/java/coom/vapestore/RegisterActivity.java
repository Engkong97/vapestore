package coom.vapestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flatdialoglibrary.dialog.FlatDialog;

import org.w3c.dom.Text;

import java.io.IOException;

import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.EncryptDecrypt;

public class RegisterActivity extends AppCompatActivity {

    ImageView btnBack;
    CardView btnRegis;
    EditText userName, pass, rePass, nama,noHp;
    DB_VAPE db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnBack = findViewById(R.id.btnBackRegis);
        btnRegis = findViewById(R.id.btnRegis);
        userName = findViewById(R.id.registUserName);
        pass = findViewById(R.id.registPass);
        rePass = findViewById(R.id.registRePass);
        nama = findViewById(R.id.registNama);
        noHp = findViewById(R.id.noTelponRegis);

        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage("Loading...");

        try {
            db = new DB_VAPE(this);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                if (TextUtils.isEmpty(userName.getText().toString())){
                    pd.dismiss();
                    userName.setError("User Name Tidak Boleh Kosong");
                } else if (TextUtils.isEmpty(pass.getText().toString())){
                    pd.dismiss();
                    pass.setError("Password Tidak Boleh Kosong");
                } else if (TextUtils.isEmpty(rePass.getText().toString())){
                    pd.dismiss();
                    rePass.setError("Harap Isi Password Anda Kembali Disini");
                } else if (!rePass.getText().toString().equals(pass.getText().toString())){
                    pd.dismiss();
                    rePass.setError("Password Tidak Sama");
                } else if (TextUtils.isEmpty(noHp.getText().toString())) {
                    pd.dismiss();
                    pass.setError("No Handphone Tidak Boleh Kosong");
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            try {
                                String secKey = EncryptDecrypt.encrypt(userName.getText().toString()+pass.getText().toString());
                                Log.i("encrypt ",secKey);
                                Log.i("decrypt", EncryptDecrypt.decrypt(secKey));
                                db.open();
                                db.register(userName.getText().toString(), pass.getText().toString(), secKey, nama.getText().toString(), noHp.getText().toString());
                                db.close();
                                final FlatDialog flatDialog = new FlatDialog(RegisterActivity.this);
                                flatDialog.setTitle("Silahkan Login")
                                        .setTitleColor(Color.parseColor("#1297E0"))
                                        .setFirstButtonText("LOGIN")
                                        .setBackgroundColor(Color.parseColor("#FFFFFF"))
                                        .setFirstButtonColor(Color.parseColor("#1297E0"))
                                        .setFirstButtonTextColor(Color.parseColor("#FFFFFF"))
                                        .withFirstButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                finish();
//                                                Toast.makeText(RegisterActivity.this, flatDialog.getFirstTextField(), Toast.LENGTH_SHORT).show();
                                            }
                                        })

                                        .show();
                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    },3000L);
                }
            }
        });
    }
}
