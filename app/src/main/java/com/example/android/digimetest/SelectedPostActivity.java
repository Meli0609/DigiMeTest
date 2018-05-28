package com.example.android.digimetest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.digimetest.Adapters.CommentListItem;
import com.example.android.digimetest.Adapters.CommentViewHolder;
import com.example.android.digimetest.Network.Comment;
import com.example.android.digimetest.Network.Post;
import com.example.android.digimetest.Network.RequestInterface;
import com.example.android.digimetest.Network.RetrofitClient;
import com.example.android.digimetest.Network.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by melisa-pc on 23.05.2018.
 */

public class SelectedPostActivity extends AppCompatActivity {

    private Post post;
    private User user;
    private String postId;
    private ArrayList<CommentViewHolder> postComments = new ArrayList<>();

    TextView postTitle, postBody, userName, commentsHeader;
    CircleImageView userAvatar;
    ListView commentList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_post_layout);

        userAvatar = findViewById(R.id.user_avatar);
        progressBar = findViewById(R.id.progressBar);
        userAvatar.setVisibility(View.GONE);

        setTitle(getResources().getString(R.string.custom_app_name));

        Intent intent = getIntent();
        postId = String.valueOf(intent.getIntExtra("Id", 0));

        progressBar.setVisibility(View.VISIBLE);
        getPostById();

    }

    /*
    Get post info by post Ic
     */
    private void getPostById()
    {
        RequestInterface service = RetrofitClient.getRetrofitInstance().create(RequestInterface.class);
        Call<ArrayList<Post>> call = service.getPost(postId);
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                if(response.body() != null) {
                    post = response.body().get(0);
                    getUserEmailById(post.getUserId());
                }else{
                    returnToPreviousActivity(getResources().getString(R.string.error_invalid_response));
                }
            }

            public void onFailure(Call<ArrayList<Post>> call, Throwable response) {
                handleResponseError(getResources().getString(R.string.error_no_response));
            }
        });
    }

    /*
    Get user by id, collect email for image url
     */
    private void getUserEmailById(int userId)
    {
        RequestInterface service = RetrofitClient.getRetrofitInstance().create(RequestInterface.class);
        Call<ArrayList<User>> call = service.getUser(String.valueOf(userId));
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if(response.body() != null) {
                    user = response.body().get(0);
                    getCommentsByPostId();
                }else{
                    returnToPreviousActivity(getResources().getString(R.string.error_invalid_response));
                }
            }

            public void onFailure(Call<ArrayList<User>> call, Throwable response) {
                handleResponseError(getResources().getString(R.string.error_no_response));
            }
        });
    }

    /*
    Get all comments for current post
     */
    private void getCommentsByPostId()
    {
        RequestInterface service = RetrofitClient.getRetrofitInstance().create(RequestInterface.class);
        Call<ArrayList<Comment>> call = service.getComments(postId);
        call.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                if(response.body() != null) {

                    for (Comment comment : response.body()) {
                        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                        View convertView = inflater.inflate(R.layout.comment_list_item_layout, null);

                        RelativeLayout commentLayout = convertView.findViewById(R.id.comment_layout);
                        postComments.add(new CommentViewHolder(commentLayout, comment));
                    }

                    updateView();
                }else{
                    returnToPreviousActivity(getResources().getString(R.string.error_invalid_response));
                }
            }

            public void onFailure(Call<ArrayList<Comment>> call, Throwable response) {
                handleResponseError(getResources().getString(R.string.error_no_response));
            }
        });
    }

    /*
    Update view
     */
    private void updateView()
    {
        progressBar.setVisibility(View.GONE);
        userAvatar.setVisibility(View.VISIBLE);

        postTitle = findViewById(R.id.post_title);
        postBody = findViewById(R.id.post_body);
        userName = findViewById(R.id.user_name);
        commentsHeader = findViewById(R.id.comments_header);

        commentList = findViewById(R.id.comment_list);

        displayPost();

        displayComments();
    }

    /*
    Display post info
     */
    private void displayPost()
    {
        Typeface face = Typeface.createFromAsset(this.getAssets(), "fonts/lobster.ttf");
        postTitle.setText(post.getTitle().replaceAll("\\s+", " "));
        postTitle.setTypeface(face);
        userName.setText(user.getName());
        userName.setTypeface(face);
        face = Typeface.createFromAsset(this.getAssets(), "fonts/amaranth.ttf");
        postBody.setText(post.getBody().replaceAll("\\s+", " "));
        postBody.setTypeface(face);
        Picasso.get().load("https://api.adorable.io/avatars/285/" + user.getEmail() + ".png").into(userAvatar);
    }

    /*
    Display comment list view
     */
    private void displayComments()
    {
        commentsHeader.setText("All comments (" + String.valueOf(postComments.size()) + ")");
        CommentListItem adapter = new CommentListItem(postComments, getApplicationContext());
        commentList.setAdapter(adapter);
    }

    /*
    Return to previous activity if request to server is failing
     */
    private void returnToPreviousActivity(String errorMessage)
    {
        Toast.makeText(SelectedPostActivity.this,
                       getResources().getString(R.string.error_message) + errorMessage,
                       Toast.LENGTH_LONG).show();
        Intent newIntent = new Intent(SelectedPostActivity.this, AllPostsActivity.class);
        startActivity(newIntent);
    }

    /*
    Check cause for request failing
     */
    private void handleResponseError(String errorMessage)
    {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mWifi.isConnected()) {
            returnToPreviousActivity(getResources().getString(R.string.no_connection));
        }else{
            returnToPreviousActivity(errorMessage);
        }
    }
}
