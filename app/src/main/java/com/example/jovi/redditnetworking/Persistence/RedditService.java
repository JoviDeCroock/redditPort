package com.example.jovi.redditnetworking.Persistence;

import com.example.jovi.redditnetworking.Domain.*;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by jovi on 12/28/2016.
 */

public interface RedditService
{
    @GET("funny.json?count=25")
    Call<Wrapper> getFunny();
    @GET("GlobalOffensive.json?count=25")
    Call<Wrapper> getCsgo();
    @GET("programming.json?count=25")
    Call<Wrapper> getProgramming();
    @GET("webdev.json?count=25")
    Call<Wrapper> getWebdev();
    @GET("androiddev.json?count=25")
    Call<Wrapper> getAndroiddev();
    @GET("2007scape.json?count=25")
    Call<Wrapper> getOsrs();
}
