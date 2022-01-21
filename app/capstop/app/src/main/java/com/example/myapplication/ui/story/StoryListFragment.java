package com.example.myapplication.ui.story;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.adapter.StoryAdapter;
import com.example.myapplication.R;
import com.example.myapplication.data.Story;
import com.example.myapplication.databinding.FragmentStorylistBinding;
import com.example.myapplication.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryListFragment extends BaseFragment implements StoryAdapter.OnItemClickListener {

    private FragmentStorylistBinding binding;
    private Call<List<Story>> request;
    private StoryAdapter adapter;

    public static StoryListFragment newInstance() {
        return new StoryListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStorylistBinding.inflate(inflater);
        return binding.getRoot(); //inflater.inflate(R.layout.fragment_storoylist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int a_num = model.a_num.getValue();
        Log.i("check", "a_ num ====== " + a_num);

        request = api.getStorys(a_num);
        request.enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if(response.code() == 200) {
                    Log.i("status", "성공");
                    List<Story> stories = response.body();

                    for(Story story : stories) {
                        Log.i("data", String.valueOf(story.getS_num()));
                        Log.i("data", story.getS_title());
                        Log.i("data", story.getS_content());
                        Log.i("data", String.valueOf(story.getS_complete()));
                        Log.i("data", String.valueOf(story.getS_progress()));
                    }
                    adapter = new StoryAdapter((ArrayList<Story>) stories, StoryListFragment.this::onItemClick);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // getParentFragment().getContext() => null이 넘어올수도 있따
                    // getActivity => null이 넘어올수잇다.  ==>>>> requireActivity => null이 넘어오지않는다.
                    binding.recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Log.i("status", "실패");

            }
        });
    }

    // 데이터를 불러와서 위젯을 바꿔야하는데 사용자가 back키를 눌러서 위젯이 없다면 nullpoint가 뜨는데
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(request != null) {
            request.cancel();
        }
    }

    @Override
    public void onItemClick(View v, int position, Story s) {

        // 데이터도 담아서 보내기
        Log.i("check", "s_num = " + s.getS_num());
        Log.i("check", "title = " + s.getS_title());
        Log.i("check", "content = " + s.getS_content());

        model.s_num.setValue(s.getS_num());
        model.s_title.setValue(s.getS_title());
        model.s_content.setValue(s.getS_content());

        controller.navigate(R.id.action_nav_storyList_to_nav_storyInfo);
    }
}