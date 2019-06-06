package com.rohit.locustryapp.activities;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG = BaseActivity.class.getName();

    public Context context;
    public Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        activity = this;
        setContentView(getContentView());
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if(this!=null)
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // pass your layout here to inflate
    protected abstract int getContentView();

    // initialize your views
    protected abstract void initView();

    // get your intent data here and set your init data like initializing the presenter
    protected abstract void initData();

    // set any listeners to views here
    protected abstract void initListener();

    @Override
    protected void onPause() {
        super.onPause();
    }
}
