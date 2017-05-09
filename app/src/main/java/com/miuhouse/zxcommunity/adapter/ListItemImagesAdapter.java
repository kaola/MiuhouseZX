package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.List;

/**
 * Created by khb on 2016/12/20.
 */
public class ListItemImagesAdapter extends BaseAdapter {

    private final Activity activity;
    private final List<String> imagesList;
    private int imWidth;
    private int imHeight;

    public ListItemImagesAdapter(Activity activity, List<String>imagesList){
        this.activity = activity;
        this.imagesList = imagesList;
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageHolder holder = null;
        final String imageUrl = imagesList.get(position);
//        final String imageUrl = "http://www.pp3.cn/uploads/201601/2016012401.jpg";

        Log.i("TAG", "==== get view imagesList.size "+imagesList.size()+" ====");
        if (convertView == null){
            holder = new ImageHolder();
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.item_listitemimages, null);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else {
            holder = (ImageHolder) convertView.getTag();
        }

        if (imagesList.size()>1) {
            int imWidth = (MyUtils.getScreenWidth(activity) - MyUtils.dip2px(activity, 40)
                            - (MyUtils.dip2px(activity, 3)*2)) / 3;
            Glide.with(activity).load(imageUrl)
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .override(imWidth, imWidth)
                    .centerCrop()
                    .into(holder.image);
        }else {
            final int imWidth = MyUtils.getScreenWidth(activity)*2/3;
            final ImageHolder finalHolder = holder;

            Glide.with(activity.getApplicationContext()).load(imageUrl)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Log.i("TAG", "resource ready!");
                            if (finalHolder.image == null) {
                                return;
                            }
                            float scale = (float) imWidth / (float) resource.getWidth();
                            imHeight = Math.round(resource.getHeight() * scale);
                            Log.i("TAG", "imWidth=" + imWidth + " imHeight=" + imHeight);
                            ViewGroup.LayoutParams p = finalHolder.image.getLayoutParams();
                            p.width = imWidth;
                            p.height = imHeight;
                            finalHolder.image.setLayoutParams(p);
                            finalHolder.image.setImageBitmap(resource);
//                            DrawableTypeRequest<String> request = Glide.with(activity.getApplicationContext()).load(imageUrl);
//                            request.diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                                    .placeholder(R.mipmap.default_error)
//                                    .error(R.mipmap.default_error)
//                                    .skipMemoryCache(false)
//                                    .thumbnail(0.1f)
//                                    .override(imWidth, imHeight)
//                                    .into(finalHolder.image);
                            notifyDataSetChanged();
//                        }
                        }
                    });

//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(new SimpleTarget<GlideDrawable>() {
//                        @Override
//                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                            Log.i("TAG", "resource ready!");
//                            if (finalHolder.image == null) {
//                                return;
//                            }
//                            float scale = (float) imWidth / (float) resource.getIntrinsicWidth();
//                            imHeight = Math.round(resource.getIntrinsicHeight() * scale);
//                            Log.i("TAG", "imWidth=" + imWidth + " imHeight=" + imHeight);
//                            ViewGroup.LayoutParams p = finalHolder.image.getLayoutParams();
//                            p.width = imWidth;
//                            p.height = imHeight;
//                            finalHolder.image.setLayoutParams(p);
//                            finalHolder.image.setImageDrawable(resource);
////                            DrawableTypeRequest<String> request = Glide.with(activity.getApplicationContext()).load(imageUrl);
////                            request.diskCacheStrategy(DiskCacheStrategy.SOURCE)
////                                    .placeholder(R.mipmap.default_error)
////                                    .error(R.mipmap.default_error)
////                                    .skipMemoryCache(false)
////                                    .thumbnail(0.1f)
////                                    .override(imWidth, imHeight)
////                                    .centerCrop()
////                                    .into(finalHolder.image);
//                        }
//                    });
        }
//        if (((MyGridView)parent).isOnMeasure){
//            return convertView;
//        }else {
//            return layoutView;
//        }
        return convertView;

    }

    class ImageHolder {
        ImageView image;

        public ImageView getImage() {
            return image;
        }

        public void setImage(ImageView image) {
            this.image = image;
        }
    }
}
