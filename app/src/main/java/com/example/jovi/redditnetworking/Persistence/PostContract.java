package com.example.jovi.redditnetworking.Persistence;

import android.provider.BaseColumns;

/**
 * Created by jovi on 1/25/2017.
 */

public class PostContract
{
    private PostContract(){}

    protected static final String SQL_CREATE_POSTS = "CREATE TABLE " + postEntry.TABLE_NAME + " ("
            + postEntry._ID + " INTEGER PRIMARY KEY," +
            postEntry.COLUMN_NAME_NAME + " TEXT," +
            postEntry.COLUMN_NAME_PHOTO + " TEXT," +
            postEntry.COLUMN_NAME_AUTHOR + " TEXT," +
            postEntry.COLUMN_NAME_URL + " TEXT," +
            postEntry.COLUMN_NAME_SCORE + " INTEGER," +
            postEntry.COLUMN_NAME_TEXT + " TEXT)";

    protected static final String SQL_DELETE_POSTS  = "DROP TABLE IF EXISTS " + postEntry.TABLE_NAME;

    protected static final String SQL_CLEAR_POSTS  = "DELETE FROM " + postEntry.TABLE_NAME;

    public static class postEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "redditposts";
        public static final String COLUMN_NAME_NAME = "Title";
        public static final String COLUMN_NAME_AUTHOR = "Author";
        public static final String COLUMN_NAME_PHOTO = "Thumbnail";
        public static final String COLUMN_NAME_URL = "URL";
        public static final String COLUMN_NAME_SCORE = "Score";
        public static final String COLUMN_NAME_TEXT = "Text";
    }
}
