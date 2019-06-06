package com.rohit.locustryapp.callBacks;

/**
 * Created by oust on 6/5/19.
 */

public interface LandingModuleCallBack {
    public void openCamera(int position);

    void removeImage(int position);

    void showFullImage(String answer);
}
