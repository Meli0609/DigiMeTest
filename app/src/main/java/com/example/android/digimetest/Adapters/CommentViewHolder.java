package com.example.android.digimetest.Adapters;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.digimetest.Network.Comment;
import com.example.android.digimetest.R;

/**
 * Created by melisa-pc on 27.05.2018.
 */

public class CommentViewHolder {

    private RelativeLayout commentLayout;
    private Comment comment;

    public CommentViewHolder(RelativeLayout commentLayout, Comment comment)
    {
        this.commentLayout = commentLayout;
        this.comment = comment;
    }

    public void setCommentLayout(RelativeLayout newCommentLayout)
    {
        this.commentLayout = newCommentLayout;
    }

    public RelativeLayout getCommentLayout()
    {
        return commentLayout;
    }

    public Comment getComment()
    {
        return comment;
    }

    public TextView getCommentNameView()
    {
        return commentLayout.findViewById(R.id.comment_name);
    }

    public TextView getCommentBodyView()
    {
        return commentLayout.findViewById(R.id.comment_body);
    }

    public Button getButtonText()
    {
        return  commentLayout.findViewById(R.id.load_more);
    }
}
