package coom.vapestore.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import coom.vapestore.LoginActivity;
import coom.vapestore.R;
import coom.vapestore.Util.SharedPref;

public class AdminActivity extends AppCompatActivity {

    CardView btnAddItem, btnInventory, btnOrder, btnLogout, btnUser;
    SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnAddItem = findViewById(R.id.btnAddItem);
        btnInventory = findViewById(R.id.btnInventory);
        btnOrder = findViewById(R.id.btnOrder);
        btnUser = findViewById(R.id.btnUser);
        btnLogout = findViewById(R.id.btnLogout);

        pref = new SharedPref(this);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                pref.saveSPInt(SharedPref.SP_IS_LOGIN, 0);
                finish();
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AddItemActivity.class));
            }
        });

        btnInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, InventoryActivity.class));
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ListUserActivity.class));
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, OrderActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
//      super.onBackPressed();
    }
}
