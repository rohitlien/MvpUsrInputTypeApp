package com.rohit.locustryapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.rohit.locustryapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by oust on 6/5/19.
 */

public class ZoomImageDialog extends Dialog{

    private Context mContext;
    private String imgFilePath;
    private ImageView mZoomImg,closeBtn;

    public ZoomImageDialog(Context context , String imgFilePath) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        this.mContext = context;
        this.imgFilePath = imgFilePath;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(mContext.getResources().getDrawable(R.color.black_semi_transparent));
        setContentView(R.layout.dialog_zoom_image_dialog);
        setCancelable(true);

        initViews();

    }

    private void initViews() {
        closeBtn = findViewById(R.id.closeBtn);
        mZoomImg = findViewById(R.id.mZoomImg);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setImageData();
    }

    private void setImageData() {
        try{
            File file = new File(imgFilePath);
            Picasso.with(mContext).load(file).into(mZoomImg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
