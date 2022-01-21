package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.StoryAdapter;
import com.example.myapplication.data.Bingo;
import com.example.myapplication.data.Story;

import java.util.ArrayList;


public class BingoAdapter extends RecyclerView.Adapter<BingoAdapter.ItemViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(View v, int position, Bingo b);
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

    private BingoAdapter.OnItemClickListener listener;
    private ArrayList<Bingo> BingoData;
    public BingoAdapter(ArrayList<Bingo> BingoData, BingoAdapter.OnItemClickListener listener) {
        this.BingoData=BingoData;
        this.listener = listener;
    }

    @NonNull
    @Override   // 화면을 필요한 갯수만큼 미리 만들어 둔다. 갯수는 listView 크기랑 각 항목 itemView 크기를 계산해서
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1. item 뷰를 머쓸지를 지정
        // 2. 지정한걸 view class로 만들어서
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ItemViewHolder holder = new BingoAdapter.ItemViewHolder(view); // 3. viewHolder에게 넘겨준다 => viewHolder 생성자에 => 어떤item뷰를 쓸지 정한 view를 담아서
        return holder; // 4. 리턴한다.
    }

    @Override   // 화면에 나갈 차례가 되어서 데이터랑 뷰랑 합쳐주는 코드
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {    // position => 전체 리스트중에 몇번째 화면을 그릴차례인지 가르쳐주는 것
        Bingo b = BingoData.get(position);
        String str = String.format("%s", b.getB_title());
        holder.textView.setText(str);
        holder.itemView.setOnClickListener(v->{
            listener.onItemClick(v, position, b);
        });
    }

    @Override   // 아이템 갯수
    public int getItemCount() {
        return (BingoData == null) ? 0 : BingoData.size();
    }

    public void setData(ArrayList<Bingo> data) {
        this.BingoData = data;
    }
}
