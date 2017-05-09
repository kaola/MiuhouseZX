package com.miuhouse.zxcommunity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.Discount;

import java.util.List;

/**
 * Created by khb on 2016/5/4.
 */
public class DiscountAdapter extends BaseAdapter {

    private List<Discount> mList;
    private Context mContext;

    public DiscountAdapter(Context mContext, List<Discount> mList){
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
        TextView discountTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_discount, null);
            holder.discountTitle = (TextView) convertView.findViewById(R.id.discountTitle);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Discount discount = mList.get(position);
        holder.discountTitle.setText(discount.getInformation());
        return convertView;
    }
}
