package coom.vapestore;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flatdialoglibrary.dialog.FlatDialog;

import java.io.IOException;
import java.sql.SQLException;

import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.ImageUtil;
import coom.vapestore.Util.SharedPref;
import coom.vapestore.model.BarangModel;

public class CartActivity extends AppCompatActivity {
    String idBarang, total;
    BarangModel item;
    Cursor csr;
    DB_VAPE db;
    Context context;
    ImageView btnMin, btnPlus, btnBack, cartImage;
    TextView jumlahBarang, nama, harga, totalHarga;
    Button btnConfirm;
    static int i = 0;
    EditText alamat;
    SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btnMin = findViewById(R.id.btnMin);
        btnPlus = findViewById(R.id.btnPlus);
        jumlahBarang = findViewById(R.id.jmlhBarang);
        nama = findViewById(R.id.namaBarangCart);
        harga = findViewById(R.id.hargaBarangCart);
        btnBack = findViewById(R.id.btnBackCart);
        totalHarga = findViewById(R.id.totalHargaCart);
        btnConfirm = findViewById(R.id.btnConfirmOrder);
        alamat = findViewById(R.id.alamatPengiriman);
        cartImage = findViewById(R.id.cartImg);

        try {
            db = new DB_VAPE(this);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Intent intent = getIntent();
        idBarang = intent.getStringExtra("CARTIDBARANG");

        pref = new SharedPref(this);
        item = new BarangModel();

        initUI();
        process();
    }

    private void process() {
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                jumlahBarang.setText(String.valueOf(i));
                total = String.valueOf(i * Integer.parseInt(csr.getString(4)));
                totalHarga.setText("Rp." + total);
                if (i == 0) {
                    btnMin.setEnabled(false);
                } else {
                    btnMin.setEnabled(true);
                }
            }
        });


        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i--;
                jumlahBarang.setText(String.valueOf(i));
                total = String.valueOf(i * Integer.parseInt(csr.getString(4)));
                totalHarga.setText("Rp." + total);
                if (i == 0) {
                    btnMin.setEnabled(false);
                } else {
                    btnMin.setEnabled(true);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                Intent intent = new Intent(CartActivity.this, DetailActivity.class);
                intent.putExtra("IDBARANG", String.valueOf(item.getBarangId()));
                startActivity(intent);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    Toast.makeText(CartActivity.this, "Pesanan Harus Lebih Dari 0", Toast.LENGTH_SHORT).show();
                } else {
                    //save to db;
                    try {
                        db.open();
                        db.saveTrx(pref.getIdUser(), pref.getUserName(), item.getNamaBarang(), String.valueOf(i), Double.valueOf(total), alamat.getText().toString(), item.getBarangId());
                        db.updateBarang(Integer.parseInt(item.getQty())-i, item.getBarangId());
                        db.close();
                        try {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final FlatDialog flatDialog = new FlatDialog(CartActivity.this);
                                    flatDialog.setTitle("Silahkan Lakukan Pembayaran Dan Melakukan Upload Bukti Pembayaran Di Menu Profile -> Konfirmasi Pembayaran")
                                            .setTitleColor(Color.parseColor("#1297E0"))
                                            .setFirstButtonText("OKE")
                                            .setBackgroundColor(Color.parseColor("#FFFFFF"))
                                            .setFirstButtonColor(Color.parseColor("#1297E0"))
                                            .setFirstButtonTextColor(Color.parseColor("#FFFFFF"))
                                            .withFirstButtonListner(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startActivity(new Intent(CartActivity.this, MainActivity.class));
                                                    finish();
//                                                Toast.makeText(RegisterActivity.this, flatDialog.getFirstTextField(), Toast.LENGTH_SHORT).show();
                                                }
                                            })

                                            .show();
                                }
                            }, 3000L);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }

        }
    });
}

    private void initUI() {
        try {
            db.open();
            csr = db.getDetailBarang(Integer.parseInt(idBarang));
            for (int i = 0; i < csr.getCount(); i++) {
                csr.moveToPosition(i);
                item.setBarangId(Integer.parseInt(csr.getString(0)));
                item.setNamaBarang(csr.getString(1));
                item.setQty(csr.getString(2));
                item.setDesc(csr.getString(3));
                item.setHarga(csr.getString(4));
                item.setImagePath(csr.getString(5));
                Log.i("model", item.toString());
            }

            nama.setText(csr.getString(1));
            harga.setText("Rp. " + csr.getString(4));
            cartImage.setImageBitmap(ImageUtil.getImagefromPath(item.getImagePath()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int qtyItem = i;
}
