package com.example.jovi.redditnetworking.Persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jovi.redditnetworking.Domain.Post;

import java.util.ArrayList;

/**
 * Created by jovi on 1/25/2017.
 */

public class PostReaderHelper extends SQLiteOpenHelper
{
    //Increment DB_Version on schema change
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "reddit.db";
    private static final String TABLE_NAME = PostContract.postEntry.TABLE_NAME;

    public PostReaderHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void clearDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(PostContract.SQL_CLEAR_POSTS);
    }

    public ArrayList<Post> getAllPosts()
    {
        ArrayList<Post> posts = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Post p = new Post(cursor.getString(1),cursor.getString(3), cursor.getString(2), cursor.getString(4), cursor.getInt(5), cursor.getString(6));
                posts.add(p);
            } while (cursor.moveToNext());
        }
        return posts;
    }

    public void writePosts(ArrayList<Post> posts)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Post p : posts)
        {
            p.title = p.title.replace("\"", "\'");
            p.selftext = p.selftext.replace("\"", "\'");
            db.execSQL("INSERT INTO " + TABLE_NAME +" ("
                    + PostContract.postEntry.COLUMN_NAME_NAME + ", "
                    + PostContract.postEntry.COLUMN_NAME_PHOTO + ", "
                    + PostContract.postEntry.COLUMN_NAME_AUTHOR + ", "
                    + PostContract.postEntry.COLUMN_NAME_URL + ", "
                    + PostContract.postEntry.COLUMN_NAME_SCORE + ", "
                    + PostContract.postEntry.COLUMN_NAME_TEXT + ")"
                    + " values (\""
                    + String.valueOf(p.title)
                    + "\", \"" + String.valueOf(p.thumbnail)
                    + "\", \"" + String.valueOf(p.author) + "\", \""
                    + String.valueOf(p.url) + "\", \""
                    + Integer.valueOf(p.score) + "\", \""
                    + String.valueOf(p.selftext) + "\");");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(PostContract.SQL_CREATE_POSTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL(PostContract.SQL_DELETE_POSTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

}
