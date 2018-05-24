package com.example.android.digimetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.digimetest.Adapters.PostListItem;
import com.example.android.digimetest.Network.Post;
import com.example.android.digimetest.Network.RequestInterface;
import com.example.android.digimetest.Network.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPostsActivity extends AppCompatActivity {

    private ListView postList;
    private ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_posts_layout);

        postList = (ListView) findViewById(R.id.post_list) ;

        RequestInterface service = RetrofitClient.getRetrofitInstance().create(RequestInterface.class);
        Call<ArrayList<Post>> call = service.getPosts();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                posts = response.body();
                updatePostList();
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Toast.makeText(AllPostsActivity.this, "Request failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updatePostList()
    {
        PostListItem adapter = new PostListItem(posts, getApplicationContext());
        postList.setAdapter(adapter);

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent newIntent = new Intent(AllPostsActivity.this, SelectedPostActivity.class);
                newIntent.putExtra("Post", posts.get(position));
                startActivity(newIntent);
            }
        });
    }
}
