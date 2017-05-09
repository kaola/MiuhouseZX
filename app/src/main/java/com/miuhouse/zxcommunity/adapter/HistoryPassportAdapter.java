package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.HistoryPassportBean;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.List;

/**
 * Created by kings on 1/11/2016.
 */
public class HistoryPassportAdapter extends BaseRecyclerViewAdapter<HistoryPassportBean> {


    public interface OnCheckmarkClickListener {
        void onCheckItemClick(int position);
    }

    private OnCheckmarkClickListener mOnCheckmstkItemClickListener;

    public void setOnCheckmarkItemClickListener(OnCheckmarkClickListener mOnCheckmstkItemClickListener) {
        this.mOnCheckmstkItemClickListener = mOnCheckmstkItemClickListener;
    }

    public HistoryPassportAdapter(Activity mContext, List<HistoryPassportBean> mList) {
        super(mContext, mList);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryPassportAdapter.NewsHolder(mLayoutInflater.inflate(R.layout.list_history_passport, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryPassportBean newsInfoBean = mList.get(position);
        if (newsInfoBean == null) {
            return;
        }
        if (holder instanceof HistoryPassportAdapter.NewsHolder) {
            NewsHolder newsHolder = (NewsHolder) holder;
            newsHolder.tvVisitName.setText(newsInfoBean.getVisitorName());
            newsHolder.tvVisitDate.setText(MyUtils.getDateFormat(newsInfoBean.getVisitTime()));

        }
    }


    public class NewsHolder extends RecyclerView.ViewHolder {
        public TextView tvVisitName;
        public TextView tvVisitDate;



        public NewsHolder(View itemView) {
            super(itemView);
            tvVisitName = (TextView) itemView.findViewById(R.id.tv_visit_name);
            tvVisitDate = (TextView) itemView.findViewById(R.id.tv_date);

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
