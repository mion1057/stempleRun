package com.example.myapplication.ui.bingo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.data.Area;
import com.example.myapplication.databinding.FragmentBingoAreaBinding;
import com.example.myapplication.ui.BaseFragment;

import net.daum.mf.map.api.MapPOIItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//빙고 지역데이터 불러오기
public class BingoareaFragment extends BaseFragment implements com.example.myapplication.adapter.AreaAdapter.OnItemClickListener {
    private FragmentBingoAreaBinding binding;
    private com.example.myapplication.adapter.AreaAdapter adapter;
    private Call<List<Area>> request;

    public static BingoareaFragment newInstance() {
        return new BingoareaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                  @Nullable Bundle savedInstanceState) {
        binding = FragmentBingoAreaBinding.inflate(inflater);
        return binding.getRoot(); //inflater.inflate(R.layout.fragment_story_area, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("ㅁㅁ", "실행중");

        request = api.getbingoAreas();
        request.enqueue(new Callback<List<Area>>() {

            // 404든 500이든 얘가 불린다.
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {

                // 정상일떈 여기서
                if(response.code() == 200) {

                    Log.i("ㅁㅁ", "성공");

                    List<Area> areas = response.body();

                    for (Area area : areas) {
                        Log.i("data", String.valueOf(area.getArea_num()));
                        Log.i("data", area.getArea_name());
                    }

                    adapter = new com.example.myapplication.adapter.AreaAdapter((ArrayList<Area>) areas, BingoareaFragment.this::onItemClick);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // getParentFragment().getContext() => null이 넘어올수도 있따
                    // getActivity => null이 넘어올수잇다.  ==>>>> requireActivity => null이 넘어오지않는다.
                    binding.recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                Log.i("ㅁㅁ", "실패");
                t.printStackTrace();
                // 토스트로 메세지 보내기
            }
        });
//        request.enqueue(new Callback<java.util.List<Area>>() {
//
//            // 404든 500이든 얘가 불린다.
//            @Override
//            public void onResponse(Call<java.util.List<Area>> call, Response<java.util.List<Area>> response) {
//                Log.i("ㅁㅁ", "여긴 성공");
//                // 정상일떈 여기서
//                if(response.code()==200) {
//                    List<Area> areas = response.body();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<java.util.List<Area>> call, Throwable t) {
//                Log.i("ㅁㅁ", "여긴 실패");
//                t.printStackTrace();
//            }
//        });

//        List2 = Server.getInstance().getApi().getUser(1);
//        List2.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if(response.code() == 200) {
//                    User user = response.body();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                t.printStackTrace();
//                // 토스트로 메세지 보내기
//            }
//        });

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
    public void onItemClick(View v, int position, Area a) {

//        List<Integer> asdf = model.a.getValue();
//        if(asdf == null) {
//            asdf = new ArrayList<>();
//        }
//        asdf.add(1);
//        asdf.add(2);
//        model.a.setValue(asdf);

        // 데이터도 담아서 보내기
        Log.i("check", "a_num = " + a.getArea_num());
        model.a_num.setValue(a.getArea_num());


//        controller.popBackStack(); // fragment 를 닫는거 =< activity에선 finish();
//        controller.popBackStack()
        controller.navigate(R.id.action_nav_storyarea_to_nav_story_list);



        // 누르면 스토리 제목 설명 을 보여주고 버튼으로 시작하기만들어줌
//        Intent intent = new Intent(this, StoryActivity.class);
//
//        intent.putExtra("a_num", a.getArea_num());
//
//        startActivity(intent);
    }
}