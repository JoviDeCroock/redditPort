package com.example.jovi.redditnetworking.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jovi.redditnetworking.Domain.Post;
import com.example.jovi.redditnetworking.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;


public class PostDetail extends Fragment
{
    @Bind(R.id.postImage)
    ImageView image;
    @Bind(R.id.postText)
    TextView txt;

    public static PostDetail newInstance(Post p) {
        PostDetail details = new PostDetail();
        Bundle args = new Bundle();
        args.putString("title", p.title);
        args.putInt("score", p.score);
        args.putString("author", p.author);
        args.putString("text", p.selftext);
        args.putString("url", p.url);
        args.putString("thumbnail", p.thumbnail);
        details.setArguments(args);
        return details;
    }

    public PostDetail(){}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        ButterKnife.bind(this, rootView);
        if (getArguments()!= null)
        {
            initView();
        }
        return rootView;
    }

    public void initView()
    {
        txt.setText("");
        String title = getArguments().getString("title");
        String author = getArguments().getString("author");
        String text = getArguments().getString("text");
        String url = getArguments().getString("url");
        String thumbnail = getArguments().getString("thumbnail");
        int score = getArguments().getInt("score");
        if(text.length() <= 2)
        {
            text = "This post has no extended text, title should tell the story!";
        }
        String s = String.format(getString(R.string.detailPost), title, author, score, url, text);
        txt.setText(s);
        if(thumbnail.length()>0 && !thumbnail.equals("self"))
        {
            Picasso.with(getContext())
                    .load(thumbnail)
                    .resize(600,450)
                    .into(image);
        }else{
            image.setImageResource(R.drawable.reddit);
        }

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }
}
