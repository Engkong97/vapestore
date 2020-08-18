package coom.vapestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.ImageUtil;
import coom.vapestore.admin.PaymentSlipActivity;
import coom.vapestore.model.TrxModel;
import coom.vapestore.profile.DetailConfirmTrxActivity;

public class TrxAdminAdapter extends RecyclerView.Adapter<TrxAdminAdapter.ViewHolder> {
    Context context;
    List<TrxModel> list;
    DB_VAPE db;

    public TrxAdminAdapter(Context context, List<TrxModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TrxAdminAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trx, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TrxAdminAdapter.ViewHolder holder, final int position) {
        holder.nama.setText(list.get(position).getUserName());
        holder.namaBarang.setText(list.get(position).getNamBarang());
        holder.harga.setText((String.valueOf(list.get(position).getTotalHarga() / Double.valueOf(list.get(position).getJumlahBarang()))));
        holder.jumlah.setText(list.get(position).getJumlahBarang());
        holder.total.setText(String.valueOf(list.get(position).getTotalHarga()));
        final int status = Integer.parseInt(list.get(position).getStatus());
        if (status == 0) {
            holder.status.setText("Belum Melakukan Pembayaran");
        }else if (status == 1){
            holder.status.setText("Menunggu Di proses");
        }else if (status == 2){
            holder.status.setText("Reject, Pembayaran Tidak Valid");
        } else {
            holder.status.setText("Dikirim");
        }
        holder.img.setImageBitmap(ImageUtil.getImagefromPath(list.get(position).getBarangImage()));
        holder.trxCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 0) {
                    Toast.makeText(context, "Nothing cant be processed", Toast.LENGTH_SHORT).show();
                }else if (status == 1){
                    Intent intent = new Intent(context, PaymentSlipActivity.class);
                    intent.putExtra("TRXIDPAYMENT", list.get(position).getTrxId());
                    intent.putExtra("TRXIMAGE", list.get(position).getTrxImage());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Sudah Dikirim", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nama,namaBarang, harga, jumlah, total, status;
        ImageView img;
        CardView trxCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama_pemesan);
            namaBarang = itemView.findViewById(R.id.nama_barang_trx);
            harga = itemView.findViewById(R.id.harga_barang_trx);
            jumlah = itemView.findViewById(R.id.jmlh_barang_trx);
            total = itemView.findViewById(R.id.total_harga_trx);
            status = itemView.findViewById(R.id.status_trx);
            img = itemView.findViewById(R.id.img_uncorfm_trx);
            trxCard = itemView.findViewById(R.id.card_trx);

        }
    }
}
