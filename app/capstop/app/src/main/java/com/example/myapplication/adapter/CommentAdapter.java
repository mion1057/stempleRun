package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.Comment;
import com.example.myapplication.data.Review;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemViewHolder>  {


    public interface OnItemClickListener{
        void onItemClick(View v, int position, Comment c);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        // ViewHolder 코드는 item.xml에 넣은 위젯의 갯수만큼 findId해서 객체생성해서 빼두는걸 한다.
        TextView name;
        TextView date;
        TextView content;
        View itemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

            name = itemView.findViewById(R.id.comment_name);
            date = itemView.findViewById(R.id.comment_date);
            content = itemView.findViewById(R.id.comment_content);

        }
    }

    private CommentAdapter.OnItemClickListener listener;
    private ArrayList<Comment> commentData;
    public CommentAdapter(ArrayList<Comment> commentData, CommentAdapter.OnItemClickListener listener) {
        this.commentData=commentData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 2. 지정한걸 view class로 만들어서
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        CommentAdapter.ItemViewHolder holder = new CommentAdapter.ItemViewHolder(view); // 3. viewHolder에게 넘겨준다 => viewHolder 생성자에 => 어떤item뷰를 쓸지 정한 view를 담아서
        return holder; // 4. 리턴한다.
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ItemViewHolder holder, int position) {
        Comment c = commentData.get(position);

        String str = String.format("%s", c.getWriter());
        holder.name.setText(str);

        // today
        Date currentTime = Calendar.getInstance().getTime();
        String today = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(currentTime);

        // db day
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Date date = c.getReg_date();

        String str2 = dateFormat.format(date);


        // 오늘 등록한 글이라면 시간으로 표시
        if(today.equals(str2)) {
            dateFormat = new  SimpleDateFormat("k:mm", Locale.getDefault());
            date = c.getReg_date();
            str2 = dateFormat.format(date);
        }

        holder.date.setText(str2);

        String str3 = String.format("%s", c.getContent());
        holder.content.setText(str3);


        holder.itemView.setOnClickListener(v->{
            listener.onItemClick(v, position, c);
        });

    }

    @Override
    public int getItemCount() {
        return (commentData == null) ? 0 : commentData.size();
    }

}
