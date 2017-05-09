package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.bean.CouponBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kings on 1/11/2016.
 */
public class CouponAdapter extends BaseRecyclerViewAdapter<CouponBean> {

    private String userId;

    public interface OnCheckmarkClickListener {
        void onCheckItemClick(int position);
    }

    private OnCheckmarkClickListener mOnCheckmstkItemClickListener;

    public void setOnCheckmarkItemClickListener(OnCheckmarkClickListener mOnCheckmstkItemClickListener) {
        this.mOnCheckmstkItemClickListener = mOnCheckmstkItemClickListener;
    }

    public CouponAdapter(Activity mContext, List<CouponBean> mList, String userId) {
        super(mContext, mList);
        this.userId = userId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CouponAdapter.CouponHolder(mLayoutInflater.inflate(R.layout.list_item_coupon, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CouponBean mCouponBean = mList.get(position);
        if (mCouponBean == null) {
            return;
        }
        if (holder instanceof CouponAdapter.CouponHolder) {
            CouponHolder couponHolder = (CouponHolder) holder;
            couponHolder.tvLastCoupon.setText("剩余" + String.valueOf(mCouponBean.getNumber() - mCouponBean.getReceived()) + "张");
            couponHolder.tvPriceCoupon.setText(String.valueOf(mCouponBean.getAmount()));
            couponHolder.tvTitle.setText(mCouponBean.getTitle());
            couponHolder.tvAddress.setText(mCouponBean.getAddress());
            //离活动开始的时间 为负则代表已经开始
            if (isActionEnd(mCouponBean.getEndTime())) {
                couponHolder.tvDate.setTextColor(mContext.getResources().getColor(R.color.btn_red));
                couponHolder.tvDate.setText(formatTime(mCouponBean.getEndTime()));
                couponHolder.imgaIcon.setImageResource(R.mipmap.youhui_ico_sj_red);
                couponHolder.btnGet.setEnabled(true);
                couponHolder.roundlayoutTime.setVisibility(View.VISIBLE);

            } else {
                couponHolder.tvDate.setTextColor(mContext.getResources().getColor(R.color.btn_enabled_false));
                couponHolder.tvDate.setText("已结束");
                couponHolder.roundlayoutTime.setVisibility(View.VISIBLE);
                couponHolder.imgaIcon.setImageResource(R.mipmap.youhui_ico_sj_gray);
                couponHolder.btnGet.setEnabled(false);
                couponHolder.btnGet.setBackgroundResource(R.drawable.selector_item_round_gray);
                couponHolder.tvLastCoupon.setVisibility(View.GONE);
            }
            if (mCouponBean.getStatus() == 0) {
                if (isActionEnd(mCouponBean.getEndTime())) {
                    couponHolder.btnGet.setText("领取");
                    couponHolder.btnGet.setBackgroundResource(R.drawable.selector_item_round_red);
                    couponHolder.btnGet.setEnabled(true);
                } else {
                    couponHolder.btnGet.setText("已结束");
                    couponHolder.btnGet.setBackgroundResource(R.drawable.selector_item_round_gray);
                    couponHolder.btnGet.setEnabled(false);
                }

            } else if (mCouponBean.getStatus() == 1) {
                couponHolder.btnGet.setEnabled(false);
                couponHolder.btnGet.setText("已领取");
                couponHolder.btnGet.setBackgroundResource(R.drawable.selector_item_round_gray);
            }

            Glide.with(mContext).load(mCouponBean.getHeadUrl()).centerCrop().override(MyUtils.dip2px(mContext, 83), MyUtils.dip2px(mContext, 83)).into(couponHolder.imgHead);

        }
    }

    private boolean isActionEnd(long endTime) {
        if (MyUtils.getCountDown(endTime) > 0) {
            return true;
        } else {
            return false;
        }

    }

    private String formatTime(long time) {
        StringBuffer stringBuffer = new StringBuffer(MyUtils.getDifference(MyUtils.getCountDown(time)).replaceFirst(":", "天").replaceFirst(":", "时"));
        stringBuffer.append("分");
        return stringBuffer.toString();
    }

    public class CouponHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView imgHead;
        //        private TextView tvSendCoupon;
        private TextView tvLastCoupon;
        private TextView tvPriceCoupon;
        private Button btnGet;
        private ImageView imgaIcon;
        private TextView tvAddress;
        private RelativeLayout roundlayoutTime;

        public CouponHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            imgHead = (ImageView) itemView.findViewById(R.id.img_head);
//            tvSendCoupon = (TextView) itemView.findViewById(R.id.tv_send_coupon);
            tvLastCoupon = (TextView) itemView.findViewById(R.id.tv_last_coupon);
            tvPriceCoupon = (TextView) itemView.findViewById(R.id.tv_price_coupon);
            btnGet = (Button) itemView.findViewById(R.id.btn_get);
            imgaIcon = (ImageView) itemView.findViewById(R.id.img_icon);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            roundlayoutTime = (RelativeLayout) itemView.findViewById(R.id.roundlayout_time);
            btnGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequestData(getLayoutPosition());
                }
            });
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

    private void sendRequestData(final int position) {
        String url = FinalData.URL_VALUE + "getCoupon";
        Map<String, Object> map = new HashMap<>();
        map.put("ownerId", userId);
        map.put("couponId", mList.get(position).getId());
        GsonRequest<BaseBean> request = new GsonRequest<>(Request.Method.POST, url, BaseBean.class, map, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean baseBean) {
                if (baseBean.getCode() == 0) {
                    mList.get(position).setStatus(1);
                    notifyDataSetChanged();
                }
                ToastUtils.showToast(mContext, baseBean.getMsg());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(mContext, "连接失败");
            }
        });
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }
}
