package com.rohit.locustryapp.activities;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rohit.locustryapp.R;
import com.rohit.locustryapp.adapters.ModuleDataAdapter;
import com.rohit.locustryapp.callBacks.LandingModuleCallBack;
import com.rohit.locustryapp.dialogs.ZoomImageDialog;
import com.rohit.locustryapp.models.ModuleData;
import com.rohit.locustryapp.presenters.LandingPresenter;
import com.rohit.locustryapp.utils.LocusTryController;
import com.rohit.locustryapp.views.LandingView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LandingActivity extends BaseActivity implements LandingView,LandingModuleCallBack {

    private LandingPresenter mPresenter;
    private TextView mProgressText;
    private ProgressBar mProgressBar;
    private LinearLayout mProgressLayout;
    private ModuleDataAdapter moduleDataAdapter;
    private RecyclerView allDataRv;
    private final int RequestPermissionCode = 1;
    private final int PICTURE_RESULT = 11;


    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mProgressLayout = findViewById(R.id.mProgressLayout);
        mProgressBar = findViewById(R.id.mProgressBar);
        mProgressText = findViewById(R.id.mProgressText);
        allDataRv = findViewById(R.id.allDataRv);
    }

    @Override
    protected void initData() {
        mPresenter = new LandingPresenter(this);
        mPresenter.getAllJsonData();
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void showLoader() {
        if (mProgressLayout.getVisibility() == View.GONE) {
            mProgressLayout.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoader() {
        if (mProgressLayout.getVisibility() == View.VISIBLE) {
            mProgressLayout.setVisibility(View.GONE);
        }
        allDataRv.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoDataMessage() {
        showLoader();
        mProgressText.setText(getResources().getString(R.string.no_data_found));
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void setOrUpdateAdapter(ArrayList<ModuleData> moduleDataArrayList) {
        if(moduleDataArrayList!=null && moduleDataArrayList.size()>0){
            hideLoader();
            if(moduleDataAdapter==null){
                moduleDataAdapter = new ModuleDataAdapter(LandingActivity.this,moduleDataArrayList,this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LandingActivity.this,LinearLayoutManager.VERTICAL,false);
                allDataRv.setLayoutManager(linearLayoutManager);
                allDataRv.setAdapter(moduleDataAdapter);
            }else{
                moduleDataAdapter.updateAdapterData(moduleDataArrayList);
            }
        }else {
            showNoDataMessage();
        }



    }

    @Override
    public void openCamera(int position) {
        if(mPresenter!=null){
            mPresenter.updateAdapterPos(position);
        }
        if( checkPermissions()){
            launchCamera();
        }else{
            requestStoragePermissions();
        }
    }

    @Override
    public void removeImage(int position) {
        if(mPresenter!=null){
            mPresenter.removeImage(position);
        }
    }

    @Override
    public void showFullImage(String imgFilePath) {
        ZoomImageDialog zoomImageDialog = new ZoomImageDialog(LandingActivity.this,imgFilePath);
        zoomImageDialog.show();
    }

    private void launchCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = FileProvider.getUriForFile(LocusTryController.getLocusAppContext(),
                LocusTryController.getLocusAppContext().getPackageName() + ".com.rohit.locustryapp.provider",
                getFile());
        Log.e("PDF", "fileUri not null " + fileUri);

        //Setting the file Uri to my photo
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);

        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent, PICTURE_RESULT);
        }
    }

    private void requestStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, RequestPermissionCode);
        }
    }

    private boolean checkPermissions() {
        int result = ContextCompat.checkSelfPermission(LandingActivity.this, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(LandingActivity.this, READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(LandingActivity.this, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RequestPermissionCode){
            launchCamera();
        }

    }

    private File getFile() {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(mPresenter!=null){
            mPresenter.saveImagePath(image.getAbsolutePath());
        }

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICTURE_RESULT) {
            if(resultCode == Activity.RESULT_OK) {
                Toast.makeText(LandingActivity.this, "Image Successfully Captured", Toast.LENGTH_SHORT).show();
                if(mPresenter!=null){
                    mPresenter.updateList();
                }
            }
        }
    }

}
