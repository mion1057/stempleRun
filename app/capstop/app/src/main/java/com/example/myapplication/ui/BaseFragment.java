package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.MainViewModel;
import com.example.myapplication.network.Server;
import com.example.myapplication.network.ServerAPI;

public class BaseFragment extends Fragment {

    protected MainViewModel model;
    protected ServerAPI api;
    protected NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(MainViewModel.class); // 얘는 데이터
        controller = Navigation.findNavController(view);    // 얘는 페이지 이동
        api = Server.getInstance().getApi();    // 웹이랑 데이터 통신할 때 사용
    }
}
