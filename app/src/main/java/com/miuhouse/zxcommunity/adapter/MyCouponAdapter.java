package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.MyCouponsBean;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.List;

/**
 * Created by kings on 1/13/2016.
 */
public class MyCouponAdapter extends BaseRecyclerViewAdapter<MyCouponsBean> {

    public interface OnCheckmarkClickListener {
        void onCheckItemClick(int position);
    }

    private OnCheckmarkClickListener mOnCheckmstkItemClickListener;

    public void setOnCheckmarkItemClickListener(OnCheckmarkClickListener mOnCheckmstkItemClickListener) {
        this.mOnCheckmstkItemClickListener = mOnCheckmstkItemClickListener;
    }

    public MyCouponAdapter(Activity mContext, List<MyCouponsBean> mList) {
        super(mContext, mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyCouponAdapter.MyCouponHolder(mLayoutInflater.inflate(R.layout.list_item_mecoupon, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyCouponsBean myCouponsBean = mList.get(position);
        if (myCouponsBean == null) {
            return;
        }
        if (holder instanceof MyCouponAdapter.MyCouponHolder) {
            MyCouponHolder myCouponHolder = (MyCouponHolder) holder;
            Glide.with(mContext).load(myCouponsBean.getCoupon().getHeadUrl()).centerCrop().override(MyUtils.dip2px(mContext, 96), MyUtils.dip2px(mContext, 101)).into(myCouponHolder.imgHead);
            myCouponHolder.tvPrice.setText(myCouponsBean.getCoupon().getAmount() + "元");
            myCouponHolder.tvTitle.setText(myCouponsBean.getCoupon().getTitle());
            myCouponHolder.tvTime.setText("有效期 " + MyUtils.getYearTime(myCouponsBean.getCoupon().getCreateTime()) + "~" + MyUtils.getMonthTime(myCouponsBean.getCoupon().getEndTime()));
            if (myCouponsBean.getType() == 0) {
                myCouponHolder.imgIsOutofdate.setImageResource(R.mipmap.me_youhuiq_ico_youxiao);
            } else {
                myCouponHolder.imgIsOutofdate.setImageResource(R.mipmap.me_youhuiq_ico_jieshu);

            }
        }
    }

    public class MyCouponHolder extends RecyclerView.ViewHolder {
        private TextView tvPrice;
        private ImageView imgHead;
        private ImageView imgIsOutofdate;
        private TextView tvTitle;
        private TextView tvTime;

        public MyCouponHolder(View itemView) {
            super(itemView);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_usefultime);
            imgHead = (ImageView) itemView.findViewById(R.id.img_head);
            imgIsOutofdate = (ImageView) itemView.findViewById(R.id.img_is_outofdate);
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
