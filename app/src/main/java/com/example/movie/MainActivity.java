package com.example.movie;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.movie.bean.JsonRootBean;
import com.example.movie.bean.Subjects;
import com.example.movie.fragment.FourFragment;
import com.example.movie.fragment.OneFragment;
import com.example.movie.fragment.ThreeFragment;
import com.example.movie.fragment.TwoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
             String url="https://douban.uieee.com/v2/movie/in_theaters";
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                        Request.Method.POST, url,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                          Log.d("volley",response.toString());
                        Snackbar sb= null;
                        Gson gson=new Gson();
                        JsonRootBean b=gson.fromJson(response.toString(), JsonRootBean.class);
                        Subjects s=b.getSubjects().get(0);

                        sb = Snackbar.make(view, s.getTitle()+","+s.getCasts().get(0).getName()+","+s.getGenres().get(0)+","+s.getYear(), Snackbar.LENGTH_LONG);
                        View v=sb.getView();
                        TextView snackbar_text=(TextView) v.findViewById(R.id.snackbar_text);
                        snackbar_text.setTextColor(Color.parseColor("#FFFFFF"));
                        sb.show();
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("volley",error.toString());
                            }
                        }
                );
                MyApplication.addRequest(jsonObjectRequest,"MainActivity");




            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fm = this.getSupportFragmentManager();
        switchFragment("首页");
    }
    FragmentManager fm;

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = item.getTitle().toString();
        switchFragment(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    String[] titles = {"首页","收藏","Top250","系统设置"};
    Fragment[] fs = {new OneFragment(),new TwoFragment(),new ThreeFragment(),new FourFragment()};
    //临时保存正被显示的Fragment
    Fragment mFragment = new OneFragment();
    public void switchFragment(String title){
        FragmentTransaction t =  fm.beginTransaction();
        Fragment f = fm.findFragmentByTag(title);
        //当前要求显示的Fragment，原来是否显示过，缓存过，如果加入过manager,就取出来显示就行了，没有就从数组中找
        if(f!=null){
            t.hide(mFragment).show(f);
            //保存正被显示的Fragment
            mFragment = f;
        }else {
            for (int i = 0; i < titles.length; i++) {
                if (titles[i].equals(title)) {
                    t.hide(mFragment);
                    t.add(R.id.relativeLayout, fs[i], title);
                    //保存正被显示的Fragment
                    mFragment = fs[i];
                }
            }
        }
        t.commit();
        this.getSupportActionBar().setTitle(title);
    }
}
