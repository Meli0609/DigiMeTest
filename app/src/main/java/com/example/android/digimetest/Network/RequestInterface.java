package com.example.android.digimetest.Network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by melisa-pc on 23.05.2018.
 */

public interface RequestInterface {

    @GET("posts")
    Call<ArrayList<Post>> getPosts();

    @GET("users/{userId}")
    Call<User> getUser(@Path("userId") String userId);

    @GET("comments")
    Call<ArrayList<Comment>> getComments();
}
