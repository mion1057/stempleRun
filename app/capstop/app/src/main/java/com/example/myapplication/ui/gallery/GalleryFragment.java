package com.example.myapplication.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.GalleryAdapter;
import com.example.myapplication.R;
import com.example.myapplication.data.Gallery;
import com.example.myapplication.ui.BaseFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends BaseFragment implements GalleryAdapter.OnItemClickListener {

    private Call<List<Gallery>> GalleryRequest;
    private GalleryAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.galleryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new GalleryAdapter(requireContext(), this);
        recyclerView.setAdapter(adapter);

        Button regiBut = view.findViewById(R.id.add_photo);
        Button popBut = view.findViewById(R.id.popular_but);
        Button lateBut = view.findViewById(R.id.latest_but);

        regiBut.setOnClickListener(v-> {
            NavController controller = Navigation.findNavController(view);
            controller.navigate(R.id.action_nav_gallery_to_addPhotoFragment);
        });

        popBut.setOnClickListener(v-> {

            GalleryRequest = api.getPopularList();
            GalleryRequest.enqueue(new Callback<List<Gallery>>() {
                @Override
                public void onResponse(Call<List<Gallery>> call, Response<List<Gallery>> response) {
                    if(response.code() == 200) {
                        List<Gallery> list = response.body();
                        adapter.updateData(list);
                        Log.i("성공2", "성공2");

                    }
                }
                @Override
                public void onFailure(Call<List<Gallery>> call, Throwable t) {
                    t.printStackTrace();
                    Log.i("실패","실패");

                }
            });
        });

        lateBut.setOnClickListener(v-> {

            GalleryRequest = api.getLatestList();
            GalleryRequest.enqueue(new Callback<List<Gallery>>() {
                @Override
                public void onResponse(Call<List<Gallery>> call, Response<List<Gallery>> response) {
                    if(response.code() == 200) {
                        List<Gallery> list = response.body();
                        adapter.updateData(list);
                        Log.i("성공2", "성공2");

                    }
                }
                @Override
                public void onFailure(Call<List<Gallery>> call, Throwable t) {
                    t.printStackTrace();
                    Log.i("실패","실패");

                }
            });
        });


        GalleryRequest = api.getGalleryList();
        GalleryRequest.enqueue(new Callback<List<Gallery>>() {
            @Override
            public void onResponse(Call<List<Gallery>> call, Response<List<Gallery>> response) {
                if(response.code() == 200) {
                    List<Gallery> list = response.body();
                    Log.e("dd", String.valueOf(list.get(1).getPic_latitude()));
                    adapter.updateData(list);
                    Log.i("성공2", "성공2");
                }
            }

            @Override
            public void onFailure(Call<List<Gallery>> call, Throwable t) {
                t.printStackTrace();
                Log.i("실패","실패");

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(GalleryRequest != null) {
            GalleryRequest.cancel();
        }
    }

    @Override
    public void onItemClick(View v, int position, Gallery g) {
        model.p_num.setValue(g.getP_num());
        model.gallery.setValue(g);

        Log.i("위도3", "위도3" + g.getPic_longitude());

        Gallery gg = new Gallery();
        gg = model.gallery.getValue();
        Log.i("#mid", "#mid" + g.getM_id());
        Log.i("위도2", " " + gg.getPic_longitude());
//        Log.i("###", "###" + gg.getP_title());
//        Log.i("###", "###" +g.getP_num());
//        Log.i("#title", "#title" + g.getP_title());
//        Log.i("#p_day", "#p_day" + g.getP_day());

        controller.navigate(R.id.action_nav_gallery_to_detailFragment);
    }
}