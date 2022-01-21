package com.example.myapplication.ui.review;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CommentAdapter;
import com.example.myapplication.adapter.ReviewAdapter;
import com.example.myapplication.data.Comment;
import com.example.myapplication.data.Review;
import com.example.myapplication.databinding.FragmentReviewdetailBinding;
import com.example.myapplication.ui.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDetailFragment extends BaseFragment {

    private FragmentReviewdetailBinding binding;
    private Call<Review> request;
    private Call<List<Comment>> request2;
    private Call<Integer> request3;
    private Call<Integer> request4;
    private CommentAdapter adapter;

    public static ReviewDetailFragment newInstance() {
        return new ReviewDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewdetailBinding.inflate(inflater);
        return binding.getRoot(); //inflater.inflate(R.layout.fragment_reviewdetail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        binding.detailCommentButton.setOnClickListener(v-> {
            request4 = api.comment_insert(model.r_num.getValue(), binding.detailCommentText.getText(), model.login_name.getValue());
            request4.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.code() == 200) {
                        Log.i("ㅁㄴㅇㄹ : ", "성공");

                        FragmentTransaction ft = getFragmentManager().beginTransaction();

                        ft.detach(ReviewDetailFragment.this).attach(ReviewDetailFragment.this).commit();

                        binding.detailCommentText.setText("");
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.i("ㅁㄴㅇㄹ : ", "실패");
                }
            });
        });

        binding.detailRecommendButton.setOnClickListener(v-> {
            Toast.makeText(requireContext(),"추천되었습니다.", Toast.LENGTH_SHORT).show();
            request3 = api.recommend(model.r_num.getValue());
            request3.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    Log.i("ㅁㄴㅇㄹ : ", "추천 성공");
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.i("ㅁㄴㅇㄹ : ", "추천 실패");
                }
            });
        });

        request = api.getReview(model.r_num.getValue());
        request.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if(response.code() == 200) {
                    Review r = response.body();
                    Log.i("ㅁㄴㅇㄹ : ", r.getMemo());

                    Log.i("ㅁㄴㅇㄹ : ",  String.valueOf(r.getNal()));

                    binding.detailTitle.setText(r.getTitle());
                    binding.detailName.setText(r.getWriter());

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

                    binding.detailDate.setText(str3);

                    binding.detailContent.setText(r.getMemo());
                }
            }
            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Log.i("ㅁㄴㅇㄹ : ", "실패");
            }
        });

        request2 = api.getComments(model.r_num.getValue());
        request2.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.code() == 200) {
                    List<Comment> List = response.body();

                    if(List != null) {
                        adapter = new CommentAdapter((ArrayList<Comment>) List, null);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

                        binding.recyclerView.setAdapter(adapter);

                        binding.detailCommentCount.setText("" + List.size());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }

}