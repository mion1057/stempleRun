package com.example.myapplication.ui.story;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.StartStory;
import com.example.myapplication.databinding.FragmentStoryStartBinding;
import com.example.myapplication.network.Server;
import com.example.myapplication.ui.BaseFragment;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryStartFragment extends BaseFragment implements MapView.CurrentLocationEventListener, MapView.POIItemEventListener, MainActivity.onKeyBackPressedListener {

    private FragmentStoryStartBinding binding;
    private Call<List<StartStory>> request;
    private Call<Integer> request2;
    private String fileName;

    private AppCompatActivity activity;

    URL fileURL;
    Bitmap bitmap;

    //    private String[] question;  // 문제 텍스트 배열
//    private String[] hintText;  // 힌트 텍스트 배열
//    private String[] hintPicture;   // 힌트 사진 파일이름 배열
//    private String[] narration; // 나레이션 파일 이름 배열
    private int s_length; // 스토리 문화재 갯수

    private ArrayList<StartStory> startStoryList; // 스토리당 문제, 힌트, 나레이션을 가지고있는 startStory 리스트

    private StartStory[] startStorys;

    private MapView mapView;
    private MapPOIItem marker;
    private ViewGroup mapViewContainer;

    // 위도, 경도 변수
    private double myLatitude;
    private double myLongitude;

    private MapCircle cultureRange;

    // 원의 위도, 경도 변수 및 원의 반지름 범위
    private double circleLatitude;
    private double circleLongitude;
    private int circleRadius;

    private Double[] array_circleLatitudes;
    private Double[] array_circleLongitudes;
    private Integer[] array_circleRadiuses;


    private ArrayList<Double> circleLatitudes = new ArrayList<Double>();
    private ArrayList<Double> circleLongitudes = new ArrayList<Double>();
    private ArrayList<Integer> circleRadiuses = new ArrayList<Integer>();

    private MediaPlayer player = new MediaPlayer();


    int music_count = 0;

    // 스토리 순서대로 듣기위한 배열 1=현재들어야할 스토리, 0=아직 안들어도되는 스토리
    int now_c_num[];

    // 해당 스토리 문화재 갯수만큼 생성 => 듣기전=0, 듣기후=1
    int check[];

    // 버튼 관련 변수 및 리스너
    private int buttonHintCheck = 0;
    private View.OnClickListener clickListener = v -> {
        if(v.getId() == R.id.buttonHint) {
            Log.i("힌트버튼1 test : ", binding.textViewHint.getText().toString());
            if(buttonHintCheck % 2 == 0) {
                //binding.buttonHint.setText("힌트 감추기");
                if(binding.textViewHint.getText().toString() != "") {
                    Log.i("힌트 텍스트 : ", binding.textViewHint.getText().toString());
                    binding.textViewHint.setVisibility(View.VISIBLE);
                }
                else {
                    Log.i("힌트 이미지 : ", binding.textViewHint.getText().toString());
                    binding.imageView.setVisibility(View.VISIBLE);
                }
                buttonHintCheck++;
                Log.i("숫자", "짝수 : " + String.valueOf(buttonHintCheck));
            }
            else {
                //binding.buttonHint.setText("힌트 보기");
                binding.textViewHint.setVisibility(View.GONE);
                binding.imageView.setVisibility(View.GONE);

                buttonHintCheck++;
                Log.i("숫자", "홀수 : " + String.valueOf(buttonHintCheck));
            }

        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStoryStartBinding.inflate(inflater);
        return binding.getRoot(); //inflater.inflate(R.layout.fragment_story_start, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void init(View view) {

        activity = (AppCompatActivity) requireActivity();

        mapView = new MapView(activity);
        // POIItem 이벤트 리스너 등록
        mapView.setPOIItemEventListener(this);
        // CurrentLocation 이벤트 리스너 등록
        mapView.setCurrentLocationEventListener(this);

        // 버튼 리스너 등록
        binding.buttonHint.setOnClickListener(clickListener);

        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        // 지도 화면에 등록
        mapViewContainer.addView(mapView);

        // 트래킹 모드 On, 나침반 기능 Off (현재 내 위치 추적 가능 On/Off)
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mapView.setShowCurrentLocationMarker(true);
        Log.i("CHECK", "mapView.getMapPointBounds : " + mapView.getPOIItems());

        marker = new MapPOIItem();
        Log.i("SHOWING", mapView.isShowingCurrentLocationMarker() + "");

        Log.i("onCreate", String.valueOf(s_length));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

//        Button end = view.findViewById(R.id.buttonEnd);
//        end.setOnClickListener(v->{
//            model.data2.setValue("abcde");
//            NavController controller = Navigation.findNavController(view);
//            controller.navigate(R.id.action_nav_story_start_to_nav_story_end);
//        });
        binding.storyTitle.setText(model.s_title.getValue());
        Log.i("check", "S_num ====== " + model.s_num.getValue());
        request = api.getStartStory(model.s_num.getValue());
        request.enqueue(new Callback<List<StartStory>>() {
            @Override
            public void onResponse(Call<List<StartStory>> call, Response<List<StartStory>> response) {
                if(response.code() == 200) {
                    startStoryList = new ArrayList<>(response.body());

                    for(StartStory s : startStoryList) {
                        Log.i("check", "문화재 이름 확인중 : " + s.getC_name());
                        Log.i("check", "문화재 완료여부 : " + s.getS_c_complete());
                    }

                    Log.i("길이", String.valueOf(startStoryList.size()));
                    s_length = startStoryList.size();

                    // 해당 나레이션을 들엇는지 체크하기 위한 배열 => 듣기전 0, 듣기후 1
                    check = new int[s_length];


                    // 문화재 갯수만큼 배열 길이 설정
                    now_c_num = new int[s_length];

                    // 스토리 순서대로 듣기위한 배열 1=현재들어야할 스토리, 0=아직 안들어도되는 스토리
                    // 진행중이였던걸수도 있으니까
                    // now_c_num[0] = 1;
                    for(int i=0; i<s_length; i++) {
                        if(startStoryList.get(i).getS_c_complete() == 0) {

                            // 현재 진행해야할 문화재 1=현재 들어야함, 0은 안들어도댐
                            now_c_num[i] = 1;

                            // 첫번째 문화재 문제 설정
                            binding.textViewProblem.setText(startStoryList.get(i).getQ_content());

                            Log.i("힌트", "ㅁㄴㅇㄹ : " + startStoryList.get(i).getH_file());
                            Log.i("힌트", "ㅁㄴㅇㄹ : " + startStoryList.get(i).getH_content());

                            // 1번째 문화재 힌트 설정
                            if(startStoryList.get(i).getH_file().equals("없음")) {
                                binding.textViewHint.setText(startStoryList.get(i).getH_content());
                                binding.textViewHint.setVisibility(View.GONE);
                            }
                            else {
//                        FileTask fileTask = new FileTask();
//                        fileTask.execute(url + "/uploads/" +startStoryList.get(0).getH_file());

                                Log.i("url", "URL = " + Server.URL);
                                Log.i("url", "asdf = " + startStoryList.get(i).getH_file());
//                            fileURL = new URL(Server.URL + "/uploads/" + startStoryList.get(0).getH_file());
//
//                            HttpURLConnection conn = (HttpURLConnection)fileURL.openConnection();
//                            conn.setDoInput(true);
//                            conn. connect();
//
//                            InputStream is = conn.getInputStream();
//
//                            Log.i("asdf", "is = " + is);
//                            bitmap = BitmapFactory.decodeStream(is);

                                String Path = Server.URL + "/uploads/" + startStoryList.get(i).getH_file();
                                Glide.with(requireContext()).load(Path).into(binding.imageView);

//                            Gallery g = data.get(position);
//                            String path = Server.URL +g.getPic_file().replace("\\", "/");
//                            Log.i("###", path);
//                            Glide.with(context).load(path).into(holder.imageView);

                                binding.imageView.setImageBitmap(bitmap);
                                binding.imageView.setVisibility(View.GONE);
                                binding.textViewHint.setText("");
                            }
                            break;
                        }
                        else {
                            check[i] = 1;
                            MapPOIItem marker = new MapPOIItem();
                            marker.setCustomImageResourceId(R.drawable.marker);
                            marker.setItemName("Default Marker");
                            marker.setTag(0);
                            /////// startStoryList라는 ArrayList를 Array로 바꿨는데 아래쪽에서 startStoryList를 다시 사용한다? =>  아주 개판이다.
                            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.valueOf(startStoryList.get(i).getC_latitude()), Double.valueOf(startStoryList.get(i).getC_longitude())));
                            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 기본으로 제공하는 BluePin 마커 모양.
                            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                            mapView.addPOIItem(marker);
                        }
                    }

                    for(int i=0; i<s_length; i++) {
                        Log.i("배열 : ", String.valueOf(now_c_num[i]));
                        Log.i("배열2 : ", String.valueOf(check[i]));
                    }

//                    // 첫번째 문화재 문제 설정
//                    binding.textViewProblem.setText(startStoryList.get(0).getQ_content());
//
//                    Log.i("힌트", "ㅁㄴㅇㄹ : " + startStoryList.get(0).getH_file());
//                    Log.i("힌트", "ㅁㄴㅇㄹ : " + startStoryList.get(0).getH_content());
//
//                    // 1번째 문화재 힌트 설정
//                    if(startStoryList.get(0).getH_file().equals("없음")) {
//                        binding.textViewHint.setText(startStoryList.get(0).getH_content());
//                        binding.textViewHint.setVisibility(View.GONE);
//                    }
//                    else {
////                        FileTask fileTask = new FileTask();
////                        fileTask.execute(url + "/uploads/" +startStoryList.get(0).getH_file());
//
//                            Log.i("url", "URL = " + Server.URL);
//                            Log.i("url", "asdf = " + startStoryList.get(0).getH_file());
////                            fileURL = new URL(Server.URL + "/uploads/" + startStoryList.get(0).getH_file());
////
////                            HttpURLConnection conn = (HttpURLConnection)fileURL.openConnection();
////                            conn.setDoInput(true);
////                            conn. connect();
////
////                            InputStream is = conn.getInputStream();
////
////                            Log.i("asdf", "is = " + is);
////                            bitmap = BitmapFactory.decodeStream(is);
//
//                            String Path = Server.URL + "/uploads/" + startStoryList.get(0).getH_file();
//                            Glide.with(requireContext()).load(Path).into(binding.imageView);
//
////                            Gallery g = data.get(position);
////                            String path = Server.URL +g.getPic_file().replace("\\", "/");
////                            Log.i("###", path);
////                            Glide.with(context).load(path).into(holder.imageView);
//
//                            binding.imageView.setImageBitmap(bitmap);
//                            binding.imageView.setVisibility(View.GONE);
//                            binding.textViewHint.setText("");
//                    }

                    for(int i = 0; i<s_length; i++) {

//                    MapPOIItem marker = new MapPOIItem();
//                    marker.setCustomImageResourceId(R.drawable.marker);
//                    marker.setItemName("Default Marker");
//                    marker.setTag(0);
//                    /////// startStoryList라는 ArrayList를 Array로 바꿨는데 아래쪽에서 startStoryList를 다시 사용한다? =>  아주 개판이다.
//                    marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.valueOf(startStoryList.get(i).getC_latitude()), Double.valueOf(startStoryList.get(i).getC_longitude())));
//                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 기본으로 제공하는 BluePin 마커 모양.
//                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//
//                    mapView.addPOIItem(marker);

                        // 지도에 Circle 생성
                        cultureRange = new MapCircle(
                                MapPoint.mapPointWithGeoCoord(Double.valueOf(startStoryList.get(i).getC_latitude()), Double.valueOf(startStoryList.get(i).getC_longitude())), // center
                                30, // radius // 원크기 설정
                                Color.argb(128, 255, 0, 0), // strokeColor
                                Color.argb(128, 0, 255, 0) // fillColor
                        );
                        cultureRange.setTag(1234);

                        // 지도에 원형 생성
                        mapView.addCircle(cultureRange);

                        Log.i("MAIN", "CircleCenterPoint : " + cultureRange.getCenter().getMapPointGeoCoord().latitude + ", "
                                + cultureRange.getCenter().getMapPointGeoCoord().longitude);

                        circleLatitude = cultureRange.getCenter().getMapPointGeoCoord().latitude;
                        circleLongitude = cultureRange.getCenter().getMapPointGeoCoord().longitude;

                        Log.i("확인", "원 위도" + String.valueOf(circleLatitude));
                        Log.i("확인", "원 경도" + String.valueOf(circleLongitude));

                        //circleLatitudes = new double[s_length];
                        circleLatitudes.add(circleLatitude);
                        //Log.i("확인", i + "번째 배열 위도 값 : " + String.valueOf(circleLatitudes[i]));

                        //circleLongitudes = new double[s_length];
                        circleLongitudes.add(circleLongitude);
                        //Log.i("확인", i + "번째 배열 경도 값 : " +  String.valueOf(circleLongitudes[i]));

                        circleRadius = cultureRange.getRadius();
                        Log.i("확인", "circleRadius : " + String.valueOf(circleRadius));
                        //circleRadiuses = new int[s_length];
                        circleRadiuses.add(circleRadius);
                        //Log.i("확인", i + "번째 배열 반지름 값 : " + String.valueOf(circleRadiuses[i]));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<StartStory>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        Log.i("MAIN", "marker : " + marker);

        Log.i("asdf", String.valueOf(s_length));

        if(marker == null) {
            // 정문 35.895198, 128.623674
            //본관 35.896730, 128.620985
            //연서관 35.896782, 128.622694
            // 정보관 35.895456, 128.621604
            marker.setMapPoint(mapPoint);
            //marker.setMapPoint(MapPoint.mapPointWithGeoCoord(35.896730, 128.620985));   // 현재 위치로 바꿔주기
            marker.setItemName("Me");
            marker.setTag(0);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            // 마커 등록
            mapView.addPOIItem(marker);
        }
        else if(marker != null && marker.getTag() == 0) {
            // 기존 마커 제거
            mapView.removePOIItem(marker);

            marker.setMapPoint(mapPoint);
            //marker.setMapPoint(MapPoint.mapPointWithGeoCoord(35.896730, 128.620985));   // 현재 위치로 바꿔주기
            marker.setItemName("Me");
            marker.setTag(0);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            // 마커 등록
            mapView.addPOIItem(marker);
        }

        //Log.i("MAIN", "getAlpha : " + marker.getAlpha() + ", getRotation : " + marker.getRotation()
        //        + ", getMapPoint : " + marker.getMapPoint().getMapPointGeoCoord().latitude + ", " + marker.getMapPoint().getMapPointGeoCoord().longitude);


        myLatitude = marker.getMapPoint().getMapPointGeoCoord().latitude;
        myLongitude = marker.getMapPoint().getMapPointGeoCoord().longitude;

        /*for(int q =0; q<s_length; q++) {
            Log.i("진입 1  : ", String.valueOf(circleLatitudes[q]));
            Log.i("진입 1  : ", String.valueOf(circleLongitudes[q]));
            Log.i("진입 1  : ", String.valueOf(circleRadiuses[q]));
        }*/


        array_circleLatitudes = circleLatitudes.toArray(new Double[circleLatitudes.size()]);
        array_circleLongitudes = circleLongitudes.toArray(new Double[circleLongitudes.size()]);
        array_circleRadiuses = circleRadiuses.toArray(new Integer[circleRadiuses.size()]);

        Log.i("MAIN", Math.pow( Double.parseDouble(String.format("%,5f", (myLatitude - circleLatitude))), 2) + ", "
                + Math.pow( Double.parseDouble(String.format("%,5f", (myLongitude - circleLongitude))), 2));
        int i = 0;
        for(i=0; i<s_length; i++) {
            Log.i("진입", Math.pow( Double.parseDouble(String.format("%,5f", (myLatitude - array_circleLatitudes[i]))), 2) + ", "
                    + Math.pow( Double.parseDouble(String.format("%,5f", (myLongitude - array_circleLongitudes[i]))), 2));
            if((Math.pow(array_circleRadiuses[i] * 0.00001, 2) >= Math.pow( Double.parseDouble(String.format("%,5f", (myLatitude - array_circleLatitudes[i]))), 2)
                    + Math.pow( Double.parseDouble(String.format("%,5f", (myLongitude - array_circleLongitudes[i]))), 2)) && now_c_num[i] == 1) {
                if(player.isPlaying() == false && music_count == 0) {
                    Log.i("진입", "햇어요");
                    music_count += 1;
                    Toast.makeText(requireContext(), "원 진입", Toast.LENGTH_LONG).show();
                }
                Log.i("음악쪽", "music_count" + String.valueOf(music_count));

                try {
                    // 원을 진입하고 나레이션이 나오고 있을때
                    if(player.isPlaying()) {
                        music_count = music_count + 1;
                        Log.i("음악쪽", "음악중");
                        Log.i("음악쪽", "i : " + String.valueOf(i));
                        //return;
                    }
                    // 원을 처음 진입햇을때
                    else if(music_count == 1 && check[i] == 0) {

                        player.setDataSource(Server.URL + "/uploads/" + startStoryList.get(i).getS_file());

                        Log.i("음악쪽", "음악시작");
                        player.prepare();

                        player.start();
                        music_count += 1;
                    }
                    // 다 들었을때
                    else {     //if(music_count != 0 && player.isPlaying() == false)
                        // 다 들엇고 다음 힌트 사진으로 업데이트 햇다면 아래의 else로 가지말고 이 if 로그를 띄워주고 반복문을 빠져나가라

                        // 마지막 문화재 다 듣고 종료할때
                        if(i == (s_length-1) && check[i] == 1) {
                            Log.i("음악쪽", "여기 실행됨?");
                            break;
                        }
                        // 마지막 문화재 나레이션을 다 들엇을 때
                        else if(i == (s_length-1)) {
                            Log.i("음악쪽", "마지막 문화재까지 다 들엇다잉");
                            player.release();
                            player=null;
                            music_count = 0;
                            check[i] = 1;
                            Toast.makeText(requireContext(), "마지막 문화재 클리어", Toast.LENGTH_SHORT).show();
                        }
                        // 마지막 문화재 빼고 나레이션이 끝낫다면 다음 힌트로 바꿔주고 토스트를 띄워준다.
                        else {
                            if(i != (s_length-1) && now_c_num[i+1] == 1 && check[i] == 1) {
                                Log.i("음악쪽", "다음 사진으로 업데이트 됏음");
                                break;
                            }
                            Log.i("음악쪽", "다음 사진으로 업데이트 할꺼임");
                            player.release();
                            player=null;
                            music_count = 0;
                            now_c_num[i] = 0;
                            now_c_num[i+1] = 1;
                            player = new MediaPlayer();
                            Log.i("음악쪽", "음악끝");
                            Log.i("음악쪽", String.valueOf(music_count));

                            Toast.makeText(requireContext(), (i+1) + "번째 문화재 클리어", Toast.LENGTH_SHORT).show();

                            ///////////////////////////////////////////////////////////////

                            check[i] = 1;

                            binding.textViewProblem.setText(startStoryList.get(i+1).getQ_content());

                            // 만약에 파일이 있다면 힌트이미지를 띄워준다.

                            Log.i("힌트 텍스트 : ", String.valueOf(startStoryList.get(i+1).getH_content()));
                            Log.i("다운로드 할 파일이름 : ", String.valueOf(startStoryList.get(i+1).getH_file()));

                            // 마지막 문화재 나레이션 들었으면 endActivity로 간다
//                            if(check[(s_length-1)] == 1) {
//                                Intent in = new Intent(this, EndActivity.class);
//                                startActivity(in);
//                                break; //////////////////////
//                            }

                            // 힌트를 다음 문화재 힌트로 바꿀때 힌트가 보이는 상태면 힌트숨긴상태로 바꿔주기
//                            if(binding.buttonHint.getText() == "힌트 감추기") {
//                                Log.i("음악쪽", "힌트 버튼이 힌트감추기냐?");
//                                buttonHintCheck += 1;
//                                binding.buttonHint.setText("힌트 보기");
//                            }
                            if(buttonHintCheck % 2 == 1) {
                                buttonHintCheck += 1;
                            }

                            //
                            if(startStoryList.get(i+1).getH_file().equals("없음")) {
                                binding.textViewHint.setText(startStoryList.get(i+1).getH_content());
                                binding.textViewHint.setVisibility(View.GONE);
                                binding.imageView.setVisibility(View.GONE);
                            }
                            else {
//                                FileTask fileTask = new FileTask();
//
//                                fileTask.execute(url + "/uploads/" +startStoryList.get(i+1).getH_file());

//                                URL fileURL = new URL(Server.URL + "/uploads/" + startStoryList.get(i+1).getH_file());
//                                HttpURLConnection conn = (HttpURLConnection)fileURL.openConnection();
//                                conn.setDoInput(true);
//                                conn. connect();
//
//                                InputStream is = conn.getInputStream();

//                                bitmap = BitmapFactory.decodeStream(is);

                                String Path = Server.URL + "/uploads/" + startStoryList.get(0).getH_file();
                                Glide.with(requireContext()).load(Path).into(binding.imageView);

                                binding.imageView.setImageBitmap(bitmap);
                                binding.imageView.setVisibility(View.GONE);
                                binding.textViewHint.setText("");
                            }
                        }
                        MapPOIItem marker = new MapPOIItem();
                        marker.setCustomImageResourceId(R.drawable.marker);
                        marker.setItemName("Default Marker");
                        marker.setTag(0);
                        /////// startStoryList라는 ArrayList를 Array로 바꿨는데 아래쪽에서 startStoryList를 다시 사용한다? =>  아주 개판이다.
                        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.valueOf(startStoryList.get(i).getC_latitude()), Double.valueOf(startStoryList.get(i).getC_longitude())));
                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 기본으로 제공하는 BluePin 마커 모양.
                        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                        mapView.addPOIItem(marker);
                    }
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.i("진입else : ", "Circle doesn't Contain me");
            }
        }
        if(check[(s_length-1)] == 1) {
            MainActivity activity = (MainActivity) getActivity();

            //뒤로가기 콜백 리스너를 해제
            activity.setOnKeyBackPressedListener(null);
            controller.navigate(R.id.action_nav_story_start_to_nav_story_end);
        }
    }



    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    // POIItem 리스너
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 데이터를 불러와서 위젯을 바꿔야하는데 사용자가 back키를 눌러서 위젯이 없다면 nullpoint가 뜨는데
        if(request != null) {
            request.cancel();
        }

        // 뒤로가기를 눌렀을때 음악이 재생중이라면 음악 종료료
       if(player!=null && player.isPlaying() == true) {
            Log.i("back", "눌림");
            player.release();
            player=null;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //메인뷰 액티비티의 뒤로가기 callback 붙이기
        ((MainActivity)context).setOnKeyBackPressedListener(this::onBackKey);
    }

    @Override
    public void onBackKey() {
        Log.i("back", "눌림aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        MainActivity activity = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        builder.setTitle("스토리 진행중").setMessage("현재까지 내용을 저장하시겠습니까?");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int j=0; j<s_length; j++) {
                    if(check[j] == 0) {
                        request2 = api.storyDuring(model.login_num.getValue(), model.s_num.getValue(), j);
                        request2.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if(response.code() == 200) {
                                    Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();

                                    //뒤로가기 콜백 리스너를 해제
                                    activity.setOnKeyBackPressedListener(null);

                                    //액티비티의 콜백을 직접호출
                                    activity.onBackPressed();
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
                    }
                }
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //뒤로가기 콜백 리스너를 해제
                activity.setOnKeyBackPressedListener(null);

                //액티비티의 콜백을 직접호출
                activity.onBackPressed();
            }
        });

        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        //뒤로가기 콜백 리스너를 해제
        //activity.setOnKeyBackPressedListener(null);

        //액티비티의 콜백을 직접호출
        //activity.onBackPressed();



    }
}