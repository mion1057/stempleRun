package com.example.myapplication.ui.idea;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.data.Review;
import com.example.myapplication.databinding.FragmentIdeaBinding;
import com.example.myapplication.ui.BaseFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdeaFragment extends BaseFragment {

    private FragmentIdeaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentIdeaBinding.inflate(inflater);
        return binding.getRoot(); //inflater.inflate(R.layout.fragment_idea, container, false);
    }

}