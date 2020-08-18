package coom.vapestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.model.UserModel;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    List<UserModel> list;
    DB_VAPE db;

    public UserAdapter(Context context, List<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list_admin, parent, false);
        UserAdapter.ViewHolder viewHolder = new UserAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        holder.nama.setText(list.get(position).getNama());
        holder.userName.setText(list.get(position).getUserName());
        holder.noHp.setText(list.get(position).getNoHp());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,userName, noHp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.txt_list_nama);
            userName = itemView.findViewById(R.id.text_list_username);
            noHp = itemView.findViewById(R.id.text_list_noHp);
        }
    }
}
