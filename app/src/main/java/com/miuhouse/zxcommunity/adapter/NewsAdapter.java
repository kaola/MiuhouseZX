package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.NewsInfoBean;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.List;

/**
 * Created by kings on 1/11/2016.
 */
public class NewsAdapter extends BaseRecyclerViewAdapter<NewsInfoBean> {


    public interface OnCheckmarkClickListener {
        void onCheckItemClick(int position);
    }

    private OnCheckmarkClickListener mOnCheckmstkItemClickListener;

    public void setOnCheckmarkItemClickListener(OnCheckmarkClickListener mOnCheckmstkItemClickListener) {
        this.mOnCheckmstkItemClickListener = mOnCheckmstkItemClickListener;
    }

    public NewsAdapter(Activity mContext, List<NewsInfoBean> mList) {
        super(mContext, mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsAdapter.NewsHolder(mLayoutInflater.inflate(R.layout.list_item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsInfoBean newsInfoBean = mList.get(position);
        if (newsInfoBean == null) {
            return;
        }
        if (holder instanceof NewsAdapter.NewsHolder) {
            NewsHolder newsHolder = (NewsHolder) holder;
            newsHolder.tvTitle.setText(newsInfoBean.getTitle());
            newsHolder.tvDate.setText(newsInfoBean.getSource());
            if (newsInfoBean.getImage() != null)
                Glide.with(mContext).load(newsInfoBean.getImage()).centerCrop().override(MyUtils.dip2px(mContext, 90), MyUtils.dip2px(mContext, 80)).into(newsHolder.imgHead);
            else
                Glide.with(mContext).load(R.mipmap.tpjiazai_xinwen).centerCrop().override(MyUtils.dip2px(mContext, 83), MyUtils.dip2px(mContext, 80)).into(newsHolder.imgHead);

        }
    }


    public class NewsHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView imgHead;


        public NewsHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            imgHead = (ImageView) itemView.findViewById(R.id.img_head);

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
