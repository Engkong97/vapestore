package coom.vapestore.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.SharedPref;
import coom.vapestore.adapter.TrxAdapter;
import coom.vapestore.model.TrxModel;

public class HistoryTrxActivity extends AppCompatActivity {
    SharedPref pref;
    DB_VAPE db;
    Context context;
    List<TrxModel> list = new ArrayList<>();
    Cursor csr;
    TrxAdapter adapter;
    RecyclerView rvHistoryTrx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_trx);

        rvHistoryTrx = findViewById(R.id.rv_history_user);

        pref = new SharedPref(this);
        adapter = new TrxAdapter(this, list);

        try {
            db = new DB_VAPE(this);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        initUI(pref.getIdUser());
    }

    private void initUI(int idUser) {
        try{
            db.open();
            csr = db.getHistoryUser(idUser);
            for (int i = 0; i<csr.getCount(); i++){
                csr.moveToPosition(i);
                TrxModel trx = new TrxModel();
                trx.setTrxId(Integer.parseInt(csr.getString(0)));
                trx.setUserName(csr.getString(7));
                trx.setNamBarang(csr.getString(1));
                trx.setUserId(pref.getIdUser());
                trx.setJumlahBarang(csr.getString(2));
                trx.setTotalHarga(Double.valueOf(csr.getString(3)));
                trx.setAlamat(csr.getString(4));
                int status = Integer.parseInt(csr.getString(5));
                if (status == 0) {
                    trx.setStatus("Belum Melakukan Pembayaran");
                }else if (status == 1){
                    trx.setStatus("Sedang Di Proses");
                } else if (status == 2) {
                    trx.setStatus("Reject, Pembayaran Tidak Valid");
                }else {
                    trx.setStatus("Dikirim");
                }
                trx.setBarangImage(csr.getString(10));
                list.add(trx);
            }

            adapter.notifyDataSetChanged();
            rvHistoryTrx.setLayoutManager(new LinearLayoutManager(this));
            rvHistoryTrx.setHasFixedSize(true);
            rvHistoryTrx.setAdapter(adapter);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
