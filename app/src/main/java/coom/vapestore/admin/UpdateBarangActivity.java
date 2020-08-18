package coom.vapestore.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flatdialoglibrary.dialog.FlatDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.ImageUtil;
import coom.vapestore.model.BarangModel;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class UpdateBarangActivity extends AppCompatActivity {

    EditText namaBarang, qtyBarang, descBarang, hargaBarang;
    Button btnSave;
    DB_VAPE db;
    Context context;
    ImageView addImage;
    static final int RESULT_LOAD_IMG = 1;
    String currentPhotoPath = "";
    List<BarangModel> list = new ArrayList<>();
    Cursor csr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_barang);

        namaBarang = findViewById(R.id.tmbhNamaBarangUpdate);
        qtyBarang = findViewById(R.id.tmbhQtyUpdate);
        descBarang = findViewById(R.id.tmbhDescUpdate);
        btnSave = findViewById(R.id.btnSaveTmbhUpdate);
        hargaBarang = findViewById(R.id.tmbhHargaUpdate);
        addImage = findViewById(R.id.addImageInventoryUpdate);

        try {
            db = new DB_VAPE(this);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Intent intent = getIntent();
        final String idBarang = intent.getStringExtra("IDBARANGUPDATE");

        final ProgressDialog pd = new ProgressDialog(UpdateBarangActivity.this);
        pd.setMessage("Loading...");

        addImage();
        initUI(Integer.parseInt(idBarang));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        updateBarang(namaBarang.getText().toString(), qtyBarang.getText().toString(), descBarang.getText().toString(), hargaBarang.getText().toString(), currentPhotoPath, Integer.parseInt(idBarang));
                    }
                }, 2000L);
            }
        });

    }

    private void updateBarang(String namaBarangUpdate, String qtyBarangUpdate, String descBarangUpdate, String hargaBarangUpdate, String currentPhotoPath, int idBarang) {
        try {
            db.open();
            db.updateBarang(namaBarangUpdate, Integer.parseInt(qtyBarangUpdate), descBarangUpdate, Double.parseDouble(hargaBarangUpdate), currentPhotoPath, idBarang);
            final FlatDialog flatDialog = new FlatDialog(UpdateBarangActivity.this);
            flatDialog.setTitle("Sukses Update Barang")
                    .setTitleColor(Color.parseColor("#1297E0"))
                    .setFirstButtonText("OKE")
                    .setBackgroundColor(Color.parseColor("#FFFFFF"))
                    .setFirstButtonColor(Color.parseColor("#1297E0"))
                    .setFirstButtonTextColor(Color.parseColor("#FFFFFF"))
                    .withFirstButtonListner(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            namaBarang.setText("");
//                            qtyBarang.setText("");
//                            descBarang.setText("");
//                            hargaBarang.setText("");
                            flatDialog.dismiss();
                            startActivity(new Intent(UpdateBarangActivity.this, InventoryActivity.class));
                            finish();
                        }
                    })

                    .show();
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initUI(int idBarang) {
        list.clear();
        try {
            db.open();
            csr = db.getDetailBarang(idBarang);
            for (int i = 0; i < csr.getCount(); i++) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        namaBarang.setText(list.get(0).getNamaBarang());
        qtyBarang.setText(list.get(0).getQty());
        descBarang.setText(list.get(0).getDesc());
        hargaBarang.setText(list.get(0).getHarga());
        currentPhotoPath = list.get(0).getImagePath();
        addImage.setImageBitmap(ImageUtil.getImagefromPath(currentPhotoPath));

    }

    public void addImage() {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Log.i("path", filePath);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            addImage.setImageBitmap(selectedImage);
            try {
                savebitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void takePicture() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent intent = new Intent(this, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, false);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
        startActivityForResult(intent, 123);
    }

    public File savebitmap(Bitmap bmp) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMdd_HHmmss");
        String date = simpleDateFormat.format(new Date());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + "VAPE");
        if (!path.exists()) {
            path.mkdirs();
        }
        File f = new File(path + File.separator + "VAPE_" + date + ".jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        currentPhotoPath = f.getAbsolutePath();
        Log.i("FILEPATH", currentPhotoPath);
        return f;
    }
}
