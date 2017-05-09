package com.miuhouse.zxcommunity.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
 * 图片库
 */
public class UpdateImageAdapter extends BaseAdapter {
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(9);
    protected List<String> mDatas = new ArrayList<String>();
    private Context context;
    private LayoutInflater inflater;
    private int selectedPosition = -1;
    private boolean shape;//为true时 可以编辑，
    private int mImageSize = 80;
    private int index = -1;
    private List<String> list = new ArrayList<>();
    private boolean isShow;
    //    private List<String> mSelectedImages = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();

    /**
     * 选择某个图片，改变选择状态
     *
     * @param image
     */
    public void select(String image) {
        if (mDatas.contains(image)) {
            mDatas.remove(image);
        }
        notifyDataSetChanged();
    }


    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
        notifyDataSetChanged();
    }

    public UpdateImageAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if (mDatas.size() >= Constants.MAX_NUM) {
            return mDatas.size();
        } else {
            return mDatas.size() + 1;
        }
    }


    public void addData(List<String> data) {
        if (mDatas != null && data != null) {
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
            mDatas.addAll(data);
        }
        notifyDataSetChanged();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final int sign = position;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            holder.add_image = (CardView) convertView.findViewById(R.id.item_grida_addimage);
            holder.tvUpdate = (TextView) convertView.findViewById(R.id.tv);
            holder.checkmark = (ImageView) convertView.findViewById(R.id.checkmark);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }
        if (shape) {
            holder.checkmark.setVisibility(View.VISIBLE);

            if (mDatas.size() == position && mDatas.size() < 9) {
                holder.checkmark.setVisibility(View.GONE);
            }
        } else {
            holder.checkmark.setVisibility(View.GONE);
        }

        if (mDatas.size() != position && mDatas.get(position) != null && !list.contains(mDatas.get(position)) && !mDatas.get(position).contains(Constants.IMGURL_HEAD)) {
            list.add(mDatas.get(position));
            MyAsyn asyn = new MyAsyn(context, getAsynResponse(holder.tvUpdate), mDatas.get(position));
            asyn.executeOnExecutor(fixedThreadPool, String.valueOf(position));
            holder.tvUpdate.setVisibility(View.VISIBLE);
        } else {
            holder.tvUpdate.setVisibility(View.GONE);
            holder.tvUpdate.setVisibility(View.GONE);
        }
        if (position == mDatas.size() && mDatas.size() < Constants.MAX_NUM) {
            holder.image.setVisibility(convertView.GONE);
            holder.add_image.setVisibility(View.VISIBLE);
        } else {
            Glide.with(context).load(mDatas.get(position)).placeholder(R.mipmap.default_error).override(MyUtils.dip2px(context, mImageSize), MyUtils.dip2px(context, mImageSize)).centerCrop().into(holder.image);
            holder.image.setVisibility(convertView.VISIBLE);
            holder.add_image.setVisibility(View.GONE);

        }
        holder.checkmark.setOnClickListener(getcheckmarkListener(position));
        return convertView;
    }

    private View.OnClickListener getcheckmarkListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDelectClickListener != null) {
                    onDelectClickListener.onDelectClick(position);
                }
            }
        };
    }

    private MyAsyn.AsyncResponse getAsynResponse(final TextView tv) {
        return new MyAsyn.AsyncResponse() {

            @Override
            public void processFinish(String result) {

                // TODO Auto-generated method stub
                Gson gson = new Gson();
                HeadUrl headUrl = gson.fromJson(result, HeadUrl.class);
                imageUrls.add(headUrl.getImages().get(0));
                tv.setVisibility(View.GONE);
            }

            @Override
            public void processError() {

            }
        };
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public class ViewHolder {
        public ImageView image;
        public CardView add_image;
        public TextView tvUpdate;
        public ImageView checkmark;
    }

    private OnDelectClickListener onDelectClickListener;

    public void setOnDelectClickListener(OnDelectClickListener onDelectClickListener) {
        this.onDelectClickListener = onDelectClickListener;
    }

    public interface OnDelectClickListener {
        public void onDelectClick(int position);
    }
}
