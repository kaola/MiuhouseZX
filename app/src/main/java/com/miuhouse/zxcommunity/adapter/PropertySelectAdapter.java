package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.Property;

import java.util.List;

/**
 * Created by kings on 1/11/2016.
 */
public class PropertySelectAdapter extends BaseRecyclerViewAdapter<Property> {

    public interface OnCheckmarkClickListener {
        void onCheckItemClick(int position);
    }

    private OnCheckmarkClickListener mOnCheckmstkItemClickListener;

    public void setOnCheckmarkItemClickListener(OnCheckmarkClickListener mOnCheckmstkItemClickListener) {
        this.mOnCheckmstkItemClickListener = mOnCheckmstkItemClickListener;
    }

    public PropertySelectAdapter(Activity mContext, List<Property> mList) {
        super(mContext, mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PropertySelectAdapter.PropertyHolder(mLayoutInflater.inflate(R.layout.list_item_select_property, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Property mProperty = mList.get(position);
        if (mProperty == null) {
            return;
        }
        if (holder instanceof PropertySelectAdapter.PropertyHolder) {
            PropertyHolder propertyHolder = (PropertyHolder) holder;
            propertyHolder.propertyName.setText(mProperty.getName());
        }
    }

    public class PropertyHolder extends RecyclerView.ViewHolder {
        public TextView propertyName;

        public PropertyHolder(View itemView) {
            super(itemView);
            propertyName = (TextView) itemView.findViewById(R.id.tv_property);
            if (mOnCheckmstkItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    isSelected = true;
                        mOnCheckmstkItemClickListener.onCheckItemClick(getLayoutPosition());
                    }
                });
            }
        }
    }
}
