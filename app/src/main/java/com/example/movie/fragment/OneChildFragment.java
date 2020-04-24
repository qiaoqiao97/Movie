package com.example.movie.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.movie.ItemSubjectAdapter;
import com.example.movie.MyApplication;
import com.example.movie.R;
import com.example.movie.bean.JsonRootBean;
import com.example.movie.bean.Subjects;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OneChildFragment extends Fragment {
    private TextView textView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.one_child_fragment_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        itemSubjectAdapter= new ItemSubjectAdapter(ss,getActivity());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        //GridLayoutManager  glm = new GridLayoutManager(OneChildFragment.this.getActivity(),2);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(itemSubjectAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                itemSubjectAdapter.upDown=0;
                volley();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if((newState==recyclerView.SCROLL_STATE_IDLE)
                        &&(lastVisibleItem+2>itemSubjectAdapter.getItemCount())
                        &&(itemSubjectAdapter.getItemCount()-1<b.getTotal())){
                    itemSubjectAdapter.upDown=1;
                    volley();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                lastVisibleItem = lm.findLastVisibleItemPosition();
            }
        } );
        volley();
    }
    int start = 0;
    List<Subjects> ss = new ArrayList<Subjects>();
    ItemSubjectAdapter itemSubjectAdapter;
    JsonRootBean b;
    public void volley(){
        swipeRefreshLayout.setRefreshing(true);
        String url ="https://douban.uieee.com/v2/movie/in_theaters";
        Log.d("volley",url);
       // String data = "{\"start\":" + start + ",\"count\":15}";
        //Log.d("volley",data);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("volley",response.toString());
                        Gson gson = new Gson();
                        b = gson.fromJson(response.toString(), JsonRootBean.class);
                        start = b.getStart() + b.getCount();
                        Log.d("volley",start + "");
                        ss = b.getSubjects();
                        itemSubjectAdapter.totalSize = b.getTotal();
                        itemSubjectAdapter.loadMoreData(ss);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley",error.toString());
                        itemSubjectAdapter.fail();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        MyApplication.addRequest(jsonObjectRequest,"OneChildFragment");
    }

}
