package com.example.myapplication.ui.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.network.Server;
import com.example.myapplication.ui.BaseFragment;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddPhotoFragment extends BaseFragment {

    private static final int REQ_CODE = 1;
    private ImageView imageView;
    private Uri uri;
    private String fileName;
    private TextView textView;
    private Call<Void> request;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_photo, container, false);

        //지도
        MapView mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        // 중심점 변경 - 예제 좌표는 서울 남산
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.54892296550104, 126.99089033876304), true);

        // 줌 레벨 변경
        mapView.setZoomLevel(4, true);

        //마커 찍기
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.54892296550104, 126.99089033876304);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);

        return v;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addBtn = view.findViewById(R.id.addPhotoButton);
        addBtn.setOnClickListener(v -> findFile());
        Button uploadBtn = view.findViewById(R.id.uploadButton);
        uploadBtn.setOnClickListener(v -> uploadFile());
        textView = view.findViewById(R.id.textViewFileName);
        imageView = view.findViewById(R.id.imageView2);

    }

    private void findFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQ_CODE);
    }

    private void uploadFile(){
        try {
            ParcelFileDescriptor pf = requireContext().getContentResolver().openFileDescriptor(uri, "r", null);
            if (pf != null) {
                InputStream is = new FileInputStream(pf.getFileDescriptor());
                File file = new File(requireContext().getCacheDir(), fileName);
                try (OutputStream os = new FileOutputStream(file)) {
                    IOUtils.copy(is, os);
                    MultipartBody.Part part = Server.getFilePart(file.getPath(), fileName);
                    request = Server.getInstance().getApi().upload("abcde", part);
                    request.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.code()==200){
                                textView.setText("uploaded");
                            } else {
                                textView.setText(String.valueOf(response.code()));

                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            textView.setText(t.getMessage());
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE && resultCode == RESULT_OK && data!=null){

            // uri 와 파일 이름 얻
            uri = data.getData();
            Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
            if(cursor!=null) {
                cursor.moveToFirst();
                fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                imageView.setImageURI(uri);
                textView.setText(fileName);
                cursor.close();
            }
        }
    }
}