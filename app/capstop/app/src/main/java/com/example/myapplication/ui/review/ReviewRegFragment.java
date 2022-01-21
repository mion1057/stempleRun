package com.example.myapplication.ui.review;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentReviewregBinding;
import com.example.myapplication.ui.BaseFragment;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRegFragment extends BaseFragment {

    private FragmentReviewregBinding binding;
    private Call<Integer> request;

    public static ReviewRegFragment newInstance() {
        return new ReviewRegFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewregBinding.inflate(inflater);
        return binding.getRoot(); //inflater.inflate(R.layout.fragment_reviewreg, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        binding.buttonSave.setOnClickListener(v-> {
            int check = binding.spinner.getSelectedItemPosition();
            Log.i("ㅁㄴㅇㄹ : ", String.valueOf(check));

            String category = "";

            if(check == 1) {
                category = "스토리";
            }
            else if (check == 2) {
                category = "빙고";
            }
            else {
                Toast.makeText(requireContext(), "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = binding.editTextTitle.getText().toString();
            String memo = binding.editTextMemo.getText().toString();
            String writer = model.login_name.getValue();

            request = api.review_insert(category, title, memo, writer);
            request.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.code() == 200) {
                        Log.i("ㅁㄴㅇㄹ : ", "게시글 등록 성공");
                        Toast.makeText(requireContext(), "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                        //model.check.setValue(1);

                        //controller.navigate(R.id.nav_review);
                        requireActivity().onBackPressed();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.i("ㅁㄴㅇㄹ : ", "게시글 등록 실패");
                }
            });

        });

        ArrayList<String> spinnerList = new ArrayList<String>();

        spinnerList.add("카테고리를 선택해주세요.");
        spinnerList.add("스토리");
        spinnerList.add("빙고");

        ArrayAdapter adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinner.setAdapter(adapter);
    }

}