package com.example.android.digimetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.digimetest.Adapters.CommentListItem;
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
    TextView postTitle, postBody, numOfComments, viewComments, userName;
    CircleImageView userAvatar;
    String imageUrl;
    ArrayList<Comment> postComments = new ArrayList<Comment>();
    ArrayList<Comment> allComments = new ArrayList<Comment>();
    ListView commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_post_layout);

        postTitle = (TextView) findViewById(R.id.post_title);
        userAvatar = (CircleImageView) findViewById(R.id.user_avatar);
        postBody = (TextView) findViewById(R.id.post_body);
        numOfComments = (TextView) findViewById(R.id.number_of_comments);
        viewComments = (TextView) findViewById(R.id.view_comments);
        commentList = (ListView) findViewById(R.id.comment_list);
        userName = (TextView) findViewById(R.id.user_name);

        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("Post");

        postTitle.setText(post.getTitle());
        postBody.setText(post.getBody());

        getUserEmailById(post.getUserId());

        getCommentsByPostId(post.getId());

        viewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewComments.getText().equals("View comments")) {
                    displayCommentList();
                }
                else{
                    hideCommentList();
                }
            }
        });
    }

    private void displayCommentList()
    {
        CommentListItem adapter = new CommentListItem(postComments, getApplicationContext());
        commentList.setAdapter(adapter);
        viewComments.setText("Hide comments");
    }

    private void hideCommentList()
    {
        commentList.setAdapter(null);
        viewComments.setText("View comments");
    }

    private void setUserAvatar()
    {
        Picasso.get().load(imageUrl).into(userAvatar);
    }

    private void getUserEmailById(int userId)
    {
        RequestInterface service = RetrofitClient.getRetrofitInstance().create(RequestInterface.class);
        Call<User> call = service.getUser(String.valueOf(userId));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                imageUrl = "https://api.adorable.io/avatars/285/" + response.body().getEmail() + ".png";
                userName.setText(response.body().getName());
                setUserAvatar();
            }

            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SelectedPostActivity.this, "Request failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCommentsByPostId(int postId)
    {
        getAllComments(postId);
    }

    private void findCommentsForCurrentPost(int postId)
    {
        for (Comment comment: allComments ) {
            if(comment.getPostId() == postId)
            {
                postComments.add(comment);
            }
        }

        numOfComments.setText(String.valueOf(postComments.size()));
    }

    private void getAllComments(final int postId)
    {
        RequestInterface service = RetrofitClient.getRetrofitInstance().create(RequestInterface.class);
        Call<ArrayList<Comment>> call = service.getComments();
        call.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {

                allComments = response.body();
                findCommentsForCurrentPost(postId);
            }

            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                Toast.makeText(SelectedPostActivity.this, "Request failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
