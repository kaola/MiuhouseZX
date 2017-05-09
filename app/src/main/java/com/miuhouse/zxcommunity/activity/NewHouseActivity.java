package com.miuhouse.zxcommunity.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.photo.GalleryActivity;
import com.miuhouse.zxcommunity.adapter.DiscountAdapter;
import com.miuhouse.zxcommunity.adapter.HuxingAdapter;
import com.miuhouse.zxcommunity.adapter.OtherPropertyAdapter;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.bean.Discount;
import com.miuhouse.zxcommunity.bean.Huxing;
import com.miuhouse.zxcommunity.bean.PropertyInfo;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.http.CustomStringRequest;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.AutoColumnLinearLayout;
import com.miuhouse.zxcommunity.widget.MyGridView;
import com.miuhouse.zxcommunity.widget.MyListView;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/4/26.
 */
public class NewHouseActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout detail;
    private int height;
    private int actualHeight;
    private int offsetHeight;
    private ViewGroup nonetwork;
    private ScrollView container;
    private ImageView mIvHouse;
    private TextView mTvImgPosition;
    private TextView mTvHouseTitle;
    private TextView mTvPriceTotal;
    private TextView mTvHouseLocation;
    private TextView mTvKaifashang;
    private TextView mTvKaipanshijian;
    private TextView mTvJiaofangshi;
    private TextView mTvWuyeleixing;
    private TextView mTvChanquannianxian;
    private TextView mTvJianzhuleixing;
    private TextView mTvZhuangxiuqingkuang;
    private TextView mTvJianzhumianji;
    private TextView mTvZhandimianji;
    private TextView mTvRongjilv;
    private TextView mTvGuihuahushu;
    private TextView mTvCheweishu;
    private TextView mTvwuyegongsi;
    private TextView mTvWuyefei;
    private TextView mTvShoulouchu;
    private ImageView mLocationImg;
    private TextView mTvLvhualv;
    private AutoColumnLinearLayout tagList;
    private TextView title;

    /**
     * 分享按钮
     */
    private TextView tvShare;

    private MyGridView mOtherList;
    private String id;
    private RelativeLayout mDiscount;
    private MyListView mDiscountList;
    private MyListView mHuxingList;
    private PropertyInfo property;
    private List<Discount> discountList;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        title = (TextView) findViewById(R.id.title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
//            case R.id.action:
//                startActivityForResult(new Intent(activity, ReleaseHouseActivity.class)
//                        .putExtra(ReleaseHouseActivity.TAG_FUNCTON, mTag)
//                        .putExtra(ReleaseHouseActivity.HOUSE, house)
//                        , REQ_UPDATE);
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_newhouse);
        nonetwork = (ViewGroup) findViewById(R.id.nonetwork);
        nonetwork.setVisibility(View.GONE);
        nonetwork.findViewById(R.id.iv_reload).setOnClickListener(this);

        container = (ScrollView) findViewById(R.id.scrollView);
        mIvHouse = (ImageView) findViewById(R.id.iv_house);
        mTvImgPosition = (TextView) findViewById(R.id.img_position);
        mTvHouseTitle = (TextView) findViewById(R.id.tv_house_title);
        mTvPriceTotal = (TextView) findViewById(R.id.tv_house_price_total);
        TextView mTvPricePer = (TextView) findViewById(R.id.tv_house_price_per);
        mTvHouseLocation = (TextView) findViewById(R.id.tv_house_location);
        tagList = (AutoColumnLinearLayout) findViewById(R.id.tagList);
        RelativeLayout location = (RelativeLayout) findViewById(R.id.locationLayout);
        location.setOnClickListener(this);

        final RelativeLayout expand = (RelativeLayout) findViewById(R.id.expandCollapse);
        detail = (LinearLayout) findViewById(R.id.property_detail);
//        测量楼盘详情view的实际高度，和被隐藏高度
        ViewTreeObserver observer = detail.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = expand.getHeight();
                actualHeight = getActualHeight();
                offsetHeight = actualHeight - height;
                detail.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        detail.measure(0, 0);
        int measuredHeight = detail.getHeight();
        RelativeLayout expandToggle = (RelativeLayout) findViewById(R.id.expandToggle);
