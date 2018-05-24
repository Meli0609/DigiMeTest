package com.example.android.digimetest.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.digimetest.Network.RequestInterface;
import com.example.android.digimetest.Network.RetrofitClient;
import com.example.android.digimetest.Network.Post;
import com.example.android.digimetest.R;
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

public class PostListItem extends ArrayAdapter<Post> {

    private ArrayList<Post> post;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        CircleImageView avatar;
    }

    public PostListItem(ArrayList<Post> data, Context context) {
        super(context, R.layout.post_list_item_layout, data);
        this.post = data;
        this.mContext = context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Post post = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.post_list_item_layout, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.post_title);
            viewHolder.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;

        viewHolder.title.setText(post.getTitle());
        viewHolder.title.setTextColor(ContextCompat.getColor(mContext, R.color.black));

        getUserEmailById(post.getUserId(), viewHolder.avatar);

        return convertView;
    }

    private void getUserEmailById(int userId, final CircleImageView avatar)
    {
        RequestInterface service = RetrofitClient.getRetrofitInstance().create(RequestInterface.class);
        Call<User> call = service.getUser(String.valueOf(userId));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                setUserAvatar("https://api.adorable.io/avatars/285/" + response.body().getEmail() + ".png", avatar);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(mContext, "Request failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUserAvatar(String url, CircleImageView avatar)
    {
        Picasso.get().load(url).into(avatar);
    }
}
