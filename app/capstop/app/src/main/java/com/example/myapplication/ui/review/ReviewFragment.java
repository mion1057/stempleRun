package com.example.myapplication.ui.review;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ReviewAdapter;
import com.example.myapplication.adapter.StoryAdapter;
import com.example.myapplication.data.Review;
import com.example.myapplication.data.Story;
import com.example.myapplication.databinding.FragmentReviewBinding;
import com.example.myapplication.ui.BaseFragment;
import com.example.myapplication.ui.story.StoryListFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends BaseFragment implements ReviewAdapter.OnItemClickListener {

    private FragmentReviewBinding binding;
    private Call<List<Review>> request;
    private Call<Integer> request2;
    private ReviewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater);
        return binding.getRoot(); //inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.regReview.setOnClickListener(v-> {
            controller.navigate(R.id.action_nav_review_to_nav_reviewReg);
        });

        request = api.getBoards();
        request.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if(response.code() == 200) {

                    List<Review> List = response.body();

                    adapter = new ReviewAdapter((ArrayList<Review>) List, ReviewFragment.this::onItemClick);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // getParentFragment().getContext() => null이 넘어올수도 있따
                    // getActivity => null이 넘어올수잇다.  ==>>>> requireActivity => null이 넘어오지않는다.
                    binding.recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Log.i("씨발 : ", "시발");
            }
        });

    }

    @Override
    public void onItemClick(View v, int position, Review r) {

        request2 = api.hits(r.getBno());
        request2.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.i("ㅁㄴㅇㄹ : ", "조회수 성공");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.i("ㅁㄴㅇㄹ : ", "조회수 실패");
            }
        });
        // 데이터도 담아서 보내기
        model.r_num.setValue(r.getBno());

        controller.navigate(R.id.action_nav_review_to_nav_reviewDetail);
    }

}