package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.data.Member;
import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.example.myapplication.databinding.FragmentSignUpBinding;
import com.example.myapplication.network.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private Call<Member> request;
    private Call<Integer> appSignUpProc;
    private Member member;
    private Call<Integer> appIdCheck;
    private Call<Integer> appIdCheck2;
    private int check = 1;
    private String checkId;
    private String checkPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.idCheckBtn.setOnClickListener(v-> {
            Log.i("버튼클릭2", "버튼클릭2");
            appIdCheck2 = Server.getInstance().getApi().appIdCheck2(binding.editTextId.getText().toString());
            appIdCheck2.enqueue(new Callback<Integer>() {

                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    check = response.body();
                    if(check > 0) {
                        Toast.makeText(SignUpActivity.this, "사용할 수 없습니다.",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "사용할 수 있습니다.",Toast.LENGTH_SHORT).show();
                        checkId = binding.editTextId.getText().toString();
                    }
                    Log.i("성공2", "성공2" + check);
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.i("실패2", "실패2");
                }
            });

        });

        binding.signUpBtn.setOnClickListener(v-> {
            if(check > 0) {
                Toast.makeText(SignUpActivity.this, "중복체크해주세요",Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkId.equals(binding.editTextId.getText().toString()) == false) {
                check = 1;
                Toast.makeText(SignUpActivity.this, "중복체크해주세요",Toast.LENGTH_SHORT).show();
            }
            if(member == null) {
                member = new Member();
            }
            member.setM_id(binding.editTextId.getText().toString());
            member.setM_pw(binding.editTextPw.getText().toString());
            member.setM_name(binding.editTextName.getText().toString());
            member.setM_email(binding.editTextEmail.getText().toString());
            appSignUpProc = Server.getInstance().getApi().appSignUpProc(member);
            appSignUpProc.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.code() == 200) {
                        if(binding.editTextPw.getText().toString().equals(binding.editTextPwConfirm.getText().toString())) {
                            Log.i("회원가입성공", "회원가입성공");
                            Log.i("비밀번호1", "비밀번호1  " + binding.editTextPw.getText().toString());
                            Log.i("비밀번호2", "비밀번호2  " + binding.editTextPwConfirm.getText().toString());

                            finish();
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "비밀번호가 다릅니다.",Toast.LENGTH_SHORT).show();
                            Log.i("비밀번호11", "비밀번호1  " + binding.editTextPw.getText().toString());
                            Log.i("비밀번호22", "비밀번호2  " + binding.editTextPwConfirm.getText().toString());
                        }

                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.i("회원가입실패", "회원가입실패");
                    t.printStackTrace();
                }
            });
        });

    }
}