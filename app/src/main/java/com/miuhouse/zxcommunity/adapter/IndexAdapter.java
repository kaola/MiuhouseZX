package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.HouseDetailActivity;
import com.miuhouse.zxcommunity.activity.HouseListActivity;
import com.miuhouse.zxcommunity.activity.NewHouseActivity;
import com.miuhouse.zxcommunity.activity.NewHouseListActivity;
import com.miuhouse.zxcommunity.bean.ZFZBean;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2016/1/21.
 */
public class IndexAdapter extends BaseAdapter {

    private String title;
    private Context mContext;
    private List<ZFZBean> mList = new ArrayList<>();
    private int size;

    private final int TITLE = 1;

    public static final String XINFANG = "新房速递";
    public static final String SHOUMAI = "房屋售卖";
    public static final String CHUZU = "房屋出租";
    private LinearLayout more;

    public IndexAdapter(Activity mContext, List<ZFZBean> mList, String title) {
        super();
        this.mContext = mContext;
        this.mList = mList;
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        TextView tvCategory;
        TextView tvPrice;
        TextView tvHuxing;
        TextView tvRemark;
        ImageView imgHouse;
        TextView tvTitle;
        TextView tvPriceUnit;
        View imgIcon;
        RelativeLayout linearCategory;
        CardView houseBody;
        LinearLayout more;
        LinearLayout propertyInfo;
        LinearLayout zfInfo;
        TextView propertyHuxing;
        TextView propertyArea;
        TextView propertyLocation;
        TextView propertyPrice;
        TextView propertyPriceUnit;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
//        Dialog dialog = new Dialog(mContext);
//        dialog.dismiss();
        if (!MyUtils.isEmptyList(mList)) {
            if (position == 0) {
                return TITLE;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_index, null);
            holder.tvCategory = (TextView) convertView.findViewById(R.id.tv_category);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvHuxing = (TextView) convertView.findViewById(R.id.tv_huxing);
            holder.tvRemark = (TextView) convertView.findViewById(R.id.tv_remark);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvPriceUnit = (TextView) convertView.findViewById(R.id.tv_price_unit);
            holder.imgHouse = (ImageView) convertView.findViewById(R.id.roundedImage_house_image);
            holder.linearCategory = (RelativeLayout) convertView.findViewById(R.id.linear_category);
            holder.more = (LinearLayout) convertView.findViewById(R.id.categoryMore);
            holder.imgIcon = (View) convertView.findViewById(R.id.img_icon);
            holder.houseBody = (CardView) convertView.findViewById(R.id.houseBody);
            holder.propertyInfo = (LinearLayout) convertView.findViewById(R.id.propertyInfo);
            holder.zfInfo = (LinearLayout) convertView.findViewById(R.id.zfInfo);
            holder.propertyHuxing = (TextView) convertView.findViewById(R.id.propertyHuxing);
            holder.propertyArea = (TextView) convertView.findViewById(R.id.propertyArea);
            holder.propertyLocation = (TextView) convertView.findViewById(R.id.propertyLocation);
            holder.propertyPrice = (TextView) convertView.findViewById(R.id.tv_property_price);
            holder.propertyPriceUnit = (TextView) convertView.findViewById(R.id.tv_property_price_unit);

            convertView.setTag(holder);
        }
        if (getItemViewType(position) == TITLE) {
            holder.linearCategory.setVisibility(View.VISIBLE);
            holder.tvCategory.setText(title);
            holder.tvCategory.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
            if (title.equals(XINFANG)) {
                holder.imgIcon.setBackgroundResource(R.color.tag_red);
                holder.tvTitle.setText(mList.get(position).getName());
                holder.propertyInfo.setVisibility(View.VISIBLE);
                holder.zfInfo.setVisibility(View.GONE);
//                holder.tvHuxing.setText(mList.get(position).getHuxing());
//                if (TextUtils.isEmpty(mList.get(position).getRemark())){
//                    holder.tvRemark.setVisibility(View.GONE);
//                }else {
//                    holder.tvRemark.setText(mList.get(position).getRemark());
//                }
                holder.propertyHuxing.setText(mList.get(position).getMinHuxing() + "-" + mList.get(position).getMaxHuxing() + "室");
                holder.propertyArea.setText(mList.get(position).getRoomsArea());
                holder.propertyLocation.setText(mList.get(position).getCity() + "市" + mList.get(position).getArea());
                holder.propertyPrice.setText(Html.fromHtml(mList.get(position).getAvgPrice() + "<small><font>元/㎡</font></small>"));
//                holder.propertyPrice.setText("元/平米");
                holder.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, NewHouseListActivity.class));
                    }
                });
                if (mList.get(position).getHeadUrl() != null) {
                    Glide.with(mContext).load(mList.get(position).getHeadUrl()).override(MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 20), MyUtils.dip2px(mContext, 200)).error(R.mipmap.tpjiazai_shouyetp).into(holder.imgHouse);
                } else {
                    Glide.with(mContext).load(R.mipmap.tpjiazai_shouyetp).override(MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 20), MyUtils.dip2px(mContext, 200)).into(holder.imgHouse);

                }
            } else if (title.equals(SHOUMAI)) {
                holder.propertyInfo.setVisibility(View.GONE);
                holder.zfInfo.setVisibility(View.VISIBLE);
                holder.imgIcon.setBackgroundResource(R.color.tag_red);
                holder.tvTitle.setText(mList.get(position).getHuxing() + "-" + mList.get(position).getTitle());
                holder.tvHuxing.setVisibility(View.GONE);
                holder.tvRemark.setText(mList.get(position).getRemark());
                holder.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        mContext.startActivity(new Intent(mContext, HouseListActivity.class)
                                .putExtra(HouseListActivity.TAG_PURPOSE, Constants.SELL));
                    }
                });

                if (mList.get(position).getImage() != null) {
                    Glide.with(mContext).load(mList.get(position).getImage()).override(MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 20), MyUtils.dip2px(mContext, 200)).error(R.mipmap.tpjiazai_shouyetp).into(holder.imgHouse);
                } else {
                    Glide.with(mContext).load(R.mipmap.tpjiazai_shouyetp).override(MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 20), MyUtils.dip2px(mContext, 200)).into(holder.imgHouse);

                }

            } else if (title.equals(CHUZU)) {
                holder.propertyInfo.setVisibility(View.GONE);
                holder.zfInfo.setVisibility(View.VISIBLE);
                holder.imgIcon.setBackgroundResource(R.color.tag_green);
                holder.tvTitle.setText(mList.get(position).getHuxing() + "-" + mList.get(position).getTitle());
                holder.tvHuxing.setVisibility(View.GONE);
                holder.tvRemark.setText(mList.get(position).getRemark());
                holder.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, HouseListActivity.class)
                                .putExtra(HouseListActivity.TAG_PURPOSE, Constants.LEASE));
                    }
                });

                if (mList.get(position).getImage() != null) {
                    Glide.with(mContext).load(mList.get(position).getImage()).override(MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 20), MyUtils.dip2px(mContext, 200)).error(R.mipmap.tpjiazai_shouyetp).into(holder.imgHouse);
                } else {
                    Glide.with(mContext).load(R.mipmap.tpjiazai_shouyetp).override(MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 20), MyUtils.dip2px(mContext, 200)).into(holder.imgHouse);

                }
            }
        } else if (title.equals(XINFANG)) {
            holder.linearCategory.setVisibility(View.GONE);
//            if (TextUtils.isEmpty(mList.get(position).getRemark())) {
//                holder.tvRemark.setVisibility(View.GONE);
//            } else {
//                holder.tvRemark.setText(mList.get(position).getRemark());
//            }
            holder.imgIcon.setBackgroundResource(R.color.tag_red);
            holder.tvTitle.setText(mList.get(position).getName());
            holder.propertyInfo.setVisibility(View.VISIBLE);
            holder.zfInfo.setVisibility(View.GONE);
//                holder.tvHuxing.setText(mList.get(position).getHuxing());
//                if (TextUtils.isEmpty(mList.get(position).getRemark())){
//                    holder.tvRemark.setVisibility(View.GONE);
//                }else {
//                    holder.tvRemark.setText(mList.get(position).getRemark());
//                }
            holder.propertyHuxing.setText(mList.get(position).getMinHuxing() + "-" + mList.get(position).getMaxHuxing() + "室");
            holder.propertyArea.setText(mList.get(position).getRoomsArea());
            holder.propertyLocation.setText(mList.get(position).getCity() + "市" + mList.get(position).getArea());
            holder.propertyPrice.setText(Html.fromHtml(mList.get(position).getAvgPrice() + "<small><font>元/㎡</font></small>"));
//                holder.propertyPrice.setText("元/平米");
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, NewHouseListActivity.class));
                }
            });
            if (mList.get(position).getHeadUrl() != null) {
                Glide.with(mContext).load(mList.get(position).getHeadUrl()).override(MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 20), MyUtils.dip2px(mContext, 200)).error(R.mipmap.tpjiazai_shouyetp).into(holder.imgHouse);
            } else {
                Glide.with(mContext).load(R.mipmap.tpjiazai_shouyetp).override(MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 20), MyUtils.dip2px(mContext, 200)).into(holder.imgHouse);

            }
        } else {
            holder.linearCategory.setVisibility(View.GONE);
            holder.propertyInfo.setVisibility(View.GONE);
            holder.tvTitle.setText(mList.get(position).getHuxing() + "-" + mList.get(position).getTitle());
            holder.tvHuxing.setVisibility(View.GONE);
            holder.tvRemark.setText(mList.get(position).getRemark());
            if (mList.get(position).getImage() != null) {
                Glide.with(mContext).load(mList.get(position).getImage()).override(MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 20), MyUtils.dip2px(mContext, 200)).error(R.mipmap.tpjiazai_shouyetp).into(holder.imgHouse);
            } else {
                Glide.with(mContext).load(R.mipmap.tpjiazai_shouyetp).override(MyUtils.getScreenWidth(mContext) - MyUtils.dip2px(mContext, 20), MyUtils.dip2px(mContext, 200)).into(holder.imgHouse);

            }
        }