//        设置展开效果
        final ImageView mIvExpand = (ImageView) findViewById(R.id.iv_expand);
        expandToggle.setOnClickListener(new View.OnClickListener() {
            boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                height = expand.getHeight();
                final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) expand.getLayoutParams();
                int duration = 500;

                Animation animation = null;
                if (!isExpanded) {
                    RotateAnimation animationExpand = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animationExpand.setDuration(duration);
                    animationExpand.setFillAfter(true);
                    mIvExpand.startAnimation(animationExpand);
                    animation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            params.height = (int) (height + (interpolatedTime * offsetHeight));
                            expand.setLayoutParams(params);
                        }
                    };
                    isExpanded = true;
                } else {
                    RotateAnimation animationCollapse = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animationCollapse.setDuration(duration);
                    animationCollapse.setFillAfter(true);
                    mIvExpand.startAnimation(animationCollapse);
                    animation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            params.height = (int) (height - (interpolatedTime * offsetHeight));
                            expand.setLayoutParams(params);
                        }
                    };
                    isExpanded = false;
                }
                animation.setDuration(duration);
                expand.startAnimation(animation);
            }
        });

        mTvKaifashang = (TextView) findViewById(R.id.tv_kaifashang);
        mTvKaipanshijian = (TextView) findViewById(R.id.tv_kaipanshijian);
        mTvJiaofangshi = (TextView) findViewById(R.id.tv_jiaofangshijian);
        mTvWuyeleixing = (TextView) findViewById(R.id.tv_wuyeleixing);
        mTvChanquannianxian = (TextView) findViewById(R.id.tv_chanquannianxian);
        mTvJianzhuleixing = (TextView) findViewById(R.id.tv_jianzhuleixing);
        mTvZhuangxiuqingkuang = (TextView) findViewById(R.id.tv_zhuangxiuqingkuang);
        mTvJianzhumianji = (TextView) findViewById(R.id.tv_jianzhumianji);
        mTvZhandimianji = (TextView) findViewById(R.id.tv_zhandimianji);
        mTvRongjilv = (TextView) findViewById(R.id.tv_rongjilv);
        mTvLvhualv = (TextView) findViewById(R.id.tv_lvhualv);
        mTvGuihuahushu = (TextView) findViewById(R.id.tv_guihuahushu);
        mTvCheweishu = (TextView) findViewById(R.id.tv_cheweishu);
        mTvwuyegongsi = (TextView) findViewById(R.id.tv_wuyegongsi);
        mTvWuyefei = (TextView) findViewById(R.id.tv_wuyefei);
        mTvShoulouchu = (TextView) findViewById(R.id.tv_shoulouchu);

        mLocationImg = (ImageView) findViewById(R.id.locationImg);

        tvShare = (TextView) findViewById(R.id.share);

        mOtherList = (MyGridView) findViewById(R.id.otherList);
