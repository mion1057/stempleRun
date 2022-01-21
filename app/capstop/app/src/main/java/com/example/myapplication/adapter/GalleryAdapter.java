package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.Gallery;
import com.example.myapplication.network.Server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private List<Gallery> data;
    private Context context;
    private OnItemClickListener listener;

    public void updateData(List<Gallery> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public GalleryAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position, Gallery g);
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {

        Gallery g = data.get(position);
        String path = Server.URL +g.getPic_file().replace("\\", "/");
        Glide.with(context).load(path).into(holder.imageView);

        // 날짜 형변환
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        Date date = g.getP_day();
        String strDate = dateFormat.format(date);

        // int형 string으로
        String reco = (String.valueOf(g.getP_recommend()));

        holder.textViewTitle.setText(g.getP_title());
        holder.textViewName.setText(g.getM_name());
        holder.textViewDate.setText(strDate);
        holder.textViewReco.setText("추천수  " + reco);
        holder.itemView.setOnClickListener(v-> {
            listener.onItemClick(v, position, g);
        });
    }

    @Override
    public int getItemCount() {
        return data==null? 0:data.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewName;
        TextView textViewReco;
        TextView textViewDate;
        ImageView imageView;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewReco = itemView.findViewById(R.id.textViewReco);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