//        if (position < size) {
//            holder.tvPriceUnit.setText("元/㎡");
//        } else {
//            holder.tvPriceUnit.setText("元/月");
//        }
        if (title.equals(XINFANG)) {
            holder.tvPriceUnit.setText("元/㎡");
        } else if (title.equals(SHOUMAI)) {
            holder.tvPriceUnit.setText("元/㎡");
        } else if (title.equals(CHUZU)) {
            holder.tvPriceUnit.setText("元/月");
        }
        holder.tvPrice.setText(String.valueOf(mList.get(position).getPrice()));


        holder.houseBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.equals(XINFANG)) {
                    mContext.startActivity(new Intent(mContext, NewHouseActivity.class)
                            .putExtra("id", mList.get(position).getId()));
                } else {
                    Intent intent = new Intent(mContext, HouseDetailActivity.class);
                    if (title.equals(SHOUMAI)) {
                        intent.putExtra(HouseDetailActivity.TAG_PURPOSE, Constants.SELL);
                    } else {
                        intent.putExtra(HouseDetailActivity.TAG_PURPOSE, Constants.LEASE);
                    }
                    intent.putExtra(HouseDetailActivity.ID, mList.get(position).getId());
                    mContext.startActivity(intent);
                }
            }
        });
        return convertView;
    }
}
