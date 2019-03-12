package com.example.recyclerviewretrofitexam;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface KoreanJsonService {
    @GET("posts")
    Call<List<Post>> listPosts();
}
