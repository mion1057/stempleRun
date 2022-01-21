package com.example.myapplication.network;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server {

    private ServerAPI api;
    private static Server instance;

    public static final String URL = "http://172.26.1.141:4007";


    private Server() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL) //여기주소만 우리 서버 주소로 바꾼다.
                .addConverterFactory(GsonConverterFactory.create()) // 얘가 자동으로 String에서 다른걸로 바꿔줌 => dto 타입으로 자동으로 바꿔줌
                .build();
        api = retrofit.create(ServerAPI.class);
    }

    public static Server getInstance() {
      if(instance == null) {
          instance = new Server();
      }
        return instance;
    };

    public ServerAPI getApi() {

        return api;
    }

    public static MultipartBody.Part getFilePart(String path, String fileName){
        MultipartBody.Part part = null;
        File file = new File(path);
        if(file.exists()){
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            part = MultipartBody.Part.createFormData("file", fileName, body);
        }
        return part;
    }
}
