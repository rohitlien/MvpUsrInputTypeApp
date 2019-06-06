package com.rohit.locustryapp.presenters;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.rohit.locustryapp.models.ModuleData;
import com.rohit.locustryapp.utils.LocusTryController;
import com.rohit.locustryapp.views.LandingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by oust on 6/5/19.
 */

public class LandingPresenter {
    private LandingView mView;
    private ArrayList<ModuleData> moduleDataArrayList;
    private int modulePostion=0;
    private String mCurrentPhotoPath;


    public LandingPresenter(LandingView mView) {
        this.mView = mView;
    }

    public void getAllJsonData() {
        if(mView!=null) {
            String jsonStr = getJsonString();
            if (jsonStr != null && !jsonStr.isEmpty()) {
                mView.showLoader();
                extractJsonData(jsonStr);
            } else {
                mView.showNoDataMessage();
            }
        }
    }

    private void extractJsonData(String jsonStr) {
        new JsonExtractor(jsonStr).execute();
    }

    // get json data from assets text file
    private String getJsonString() {
        StringBuilder returnString = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(LocusTryController.getLocusAppContext().getAssets().open("test_json.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                returnString.append(mLine);

            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return returnString.toString();
    }

    public void updateAdapterPos(int position) {
        this.modulePostion = position;
    }

    public void saveImagePath(String absolutePath) {
        this.mCurrentPhotoPath = absolutePath;
    }

    public void updateList() {
        try {
            if(mCurrentPhotoPath!=null && !mCurrentPhotoPath.isEmpty()) {
                moduleDataArrayList.get(modulePostion).setAnswer(mCurrentPhotoPath);
                mCurrentPhotoPath = "";
            }
            if(mView!=null){
                mView.setOrUpdateAdapter(moduleDataArrayList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeImage(int position) {
        try{
            moduleDataArrayList.get(position).setAnswer("");
            if(mView!=null){
                mView.setOrUpdateAdapter(moduleDataArrayList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class JsonExtractor extends AsyncTask<Void,Void,Void>{

        private String testJson = "";
        private ArrayList<ModuleData> moduleDataList;

        public JsonExtractor(String testJson) {
            this.testJson = testJson;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                moduleDataList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(testJson);
                Log.d("json",jsonArray.toString());

                if(jsonArray!=null && jsonArray.length()>0){
                    Gson gson = new Gson();
                    for(int i=0;i<jsonArray.length();i++){
                        ModuleData moduleData = gson.fromJson(jsonArray.optJSONObject(i).toString(),ModuleData.class);
                        moduleDataList.add(moduleData);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            moduleDataArrayList = moduleDataList;
            notifyDataFound();

        }
    }

    private void notifyDataFound() {
        if(mView!=null){
            mView.setOrUpdateAdapter(moduleDataArrayList);
        }
    }


}
