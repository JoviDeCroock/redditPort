package com.example.jovi.redditnetworking.Persistence;

import com.example.jovi.redditnetworking.Domain.*;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * Created by jovi on 12/28/2016.
 */

public interface RedditService
{
    @GET("/r/{subreddit}.json?limit=")
    Call<Wrapper> getPosts(@Path("subreddit") String subreddit, @Query("limit") int limit, @Query("after") String after);
}
