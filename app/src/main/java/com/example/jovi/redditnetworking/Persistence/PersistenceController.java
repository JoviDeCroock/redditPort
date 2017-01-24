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

    public PersistenceController()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/r/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rService = retrofit.create(RedditService.class);
    }

    public Call<Wrapper> getFunny() {
        Call<Wrapper> call = rService.getFunny();
        return call;
    }

    public Call<Wrapper> getCsgo()
    {
        Call<Wrapper> call = rService.getCsgo();
        return call;
    }

    public Call<Wrapper> getProgramming()
    {
        Call<Wrapper> call = rService.getProgramming();
        return call;
    }

    public Call<Wrapper> getWebdev()
    {
        Call<Wrapper> call = rService.getWebdev();
        return call;
    }

    public Call<Wrapper> getAndroiddev()
    {
        Call<Wrapper> call = rService.getAndroiddev();
        return call;
    }

    public Call<Wrapper> getOsrs()
    {
        Call<Wrapper> call = rService.getOsrs();
        return call;
    }
}
