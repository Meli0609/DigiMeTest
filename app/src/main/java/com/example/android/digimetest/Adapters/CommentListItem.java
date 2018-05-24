package com.example.android.digimetest.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.digimetest.Network.Comment;
import com.example.android.digimetest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by melisa-pc on 24.05.2018.
 */

public class CommentListItem extends ArrayAdapter<Comment> {

    private ArrayList<Comment> comment;
    Context mContext;

    public CommentListItem(ArrayList<Comment> data, Context context) {
        super(context, R.layout.comment_list_item_layout, data);
        this.comment = data;
        this.mContext = context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Comment comment = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.comment_list_item_layout, parent, false);

        TextView commentName = (TextView) convertView.findViewById(R.id.comment_name);
        TextView commentBody = (TextView) convertView.findViewById(R.id.comment_body);
        CircleImageView avatar = (CircleImageView) convertView.findViewById(R.id.avatar);

        commentName.setText(comment.getName());
        commentBody.setText(comment.getBody());
        commentName.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        commentBody.setTextColor(ContextCompat.getColor(mContext, R.color.black));

        Picasso.get().load("https://api.adorable.io/avatars/285/" + comment.getEmail() + ".png").into(avatar);

        lastPosition = position;

        return convertView;
    }
}
