package com.miuhouse.zxcommunity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.Huxing;
import com.miuhouse.zxcommunity.utils.L;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.List;

/**
 * Created by khb on 2016/5/5.
 */
public class HuxingAdapter extends BaseAdapter{
    private List<Huxing> mList;
    private Context mContext;

    public HuxingAdapter(Context mContext, List<Huxing> mList) {
        super();
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        ImageView huxingImg;
        TextView huxingTitle;
        TextView huxingPrice;
        TextView huxingLayout;
        TextView huxingArea;
        TextView huxingDesc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        L.i("huxing getview " + position);
        ViewHolder holder= null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_huxing, null);
            holder.huxingImg = (ImageView) convertView.findViewById(R.id.huxingImg);
            holder.huxingPrice = (TextView) convertView.findViewById(R.id.huxingPrice);
            holder.huxingTitle = (TextView) convertView.findViewById(R.id.huxingTitle);
            holder.huxingLayout = (TextView) convertView.findViewById(R.id.huxingLayout);
            holder.huxingArea = (TextView) convertView.findViewById(R.id.huxingArea);
            holder.huxingDesc = (TextView) convertView.findViewById(R.id.huxingDesc);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Huxing huxing = mList.get(position);
        holder.huxingTitle.setText(huxing.getTitle());
        holder.huxingPrice.setText(huxing.getPrice() + "元/平米");
        holder.huxingLayout.setText(huxing.getApartment());
        holder.huxingArea.setText(huxing.getArea() + "平米");
        holder.huxingDesc.setText(huxing.getComment());
        if (!MyUtils.isEmptyList(huxing.getImages())){
            Glide.with(mContext).load(huxing.getImages().get(0))
                    .placeholder(R.mipmap.default_error)
                    .into(holder.huxingImg);
        }
        return convertView;
    }
}
