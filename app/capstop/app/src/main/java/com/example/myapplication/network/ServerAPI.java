package com.example.myapplication.network;

import android.text.Editable;

import com.example.myapplication.data.Area;
import com.example.myapplication.data.Bingo;
import com.example.myapplication.data.Comment;
import com.example.myapplication.data.Gallery;
import com.example.myapplication.data.Review;
import com.example.myapplication.data.Member;
import com.example.myapplication.data.StartStory;
import com.example.myapplication.data.Story;
import com.example.myapplication.data.User;

import java.sql.Timestamp;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServerAPI {

    // 데이터 보낼때는 @Query("이름") 타입 이름
    @GET("users")
    Call<List<User>> getList();

    @GET("users/{id}")
    Call<User> getUser(@Path("id") int id); // @Path는 users/{id} 이거 id 같은거 ex) 게시판에서 해당 게시글 상세 들어갈때

    // story Start
    @GET("storymake/app_area")
    Call<List<Area>> getAreas();

    @GET("storymake/app_area_storyList")
    Call<List<Story>> getStorys(@Query("a_num") int a_num); //@Query는 서버로 파라메타 날릴때 쓰면 댐

    @GET("storymake/storyStart")
    Call<List<StartStory>> getStartStory(@Query("s_num") int s_num);

    @GET("uploads/{fileName}")
    Call<Response> getFile(@Path("fileName") String fileName);

    @GET("storymake/app_storyDuring")
    Call<Integer> storyDuring(@Query("m_num") int m_num, @Query("s_num") int s_num, @Query("count") int count);

    @GET("storymake/app_storyEnd")
    Call<Integer> storyEnd(@Query("m_num") int m_num, @Query("s_num") int s_num);
    // story End

    // Bingo Start
    @GET("bingo/Bingo_app_area")
    Call<List<Area>> getbingoAreas();

    @GET("bingo/Bingo_app_list")
    Call<List<Bingo>> getBingoList(@Query("a_num") int a_num);
    // Bingo End

    // Review
        @GET("review/app_getList")
        Call<List<Review>> getBoards();

        @GET("review/app_getReview")
        Call<Review> getReview(@Query("r_num") int r_num);

        @GET("review/app_hits")
        Call<Integer> hits(@Query("r_num") int r_num);

        @GET("review/app_recommend")
        Call<Integer> recommend(@Query("r_num") int r_num);

        @GET("review/app_insert")
        Call<Integer> review_insert(@Query("type") String type, @Query("title") String title, @Query("memo") String memo, @Query("writer") String writer);
    //

    // comment
        @GET("comment/app_list")
        Call<List<Comment>> getComments(@Query("r_num") int r_num);

        @GET("comment/app_insert")
        Call<Integer> comment_insert(@Query("r_num") int r_num, @Query("content") Editable content, @Query("writer") String writer);
    //


    // Post
    @GET("app_post/post")
    Call<List<Gallery>> getGalleryList();

    @GET("app_post/popular")
    Call<List<Gallery>> getPopularList();

    @GET("app_post/latest")
    Call<List<Gallery>> getLatestList();

    @GET("app_post/postInfoList")
    Call<List<Gallery>> getPost(@Query("p_num") int p_num);

    @POST("app_post/app_reco")
    Call<Integer> addReco(@Query("p_num") int p_num, @Query("m_id") String m_id);

    @GET("app_post/app_reco_check")
    Call<Integer> checkReco(@Query("p_num") int p_num, @Query("m_id") String m_id);

    @Multipart
    @POST("storymake/app_test")
    Call<Void> upload(
            @Part("title") String title,
            @Part MultipartBody.Part file
    );

    // Sing Up
    @POST("app_member/app_idcheck")
    Call<Integer> appIdCheck2(@Query("m_id") String m_id);

    @GET("app_member/app_idcheck")
    Call<Integer> appIdCheck();

    @POST("app_member/app_signup")
    Call<Integer> appSignUpProc(@Body Member member);

    // Login

    @POST("app_login/login")
    Call<List<String>> appLogin(@Query("id") String id, @Query("pw") String pw);

    @POST("app_member/app_autoLoginCheck")
    Call<Integer> appAutoLoginCheck(@Query("id") String id, @Query("pw") String pw);
    //
}
