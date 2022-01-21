package com.example.myapplication.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.myapplication.adapter.GalleryAdapter;
import com.example.myapplication.R;
import com.example.myapplication.data.Gallery;
import com.example.myapplication.databinding.FragmentDetailBinding;
import com.example.myapplication.network.Server;
import com.example.myapplication.ui.BaseFragment;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends BaseFragment {

    private FragmentDetailBinding binding;
    private Call<Integer> request;
    private GalleryAdapter adapter;
    private Context context;
    private Call<Integer> requestReco;
    private Call<Integer> checkReco;
    private int check = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        binding = FragmentDetailBinding.inflate(inflater);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Gallery gg = new Gallery();
        gg = model.gallery.getValue();

        MapView mapView = new MapView(requireActivity());

        ViewGroup mapViewContainer = view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(gg.getPic_latitude()), Double.parseDouble(gg.getPic_longitude())), true);

        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(Double.parseDouble(gg.getPic_latitude()), Double.parseDouble(gg.getPic_longitude()));
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);

        mapView.addPOIItem(marker);

        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        Date date = gg.getP_day();
        String strDate = dateFormat.format(date);

        String path = Server.URL + gg.getPic_file().replace("\\", "/");
        Glide.with(requireContext()).load(path).into(binding.postPhoto);


        Log.i("$title", "$title" + gg.getP_title());
        Log.i("#date", "#date" + strDate);
        Log.i("위도", " " + gg.getPic_latitude());
        Log.i("경도", " " + gg.getPic_longitude());
        binding.postTitle.setText("제목  " + gg.getP_title());
        binding.postWriter.setText("작성자  " + gg.getM_name());
        binding.postDate.setText(strDate);
        binding.recoBtn.setText("추천하기  " + gg.getP_recommend());


        checkReco = Server.getInstance().getApi().checkReco(gg.getP_num(), "123456");
        checkReco.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                check = response.body();
                Log.i("check", "" + check);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }

        });

        final int p_num = gg.getP_num();
        binding.recoBtn.setOnClickListener(v-> {
            if(check > 0){
                Toast.makeText(requireContext(), "이미 추천했습니다.",Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(requireContext(), "추천.",Toast.LENGTH_SHORT).show();
            }

            requestReco = Server.getInstance().getApi().addReco(p_num, "123456");
            requestReco.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    int result = response.body();
                    Log.i("check", "result reco " + result);
                    model.gallery.getValue().setP_recommend(result);
                    binding.recoBtn.setText("추천하기 " + model.gallery.getValue().getP_recommend());
                }


                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(requestReco != null) {
            requestReco.cancel();
        }
        if(request != null) {
            request.cancel();
        }
    }

}