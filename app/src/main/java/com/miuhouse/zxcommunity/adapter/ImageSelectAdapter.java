package com.miuhouse.zxcommunity.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.HeadUrl;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyAsyn;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by khb on 2016/1/29.
 */
public class ImageSelectAdapter extends BaseAdapter {

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(9);
    private List<String> mList;
    private Context mContext;
    private List<String> tempList;
//    private ViewHolder holder;
    private MyAsyn myAsyn;

    public List<String> getImageUrls() {
        return imageUrls;
    }

    private List<String> imageUrls;

    public ImageSelectAdapter(Context context, List<String> list){
        mContext = context;
        mList = list;
        tempList = new ArrayList<>();
        imageUrls = new ArrayList<>();
        if (!MyUtils.isEmptyList(mList)){
            imageUrls.addAll(mList);
        }
    }

    @Override
    public int getCount() {
        return mList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        ImageView image;
        RelativeLayout wrapper;
        TextView uploading;
        ImageView delete;
        boolean hasUploaded = false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            if (position == 0){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_selectimage_head, null);
                holder.wrapper = (RelativeLayout) convertView.findViewById(R.id.wrapper);
            }else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_selectimage, null);
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                holder.uploading = (TextView) convertView.findViewById(R.id.uploading);
                holder.delete = (ImageView) convertView.findViewById(R.id.checkmark);
                convertView.setTag(holder);
            }
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position > 0){
            final int p = position -1;
            Glide.with(mContext).load(mList.get(p)).placeholder(R.mipmap.umeng_socialize_share_pic).into(holder.image);

            if (!MyUtils.isEmptyList(mList)
                    && !tempList.contains(mList.get(p))
                    && !mList.get(p).contains(Constants.IMGURL_HEAD)){
                tempList.add(mList.get(p));
                myAsyn = new MyAsyn(mContext, getAsynResponse(holder), mList.get(p));
                myAsyn.executeOnExecutor(fixedThreadPool, p + "");
            }else {
                holder.uploading.setVisibility(View.GONE);
                holder.hasUploaded = true;
            }
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(p, holder);
                }
            });
        }
        return convertView;
    }

    private void delete(int position, ViewHolder holder) {
        mList.remove(position);
        if (myAsyn != null && myAsyn.getStatus().equals(AsyncTask.Status.RUNNING)) {
            myAsyn.cancel(true);
        }
        if (holder.hasUploaded){
            imageUrls.remove(position);
        }
        notifyDataSetChanged();
    }

    private MyAsyn.AsyncResponse getAsynResponse(final ViewHolder holder) {
        return new MyAsyn.AsyncResponse() {

            @Override
            public void processFinish(String result) {

                // TODO Auto-generated method stub
                Gson gson = new Gson();
                HeadUrl headUrl = gson.fromJson(result, HeadUrl.class);
                imageUrls.add(headUrl.getImages().get(0));
                holder.uploading.setVisibility(View.GONE);
                holder.hasUploaded = true;
            }

            @Override
            public void processError() {
                holder.uploading.setText("上传失败");
                holder.hasUploaded = false;
            }
        };
    }

}
