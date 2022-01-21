package com.example.myapplication;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.network.Server;
import com.example.myapplication.network.ServerAPI;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ServerAPI api;
    private Call<List<String>> request;

    private List<String> data;

    protected NavController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signUpButton.setOnClickListener(v-> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        binding.loginButton.setOnClickListener(v-> {
            String id = binding.id.getText().toString();
            String pw = binding.password.getText().toString();

            Log.i("login", id);
            Log.i("login", pw);
            api = Server.getInstance().getApi();
            request = api.appLogin(id, pw);
            request.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    Log.i("login : ", "1");
                    if(response.code() == 200) {
                        Log.i("login : ", "2");
                        data = response.body();
                        Log.i("login : ", "3");
                        for(String s : data) {
                            Log.i("login", s);
                        }

                        // 아이디 비밀번호가 맞다면
                        if(data.size() != 1) {

                            SharedPreferences login_data = getSharedPreferences("login_data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = login_data.edit();

                            String loginNum = "";
                            String loginId = "";
                            String loginPw = "";
                            String loginName = "";
                            String loginEmail = "";

                            // 자동 로그인을 체크 햇다면
                            if(binding.checkBox.isChecked()) {

                                Log.i("edit", data.get(0));
                                editor.putString("num", data.get(0));
                                Log.i("edit", data.get(1));
                                editor.putString("id", data.get(1));
                                Log.i("edit", data.get(2));
                                editor.putString("pw", data.get(2));
                                Log.i("edit", data.get(3));
                                editor.putString("name", data.get(3));
                                Log.i("edit", data.get(4));
                                editor.putString("email", data.get(4));

                                editor.apply();

                                loginNum = login_data.getString("num", null);
                                loginId = login_data.getString("id", null);
                                loginPw = login_data.getString("pw", null);
                                loginName = login_data.getString("name", null);
                                loginEmail = login_data.getString("email", null);

                                Log.i("edit : ", loginNum);
                                Log.i("edit : ", loginId);
                                Log.i("edit : ", loginPw);
                                Log.i("edit : ", loginName);
                                Log.i("edit : ", loginEmail);
                            }
                            // 자동 로그인을 체크 안햇다면
                            else {
//                                loginNum = login_data.getString("num", null);
//                                loginId = login_data.getString("id", null);
//                                loginName = login_data.getString("name", null);

                                loginNum = data.get(0);
                                loginId = data.get(1);
                                loginName = data.get(3);
                                loginEmail = data.get(4);

                                if(loginId != null) {
                                    editor.remove("num");
                                    editor.remove("id");
                                    editor.remove("pw");
                                    editor.remove("name");
                                    editor.remove("email");

                                    editor.apply();
                                }
                            }
                            Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent();
                            intent.putExtra("num", loginNum);
                            intent.putExtra("id", loginId);
                            intent.putExtra("name", loginName);
                            intent.putExtra("email", loginEmail);
                            setResult(Activity.RESULT_OK, intent);

                            Toast.makeText(LoginActivity.this, loginName + "님 환영합니다.", Toast.LENGTH_SHORT).show();

                            finish();

                        }
                        // 아이디 비밀번호가 틀렸다면
                        else {
                            Toast.makeText(LoginActivity.this, data.get(0), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    Log.i("login", "failed");
                    Log.i("login", "4");
                }
            });
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);						// 태스크를 백그라운드로 이동
        finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
        android.os.Process.killProcess(android.os.Process.myPid());	// 앱 프로세스 종료
    }
}