package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.House;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.MyRoundImageView;

import java.util.List;

/**
 * Created by khb on 2016/1/20.
 */
public class GuessAdapter extends BaseAdapter {

    private Context mContext;
    private List<House> mList;

    public GuessAdapter(Activity mContext, List<House> mList) {
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
        House house = mList.get(position);
        holder.tvHouse.setText(house.getPropertyName() + " " + house.getTitle());
        holder.price.setText(house.getPrice() + (isSellHouse(house) ? "万/套" : "元/月"));
        holder.desc.setText(house.getRemark());
        if (!MyUtils.isEmptyList(house.getImages())) {
            Glide.with(mContext).load(house.getImages().get(0)).placeholder(R.mipmap.default_error).into(holder.ivHouse);
        }
        return convertView;
    }

    private boolean isSellHouse(House house){
        return (MyUtils.isEmptyList(house.getPtss()) || house.getJznd()!=null || house.getCqnx()!=null);
    }
}
