package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.NewsInfoBean;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by khb on 2016/11/22.
 */
public class ImprovedNewsAdapter extends BaseRecyclerViewAdapter<NewsInfoBean>{
    public ImprovedNewsAdapter(Activity mContext, List<NewsInfoBean> mList) {
        super(mContext, mList);
    }

    public interface OnCheckmarkClickListener {
        void onCheckItemClick(int position);
    }

    private OnCheckmarkClickListener mOnCheckmstkItemClickListener;

    public void setOnCheckmarkItemClickListener(OnCheckmarkClickListener mOnCheckmstkItemClickListener) {
        this.mOnCheckmstkItemClickListener = mOnCheckmstkItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImprovedNewsHolder(LayoutInflater.from(mContext).inflate(R.layout.item_improvednews, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsInfoBean news = mList.get(position);
        if (news == null) {
            return;
        }
        if (holder instanceof ImprovedNewsHolder) {
            ImprovedNewsHolder mholder = (ImprovedNewsHolder) holder;
            mholder.title.setText(news.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            mholder.date.setText(format.format(new Date(Long.parseLong(news.getCreateTime()))));
            if (news.getImage() != null){
                Glide.with(mContext).load(news.getImage()).centerCrop()
                        .override(MyUtils.dip2px(mContext, 90), MyUtils.dip2px(mContext, 80))
                        .into(mholder.head);
            }else {
                Glide.with(mContext).load(R.mipmap.tpjiazai_xinwen).centerCrop()
                        .override(MyUtils.dip2px(mContext, 83), MyUtils.dip2px(mContext, 80))
                        .into(mholder.head);
            }
        }
    }

    public class ImprovedNewsHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView date;
        ImageView head;

        public ImprovedNewsHolder(View itemView) {
            super(itemView);

            head = (ImageView) itemView.findViewById(R.id.img_head);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            date = (TextView) itemView.findViewById(R.id.tv_date);

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
