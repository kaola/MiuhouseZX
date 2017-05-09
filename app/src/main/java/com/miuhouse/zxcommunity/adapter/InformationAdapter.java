package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.InformationBean;
import com.miuhouse.zxcommunity.bean.NewsInfoBean;

import java.util.List;

/**
 * Created by kings on 12/16/2016.
 */
public class InformationAdapter extends BaseRecyclerViewAdapter<NewsInfoBean> {

    public InformationAdapter(Activity mContext, List<NewsInfoBean> mList, OnCheckmarkClickListener mOnCheckMarkClickListener) {
        super(mContext, mList, mOnCheckMarkClickListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InformationAdapter.InformationHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_information, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsInfoBean informationBean = mList.get(position);
        if (informationBean == null) {
            return;
        }
        if (holder instanceof InformationAdapter.InformationHolder) {
            InformationHolder informationHolder = (InformationHolder) holder;
            informationHolder.title.setText(informationBean.getTitle());
            informationHolder.tvConnent.setText(informationBean.getDescription());
        }

    }

    public class InformationHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView tvConnent;

        public InformationHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            tvConnent = (TextView) itemView.findViewById(R.id.tv_description);

            if (mOnCheckmstkItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnCheckmstkItemClickListener.onCheckItemClick(getLayoutPosition());
                    }
                });
            }
        }
    }
}
