package com.example.jovi.redditnetworking;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jovi.redditnetworking.Domain.*;
import com.example.jovi.redditnetworking.Persistence.PersistenceController;
import com.example.jovi.redditnetworking.Persistence.PostReaderHelper;
import com.example.jovi.redditnetworking.views.PostDetail;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PersistenceController pc = new PersistenceController();
    private ArrayList<Post> posts;
    private String choiche;
    private PostReaderHelper mDbHelper;
    private String after = "";
    private ArrayAdapter<String> adapter;
    private int last, topOffset = 0;
    @Bind(R.id.content_main) RelativeLayout r;
    @Bind(R.id.listPosts) ListView list;
    @Bind(R.id.txtInit) TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDbHelper = new PostReaderHelper(getApplicationContext());
        asyncDBRead dbread = new asyncDBRead();
        dbread.execute();
    }
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
            list.setVisibility(View.VISIBLE);
            return;
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_refresh)
        {
            SharedPreferences prefs = getSharedPreferences("redditNet", MODE_PRIVATE);
            String restId = prefs.getString("choiche", null);
            if (restId != null) {
                Call<Wrapper> call = getCall(restId);
                getPosts(call);
                return true;
            }
        }else if(id == android.R.id.home){
            onBackPressed();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            list.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        after = "";
        int id = item.getItemId();
        /*GET call*/
        Call<Wrapper> call = getCall(id);
        getPosts(call);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        /*PUT in shared prefs*/
        SharedPreferences.Editor editor = getSharedPreferences("redditNet", MODE_PRIVATE).edit();
        editor.putString("choiche", choiche);
        editor.apply();
        return true;
    }
    private void filldrawer(ArrayList<Post> posts)
    {
        if(posts.size()>30){
            last = list.getFirstVisiblePosition();
            View v = list.getChildAt(0);
            topOffset = (v == null) ? 0 : v.getTop();
        }else{
            last = 0;
            topOffset = 0;
        }
        ArrayList<String> strings = new ArrayList<String>();
        for (Post p : posts) {
            String x = String.format(getString(R.string.post), p.title, p.author, p.score);
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
        if(list.getFooterViewsCount() < 1)
        {
            View v = getLayoutInflater().inflate(R.layout.footer, null);
            list.addFooterView(v);
            final Button loadMore = (Button)v.findViewById(R.id.specialInsButton);
            loadMore.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    loadMore();
                }
            });
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDetails(i);
            }
        });
        SharedPreferences prefs = getSharedPreferences("redditNet", MODE_PRIVATE);
        String restoredText = prefs.getString("choiche", null);
        this.setTitle(restoredText);
        if(last != 0)
        {
            list.setSelectionFromTop(last, topOffset);
        }

    }
    private void showDetails(int i)
    {
        PostDetail details = PostDetail.newInstance(posts.get(i));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.content_main, details).addToBackStack(null);
        list.setVisibility(View.INVISIBLE);
        ft.commit();
    }
    private void getPosts(Call c)
    {
        c.enqueue(new Callback<Wrapper>()
        {
            @Override
            public void onResponse(Call<Wrapper> call, Response<Wrapper> response) {
                if (response.isSuccessful()) {
                    Wrapper w = response.body();
                    if (after.equals(""))
                        posts = new ArrayList<>();
                    after = w.data.after;
                    for(PostWrapper p : w.data.children)
                    {
                        posts.add(p.data);
                    }
                    if(posts.size()<=30)
                    {
                        asyncDBWrite a = new asyncDBWrite();
                        a.execute();
                    }
                    filldrawer(posts);
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private Call<Wrapper> getCall(String id)
    {
        if (id.equals("csgo")) {
            return pc.getPosts("globaloffensive", after);
        } else if (id.equals("funny")) {
            return pc.getPosts("funny", after);
        } else if (id.equals("osrs")) {
            return pc.getPosts("2007scape", after);
        } else if (id.equals("programming")) {
            return pc.getPosts("programming", after);
        } else if (id.equals("webdev")) {
            return pc.getPosts("webdev", after);
        } else if (id.equals("android")) {
            return pc.getPosts("androiddev", after);
        }
        return null;
    }
    private Call<Wrapper> getCall(int id)
    {
        if (id == R.id.nav_csgo) {
            choiche="csgo";
            return pc.getPosts("globaloffensive", after);
        } else if (id == R.id.nav_funny) {
            choiche = "funny";
            return pc.getPosts("funny", after);
        } else if (id == R.id.nav_osrs) {
            choiche = "osrs";
            return pc.getPosts("2007scape", after);
        } else if (id == R.id.nav_programming) {
            choiche = "programming";
            return pc.getPosts("programming", after);
        } else if (id == R.id.nav_webdev) {
            choiche = "webdev";
            return pc.getPosts("webdev", after);
        } else if (id == R.id.nav_androiddev) {
            choiche = "android";
            return pc.getPosts("androiddev", after);
        }
        return null;
    }
    private void loadMore()
    {
        SharedPreferences prefs = getSharedPreferences("redditNet", MODE_PRIVATE);
        String restId = prefs.getString("choiche", null);
        if(restId != null)
        {
            Call<Wrapper> call = getCall(restId);
            getPosts(call);
        }
    }
    private class asyncDBRead extends AsyncTask<String, Void, ArrayList<Post>>
    {
        @Override
        protected ArrayList<Post> doInBackground(String... strings) {
            ArrayList<Post> list = mDbHelper.getAllPosts();
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Post> list)
        {
            posts = list;
            if(posts.size()>0)
            {
                txt.setText("");
                filldrawer(posts);
            }
        }
    }
    protected class asyncDBWrite extends AsyncTask<ArrayList<Post>, Void, Void>
    {
        @Override
        protected Void doInBackground(ArrayList<Post>... arrayLists) {
            mDbHelper.clearDB();
            mDbHelper.writePosts(posts);
            return null;
        }
    }
}
