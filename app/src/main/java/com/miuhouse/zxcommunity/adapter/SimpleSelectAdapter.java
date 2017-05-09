package com.miuhouse.zxcommunity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;

import java.util.List;

/**
 * Created by kings on 3/30/2016.
 */
public class SimpleSelectAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    public List<String> strings;

    public SimpleSelectAdapter(Context context, List<String> strings) {
        this.strings = strings;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return strings.size();
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
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {

            view = layoutInflater.inflate(R.layout.list_item_simple, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        viewHolder.textView.setText(strings.get(position));

        return view;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
