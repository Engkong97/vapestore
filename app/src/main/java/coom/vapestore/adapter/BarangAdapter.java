package coom.vapestore.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.ImageUtil;
import coom.vapestore.admin.UpdateBarangActivity;
import coom.vapestore.model.BarangModel;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.ViewHolder> {
    Context context;
    List<BarangModel> list;
    DB_VAPE db;

    public BarangAdapter(Context context, List<BarangModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BarangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_barang_admin, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BarangAdapter.ViewHolder holder, final int position) {
        holder.nama.setText(list.get(position).getNamaBarang());
        holder.qty.setText(list.get(position).getQty());
        holder.desc.setText(list.get(position).getDesc());
        holder.harga.setText(list.get(position).getHarga());
        holder.img.setImageBitmap(ImageUtil.getImagefromPath(list.get(position).getImagePath()));
        holder.btnDetailBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateBarangActivity.class);
                intent.putExtra("IDBARANGUPDATE", String.valueOf(list.get(position).getBarangId()));
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nama,qty,desc, harga;
        ImageView img;
        CardView btnDetailBarang;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.text_nama_barang);
            qty = itemView.findViewById(R.id.text_qty_barang);
            desc = itemView.findViewById(R.id.text_desc_barang);
            harga = itemView.findViewById(R.id.text_harga_barang);
            img = itemView.findViewById(R.id.imgInventory);
            btnDetailBarang = itemView.findViewById(R.id.cardBarang);

        }
    }
}