//        猜你喜欢列表刷新后页面会跳到猜你喜欢顶部，
//        这里解决方案是先将列表隐藏，到滚到底部时再显示列表
        //或许不让列表获取焦点也可以
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        int offY = v.getScrollY();
                        int visibleHeight = v.getHeight();
                        int actualHeight = container.getChildAt(0).getMeasuredHeight();
                        if ((offY + visibleHeight) == actualHeight) {
                            mOtherList.setVisibility(View.VISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int offYy = v.getScrollY();
                        int visibleHeightt = v.getHeight();
                        int actualHeightt = container.getChildAt(0).getMeasuredHeight();
                        if ((offYy + visibleHeightt) == actualHeightt) {
                            mOtherList.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                return false;
            }
        });

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.handleShare(NewHouseActivity.this, "http://zx.miuhouse.com/mobile/newPropertyInfo/"
                        + property.getId(), property.getName() + " " + property.getAvgPrice() + "元/㎡起", (property.getImages() != null && property.getImages().size() > 0) ? property.getImages().get(0):null);
            }
        });
        mDiscount = (RelativeLayout) findViewById(R.id.discount);
        mDiscountList = (MyListView) findViewById(R.id.discountList);
        RelativeLayout mHuxing = (RelativeLayout) findViewById(R.id.huxing);
        mHuxingList = (MyListView) findViewById(R.id.huxingList);

    }

    private int getActualHeight() {
        int childCount = detail.getChildCount();
        ViewGroup firstChild = (ViewGroup) detail.getChildAt(0);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) firstChild.getLayoutParams();
        int firstHeight = params.topMargin + params.bottomMargin + firstChild.getHeight();
        int i = 1;
        int height1 = 0;
        while (detail.getChildAt(i) != null) { //没有一直循环下去而是只取一个值的原因是：未显示出来的childView没有高度
            if (detail.getChildAt(i) instanceof ViewGroup) {
                height1 = detail.getChildAt(i).getHeight();
                break;
            }
            i++;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) detail.getLayoutParams();
        int topMargin = layoutParams.topMargin;
        int bottomMargin = layoutParams.bottomMargin;
        int margin = topMargin + bottomMargin;
        int contentHeight = margin + height1 * (childCount - 1);
        int detailHeight = contentHeight + firstHeight;
        return detailHeight;
    }

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
        getNewPropertyInfo();
    }

    private void getNewPropertyInfo() {
        final ProgressFragment progress = ProgressFragment.newInstance();
        progress.setMessage(getResources().getString(R.string.loading));
        progress.show(getSupportFragmentManager(), getClass().getName());

        String url = FinalData.URL_VALUE + "newPropertyInfo";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        GsonRequest<NewHouseBean> request = new GsonRequest<NewHouseBean>(Request.Method.POST, url, NewHouseBean.class, map,
                new Response.Listener<NewHouseBean>() {
                    @Override
                    public void onResponse(NewHouseBean newHouse) {
                        showLogD(newHouse.toString());
                        progress.dismiss();
                        if (newHouse != null && newHouse.getCode() == 0) {
                            fillView(newHouse);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progress.dismiss();
                        if (!MyUtils.isNetworkConnected(context)) {
                            findViewById(R.id.nonetwork).setVisibility(View.VISIBLE);
                            findViewById(R.id.scrollView).setVisibility(View.GONE);
                            findViewById(R.id.consult).setVisibility(View.GONE);
                            return;
                        }
                        nonetwork.setVisibility(View.VISIBLE);
                        findViewById(R.id.consult).setVisibility(View.GONE);
                        container.setVisibility(View.GONE);
                        ((TextView) nonetwork.findViewById(R.id.tv_main)).setText(getResources().getString(R.string.request_failed));
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fillView(NewHouseBean newHouseBean) {
        property = newHouseBean.getNewProperty();
        mTvKaifashang.setText(property.getDevelopers());
        mTvKaipanshijian.setText(property.getStartTime());
        mTvJiaofangshi.setText(getStr(property.getLaunchTime()));
        mTvWuyeleixing.setText(getStr(property.getType()));
        mTvChanquannianxian.setText(property.getCqnx() + "");
        mTvJianzhuleixing.setText(getStr(property.getJzlx()));
        mTvZhuangxiuqingkuang.setText(getStr(property.getZxqk()));
        mTvJianzhumianji.setText(property.getJzmj() + "平米");
        mTvZhandimianji.setText(property.getZdmj() + "平米");
        mTvRongjilv.setText(property.getRjl() + "");
        mTvLvhualv.setText(property.getLhl() * 100 + "%");
        mTvGuihuahushu.setText(property.getGhhs() + "");
        mTvCheweishu.setText(property.getCws() + "");
        mTvwuyegongsi.setText(property.getCompany());
        mTvWuyefei.setText(property.getWyf() + "元/平米/月");
        mTvShoulouchu.setText(property.getSlc());
//        显示标签
        ArrayList<String> labels = property.getLabel();
        if (!MyUtils.isEmptyList(labels)) {
            for (int i = 0; i < labels.size(); i++) {
                TextView tag = createTag(labels.get(i), i);
                tagList.addView(tag);
            }
        }
        if (!MyUtils.isEmptyList(property.getImages())) {
            Glide.with(this).load(getStr(property.getImages().get(0)))
                    .placeholder(R.mipmap.default_error_big)
                    .into(mIvHouse);
        }
        mIvHouse.setOnClickListener(this);
        mTvImgPosition.setText("1/" + property.getImages().size());
        mTvPriceTotal.setText(Html.fromHtml(property.getAvgPrice() + "<small><small><font>元/㎡起</font></small></small>"));
        mTvHouseTitle.setText(getStr(property.getRemark()));
        title.setText(getStr(property.getName()));
        mTvHouseLocation.setText("地址：" + getStr(property.getArea() + property.getStreet()));
        Glide.with(this).load(getStr(property.getMapUrl()))
                .placeholder(R.mipmap.default_error_big)
                .into(mLocationImg);
        ((TextView) findViewById(R.id.surrounding)).setText("楼盘地址：" + getStr(property.getCity() + property.getArea() + property.getStreet()));

        discountList = newHouseBean.getDiscounts();
        if (MyUtils.isEmptyList(discountList)) {
            mDiscount.setVisibility(View.GONE);
            findViewById(R.id.getDiscount).setEnabled(false);
        } else {
            DiscountAdapter dAdapter = new DiscountAdapter(this, discountList);
            mDiscountList.setAdapter(dAdapter);
            backToTheTop();
            mDiscountList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(activity, GetDiscountActivity.class)
                            .putExtra("discounts", (Serializable) discountList)
                            .putExtra("property", property));
                }
            });
            findViewById(R.id.getDiscount).setEnabled(true);
        }
        final List<Huxing> huxingList = newHouseBean.getNewHuxings();
        if (MyUtils.isEmptyList(huxingList)) {
            mHuxingList.setVisibility(View.GONE);
        } else {
            HuxingAdapter hAdapter = new HuxingAdapter(this, huxingList);
            mHuxingList.setAdapter(hAdapter);
            backToTheTop();
            mHuxingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(activity, HuxingActivity.class)
                            .putExtra("id", huxingList.get(position).getId())
                            .putExtra("discounts", (Serializable) discountList)
                            .putExtra("property", property));
                }
            });
        }

        getOtherProperties();
    }

    private void backToTheTop() {
//        页面自动定位到listview，采用重新让scrollview定位回首位来解决，但是用户可能会察觉
        container.post(new Runnable() {
            @Override
            public void run() {
                container.scrollTo(0, 0);
            }
        });
    }

    private String getStr(String s) {
        return s == null ? "" : s;
    }

    private void getOtherProperties() {

        String url = FinalData.URL_VALUE + "newPropertyOther";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
//        map.put("cityId", id);
//        map.put("area", id);
//        map.put("apartment", id);
        map.put("page", 1);
        map.put("pageSize", 6);

        GsonRequest<OtherProperties> request = new GsonRequest<>(Request.Method.POST, url, OtherProperties.class, map,
                new Response.Listener<OtherProperties>() {
                    @Override
                    public void onResponse(final OtherProperties otherProperties) {
                        if (otherProperties.getCode() == 0
                                && otherProperties != null
                                && !MyUtils.isEmptyList(otherProperties.getNewPropertyInfos())) {
                            OtherPropertyAdapter oAdapter = new OtherPropertyAdapter(activity, otherProperties.getNewPropertyInfos());
                            mOtherList.setAdapter(oAdapter);
                            backToTheTop();
                            mOtherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startActivity(new Intent(activity, NewHouseActivity.class)
                                            .putExtra("id", otherProperties.getNewPropertyInfos().get(position).getId()));
                                    finish();
                                }
                            });
                        } else {
                            findViewById(R.id.guessContainer).setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        findViewById(R.id.guessContainer).setVisibility(View.GONE);
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private TextView createTag(String stringTag, int i) {
        TextView tag = new TextView(this);
        tag.setText(stringTag);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tag.setLayoutParams(params);
        tag.setPadding(MyUtils.dip2px(this, 2), 0, MyUtils.dip2px(this, 2), 0);
        int mod = i % 4;
        switch (mod) {
            case 1:
                tag.setBackgroundResource(R.drawable.shape_housetag_red);
                tag.setTextColor(this.getResources().getColor(R.color.tag_red));
                break;
            case 2:
                tag.setBackgroundResource(R.drawable.shape_housetag_purple);
                tag.setTextColor(this.getResources().getColor(R.color.tag_purple));
                break;
            case 3:
                tag.setBackgroundResource(R.drawable.shape_housetag_blue);
                tag.setTextColor(this.getResources().getColor(R.color.tag_blue));
                break;
            case 0:
                tag.setBackgroundResource(R.drawable.shape_housetag_green);
                tag.setTextColor(this.getResources().getColor(R.color.tag_green));
                break;
        }
//        tag.setTextSize(12);
        return tag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_house:
                startActivity(new Intent(activity, GalleryActivity.class)
                        .putStringArrayListExtra("imgPath", property.getImages()));
                break;
            case R.id.iv_reload:
                nonetwork.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
                getNewPropertyInfo();
                break;
            case R.id.locationLayout:
                startActivity(new Intent(activity, MapActivity.class)
                        .putExtra(MapActivity.LocX, property.getLocY())
                        .putExtra(MapActivity.LocY, property.getLocX())
                        .putExtra(MapActivity.TITLE, property.getName()));
                break;
        }
    }

    public void getDiscount(View v) {
        startActivity(new Intent(activity, GetDiscountActivity.class)
                .putExtra("discounts", (Serializable) discountList)
                .putExtra("property", property));
    }

    public void call(View v) {
        if (property != null && property.getMobile() != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    Toast.makeText(this, "权限已被禁止，要想重新开启，请在手机的权限管理中找到" + getResources().getString(R.string.app_name) + "应用，找到拨打电话权限并选择允许", Toast.LENGTH_LONG).show();
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + property.getMobile());
                intent.setData(data);
                startActivity(intent);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + property.getMobile());
                intent.setData(data);
                startActivity(intent);
            } else {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void notify(View v) {
        if (!MyUtils.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            String id = AccountDBTask.getUserBean().getId();
            notifyDiscount(id, property);
        }
    }

    private void notifyDiscount(String id, PropertyInfo property) {
        final ProgressFragment progress = ProgressFragment.newInstance();
        progress.setMessage(getResources().getString(R.string.Is_sending_a_request));
        progress.setCancelable(false);
        progress.show(getSupportFragmentManager(), getClass().getName());

        String url = FinalData.URL_VALUE + "discountNotice";
        Map<String, Object> map = new HashMap<>();
        map.put("ownerId", id);
        map.put("newPropertyId", property.getId());
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, url, map,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progress.dismiss();
                        showToast(getResources().getString(R.string.request_sent_successfully));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progress.dismiss();
                        showToast(getResources().getString(R.string.request_failed));
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void moreHuxing(View v) {
        startActivity(new Intent(this, HuxingListActivity.class)
                .putExtra(HuxingListActivity.PROPERTY, property));
    }

    private class NewHouseBean extends BaseBean {
        List<Discount> discounts;
        List<Huxing> newHuxings;
        PropertyInfo newProperty;

        public List<Discount> getDiscounts() {
            return discounts;
        }

        public void setDiscounts(List<Discount> discounts) {
            this.discounts = discounts;
        }

        public List<Huxing> getNewHuxings() {
            return newHuxings;
        }

        public void setNewHuxings(List<Huxing> newHuxings) {
            this.newHuxings = newHuxings;
        }

        public PropertyInfo getNewProperty() {
            return newProperty;
        }

        public void setNewProperty(PropertyInfo newProperty) {
            this.newProperty = newProperty;
        }
    }

    private class OtherProperties extends BaseBean {
        List<PropertyInfo> newPropertyInfos;

        public List<PropertyInfo> getNewPropertyInfos() {
            return newPropertyInfos;
        }

        public void setNewPropertyInfos(List<PropertyInfo> newPropertyInfos) {
            this.newPropertyInfos = newPropertyInfos;
        }
    }


    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }
}
