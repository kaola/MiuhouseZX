package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;

import java.util.List;

/**
 * Created by khb on 2016/1/21.
 */
public class EquipAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;

    public EquipAdapter(Activity mContext, List<String> mList) {
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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        TextView tv_equip;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView!=null){
            holder = (ViewHolder) convertView.getTag();
        }else{
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_house_equip, null);
            holder.tv_equip = (TextView) convertView.findViewById(R.id.tv_equip);
            convertView.setTag(holder);
        }
        holder.tv_equip.setText(mList.get(position));
        return convertView;
    }
}
