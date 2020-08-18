package coom.vapestore.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flatdialoglibrary.dialog.FlatDialog;

import java.io.IOException;

import coom.vapestore.LoginActivity;
import coom.vapestore.R;
import coom.vapestore.RegisterActivity;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.EncryptDecrypt;
import coom.vapestore.Util.SharedPref;

public class ChangePasswordActivity extends AppCompatActivity {

    CardView btnSave;
    ImageView btnBack;
    EditText oldPass, newPass, reNewPass;
    DB_VAPE db;
    Context context;
    SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btnBack = findViewById(R.id.btnBackChangePass);
        btnSave = findViewById(R.id.btnSaveChangePass);
        oldPass = findViewById(R.id.oldPass);
        newPass = findViewById(R.id.newPass);
        reNewPass = findViewById(R.id.reNewPass);

        pref = new SharedPref(this);

        final ProgressDialog pd = new ProgressDialog(ChangePasswordActivity.this);
        pd.setMessage("Loading...");

        try {
            db = new DB_VAPE(this);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePasswordActivity.this, ProfileActivity.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                if (TextUtils.isEmpty(oldPass.getText().toString())){
                    pd.dismiss();
                    oldPass.setError("Password Lama Tidak Boleh Kosong");
                } else if (TextUtils.isEmpty(newPass.getText().toString())){
                    pd.dismiss();
                    newPass.setError("Password Baru Tidak Boleh Kosong");
                } else if (TextUtils.isEmpty(reNewPass.getText().toString())){
                    pd.dismiss();
                    reNewPass.setError("Harap Isi Password Baru Anda Kembali Disini");
                } else if (!newPass.getText().toString().equals(reNewPass.getText().toString())) {
                    pd.dismiss();
                    reNewPass.setError("Password Baru Anda Tidak Sama");
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            try {
                                String secKey = EncryptDecrypt.encrypt(pref.getUserName()+newPass.getText().toString());
                                db.open();
                                db.changePass(newPass.getText().toString(), secKey, pref.getIdUser());
                                db.close();
                                final FlatDialog flatDialog = new FlatDialog(ChangePasswordActivity.this);
                                flatDialog.setTitle("Sukses Mengganti Password")
                                        .setTitleColor(Color.parseColor("#1297E0"))
                                        .setFirstButtonText("Oke")
                                        .setBackgroundColor(Color.parseColor("#FFFFFF"))
                                        .setFirstButtonColor(Color.parseColor("#1297E0"))
                                        .setFirstButtonTextColor(Color.parseColor("#FFFFFF"))
                                        .withFirstButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(ChangePasswordActivity.this, ProfileActivity.class));
                                                finish();
//                                                Toast.makeText(RegisterActivity.this, flatDialog.getFirstTextField(), Toast.LENGTH_SHORT).show();
                                            }
                                        })

                                        .show();
                            } catch (Exception e){

                            }
                        }
                    }, 3000L);
                }
            }
        });

    }
}
