package com.example.myapplication.ui.bingo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.adapter.BingoAdapter;
import com.example.myapplication.R;
import com.example.myapplication.data.Bingo;
import com.example.myapplication.databinding.FragmentStorylistBinding;
import com.example.myapplication.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BingolistFragment extends BaseFragment implements BingoAdapter.OnItemClickListener {

    private FragmentStorylistBinding binding;
    private Call<List<Bingo>> request;
    private BingoAdapter adapter;

    public static BingolistFragment newInstance() {
        return new BingolistFragment();
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

        request = api.getBingoList(a_num);
        request.enqueue(new Callback<List<Bingo>>() {
            @Override
            public void onResponse(Call<List<Bingo>> call, Response<List<Bingo>> response) {
                if(response.code() == 200) {
                    Log.i("status", "성공");
                    List<Bingo> Bingos = response.body();

                    for(Bingo bingo : Bingos) {
                        Log.i("data", String.valueOf(bingo.getB_num()));
                        Log.i("data", bingo.getB_title());
                        Log.i("data", bingo.getB_content());
                    }
                    adapter = new BingoAdapter((ArrayList<Bingo>) Bingos, BingolistFragment.this::onItemClick);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // getParentFragment().getContext() => null이 넘어올수도 있따
                    // getActivity => null이 넘어올수잇다.  ==>>>> requireActivity => null이 넘어오지않는다.
                    binding.recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Bingo>> call, Throwable t) {
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
    public void onItemClick(View v, int position, Bingo b) {

        // 데이터도 담아서 보내기
        Log.i("check", "b_num = " + b.getB_num());
        Log.i("check", "title = " + b.getB_title());
        Log.i("check", "content = " + b.getB_content());

        model.b_num.setValue(b.getB_num());
        model.b_title.setValue(b.getB_title());
        model.b_content.setValue(b.getB_content());

        controller.navigate(R.id.action_nav_storyList_to_nav_storyInfo);
    }
}