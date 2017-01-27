package com.example.jovi.redditnetworking.Persistence;

import com.example.jovi.redditnetworking.Domain.*;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jovi on 12/28/2016.
 */

public class PersistenceController {
    private Retrofit retrofit;
    private RedditService rService;
    private int lim = 25;

    public PersistenceController()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/r/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rService = retrofit.create(RedditService.class);
    }

    public Call<Wrapper> getPosts(String subreddit, String after)
    {
        Call<Wrapper> call = rService.getPosts(subreddit, lim,after);
        return call;
    }
}
