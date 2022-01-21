package com.example.myapplication.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Review;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ItemViewHolder>  {


    public interface OnItemClickListener{
        void onItemClick(View v, int position, Review r);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        // ViewHolder 코드는 item.xml에 넣은 위젯의 갯수만큼 findId해서 객체생성해서 빼두는걸 한다.
        TextView title;
        TextView name;
        TextView date;
        TextView hits;
        TextView recommend;
        View itemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.boardTitle);
            name = itemView.findViewById(R.id.boardName);
            date = itemView.findViewById(R.id.boardDate);
            hits = itemView.findViewById(R.id.boardHits);
            recommend = itemView.findViewById(R.id.boardRecommend);
        }
    }

    private ReviewAdapter.OnItemClickListener listener;
    private ArrayList<Review> reviewData;
    public ReviewAdapter(ArrayList<Review> reviewData, ReviewAdapter.OnItemClickListener listener) {
        this.reviewData=reviewData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 2. 지정한걸 view class로 만들어서
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        ReviewAdapter.ItemViewHolder holder = new ReviewAdapter.ItemViewHolder(view); // 3. viewHolder에게 넘겨준다 => viewHolder 생성자에 => 어떤item뷰를 쓸지 정한 view를 담아서
        return holder; // 4. 리턴한다.
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ItemViewHolder holder, int position) {
        Review r = reviewData.get(position);

        String str = String.format("%s", r.getTitle());
        holder.title.setText(str);

        String str2 = String.format("%s", r.getWriter());
        holder.name.setText(str2);


        // today
        Date currentTime = Calendar.getInstance().getTime();
        String today = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(currentTime);

        // db day
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Date date = r.getNal();

        String str3 = dateFormat.format(date);


        // 오늘 등록한 글이라면 시간으로 표시
        if(today.equals(str3)) {
            dateFormat = new  SimpleDateFormat("k:mm", Locale.getDefault());
            date = r.getNal();
            str3 = dateFormat.format(date);
        }

        holder.date.setText(str3);

        String str4 = String.format("%s", r.getA_chu());
        holder.hits.setText(str4);

        String str5 = String.format("%s", r.getC_num());
        holder.recommend.setText(str5);

        holder.itemView.setOnClickListener(v->{
            listener.onItemClick(v, position, r);
        });

    }

    @Override
    public int getItemCount() {
        return (reviewData == null) ? 0 : reviewData.size();
    }

}
