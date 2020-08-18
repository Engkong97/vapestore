package coom.vapestore.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
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

import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.admin.AddItemActivity;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class DetailConfirmTrxActivity extends AppCompatActivity {
    ImageView imgConfirm;
    String currentPhotoPath = "";
    CardView btnConfirmPay;
    DB_VAPE db;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_confirm_trx);

        imgConfirm = findViewById(R.id.imgBuktiPay);
        btnConfirmPay = findViewById(R.id.btnConfirmPay);

        Intent intent = getIntent();
        final String trcId = intent.getStringExtra("TRXID");

        try {
            db = new DB_VAPE(this);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        imgConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        btnConfirmPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfirm(currentPhotoPath, Integer.parseInt(trcId));
            }
        });
    }

    public void saveConfirm(String path, int trxId){
        try {
            db.open();
            db.confirmTrx(path, trxId);
            db.close();
            final FlatDialog flatDialog = new FlatDialog(DetailConfirmTrxActivity.this);
            flatDialog.setTitle("Sukses Upload Bukti Pembayaran")
                    .setTitleColor(Color.parseColor("#1297E0"))
                    .setFirstButtonText("OKE")
                    .setBackgroundColor(Color.parseColor("#FFFFFF"))
                    .setFirstButtonColor(Color.parseColor("#1297E0"))
                    .setFirstButtonTextColor(Color.parseColor("#FFFFFF"))
                    .withFirstButtonListner(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(DetailConfirmTrxActivity.this, ProfileActivity.class));
                            flatDialog.dismiss();
                            finish();
                        }
                    })

                    .show();
        } catch (SQLException e){
            e.printStackTrace();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Log.i("path", filePath);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            imgConfirm.setImageBitmap(selectedImage);
            try {
                savebitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
        File f = new File(path+File.separator+"VAPE_TRX_"+date+".jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        currentPhotoPath = f.getAbsolutePath();
        Log.i("FILEPATH", currentPhotoPath);
        return f;
    }
}
