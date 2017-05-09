package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.photo.GalleryActivity;
import com.miuhouse.zxcommunity.adapter.HuxingAdapter;
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
import com.miuhouse.zxcommunity.widget.MyGridView;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/5/3.
 */
public class HuxingActivity extends BaseActivity {

    private String id;
    private ImageView mIvHuxing;
    private ScrollView container;
    private TextView mTvImgPosition;
    private TextView mTvHuxingTitle;
    private TextView mTvPriceTotal;
    private ViewGroup nonetwork;
    private TextView mTvZx;
    private TextView mTvDfl;
    private TextView mTvCx;
    private TextView mDesc;
    private MyGridView mOtherList;
    private TextView title;
    private TextView tvShare;
    private String propertyName;
    private Huxing huxing;
    private List<Discount> discountList;
    private PropertyInfo property;


    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        title = (TextView) findViewById(R.id.title);
        tvShare = (TextView) findViewById(R.id.share);
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.handleShare(HuxingActivity.this, "http://zx.miuhouse.com/mobile/newHuxing/"
                        + huxing.getId() + "/" + huxing.getNewPropertyId(), mTvHuxingTitle.getText().toString(), (huxing.getImages() != null && huxing.getImages().size() > 0) ? huxing.getImages().get(0) : null);
            }

            ;
        });
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
        setContentView(R.layout.activity_huxing);
        nonetwork = (ViewGroup) findViewById(R.id.nonetwork);
        nonetwork.setVisibility(View.GONE);

        container = (ScrollView) findViewById(R.id.scrollView);
        mIvHuxing = (ImageView) findViewById(R.id.iv_house);
        mTvImgPosition = (TextView) findViewById(R.id.img_position);
        mTvHuxingTitle = (TextView) findViewById(R.id.tv_house_title);
        mTvPriceTotal = (TextView) findViewById(R.id.tv_house_price_total);

        mTvZx = (TextView) findViewById(R.id.tvZx);
        mTvDfl = (TextView) findViewById(R.id.tvDfl);
        mTvCx = (TextView) findViewById(R.id.tvCx);

        mDesc = (TextView) findViewById(R.id.desc);

        RelativeLayout mOther = (RelativeLayout) findViewById(R.id.guessContainer);
        mOtherList = (MyGridView) findViewById(R.id.otherList);
    }

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
        discountList = (List<Discount>) getIntent().getSerializableExtra("discounts");
        property = (PropertyInfo) getIntent().getSerializableExtra("property");
        getHuxingInfo(id);
    }

    private void getHuxingInfo(String id) {
        final ProgressFragment progress = ProgressFragment.newInstance();
        progress.setMessage(getResources().getString(R.string.loading));
        progress.show(getSupportFragmentManager(), getClass().getName());

        String url = FinalData.URL_VALUE + "newHuxingInfo";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        GsonRequest<HuxingBean> request = new GsonRequest<HuxingBean>(Request.Method.POST, url, HuxingBean.class, map,
                new Response.Listener<HuxingBean>() {
                    @Override
                    public void onResponse(HuxingBean huxingBean) {
                        progress.dismiss();
                        if (huxingBean.getCode() == 0 && huxingBean != null) {
                            fillView(huxingBean);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progress.dismiss();
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fillView(HuxingBean huxingBean) {
        huxing = huxingBean.getNewHuxing();
        title.setText(property.getName());
        mTvHuxingTitle.setText(huxing.getTitle() + " " + huxing.getApartment() + " " + huxing.getArea() + "平米");
        mTvPriceTotal.setText(huxing.getPrice() + "元/平米");
        mTvCx.setText(huxing.getCx());
        mTvDfl.setText(huxing.getDfl() + "%");
        mTvZx.setText(huxing.getZxqk());
        if (!MyUtils.isEmptyList(huxing.getImages())) {
            mTvImgPosition.setText("1/" + huxing.getImages().size());
            Glide.with(this).load(huxing.getImages().get(0))
                    .placeholder(R.mipmap.default_error_big)
                    .into(mIvHuxing);
            mIvHuxing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, GalleryActivity.class)
                            .putExtra("imgPath", huxing.getImages()));
                }
            });
        }
        mDesc.setText(huxing.getComment());
        getOtherHuxing(huxing);
    }

    private void getOtherHuxing(Huxing huxing) {
        String url = FinalData.URL_VALUE + "newHuxingOthers";
        Map<String, Object> map = new HashMap<>();
        map.put("newPropertyId", huxing.getNewPropertyId());
        map.put("id", huxing.getId());
        map.put("page", 1);
        map.put("pageSize", 10);
        GsonRequest<HuxingListBean> request = new GsonRequest<>(Request.Method.POST, url, HuxingListBean.class, map,
                new Response.Listener<HuxingListBean>() {
                    @Override
                    public void onResponse(final HuxingListBean otherBean) {
                        if (otherBean.getCode() == 0 && !MyUtils.isEmptyList(otherBean.getNewHuxings())) {
                            HuxingAdapter hAdapter = new HuxingAdapter(activity, otherBean.getNewHuxings());
                            mOtherList.setAdapter(hAdapter);
                            mOtherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    startActivity(new Intent(activity, HuxingActivity.class)
                                            .putExtra("id", otherBean.getNewHuxings().get(position).getId())
                                            .putExtra("property", property));
                                }
                            });
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void getDiscount(View v) {
        startActivity(new Intent(activity, GetDiscountActivity.class)
                .putExtra("discounts", (Serializable) discountList)
                .putExtra("property", property));
    }

    public void call(View v) {
        if (property != null && property.getMobile() != null) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + property.getMobile());
            intent.setData(data);
            startActivity(intent);
        }
    }

    public void notify(View v) {
        if (!MyUtils.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            String id = AccountDBTask.getUserBean().getId();
            notifyDiscount(id, huxing.getNewPropertyId());
        }
    }

    private void notifyDiscount(String id, String propertyId) {
        final ProgressFragment progress = ProgressFragment.newInstance();
        progress.setMessage(getResources().getString(R.string.Is_sending_a_request));
        progress.setCancelable(false);
        progress.show(getSupportFragmentManager(), getClass().getName());

        String url = FinalData.URL_VALUE + "discountNotice";
        Map<String, Object> map = new HashMap<>();
        map.put("ownerId", id);
        map.put("newPropertyId", propertyId);
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


    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }

    private class HuxingBean extends BaseBean {
        Huxing newHuxing;

        public Huxing getNewHuxing() {
            return newHuxing;
        }

        public void setNewHuxing(Huxing newHuxing) {
            this.newHuxing = newHuxing;
        }
    }

    public class HuxingListBean extends BaseBean {
        List<Huxing> newHuxings;

        public List<Huxing> getNewHuxings() {
            return newHuxings;
        }

        public void setNewHuxings(List<Huxing> newHuxings) {
            this.newHuxings = newHuxings;
        }
    }
}
