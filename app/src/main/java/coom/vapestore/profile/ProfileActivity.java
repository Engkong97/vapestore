package coom.vapestore.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import coom.vapestore.LoginActivity;
import coom.vapestore.R;
import coom.vapestore.Util.SharedPref;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout btnGantiPass, btnHistory, btnLogout, btnConfirmTrx;
    SharedPref pref;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnGantiPass = findViewById(R.id.btnGantiPassword);
        btnHistory = findViewById(R.id.btnRiwayat);
        btnLogout = findViewById(R.id.btnLogout);
        userName = findViewById(R.id.userName);
        btnConfirmTrx = findViewById(R.id.btnConfirmTrx);

        pref = new SharedPref(this);

        userName.setText(pref.getUserName());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                pref.saveSPInt(SharedPref.SP_IS_LOGIN, 0);
                pref.saveSPInt(SharedPref.SP_ID_USER, 0);
                finish();
            }
        });

        btnGantiPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
            }
        });

        btnConfirmTrx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ConfirmTransactionActivity.class));
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HistoryTrxActivity.class));
            }
        });
    }
}
