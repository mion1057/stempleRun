package com.example.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.Gallery;

import java.util.List;

public class MainViewModel extends ViewModel {
    // MutableLiveData<> => observe를 사용할수잇다.
    // 배열은 MutableLiveData<List<Integer>> 로 사용
    // observe는 pointer(주소) 변경시 호출 => 배열 내 값변경으로는 호출이 안댐
    // 배열 내 값이 바뀐다면 새 배열에 값을 넣어서 호출

    public MutableLiveData<String> data = new MutableLiveData<>();
    public MutableLiveData<String> data2 = new MutableLiveData<>();

    // login
        public MutableLiveData<Integer> login_num = new MutableLiveData<>();
        public MutableLiveData<String> login_id = new MutableLiveData<>();
        public MutableLiveData<String> login_name = new MutableLiveData<>();
        public MutableLiveData<String> login_email = new MutableLiveData<>();
    //

    // story
    public MutableLiveData<Integer> a_num = new MutableLiveData<>();
    public MutableLiveData<Integer> s_num = new MutableLiveData<>();
    public MutableLiveData<String> s_title = new MutableLiveData<>();
    public MutableLiveData<String> s_content = new MutableLiveData<>();
    //

    // bingo
    public MutableLiveData<Integer> b_num = new MutableLiveData<>();
    public MutableLiveData<String> b_title = new MutableLiveData<>();
    public MutableLiveData<String> b_content = new MutableLiveData<>();

    //

    // review
    public MutableLiveData<Integer> r_num = new MutableLiveData<>();
    //

    // gallery
    public MutableLiveData<Integer> p_num = new MutableLiveData<>();
    public MutableLiveData<Gallery> gallery = new MutableLiveData<>();
    //
}
