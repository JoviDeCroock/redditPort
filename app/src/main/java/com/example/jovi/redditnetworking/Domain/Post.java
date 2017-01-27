package com.example.jovi.redditnetworking.Domain;

/**
 * Created by jovi on 12/28/2016.
 */

public class Post
{
    public String title;
    public String author;
    public int score;
    public String thumbnail;
    public String url;
    public String selftext;

    public Post(){}

    public Post(String title, String author, String thumbnail, String url, int score, String selftext)
    {
        this.title = title;
        this.author = author;
        this.thumbnail = thumbnail;
        this.url = url;
        this.score = score;
        this.selftext = selftext;
    }
}
