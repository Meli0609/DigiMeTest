package com.example.android.digimetest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private TextView listHeader;
    ProgressBar progressBar;
    ImageButton reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_posts_layout);

        setTitle(getResources().getString(R.string.custom_app_name));

        postList = findViewById(R.id.post_list) ;
        listHeader = findViewById(R.id.list_header);
        progressBar = findViewById(R.id.progressBar);
        reload = findViewById(R.id.reload_button);

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reload.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                getPosts();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        getPosts();
    }

    /*
    Get all posts
     */
    private void getPosts()
    {
        RequestInterface service = RetrofitClient.getRetrofitInstance().create(RequestInterface.class);
        Call<ArrayList<Post>> call = service.getPosts();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                if(response.body() != null) {
                    posts = response.body();
                    updatePostList();
                }else {
                    notifyUserAboutError(getResources().getString(R.string.error_invalid_response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable response) {
                handleResponseError(getResources().getString(R.string.error_no_response));
            }
        });
    }

    /*
    Update post litview
     */
    private void updatePostList()
    {
        listHeader.setText("All posts (" + String.valueOf(posts.size()) + ")");
        PostListItem adapter = new PostListItem(posts, getApplicationContext());
        postList.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(AllPostsActivity.this, SelectedPostActivity.class);
                newIntent.putExtra("Id", posts.get(position).getId());
                startActivity(newIntent);
            }
        });
    }

    /*
    User notification about request errors
     */
    private void notifyUserAboutError(String errorMessage)
    {
        progressBar.setVisibility(View.GONE);
        reload.setVisibility(View.VISIBLE);
        Toast.makeText(AllPostsActivity.this,
                       this.getResources().getString(R.string.error_message) + errorMessage,
                       Toast.LENGTH_LONG).show();
    }

    /*
    Chech response fail reasons
     */
    private void handleResponseError(String message)
    {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mWifi.isConnected()) {
            notifyUserAboutError(this.getResources().getString(R.string.no_connection));
        }else{
            notifyUserAboutError(message);
        }
    }
}
