package com.example.myapplication.ui.story;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentStoryinfoBinding;
import com.example.myapplication.ui.BaseFragment;

import java.util.zip.Inflater;

public class StoryInfoFragment extends BaseFragment {

    private FragmentStoryinfoBinding binding;

    public static StoryInfoFragment newInstance() {

        return new StoryInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStoryinfoBinding.inflate(inflater);
        return binding.getRoot(); //inflater.inflate(R.layout.fragment_storyinfo, container, false);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        binding.storyTitle.setText(model.s_title.getValue());
//        binding.storyContent.setText(model.s_content.getValue());
//
//        binding.start.setOnClickListener(v -> {
//            controller.navigate(R.id.action_nav_storyInfo_to_nav_story_start);
//        });
//
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.storyTitle.setText(model.s_title.getValue());
        binding.storyContent.setText(model.s_content.getValue());

        binding.start.setOnClickListener(v -> {
            controller.navigate(R.id.action_nav_storyInfo_to_nav_story_start);
        });
    }
}