package com.example.android.digimetest.Adapters;

import android.content.Context;
import android.graphics.Typeface;
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

    Context mContext;

    private static class ViewHolder {
        TextView title, userName;
        CircleImageView avatar;
    }

    /*
    List Item View Holder
     */
    public PostListItem(ArrayList<Post> data, Context context) {
        super(context, R.layout.post_list_item_layout, data);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Post post = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.post_list_item_layout, parent, false);
            viewHolder.title = convertView.findViewById(R.id.post_title);
            viewHolder.userName = convertView.findViewById(R.id.user_name);
            viewHolder.avatar = convertView.findViewById(R.id.avatar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(post.getTitle().replaceAll("\\s+", " "));
        Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/amaranth.ttf");
        viewHolder.title.setTypeface(face);
        getUserById(post.getUserId(), viewHolder.avatar, viewHolder.userName);

        return convertView;
    }

    /*
    Get User info by id
     */
    private void getUserById(final int userId, final CircleImageView avatar, final TextView userName) {
        RequestInterface service = RetrofitClient.getRetrofitInstance().create(RequestInterface.class);
        Call<ArrayList<User>> call = service.getUser(String.valueOf(userId));
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.body() != null) {

                    setUserAvatar("https://api.adorable.io/avatars/285/"
                                  + response.body().get(0).getEmail()
                                  + ".png", avatar);
                    userName.setText(response.body().get(0).getName());
                    Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/lobster.ttf");
                    userName.setTypeface(face);

                } else {
                    stopListLoad(mContext.getResources().getString(R.string.error_invalid_response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable response) {
                stopListLoad(mContext.getResources().getString(R.string.error_no_response));
            }
        });
    }

    /*
    Set user avatar by image Url
     */
    private void setUserAvatar(String url, CircleImageView avatar) {
        Picasso.get().load(url).into(avatar);
    }

    /*
    Stop listView item load, if get post request is failing
     */
    private void stopListLoad(String errorMessage) {

        Toast.makeText(mContext, mContext.getResources().getString(R.string.error_message) + errorMessage,
                       Toast.LENGTH_LONG).show();
        try {
            this.finalize();
        }catch (Throwable e)
        {
            e.printStackTrace();
        }

    }
}