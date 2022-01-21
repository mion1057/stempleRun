package com.example.myapplication.ui.story;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.MainViewModel;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentStoryEndBinding;
import com.example.myapplication.ui.BaseFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryEndFragment extends BaseFragment {

    private FragmentStoryEndBinding binding;

    private Call<Integer> request;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_story_end, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
//        model.data2.observe(getViewLifecycleOwner(),str->{
//
//        });

        request = api.storyEnd(model.login_num.getValue(), model.s_num.getValue());
        request.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200) {
                    Log.i("ㅁㄴㅇㄹ", "성공");

                    int i = response.body();

                    if(i == 1) {
                        Log.i("ㅁㄴㅇㄹ", "등록 성공");
                    }
                    else {
                        Log.i("ㅁㄴㅇㄹ", "등록 실패");
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("축하합니다.").setMessage("스토리를 모두 완료하였습니다.");

                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // back stack 제거 => 근데 화면은 첫화면 그대론데 위에 글자만 바뀜
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.popBackStack (null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            controller.navigate(R.id.nav_story_area);
                        }
                    });

                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();

                    //controller.navigate(R.id.nav_story_area);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.i("ㅁㄴㅇㄹ", "실패");
            }
        });

    }
}