package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.House;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.List;

/**
 * Created by khb on 2016/2/1.
 */
public class HouseListDeletableAdapter extends BaseRVAdapter<House> {

    public boolean isEditing() {
        return isEditing;
    }

    private boolean isEditing;
    private HouseItemDeletableHolder mHolder;

    private List<House> mList;
    private int tag;

    public HouseListDeletableAdapter(Activity mContext, List<House> mList, int tag) {
        super(mContext, mList);
        this.mList = mList;
        this.tag = tag;
    }

    public static interface OnHouseClickListener{
        void onHouseClick(View view, int position);
        void onHouseDeleteClick(View view, int position);
    }

    private OnHouseClickListener mOnHouseClickListener;

    public void setOnHouseClickListener(OnHouseClickListener mOnHouseClickListener){
        this.mOnHouseClickListener = mOnHouseClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HouseItemDeletableHolder(mLayoutInflater.inflate(R.layout.item_house_deleteable, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        House house = mList.get(position);
        mHolder = (HouseItemDeletableHolder)holder;
        mHolder.houseTitle.setText(house.getTitle());
        if (!MyUtils.isEmptyList(house.getImages())) {
            Glide.with(mContext).load(house.getImages().get(0)).error(R.mipmap.default_error).placeholder(R.mipmap.default_error).into(mHolder.iv_house);
        }
        mHolder.houseLocation.setText(house.getAddress());
        mHolder.houseArea.setText((int) house.getArea() + "平米");
        mHolder.estateName.setText(house.getPropertyName());
        if (tag == Constants.SELL) {
            mHolder.housePrice.setText(house.getPrice() + "万/套");
        }else{
//            BigDecimal bigDecimal = new BigDecimal(house.getPrice() * 10000 / house.getArea()).setScale(0, BigDecimal.ROUND_HALF_UP);
            mHolder.housePrice.setText(house.getPrice() + "元/月");
        }
        if (house.getType() == House.AVAILABLE){
//            mHolder.houseStatus.setVisibility(View.INVISIBLE);
            switch (house.getIsCheck()) {
                case -1:
                    mHolder.houseStatus.setText("未审核通过");
                    break;
                case 0:
                    mHolder.houseStatus.setText("审核中");
                    break;
            }
        }else{
            mHolder.houseStatus.setText("已下架");
        }
        if (isEditing) {
            if (house.getType() == 0) {
                int dx = (int) mContext.getResources().getDimension(R.dimen.soldOut_width);
                mHolder.scrollContainer.smoothScrollTo(dx, 0);
            }else{
                mHolder.scrollContainer.smoothScrollTo(0, 0);
            }
        }else{
            mHolder.scrollContainer.smoothScrollTo(0, 0);
        }
    }

    public class HouseItemDeletableHolder extends RecyclerView.ViewHolder{

        public HorizontalScrollView scrollContainer;
        public LinearLayout houseRight;
        public RelativeLayout houseWrapper;
        public ImageView iv_house;
        public TextView houseTitle;
        public TextView houseLocation;
        public TextView houseArea;
        public TextView estateName;
        public TextView houseStatus;
        public TextView housePrice;
        public TextView soldOut;

        public HouseItemDeletableHolder(final View itemView) {
            super(itemView);
            houseRight = (LinearLayout) itemView.findViewById(R.id.houseRight);
            iv_house = (ImageView) itemView.findViewById(R.id.iv_house);
            houseTitle = (TextView) itemView.findViewById(R.id.houseTitle);
            houseLocation = (TextView) itemView.findViewById(R.id.houseLocation);
            houseArea = (TextView) itemView.findViewById(R.id.houseArea);
            estateName = (TextView) itemView.findViewById(R.id.estateName);
            houseStatus = (TextView) itemView.findViewById(R.id.houseStatus);
            housePrice = (TextView) itemView.findViewById(R.id.housePrice);
            soldOut = (TextView) itemView.findViewById(R.id.soldOut);


            WindowManager wm = mContext.getWindowManager();
            int screenWidth = wm.getDefaultDisplay().getWidth();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
            houseRight.setLayoutParams(params);
            houseWrapper = (RelativeLayout) itemView.findViewById(R.id.houseWraper);
            scrollContainer = (HorizontalScrollView) itemView.findViewById(R.id.hsv);
            houseWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnHouseClickListener != null){
                        mOnHouseClickListener.onHouseClick(v, getLayoutPosition());
                    }
                }
            });
            soldOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnHouseClickListener != null) {
                        mOnHouseClickListener.onHouseDeleteClick(v, getLayoutPosition());
                    }
                }
            });
            scrollContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    public void startEditing(){
        isEditing = true;
//        ToggleDeleteButton(isEditing);
    }

    public void finishEditing(){
        isEditing = false;
//        ToggleDeleteButton(isEditing);
    }

    private void ToggleDeleteButton(boolean isEditing) {

    }


}
