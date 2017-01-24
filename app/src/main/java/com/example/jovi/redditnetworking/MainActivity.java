package com.example.jovi.redditnetworking;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jovi.redditnetworking.Domain.*;
import com.example.jovi.redditnetworking.Persistence.PersistenceController;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PersistenceController pc = new PersistenceController();
    private PostWrapper[] posts;
    private ArrayAdapter<String> adapter;
    @Bind(R.id.content_main) RelativeLayout r;
    @Bind(R.id.listPosts) ListView list;
    @Bind(R.id.txtInit) TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("reddit", MODE_PRIVATE);
        int restId = prefs.getInt("choiche", -1);
        if (restId != -1) {
            Call<Wrapper> call;
            if (restId == R.id.nav_csgo) {
                call = pc.getCsgo();
                getPosts(call);
            } else if (restId == R.id.nav_funny) {
                call = pc.getFunny();
                getPosts(call);
            } else if (restId == R.id.nav_osrs) {
                call = pc.getOsrs();
                getPosts(call);
            } else if (restId == R.id.nav_programming) {
                call = pc.getProgramming();
                getPosts(call);
            } else if (restId == R.id.nav_webdev) {
                call = pc.getWebdev();
                getPosts(call);
            } else if (restId == R.id.nav_androiddev) {
                call = pc.getAndroiddev();
                getPosts(call);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
/*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        SharedPreferences.Editor editor = getSharedPreferences("reddit", MODE_PRIVATE).edit();
        editor.putInt("choiche", id);
        editor.apply();
        Call<Wrapper> call;
        if (id == R.id.nav_csgo) {
            call = pc.getCsgo();
            getPosts(call);
        } else if (id == R.id.nav_funny) {
            call = pc.getFunny();
            getPosts(call);
        } else if (id == R.id.nav_osrs) {
            call = pc.getOsrs();
            getPosts(call);
        } else if (id == R.id.nav_programming) {
            call = pc.getProgramming();
            getPosts(call);
        } else if (id == R.id.nav_webdev) {
            call = pc.getWebdev();
            getPosts(call);
        } else if (id == R.id.nav_androiddev) {
            call = pc.getAndroiddev();
            getPosts(call);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPosts(Call c)
    {
        c.enqueue(new Callback<Wrapper>()
        {
            @Override
            public void onResponse(Call<Wrapper> call, Response<Wrapper> response) {
                if (response.isSuccessful()) {
                    Wrapper w = response.body();
                    posts = w.data.children;
                    ArrayList<String> strings = new ArrayList<String>();
                    for (PostWrapper p : posts) {
                        String x = String.format(getString(R.string.post), p.data.title, p.data.author, p.data.score);
                        strings.add(x);
                    }
                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            strings) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            TextView textView = (TextView) view.findViewById(android.R.id.text1);

                            textView.setTextColor(Color.BLACK);

                            return view;
                        }
                    };
                    txt.setText("");
                    list.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
