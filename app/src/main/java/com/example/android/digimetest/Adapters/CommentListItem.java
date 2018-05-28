package com.example.android.digimetest.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.digimetest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by melisa-pc on 24.05.2018.
 */

public class CommentListItem extends ArrayAdapter<CommentViewHolder> {

    private Context mContext;

    public CommentListItem(ArrayList<CommentViewHolder> data, Context context) {
        super(context, R.layout.comment_list_item_layout, data);
        this.mContext = context;

    }

    /*
    Check if text is truncated
     */
    private static boolean isTextTruncated( String text, TextView textView )
    {
        if ( textView != null && text != null )
        {
            Layout layout = textView.getLayout();
            if ( layout != null )
            {
                int lines = layout.getLineCount();
                if ( lines > 0 )
                {
                    int ellipsisCount = layout.getEllipsisCount( lines - 1 );
                    if ( ellipsisCount > 0 )
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
    Change textView Layout due to need of expansion
     */
    private void expandTextView(TextView textView, String text)
    {
        ViewGroup.LayoutParams textparams = textView.getLayoutParams();

        float textWidth = textView.getPaint().measureText(text);
        float textViewWidth = textView.getMeasuredWidth();
        float TextLeftToWriteWidth = textWidth - textViewWidth;
        int numOfRowsToAdd = (int)(TextLeftToWriteWidth / textViewWidth) + 2;
        textparams.height = textparams.height + numOfRowsToAdd*40;
        textView.setMaxLines(textView.getMaxLines() + numOfRowsToAdd);
        textView.setLayoutParams(textparams);
    }

    /*
    Expand comment item
     */
    private void expandView(RelativeLayout newLayoutParams, CommentViewHolder commentLayout)
    {
        ViewGroup.LayoutParams params = newLayoutParams.getLayoutParams();

        TextView commentName = newLayoutParams.findViewById(R.id.comment_name);
        TextView commentBody = newLayoutParams.findViewById(R.id.comment_body);
        Button button = newLayoutParams.findViewById(R.id.load_more);
        button.setText("Less");

        if(isTextTruncated(commentLayout.getComment().getName(), commentName))
        {
            expandTextView(commentName, commentLayout.getComment().getName());
        }

        if(isTextTruncated(commentLayout.getComment().getBody(), commentBody))
        {
            expandTextView(commentBody, commentLayout.getComment().getBody());
        }

        params.height = commentName.getLayoutParams().height +
                        commentBody.getLayoutParams().height +
                        button.getLayoutParams().height;

        newLayoutParams.setLayoutParams(params);

        commentLayout.setCommentLayout(newLayoutParams);
        notifyDataSetChanged();
    }

    /*
    Collapse comment item
     */
    private void collapseView(RelativeLayout newLayoutParams, CommentViewHolder commentLayout)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View convertView = inflater.inflate(R.layout.comment_list_item_layout, null);
        Button button = newLayoutParams.findViewById(R.id.load_more);
        button.setText("More");

        newLayoutParams = convertView.findViewById(R.id.comment_layout);
        commentLayout.setCommentLayout(newLayoutParams);
        notifyDataSetChanged();
    }

    /*
    Set button layout
     */
    private void setButtonLayout(Button button, String text, Typeface face)
    {
        button.setText(text);
        button.setTypeface(face);
    }

    /*
    Set textView Layout
     */
    private void setTextViewLayout(TextView textView,
                                 ViewGroup.LayoutParams layoutParams,
                                 int maxLines,
                                 String text,
                                 Typeface face)
    {
        textView.setLayoutParams(layoutParams);
        textView.setMaxLines(maxLines);
        textView.setText(text);
        textView.setTypeface(face);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final CommentViewHolder commentLayout = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.comment_list_item_layout, parent, false);

        TextView commentName = convertView.findViewById(R.id.comment_name);
        TextView commentBody = convertView.findViewById(R.id.comment_body);
        CircleImageView avatar = convertView.findViewById(R.id.user_avatar);
        final Button loadMore = convertView.findViewById(R.id.load_more);
        final RelativeLayout commentView = convertView.findViewById(R.id.comment_layout);

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loadMore.getText().equals("More"))
                {
                    expandView(commentView, commentLayout);
                }else{
                    collapseView(commentView, commentLayout);
                }
            }
        });

        setTextViewLayout(commentName,
                          commentLayout.getCommentNameView().getLayoutParams(),
                          commentLayout.getCommentNameView().getMaxLines(),
                          commentLayout.getComment().getName().replaceAll("\\s+", " "),
                          Typeface.createFromAsset(mContext.getAssets(), "fonts/lobster.ttf"));

        setButtonLayout(loadMore,
                        String.valueOf(commentLayout.getButtonText().getText()),
                        Typeface.createFromAsset(mContext.getAssets(), "fonts/lobster.ttf"));

        setTextViewLayout(commentBody,
                          commentLayout.getCommentBodyView().getLayoutParams(),
                          commentLayout.getCommentBodyView().getMaxLines(),
                          commentLayout.getComment().getBody().replaceAll("\\s+", " "),
                          Typeface.createFromAsset(mContext.getAssets(), "fonts/amaranth.ttf"));

        commentView.setLayoutParams(commentLayout.getCommentLayout().getLayoutParams());

        Picasso.get().load("https://api.adorable.io/avatars/285/" + commentLayout.getComment().getEmail() + ".png").into(avatar);

        return convertView;
    }
}
