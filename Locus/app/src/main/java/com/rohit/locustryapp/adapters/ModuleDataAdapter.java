package com.rohit.locustryapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rohit.locustryapp.R;
import com.rohit.locustryapp.callBacks.LandingModuleCallBack;
import com.rohit.locustryapp.models.ModuleData;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oust on 6/5/19.
 */

public class ModuleDataAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<ModuleData> itemDatas;
    private LayoutInflater mInflater;
    private int PHOTO_MODE = 0, SINGLE_CHOICE_MODE = 1, COMMENT_MODE = 2;
    private LandingModuleCallBack landingModuleCallBack;

    public ModuleDataAdapter(Context mContext, ArrayList<ModuleData> itemDatas,LandingModuleCallBack landingModuleCallBack) {
        this.mContext = mContext;
        this.itemDatas = itemDatas;
        this.landingModuleCallBack = landingModuleCallBack;
        mInflater = LayoutInflater.from(mContext);
    }

    public void updateAdapterData(ArrayList<ModuleData> moduleDataArrayList) {
        this.itemDatas = moduleDataArrayList;
        notifyDataSetChanged();
    }


    public class PhotoTypeViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout  mTextLayout;
        private RelativeLayout mImageLayout;
        private ImageView upload_img_view,mCancelPicBtn;
        private TextView mTitle;

        public PhotoTypeViewHolder(View convertView) {
            super(convertView);
            this.mImageLayout = convertView.findViewById(R.id.mImageLayout);
            this.mTextLayout = convertView.findViewById(R.id.mTextLayout);
            upload_img_view = convertView.findViewById(R.id.upload_img_view);
            mCancelPicBtn = convertView.findViewById(R.id.mCancelPicBtn);
            mTitle = convertView.findViewById(R.id.mTitle);
        }
    }

    public class CommentTypeViewHolder extends RecyclerView.ViewHolder {
        private EditText mCommentEt;
        private SwitchCompat mCommentSwitch;

        public CommentTypeViewHolder(View itemView) {
            super(itemView);
            mCommentEt = itemView.findViewById(R.id.mCommentEt);
            mCommentSwitch = itemView.findViewById(R.id.mCommentSwitch);
        }
    }

    public class SingleChoiceTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private RadioButton mRadioBtnA,mRadioBtnB,mRadioBtnC,mRadioBtnD;

        public SingleChoiceTypeViewHolder(View itemView) {
            super(itemView);
            this.mTitle = itemView.findViewById(R.id.mTitle);
            mRadioBtnA = itemView.findViewById(R.id.mRadioBtnA);
            mRadioBtnB = itemView.findViewById(R.id.mRadioBtnB);
            mRadioBtnC = itemView.findViewById(R.id.mRadioBtnC);
            mRadioBtnD = itemView.findViewById(R.id.mRadioBtnD);
        }
    }

    // Decide each type here and inflate that view only
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == PHOTO_MODE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
            return new PhotoTypeViewHolder(view);
        } else if (viewType == SINGLE_CHOICE_MODE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_choice_item, parent, false);
            return new SingleChoiceTypeViewHolder(view);
        } else if (viewType == COMMENT_MODE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            return new CommentTypeViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ModuleData moduleData = itemDatas.get(position);
            if(moduleData.isNotNull()){
                int type = moduleData.getTypeInt();
                if(type==PHOTO_MODE){
                    setPhotoModeData((PhotoTypeViewHolder)holder,moduleData,position);
                }else if(type==COMMENT_MODE){
                    setCommentModeData((CommentTypeViewHolder)holder,moduleData,position);
                }else if(type == SINGLE_CHOICE_MODE){
                    setSingleChoicemodeData((SingleChoiceTypeViewHolder)holder,moduleData,position);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSingleChoicemodeData(SingleChoiceTypeViewHolder holder, ModuleData moduleData, int position) {
        setTitle(holder.mTitle,moduleData.getTitle());
        if(moduleData.getDataMap()!=null){
            List<String> options = moduleData.getDataMap().getOptions();
            if(options!=null && options.size()>0){
                holder.mRadioBtnA.setVisibility(View.VISIBLE);
                holder.mRadioBtnA.setText(options.get(0));
                if(options.size()>1){
                    holder.mRadioBtnB.setVisibility(View.VISIBLE);
                    holder.mRadioBtnB.setText(options.get(1));
                    if(options.size()>2){
                        holder.mRadioBtnC.setVisibility(View.VISIBLE);
                        holder.mRadioBtnC.setText(options.get(2));
                        if(options.size()>3){
                            holder.mRadioBtnD.setVisibility(View.VISIBLE);
                            holder.mRadioBtnD.setText(options.get(3));
                        }else{
                            holder.mRadioBtnD.setVisibility(View.GONE);
                        }
                    }else{
                        holder.mRadioBtnC.setVisibility(View.GONE);
                        holder.mRadioBtnD.setVisibility(View.GONE);
                    }
                }else{
                    holder.mRadioBtnB.setVisibility(View.GONE);
                    holder.mRadioBtnC.setVisibility(View.GONE);
                    holder.mRadioBtnD.setVisibility(View.GONE);
                }
            }else{
                holder.mRadioBtnA.setVisibility(View.GONE);
                holder.mRadioBtnB.setVisibility(View.GONE);
                holder.mRadioBtnC.setVisibility(View.GONE);
                holder.mRadioBtnD.setVisibility(View.GONE);
            }
        }

    }

    private void setCommentModeData(final CommentTypeViewHolder holder, ModuleData moduleData, int position) {
        holder.mCommentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holder.mCommentEt.setVisibility(View.VISIBLE);
                }else{
                    holder.mCommentEt.setVisibility(View.GONE);
                }
            }
        });



    }

    private void setPhotoModeData(PhotoTypeViewHolder holder, final ModuleData moduleData, final int position) {
        setTitle(holder.mTitle,moduleData.getTitle());

        if(moduleData.getAnswer()!=null && !moduleData.getAnswer().isEmpty()){
            holder.mTextLayout.setVisibility(View.GONE);
            holder.mImageLayout.setVisibility(View.VISIBLE);
            holder.mCancelPicBtn.setVisibility(View.VISIBLE);
            setImage(holder.upload_img_view,moduleData.getAnswer());
        }else{
            holder.mTextLayout.setVisibility(View.VISIBLE);
            holder.mImageLayout.setVisibility(View.GONE);
            holder.upload_img_view.setImageBitmap(null);
            holder.mCancelPicBtn.setVisibility(View.GONE);
        }

        holder.mTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(landingModuleCallBack!=null){
                    landingModuleCallBack.openCamera(position);
                }
            }
        });

        holder.mCancelPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(landingModuleCallBack!=null){
                    landingModuleCallBack.removeImage(position);
                }
            }
        });

        holder.mImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(landingModuleCallBack!=null){
                    landingModuleCallBack.showFullImage(moduleData.getAnswer());
                }
            }
        });


    }

    // inflate the image which is saved in external storage here .
    private void setImage(ImageView upload_img_view, String answer) {
        try {
            File file = new File(answer);
            Picasso.with(mContext).load(file).into(upload_img_view);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // set data title here
    private void setTitle(TextView mTitle, String title) {
        if(title!=null && !title.isEmpty()) {
            mTitle.setText(title);
        }else{
            mTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return itemDatas.size();
    }


    @Override
    public int getItemViewType(int position) {
        return itemDatas.get(position).getTypeInt();
    }


}

