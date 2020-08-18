package coom.vapestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.SharedPref;
import coom.vapestore.adapter.BarangAdapter;
import coom.vapestore.adapter.BarangMainAdapter;
import coom.vapestore.model.BarangModel;
import coom.vapestore.profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    CardView btnLogin, btnProfile;
    SharedPref pref;
    RecyclerView rvListBarang;
    DB_VAPE db;
    Cursor csr;
    Context context;
    BarangMainAdapter adapter;
    List<BarangModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLoginMain);
        btnProfile = findViewById(R.id.btnProfil);
        rvListBarang = findViewById(R.id.rvMain);
        adapter = new BarangMainAdapter(this, list);
        pref = new SharedPref(this);

        try{
            db = new DB_VAPE(this);
        } catch (Exception e){
            Toast.makeText(this, e.getCause().getMessage(), Toast.LENGTH_SHORT).show();
        }

        initUI();

        Log.i("ISLOGIN", String.valueOf(pref.getIslogin()));
        if (pref.getIslogin() == 1){
            btnLogin.setVisibility(View.GONE);
            btnProfile.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnProfile.setVisibility(View.GONE);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
//                finish();
//                pref.saveSPInt(SharedPref.SP_IS_LOGIN, 0);
            }
        });
    }

    private void initUI() {
        list.clear();
        try{
            db.open();
            csr = db.getBarang();
            for (int i= 0; i<csr.getCount(); i++){
                csr.moveToPosition(i);
                BarangModel item = new BarangModel();
                item.setBarangId(Integer.parseInt(csr.getString(0)));
                item.setNamaBarang(csr.getString(1));
                item.setQty(csr.getString(2));
                item.setDesc(csr.getString(3));
                item.setHarga(csr.getString(4));
                item.setImagePath(csr.getString(5));
                list.add(item);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
        rvListBarang.setLayoutManager(new GridLayoutManager(this, 2));
        rvListBarang.setHasFixedSize(true);
        rvListBarang.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
       moveTaskToBack(true);
    }
}
