package com.rohit.locustryapp.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by oust on 6/5/19.
 */

public class LocusTryController extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getLocusAppContext(){
        return mContext;
    }
}
