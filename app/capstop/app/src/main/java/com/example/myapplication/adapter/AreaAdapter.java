package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Area;

import java.util.ArrayList;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ItemViewHolder>  {


    public interface OnItemClickListener{
        void onItemClick(View v, int position, Area a);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        // ViewHolder 코드는 item.xml에 넣은 위젯의 갯수만큼 findId해서 객체생성해서 빼두는걸 한다.
        TextView textView;
        View itemView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            textView = itemView.findViewById(R.id.textViewTitle);
        }
    }

    private AreaAdapter.OnItemClickListener listener;
    private ArrayList<Area> areaData;
    public AreaAdapter(ArrayList<Area> areaData, AreaAdapter.OnItemClickListener listener) {
        this.areaData=areaData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AreaAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 2. 지정한걸 view class로 만들어서
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        AreaAdapter.ItemViewHolder holder = new AreaAdapter.ItemViewHolder(view); // 3. viewHolder에게 넘겨준다 => viewHolder 생성자에 => 어떤item뷰를 쓸지 정한 view를 담아서
        return holder; // 4. 리턴한다.
    }

    @Override
    public void onBindViewHolder(@NonNull AreaAdapter.ItemViewHolder holder, int position) {
        Area a = areaData.get(position);
        String str = String.format("%s", a.getArea_name());
        holder.textView.setText(str);
        holder.itemView.setOnClickListener(v->{
            listener.onItemClick(v, position, a);
        });

    }

    @Override
    public int getItemCount() {
        return (areaData == null) ? 0 : areaData.size();
    }

}
