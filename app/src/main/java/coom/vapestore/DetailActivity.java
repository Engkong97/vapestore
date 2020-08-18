package coom.vapestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;

import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.ImageUtil;
import coom.vapestore.Util.SharedPref;
import coom.vapestore.model.BarangModel;

public class DetailActivity extends AppCompatActivity {
    BarangModel model;
    DB_VAPE db;
    Context context;
    Cursor csr;
    String id;
    SharedPref pref;
    TextView namaBarang, hargaBarang, deskripsi;
    Button btnBeli;
    ImageView btnBack, img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        namaBarang = findViewById(R.id.namaBarangDetail);
        hargaBarang = findViewById(R.id.hargaBarangDetail);
        deskripsi = findViewById(R.id.desc_barang_detail);
        btnBeli = findViewById(R.id.btnBeli);
        btnBack = findViewById(R.id.btnBackDetail);
        img = findViewById(R.id.image_barang_detail);

        pref = new SharedPref(this);

        try {
            db = new DB_VAPE(this);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Intent intent = getIntent();
        id = intent.getStringExtra("IDBARANG");

        model = new BarangModel();

        initUI();
    }

    private void initUI() {
        try{
            db.open();
            csr = db.getDetailBarang(Integer.parseInt(id));
            for (int i = 0; i<csr.getCount(); i++){
                csr.moveToPosition(i);
                model.setBarangId(Integer.parseInt(csr.getString(0)));
                model.setNamaBarang(csr.getString(1));
                model.setQty(csr.getString(2));
                model.setDesc(csr.getString(3));
                model.setHarga(csr.getString(4));
                model.setImagePath(csr.getString(5));
                Log.i("model", model.toString());
            }
            namaBarang.setText(model.getNamaBarang());
            hargaBarang.setText("Rp. "+model.getHarga());
            deskripsi.setText(model.getDesc());
            img.setImageBitmap(ImageUtil.getImagefromPath(model.getImagePath()));

            btnBeli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pref.getIslogin()==0){
                        Toast.makeText(DetailActivity.this,"Silahkan Login Terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(DetailActivity.this, CartActivity.class);
                        intent.putExtra("CARTIDBARANG", String.valueOf(model.getBarangId()));
                        startActivity(intent);
                    }
                }
            });

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                    finish();
                }
            });
            db.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
