package coom.vapestore.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;

import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.ImageUtil;

public class PaymentSlipActivity extends AppCompatActivity {

    ImageView img;
    CardView btnConfirm, btnReject;
    DB_VAPE db;
    Context context;
    int  trxId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_slip);

        img = findViewById(R.id.imgPaymentSlip);
        btnConfirm = findViewById(R.id.btnConfirmTrx);
        btnReject = findViewById(R.id.btnRejectTrx);

        try {
            db = new DB_VAPE(this);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Intent intent = getIntent();
        trxId = intent.getIntExtra("TRXIDPAYMENT", 0);
        String imagePath = intent.getStringExtra("TRXIMAGE");

        img.setImageBitmap(ImageUtil.getImagefromPath(imagePath));
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmTrx(trxId);
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectTrx(trxId);
            }
        });
    }

    private void confirmTrx(int trxId) {
        try {
            db.open();
            db.confirmTrxAdmibn(trxId);
            db.close();
            startActivity(new Intent(PaymentSlipActivity.this, OrderActivity.class));
            finish();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void rejectTrx(int trxId) {
        try {
            db.open();
            db.rejectTrxAdmin(trxId);
            db.close();
            startActivity(new Intent(PaymentSlipActivity.this, OrderActivity.class));
            finish();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
