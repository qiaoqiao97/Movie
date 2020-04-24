package com.example.movie.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.movie.MyApplication;
import com.example.movie.R;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ThreeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.three_fragment_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView iv=(ImageView)view.findViewById(R.id.imageView);
        ImageLoader.getInstance().displayImage("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=158710161,3575221320&fm=26&gp=0.jpg",iv, MyApplication.getLoaderOptions());
    }

}
