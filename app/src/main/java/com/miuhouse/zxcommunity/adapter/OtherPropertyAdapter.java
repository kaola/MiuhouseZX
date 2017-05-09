package com.miuhouse.zxcommunity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.PropertyInfo;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.MyRoundImageView;

import java.util.List;

/**
 * Created by khb on 2016/5/5.
 */
public class OtherPropertyAdapter extends BaseAdapter{
    private  Context mContext;
    private List<PropertyInfo> mList;

    public OtherPropertyAdapter(Context mContext, List<PropertyInfo> mList) {
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
        MyRoundImageView ivHouse;
        TextView tvHouse;
        TextView price;
        TextView desc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView!=null){
            holder = (ViewHolder) convertView.getTag();
        }else{
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_guess, null);
            holder.ivHouse = (MyRoundImageView) convertView.findViewById(R.id.iv_house);
            holder.tvHouse = (TextView) convertView.findViewById(R.id.houseName);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            convertView.setTag(holder);
        }
        PropertyInfo property = mList.get(position);
        holder.tvHouse.setText(property.getName());
        holder.price.setText(property.getAvgPrice() + "元/㎡起" );
        holder.desc.setText("");
        if (!MyUtils.isEmptyList(property.getImages())) {
            Glide.with(mContext).load(property.getImages().get(0)).placeholder(R.mipmap.default_error).into(holder.ivHouse);
        }
        return convertView;
    }
}
