package coom.vapestore.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import java.util.Date;

import coom.vapestore.BuildConfig;
import coom.vapestore.LoginActivity;
import coom.vapestore.R;
import coom.vapestore.RegisterActivity;
import coom.vapestore.Sqlite.DB_VAPE;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class AddItemActivity extends AppCompatActivity {

    EditText namaBarang, qtyBarang, descBarang, hargaBarang;
    Button btnSave;
    DB_VAPE db;
    Context context;
    ImageView addImage;
    static final int RESULT_LOAD_IMG = 1;
    String currentPhotoPath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        namaBarang = findViewById(R.id.tmbhNamaBarang);
        qtyBarang = findViewById(R.id.tmbhQty);
        descBarang = findViewById(R.id.tmbhDesc);
        btnSave = findViewById(R.id.btnSaveTmbh);
        hargaBarang = findViewById(R.id.tmbhHarga);
        addImage = findViewById(R.id.addImageInventory);

        context = AddItemActivity.this;

        try {
            db = new DB_VAPE(this);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        final ProgressDialog pd = new ProgressDialog(AddItemActivity.this);
        pd.setMessage("Loading...");

        addImage();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                if (TextUtils.isEmpty(namaBarang.getText().toString())) {
                    namaBarang.setError("Nama Barang Tidak Boleh Kosong");
                } else if (TextUtils.isEmpty(qtyBarang.getText().toString())) {
                    qtyBarang.setError("Quantity Barang Tidak Boleh Kosong");
                } else if (TextUtils.isEmpty(descBarang.getText().toString())) {
                    descBarang.setError("Deskripsi Barang Tidak Boleh Kosong");
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            try {
                                double harga = Double.valueOf(hargaBarang.getText().toString());
                                db.open();
                                db.insertBarang(namaBarang.getText().toString(), Integer.valueOf(qtyBarang.getText().toString()), descBarang.getText().toString(), harga, currentPhotoPath);
                                db.close();
                                final FlatDialog flatDialog = new FlatDialog(AddItemActivity.this);
                                flatDialog.setTitle("Sukses Menambahkan Barang")
                                        .setTitleColor(Color.parseColor("#1297E0"))
                                        .setFirstButtonText("OKE")
                                        .setBackgroundColor(Color.parseColor("#FFFFFF"))
                                        .setFirstButtonColor(Color.parseColor("#1297E0"))
                                        .setFirstButtonTextColor(Color.parseColor("#FFFFFF"))
                                        .withFirstButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                namaBarang.setText("");
                                                qtyBarang.setText("");
                                                descBarang.setText("");
                                                hargaBarang.setText("");
                                                Drawable drawable = context.getResources().getDrawable(R.drawable.image);
                                                final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                                                final Canvas canvas = new Canvas(bmp);
                                                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                                                drawable.draw(canvas);
                                                addImage.setImageBitmap(bmp);
                                                flatDialog.dismiss();
                                            }
                                        })

                                        .show();
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }, 3000L);

                }
            }
        });
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
        File path =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + "VAPE");
        if (!path.exists()){
            path.mkdirs();
        }
        File f = new File(path+File.separator+"VAPE_"+date+".jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        currentPhotoPath = f.getAbsolutePath();
        Log.i("FILEPATH", currentPhotoPath);
        return f;
    }
}
