package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.ComplainDetailActivity;
import com.miuhouse.zxcommunity.activity.photo.GalleryActivity;
import com.miuhouse.zxcommunity.bean.Complain;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.MyGridView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by khb on 2016/12/20.
 */
public class ComplainAdapter extends BaseRVAdapter<Complain>{

    public ComplainAdapter(Activity mContext, List mList) {
        super(mContext, mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ComplainHolder(LayoutInflater.from(mContext).inflate(R.layout.item_complain, null));
    }

    class ComplainHolder extends RecyclerView.ViewHolder{

        private LinearLayout complainLayout;
        TextView time;
        TextView type;
        TextView topic;
        MyGridView images;

        public ComplainHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            type = (TextView) itemView.findViewById(R.id.type);
            topic = (TextView) itemView.findViewById(R.id.topic);
            images = (MyGridView) itemView.findViewById(R.id.images);
            complainLayout = (LinearLayout) itemView.findViewById(R.id.complainLayout);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ComplainHolder mholder = (ComplainHolder) holder;
        final Complain complain = mList.get(position);
        mholder.topic.setText(complain.getContent());
        setType(mholder, complain);
        setTime(mholder, complain);
        final ArrayList<String> images = (ArrayList<String>) complain.getImages();
        if (!MyUtils.isEmptyList(images)) {
            mholder.images.setVisibility(View.VISIBLE);
            ListItemImagesAdapter adapter = new ListItemImagesAdapter(mContext, images);
            if (images.size()>1) {
                mholder.images.setNumColumns(3);
                mholder.images.setHorizontalSpacing(20);
            }/*else if (images.size() == 2){
                mholder.images.setNumColumns(2);
            }*/else if (images.size() == 1){
                mholder.images.setNumColumns(1);
                mholder.images.setHorizontalSpacing(20);
            }
            mholder.images.setAdapter(adapter);
            mholder.images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mContext.startActivity(new Intent(mContext, GalleryActivity.class)
                        .putStringArrayListExtra("imgPath", images)
                        .putExtra("index", position));
                }
            });

        }else {
            mholder.images.setVisibility(View.GONE);
        }
        mholder.complainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ComplainDetailActivity.class)
                        .putExtra("id", complain.getId()));
            }
        });
    }

    private void setType(ComplainHolder mholder, Complain complain) {
        String strType = null;
        switch (complain.getType()){
            case Complain.REPAIR:
                strType = "报修";
                break;
            case Complain.COMPLAIN:
                strType = "投诉";
                break;
            case Complain.ADVICE:
                strType = "建议";
                break;
            case Complain.LIKE:
                strType = "表扬";
                break;
        }
        mholder.type.setText(strType);
    }

    private void setTime(ComplainHolder mholder, Complain complain) {
        long time = complain.getCreateTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
        String strTime = formatter.format(new Date(time));
        mholder.time.setText(strTime);
    }
}
