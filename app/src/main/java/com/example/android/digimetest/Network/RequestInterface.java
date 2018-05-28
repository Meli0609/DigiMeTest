package com.example.android.digimetest.Network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by melisa-pc on 23.05.2018.
 */

public interface RequestInterface {

    @GET("posts")
    Call<ArrayList<Post>> getPosts();

    @GET("posts")
    Call<ArrayList<Post>> getPost(@Query("id") String postId);

    @GET("users")
    Call<ArrayList<User>> getUser(@Query("id") String userId);

    @GET("comments")
    Call<ArrayList<Comment>> getComments(@Query("postId") String postId);
}
