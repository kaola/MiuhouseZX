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
import com.miuhouse.zxcommunity.bean.House;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.AutoColumnLinearLayout;

import java.util.List;

/**
 * Created by khb on 2016/1/8.
 */
public class HouseListAdapter extends BaseRVAdapter<House> {
    private int tag;

    public HouseListAdapter(Activity mContext, List<House> mList, int tag) {
        super(mContext, mList);
        this.tag = tag;
    }

    public static interface OnHouseClickListener{
        void onHouseClick(View view, int position);
    }

    private OnHouseClickListener mOnHouseClickListener;

    public void setOnHouseClickListener(OnHouseClickListener mOnHouseClickListener){
        this.mOnHouseClickListener = mOnHouseClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HouseItemHolder(mLayoutInflater.inflate(R.layout.item_house, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        House house = mList.get(position);
        HouseItemHolder mHolder = (HouseItemHolder) holder;
        if (!MyUtils.isEmptyList(house.getImages())) {
            Glide.with(mContext).load(house.getImages().get(0)).error(R.mipmap.default_error).placeholder(R.mipmap.default_error).into(mHolder.ivHouse);
        }
        mHolder.houseTitle.setText(house.getTitle());
        mHolder.houseLocation.setText(house.getAddress());
        mHolder.houseArea.setText(house.getArea()+"平米");
        mHolder.estateName.setText(house.getPropertyName());
        mHolder.houseLayout.setText(house.getHuxing());
        mHolder.housePrice.setText(house.getPrice()+"");
        if (tag == Constants.SELL) {
            mHolder.priceUnit.setText("万/套");
        }else{
//            mHolder.housePrice.setText(Math.rint(house.getPrice()*10000/house.getArea()) + "元/平米");
            mHolder.priceUnit.setText("元/月");
        }
        mHolder.houseTags.removeAllViews();
        if (!MyUtils.isEmptyList(house.getLabel())) {
            for (int i = 0; i < house.getLabel().size(); i++) {
                String stringTag = house.getLabel().get(i);
                mHolder.houseTags.addView(createTag(stringTag, i));
            }
        }
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

    public class HouseItemHolder extends RecyclerView.ViewHolder{

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

        public HouseItemHolder(final View itemView) {
            super(itemView);
            rv_house = (RelativeLayout) itemView.findViewById(R.id.rv_house);

            ivHouse = (ImageView) itemView.findViewById(R.id.iv_house);
            houseTitle = (TextView) itemView.findViewById(R.id.houseTitle);
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
                    if (mOnHouseClickListener != null){
                        mOnHouseClickListener.onHouseClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }
}
