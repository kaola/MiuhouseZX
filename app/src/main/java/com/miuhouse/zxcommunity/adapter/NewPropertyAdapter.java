package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.NewHouseListActivity;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.AutoColumnLinearLayout;

import java.util.List;

/**
 * Created by khb on 2016/5/6.
 */
public class NewPropertyAdapter extends BaseRVAdapter<NewHouseListActivity.NewPropertyInfo>{
    public NewPropertyAdapter(Activity activity, List<NewHouseListActivity.NewPropertyInfo> list) {
        super(activity, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewPropertyItemHolder(mLayoutInflater.inflate(R.layout.item_house, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewHouseListActivity.NewPropertyInfo property =  mList.get(position);
        NewPropertyItemHolder mHolder = (NewPropertyItemHolder) holder;
        if (property.getHeadUrl()!=null) {
            Glide.with(mContext).load(property.getHeadUrl())
                    .error(R.mipmap.default_error)
                    .placeholder(R.mipmap.default_error)
                    .into(mHolder.ivHouse);
        }
        mHolder.houseTitle.setText(property.getName());
        mHolder.houseLocation.setText(property.getCity() + property.getArea());
        mHolder.houseArea.setText(property.getRoomArea());
//        mHolder.estateName.setText(property.get);
        mHolder.houseLayout.setText(property.getMinHuxing() + "-" + property.getMaxHuxing() + "室");
        mHolder.housePrice.setText(property.getAvgPrice() + "");
//        if (tag == Constants.SELL) {
//            mHolder.priceUnit.setText("万/套");
//        }else{
////            mHolder.housePrice.setText(Math.rint(house.getPrice()*10000/house.getArea()) + "元/平米");
//            mHolder.priceUnit.setText("元/月");
//        }
        mHolder.houseTags.removeAllViews();
        if (!MyUtils.isEmptyList(property.getLabel())) {
            for (int i = 0; i < property.getLabel().size(); i++) {
                String stringTag = property.getLabel().get(i);
                mHolder.houseTags.addView(createTag(stringTag, i));
            }
        }
        mHolder.discount.setVisibility(property.getHasDiscount() == 0 ? View.GONE : View.VISIBLE);
    }

    private class NewPropertyItemHolder extends RecyclerView.ViewHolder {

        public ImageView ivHouse;
        public TextView houseTitle;
        public TextView houseLocation;
        public TextView houseArea;
        public TextView estateName;
        public TextView housePrice;
        public TextView priceUnit;
        public RelativeLayout rv_house;
        public TextView houseLayout;
        public AutoColumnLinearLayout houseTags;
        public ImageView discount;

        public NewPropertyItemHolder(View inflate) {
            super(inflate);
            rv_house = (RelativeLayout) itemView.findViewById(R.id.rv_house);

            ivHouse = (ImageView) itemView.findViewById(R.id.iv_house);
            houseTitle = (TextView) itemView.findViewById(R.id.houseTitle);
            discount = (ImageView)itemView.findViewById(R.id.hasDiscount);
            houseLocation = (TextView) itemView.findViewById(R.id.houseLocation);
            houseArea = (TextView) itemView.findViewById(R.id.houseArea);
            estateName = (TextView) itemView.findViewById(R.id.estateName);
            housePrice = (TextView) itemView.findViewById(R.id.housePrice);
            priceUnit = (TextView) itemView.findViewById(R.id.unit);
            houseLayout = (TextView) itemView.findViewById(R.id.houseLayout);
            houseTags = (AutoColumnLinearLayout) itemView.findViewById(R.id.tags);
            houseTags.setTotalSpanCount(1);

            rv_house.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNewPropertyClickListener != null){
                        mOnNewPropertyClickListener.onPropertyClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }

    public static interface OnNewPropertyClickListener {
        void onPropertyClick(View view, int position);
    }

    private OnNewPropertyClickListener mOnNewPropertyClickListener;

    public void setOnNewPropertyClickListener(OnNewPropertyClickListener mOnNewPropertyClickListener){
        this.mOnNewPropertyClickListener = mOnNewPropertyClickListener;
    }


    private TextView createTag(String stringTag, int i){
        TextView tag = new TextView(mContext);
        tag.setText(stringTag);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tag.setLayoutParams(params);
        tag.setPadding(MyUtils.dip2px(mContext, 2), 0, MyUtils.dip2px(mContext, 2), 0);
        int mod = i%4;
        switch (mod){
            case 1:
                tag.setBackgroundResource(R.drawable.shape_housetag_red);
                tag.setTextColor(mContext.getResources().getColor(R.color.tag_red));
                break;
            case 2:
                tag.setBackgroundResource(R.drawable.shape_housetag_purple);
                tag.setTextColor(mContext.getResources().getColor(R.color.tag_purple));
                break;
            case 3:
                tag.setBackgroundResource(R.drawable.shape_housetag_blue);
                tag.setTextColor(mContext.getResources().getColor(R.color.tag_blue));
                break;
            case 0:
                tag.setBackgroundResource(R.drawable.shape_housetag_green);
                tag.setTextColor(mContext.getResources().getColor(R.color.tag_green));
                break;
        }
        tag.setTextSize(12);
        return tag;
    }
}
