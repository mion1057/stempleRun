package com.example.myapplication.ui.signup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.data.Member;
import com.example.myapplication.databinding.FragmentSignUpBinding;
import com.example.myapplication.network.Server;
import com.example.myapplication.ui.BaseFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends BaseFragment {

    private FragmentSignUpBinding binding;
    private Call<Member> request;
    private Call<Integer> appSignUpProc;
    private Member member;
    private Call<Integer> appIdCheck;
    private Call<Integer> appIdCheck2;
    private int check = 1;
    private String checkId;
    private String checkPw;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.idCheckBtn.setOnClickListener(v-> {
            Log.i("버튼클릭2", "버튼클릭2");
           appIdCheck2 = Server.getInstance().getApi().appIdCheck2(binding.editTextId.getText().toString());
           appIdCheck2.enqueue(new Callback<Integer>() {

               @Override
               public void onResponse(Call<Integer> call, Response<Integer> response) {
                   check = response.body();
                   if(check > 0) {
                       Toast.makeText(requireContext(), "사용할 수 없습니다.",Toast.LENGTH_SHORT).show();
                   } else {
                       Toast.makeText(requireContext(), "사용할 수 있습니다.",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(requireContext(), "중복체크해주세요",Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkId.equals(binding.editTextId.getText().toString()) == false) {
                check = 1;
                Toast.makeText(requireContext(), "중복체크해주세요",Toast.LENGTH_SHORT).show();
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
                            NavController controller = Navigation.findNavController(view);
                            controller.navigate(R.id.nav_story_area);
                        }
                        else {
                            Toast.makeText(requireContext(), "비밀번호가 다릅니다.",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(appSignUpProc != null) {
            appSignUpProc.cancel();
        }
    }
}