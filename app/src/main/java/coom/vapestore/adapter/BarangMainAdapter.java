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

import coom.vapestore.DetailActivity;
import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.ImageUtil;
import coom.vapestore.model.BarangModel;

public class BarangMainAdapter extends RecyclerView.Adapter<BarangMainAdapter.ViewHolder> {
    Context context;
    List<BarangModel> list;
    DB_VAPE db;

    public BarangMainAdapter(Context context, List<BarangModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BarangMainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BarangMainAdapter.ViewHolder holder, final int position) {
        holder.nama.setText(list.get(position).getNamaBarang());
        holder.harga.setText("Rp. "+list.get(position).getHarga());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("IDBARANG", String.valueOf(list.get(position).getBarangId()));
                context.startActivity(intent);
            }
        });
        holder.img.setImageBitmap(ImageUtil.getImagefromPath(list.get(position).getImagePath()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,harga;
        CardView item;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.namaBarangMain);
            harga = itemView.findViewById(R.id.hargaBarangMain);
            item = itemView.findViewById(R.id.cardMain);
            img = itemView.findViewById(R.id.imgMain);
        }
    }
}
