package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.data.User;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.network.Server;
import com.example.myapplication.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {

    private Call<List<User>> List;
    private Call<User> List2;

    private FragmentHomeBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot(); //inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List = api.getList();
        List.enqueue(new Callback<List<User>>() {

            // 404든 500이든 얘가 불린다.
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                // 정상일떈 여기서
                if(response.code()==200) {
                    List<User> users = response.body();
                    for(User a : users) {
                        Log.i("asdf", a.email);
                    }
                }
            }
            //  에러면 여기
            @Override
            public void onFailure(Call<java.util.List<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        List2 = api.getUser(1);
        List2.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200) {
                    User user = response.body();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                // 토스트로 메세지 보내기
            }
        });

    }

    // 데이터를 불러와서 위젯을 바꿔야하는데 사용자가 back키를 눌러서 위젯이 없다면 nullpoint가 뜨는데
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(List != null) {
            List.cancel();
        }

    }
}