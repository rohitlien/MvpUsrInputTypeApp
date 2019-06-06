package com.rohit.locustryapp.views;

import com.rohit.locustryapp.models.ModuleData;

import java.util.ArrayList;

/**
 * Created by oust on 6/5/19.
 */

public interface LandingView {
    void showLoader();

    void showNoDataMessage();

    void setOrUpdateAdapter(ArrayList<ModuleData> moduleDataArrayList);
}
