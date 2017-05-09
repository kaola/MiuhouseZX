package com.miuhouse.zxcommunity.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.BaoxiuActivity;
import com.miuhouse.zxcommunity.activity.BrowseActivity;
import com.miuhouse.zxcommunity.activity.CouponActivity;
import com.miuhouse.zxcommunity.activity.FunctionActivity;
import com.miuhouse.zxcommunity.activity.InformationActivity;
import com.miuhouse.zxcommunity.activity.LoginActivity;
import com.miuhouse.zxcommunity.activity.MainActivity;
import com.miuhouse.zxcommunity.activity.NeedHouseAactivity;
import com.miuhouse.zxcommunity.activity.NewHouseListActivity;
import com.miuhouse.zxcommunity.activity.NewsActivity;
import com.miuhouse.zxcommunity.activity.NewsListActivity;
import com.miuhouse.zxcommunity.activity.PropertyActivity;
import com.miuhouse.zxcommunity.activity.ReleaseHouseActivity;
import com.miuhouse.zxcommunity.activity.user.UserInfoActivity;
import com.miuhouse.zxcommunity.adapter.IndexAdapter;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.BannersBean;
import com.miuhouse.zxcommunity.bean.IndexBean;
import com.miuhouse.zxcommunity.bean.NewsInfoBean;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.L;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.MyListView;
import com.miuhouse.zxcommunity.widget.MyRoundImageView;
import com.miuhouse.zxcommunity.widget.NotCheckedPopupWindow;
import com.miuhouse.zxcommunity.widget.PublicNoticeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by kings on 1/6/2016.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private final static long PROPERTYID_DEFAULT = 4;

    private View view;

    private TextView tvTitle;

    private ListView lvIndex;

    private View listViewHeadView;

    private IndexAdapter adapter;
    //活动广告
    private ImageView imgHudong;

    private ViewPager vpBannerview;

    private LinearLayout linearBannerPoint;

    private List<ImageView> imgViewBanner;

    private TextView tvHour;

    private TextView tvMinute;

    private TextView tvSecond;

    private PublicNoticeView tvPublicNotice;

    //    private ArrayList<ZFZBean> list = new ArrayList<>();

    private List<BannersBean> bannerBean = new ArrayList<>();

    public static final int REQUSET_OK = 1;

    private int currentItem = 0;

    protected int lastPosition;

    private CountDownTimer timer;

    private int huodongID;

    /**
     * 物业电话号码
     */
    private String companyMobile;
    // 假如没有登录 服务返回默认的propertyID,和propertyName,用户登录使用用户的信息,选择小区显示 用户选择的小区
    //全局变量 propertyName 现在记录用户从小区列表选择的小区
    //    private String propertyName;

    /**
     * 计时器
     */
    private ScheduledExecutorService scheduledExecutorService;

    final Handler handler = new Handler() {

        @Override public void handleMessage(Message msg) {

            if (imgViewBanner != null && imgViewBanner.size() > 0) {
                vpBannerview.setCurrentItem(msg.arg1);
            }
        }
    };
    private LinearLayout content_container;
    private LinearLayout houselist_container;
    private MyListView listViewXF;
    private MyListView listViewSM;
    private MyListView listViewCZ;
    private long cityId;
    private String propertName;

    @Override public void initData() {

        if (MyUtils.isLoggedIn()) {
            request(MyApplication.getInstance().getUserBean().getPropertyId(), null, 0, 0);
        } else {
            //            request(PROPERTYID_DEFAULT, null);
            Lbs();
        }
    }

    public void Lbs() {

        AMapLocationClient mapLocationClient = new AMapLocationClient(context);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
            .setNeedAddress(true)
            .setOnceLocation(true)
            .setInterval(1000);
        mapLocationClient.setLocationOption(option);
        mapLocationClient.setLocationListener(new AMapLocationListener() {

            @Override public void onLocationChanged(AMapLocation aMapLocation) {

                if (aMapLocation != null) {
                    //                    if (aMapLocation.getErrorCode()==0){
                    //                    showLogD("Lat " + aMapLocation.getCity() + " ---- Long " + aMapLocation.getLongitude());
                    MyApplication.getInstance().setCity(aMapLocation.getCity().replace("市", ""));
                    request(PROPERTYID_DEFAULT, "御景东方", aMapLocation.getLongitude(),
                        aMapLocation.getLatitude());
                } else {
                    Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode()
                        + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                    request(PROPERTYID_DEFAULT, "御景东方", 0, 0);
                }

            }
        });
        mapLocationClient.startLocation();
    }

    @Override public View initView(LayoutInflater inflater) {

        view = inflater.inflate(R.layout.fragment_home, null);
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.titlebar);
        tvTitle = (TextView) mToolbar.findViewById(R.id.title);

        //显示地址图标
        ((ImageView) mToolbar.findViewById(R.id.img_address)).setVisibility(View.VISIBLE);
        tvTitle.setClickable(true);

        mToolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        tvTitle.setTextColor(Color.parseColor("#ffffff"));
        tvTitle.setClickable(true);

        content_container = (LinearLayout) view.findViewById(R.id.content_container);
        //        lvIndex = (ListView) view.findViewById(R.id.lv_index);
        houselist_container = (LinearLayout) view.findViewById(R.id.houselist_container);
        listViewHeadView = LayoutInflater.from(activity).inflate(R.layout.list_head_layout, null);

        imgHudong = (ImageView) listViewHeadView.findViewById(R.id.img_huodong);
        vpBannerview = (ViewPager) listViewHeadView.findViewById(R.id.vp_bannerview);
        linearBannerPoint =
            (LinearLayout) listViewHeadView.findViewById(R.id.linearLayout_banner_point);
        tvHour = (TextView) listViewHeadView.findViewById(R.id.roundTv_hour);
        tvMinute = (TextView) listViewHeadView.findViewById(R.id.roundTv_minute);
        tvSecond = (TextView) listViewHeadView.findViewById(R.id.roundTv_second);
        tvPublicNotice = (PublicNoticeView) listViewHeadView.findViewById(R.id.tv_public_notice);
        listViewHeadView.findViewById(R.id.linear_xiaoqu).setOnClickListener(this);
        listViewHeadView.findViewById(R.id.sellHouseNeed).setOnClickListener(this);
        listViewHeadView.findViewById(R.id.leaseHouseNeed).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.linear_more).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.buyHouse).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.rentHouse).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.sellHouse).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.leaseHouse).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.relative_news).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.linear_jiaofei).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.linear_baoxiu).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.linear_passport).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.linear_express).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.linear_glcs).setOnClickListener(this);

        listViewHeadView.findViewById(R.id.linear_snzx).setOnClickListener(this);

        content_container.addView(listViewHeadView, 0);

        tvTitle.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                startActivityForResult(new Intent(activity, PropertyActivity.class), REQUSET_OK);
                activity.overridePendingTransition(R.anim.slide_in_from_right,
                    R.anim.slide_out_to_right);
            }
        });

        listViewHeadView.findViewById(R.id.getQuan).setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                if (!MyUtils.isLoggedIn()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));

                } else {
                    getActivity().startActivity(new Intent(getActivity(), CouponActivity.class));
                }
            }

        });

        imgHudong.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                activity.startActivity(
                    new Intent(activity, BrowseActivity.class).putExtra(BrowseActivity.BROWSER_KEY,
                        FinalData.URL_HUODONG + huodongID).putExtra("title", "最新热点"));
            }
        });
        return view;
    }

    private void initBanner(int size) {

        if (imgViewBanner != null) {
            imgViewBanner.clear();
        } else {
            imgViewBanner = new ArrayList<>();
        }
        linearBannerPoint.removeAllViews();

        for (int i = 0; i < size; i++) {
            ImageView img = getImageView(size);
            ImageView point = new ImageView(getActivity());
            final LinearLayout.LayoutParams params = generateDefaultLayoutParams();
            params.rightMargin = 20;
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.point_bg);

            linearBannerPoint.addView(point);
            //防止选择小区回来，有连个pointView的enabled为true
            point.setEnabled(false);
            img.setOnClickListener(getOnClickListener(i, bannerBean));
            imgViewBanner.add(img);
        }
        vpBannerview.setAdapter(new MyAdapter(imgViewBanner));
        linearBannerPoint.getChildAt(0).setEnabled(true);
        vpBannerview.setOnPageChangeListener(new MyPageChangeListener());
    }

    private View.OnClickListener getOnClickListener(final int i,
        final List<BannersBean> bannerBeans) {

        return new View.OnClickListener() {

            @Override public void onClick(View arg0) {

                if (bannerBeans == null || bannerBeans.size() == 0) {
                    return;
                }
                activity.startActivity(
                    new Intent(activity, BrowseActivity.class).putExtra(BrowseActivity.BROWSER_KEY,
                        FinalData.URL_OTHER + bannerBeans.get(i).getId())
                        .putExtra("title", "最新热点"));

            }
        };
    }

    protected LayoutParams generateDefaultLayoutParams() {

        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    // 实例化一个imageView
    private ImageView getImageView(int i) {

        final ImageView imageView = new ImageView(activity);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    //Fragment 可见状态
    @Override public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
    }

    @Override public void onStart() {

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 5, 5, TimeUnit.SECONDS);
        super.onStart();
    }

    private class ScrollTask implements Runnable {

        public void run() {

            synchronized (vpBannerview) {
                if (imgViewBanner != null && imgViewBanner.size() > 0) {
                    currentItem = (currentItem + 1) % imgViewBanner.size();
                }

                Message message = new Message();
                message.arg1 = currentItem;
                handler.sendMessage(message);
            }

        }
    }

    private void startCountDownTime(long time) {
        /**
         * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
         * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
         * 在onDestroy()回调函数中 取消倒计时
         * 有onTick，onFinsh、cancel和start方法
         */

        if (time <= 0) {
            return;
        }
        timer = new CountDownTimer(time, 1000) {

            @Override public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
                String[] strArray = MyUtils.getDifference(millisUntilFinished).split(":");
                //                startCountDownTime(MyUtils.getCountDown(indexBean.getCoupon().getEndTime()));
                tvHour.setText(strArray[0]);
                tvMinute.setText(strArray[1]);
                tvSecond.setText(strArray[2]);
            }

            @Override public void onFinish() {

                Log.d("TAG", "onFinish -- 倒计时结束");
            }
        };
        timer.start();// 开始计时
        //timer.cancel(); // 取消
    }

    @Override public void onResume() {

        super.onResume();
    }

    @Override public void onStop() {

        Log.i("TAG", "onStop");
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    @Override public void onDestroy() {

        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }

    @Override public void onClick(View v) {

        switch (v.getId()) {
            case R.id.linear_xiaoqu:
                activity.startActivity(
                    new Intent(activity, NewsListActivity.class).putExtra(NewsListActivity.TITLE,
                        "正兴简介")
                        .putExtra(NewsListActivity.PROPERTYID,
                            SPUtils.getSPData(Constants.PROPERTYID, PROPERTYID_DEFAULT))
                        .putExtra(NewsListActivity.TYPE, 5));
                break;
            case R.id.sellHouseNeed:
                activity.startActivity(
                    new Intent(activity, NeedHouseAactivity.class).putExtra("tag", Constants.SELL));
                break;
            case R.id.leaseHouseNeed:
                activity.startActivity(
                    new Intent(activity, NeedHouseAactivity.class).putExtra("tag",
                        Constants.LEASE));
                break;
            case R.id.linear_more:
                //                activity.startActivity(new Intent(activity, HouseListActivity.class)
                //                        .putExtra(HouseListActivity.TAG_PURPOSE, Constants.SELL));
                activity.startActivity(
                    new Intent(activity, FunctionActivity.class).putExtra("cityId", cityId)
                        .putExtra("propertName", propertName)
                        .putExtra("companyMobile", companyMobile));

                break;
            case R.id.buyHouse:
                activity.startActivity(
                    new Intent(activity, BrowseActivity.class).putExtra(BrowseActivity.BROWSER_KEY,
                        FinalData.URL_HEAD + "/mobile/xqwy/" + SPUtils.getSPData(
                            Constants.PROPERTYID, PROPERTYID_DEFAULT))
                        .putExtra("title", "小区无忧")
                        .putExtra("shareContent", propertName));
                break;
            case R.id.rentHouse:
                activity.startActivity(
                    new Intent(activity, NewHouseListActivity.class).putExtra("propertyId",
                        SPUtils.getSPData(Constants.PROPERTYID, PROPERTYID_DEFAULT))
                        .putExtra("locX", 0d)
                        .putExtra("locY", 0d));
                break;
            case R.id.sellHouse:
                if (MyUtils.isLoggedIn() && AccountDBTask.getUserBean().getStatus() != 1) {
                    showCheck();
                    return;
                }
                activity.startActivity(new Intent(activity, ReleaseHouseActivity.class).putExtra(
                    ReleaseHouseActivity.TAG_FUNCTON, Constants.SELL));
                break;
            case R.id.leaseHouse:
                if (MyUtils.isLoggedIn() && AccountDBTask.getUserBean().getStatus() != 1) {
                    showCheck();
                    return;
                }
                activity.startActivity(new Intent(activity, ReleaseHouseActivity.class).putExtra(
                    ReleaseHouseActivity.TAG_FUNCTON, Constants.LEASE));
                break;
            case R.id.relative_news:
                Log.i("TAG", "cityId" + cityId);
                activity.startActivity(
                    new Intent(activity, NewsActivity.class).putExtra("cityId", cityId));
                break;

            case R.id.linear_jiaofei:
                activity.startActivity(
                    new Intent(activity, NewsListActivity.class).putExtra(NewsListActivity.TITLE,
                        "社区动态")
                        .putExtra(NewsListActivity.PROPERTYID,
                            SPUtils.getSPData(Constants.PROPERTYID, PROPERTYID_DEFAULT))
                        .putExtra(NewsListActivity.TYPE, 6));
                break;
            case R.id.linear_baoxiu:
                //                if (!MyUtils.isLoggedIn()) {
                //                    startActivity(new Intent(activity, LoginActivity.class));
                //                } else {
                //                    activity.startActivity(new Intent(activity, BaoxiuActivity.class));
                //                }
                //                activity.startActivity(new Intent(activity, NewHouseActivity.class)
                //                    .putExtra("id", MyApplication.getInstance().getUserBean().getPropertyId()));
                ToastUtils.showToast(getActivity(), "暂未开通");
                break;
            case R.id.linear_passport:
                if (!MyUtils.isLoggedIn()) {
                    startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    activity.startActivity(new Intent(activity, BaoxiuActivity.class));
                }
                //                if (!MyUtils.isLoggedIn()) {
                //                    startActivity(new Intent(activity, LoginActivity.class));
                //                } else {
                //                    if (MyApplication.getInstance().getUserBean().getStatus() == 0) {
                //                        ToastUtils.showToast(activity, "审核中");
                //                    } else if (MyApplication.getInstance().getUserBean().getStatus() == -1) {
                //                        ToastUtils.showToast(activity, "审核不通过");
                //                    } else if (MyApplication.getInstance().getUserBean().getStatus() == -2) {
                //                        ToastUtils.showToast(activity, "禁用");
                //                    } else {
                //                        activity.startActivity(new Intent(activity, PassportActivity.class));
                //                    }
                //
                //                }
                break;
            case R.id.linear_express:
                //                activity.startActivity(new Intent(activity, ExpressActivity.class));
                activity.startActivity(
                    new Intent(activity, NewsActivity.class).putExtra("cityId", cityId));
                break;
            case R.id.linear_glcs:
                activity.startActivity(
                    new Intent(activity, InformationActivity.class).putExtra("type",
                        Constants.TYPE_GLCS));
                break;
            case R.id.linear_snzx:
                activity.startActivity(
                    new Intent(activity, InformationActivity.class).putExtra("type",
                        Constants.TYPE_SNZX));
                break;
        }
    }

    private void showCheck() {

        final NotCheckedPopupWindow popup = new NotCheckedPopupWindow(activity, view);
        popup.setOnMainClickListener(new NotCheckedPopupWindow.OnMainClickListener() {

            @Override public void onClick() {

                startActivity(new Intent(activity, UserInfoActivity.class));
                popup.dismiss();
            }
        });
        popup.show();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MainActivity.REQUSET_OK) {
            if (resultCode == MainActivity.RESULT_OK) {
                currentItem = 0;
                lastPosition = 0;
                String propertyName = data.getStringExtra("property");
                long propertyId = data.getLongExtra("propertyID", 0);
                request(propertyId, propertyName, 0, 0);

                ((NotificationFragment) FragmentFactory.getFragment(
                    Constants.NOTIFICATION)).initData();
            }
        }
    }

    private void request(final long propertyId, final String propertyName, double locX,
        double locY) {

        propertName = propertyName;
        String url = FinalData.URL_VALUE + "index";
        Map<String, Object> map = new HashMap<>();
        Log.i("TAG", "propertyId=" + propertyId);
        if (propertyId > PROPERTYID_DEFAULT) {
            map.put("propertyId", propertyId);
        } else if (propertyId == PROPERTYID_DEFAULT && locX != 0 && locY != 0) {
            //            map.put("locX", locX);
            //            map.put("locY", locY);
            map.put("propertyId", propertyId);
        }
        //        没登录时传IMEI号，后台注册一个临时环信账号，账号即IMEI
        if (!MyUtils.isLoggedIn()) {
            map.put("imeis", MyApplication.getInstance().getIMEI());
        }
        GsonRequest<IndexBean> request =
            new GsonRequest<>(Request.Method.POST, url, IndexBean.class, map,
                new Response.Listener<IndexBean>() {

                    @Override public void onResponse(IndexBean indexBean) {
                        //                将返回的propertyId保存本地
                        //                如果有返回的propertyId，说明请求时没有传propertyId，就保存
                        //                如果没有返回的propertyId，说明请求前已经有传，就保存传的propertyId
                        if (indexBean.getPropertyId() > 0) {
                            SPUtils.saveSPData(Constants.PROPERTYID, indexBean.getPropertyId());
                        } else {
                            SPUtils.saveSPData(Constants.PROPERTYID, propertyId);
                        }
                        cityId = indexBean.getCityId();
                        companyMobile = indexBean.getCompanyMobile();
                        fillUI(indexBean, propertyName);

                        //                list.clear();
                        bannerBean.clear();
                        //租房和售房放在一个ListView 前面放的是售房，后面是租房
                        listViewXF = new MyListView(activity);
                        listViewSM = new MyListView(activity);
                        listViewCZ = new MyListView(activity);
                        IndexAdapter adapterXF =
                            new IndexAdapter(getActivity(), indexBean.getNewPropertyInfos(),
                                IndexAdapter.XINFANG);
                        IndexAdapter adapterSM =
                            new IndexAdapter(getActivity(), indexBean.getEsfs(),
                                IndexAdapter.SHOUMAI);
                        IndexAdapter adapterCZ =
                            new IndexAdapter(getActivity(), indexBean.getZfs(), IndexAdapter.CHUZU);
                        listViewXF.setAdapter(adapterXF);
                        listViewSM.setAdapter(adapterSM);
                        listViewCZ.setAdapter(adapterCZ);
                        houselist_container.removeAllViews();
                        houselist_container.addView(listViewXF);
                        houselist_container.addView(listViewSM);
                        houselist_container.addView(listViewCZ);

                        bannerBean.addAll(indexBean.getBanners());
                        initBanner(indexBean.getBanners().size());
                        for (int i = 0; i < indexBean.getBanners().size(); i++) {
                            // TOP 广告原先是170dp BOTTOM广告原先是140dp
                            Glide.with(getActivity())
                                .load(indexBean.getBanners().get(i).getImage())
                                .fitCenter()
                                .override(MyUtils.getScreenWidth(getActivity()),
                                    (MyUtils.dip2px(activity, 200)))
                                .into(imgViewBanner.get(i));
                        }
                        if (!MyUtils.isLoggedIn()) {
                            loginTempHX();
                        } else {
                            //                二次刷新公告页面，因为第一次没拿到propertyId，很无奈
                            ((NotificationFragment) FragmentFactory.getFragment(
                                Constants.NOTIFICATION)).getNotificationList();
                        }
                    }
                }, new Response.ErrorListener() {

                @Override public void onErrorResponse(VolleyError volleyError) {

                }
            });
        request.setTag(MainActivity.TAG);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void loginTempHX() {
        //        后台注册临时环信ID是用手机IMEI号
        EMClient.getInstance()
            .login(MyApplication.getInstance().getIMEI(), Constants.PASSWORD, new EMCallBack() {

                @Override public void onSuccess() {

                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                    boolean updatenick = EMClient.getInstance()
                        .updateCurrentUserNick(MyApplication.currentUserNick.trim());
                    if (!updatenick) {
                        L.i(getClass().getName(), "update current user nick fail");
                    }
                    //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
                    //                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                    L.i("temp hx logged in");
                }

                @Override public void onError(int i, String s) {

                    L.i("Fuck No! " + s);
                }

                @Override public void onProgress(int i, String s) {

                }
            });
    }

    private void fillUI(IndexBean indexBean, final String propertyName) {

        int size = 0;
        //新闻头条 轮播数据集，list中包含社会和小区新闻
        List<NewsInfoBean> mListCont = new ArrayList<>();
        for (int i = 0; i < indexBean.getIndexImages().size(); i++) {
            switch (indexBean.getIndexImages().get(i).getType()) {
                case 1:
                    ((TextView) listViewHeadView.findViewById(R.id.tv_coupon_title)).setText(
                        indexBean.getIndexImages().get(i).getTitle());
                    ((TextView) listViewHeadView.findViewById(R.id.tv_coupon_description)).setText(
                        indexBean.getIndexImages().get(i).getDescription());
                    RelativeLayout getquan =
                        (RelativeLayout) listViewHeadView.findViewById(R.id.getQuan);
                    TextView tvGetQuan = (TextView) listViewHeadView.findViewById(R.id.tv_getQuan);
                    if (indexBean.getCoupon() != null) {
                        Glide.with(getActivity())
                            .load(indexBean.getCoupon().getHeadUrl())
                            .error(R.mipmap.default_error)
                            .into(
                                (MyRoundImageView) listViewHeadView.findViewById(R.id.img_coupon));
                        getquan.setBackgroundResource(R.color.bg_red);
                        tvGetQuan.setText("立即抢券");
                        ((MyRoundImageView) listViewHeadView.findViewById(
                            R.id.img_coupon)).setVisibility(View.VISIBLE);
                        listViewHeadView.findViewById(R.id.defaultCoupon).setVisibility(View.GONE);
                    } else {
                        ((MyRoundImageView) listViewHeadView.findViewById(
                            R.id.img_coupon)).setVisibility(View.GONE);
                        listViewHeadView.findViewById(R.id.defaultCoupon)
                            .setVisibility(View.VISIBLE);
                        tvGetQuan.setText("暂无优惠");
                    }
                    break;

                case 5:
                    ((TextView) listViewHeadView.findViewById(R.id.tv_sell_house_need)).setText(
                        indexBean.getIndexImages().get(i).getTitle());
                    ((TextView) listViewHeadView.findViewById(
                        R.id.tv_sell_house_need_description)).setText(
                        indexBean.getIndexImages().get(i).getDescription());
                    Glide.with(getActivity())
                        .load(indexBean.getIndexImages().get(i).getImage())
                        .override(MyUtils.dip2px(getActivity(), 78),
                            MyUtils.dip2px(getActivity(), 50))
                        .into((ImageView) listViewHeadView.findViewById(R.id.img_sell_house_need));

                    break;

                case 6:
                    ((TextView) listViewHeadView.findViewById(R.id.tv_let_title_need)).setText(
                        indexBean.getIndexImages().get(i).getTitle());
                    ((TextView) listViewHeadView.findViewById(
                        R.id.tv_let_description_need)).setText(
                        indexBean.getIndexImages().get(i).getDescription());
                    Glide.with(getActivity())
                        .load(indexBean.getIndexImages().get(i).getImage())
                        .override(MyUtils.dip2px(getActivity(), 78),
                            MyUtils.dip2px(getActivity(), 50))
                        .into((ImageView) listViewHeadView.findViewById(R.id.img_let_need));

                    break;
                case 3:
                    ((TextView) listViewHeadView.findViewById(R.id.tv_sell_house)).setText(
                        indexBean.getIndexImages().get(i).getTitle());
                    ((TextView) listViewHeadView.findViewById(
                        R.id.tv_sell_house_description)).setText(
                        indexBean.getIndexImages().get(i).getDescription());
                    Glide.with(getActivity())
                        .load(indexBean.getIndexImages().get(i).getImage())
                        .override(MyUtils.dip2px(getActivity(), 78),
                            MyUtils.dip2px(getActivity(), 50))
                        .into((ImageView) listViewHeadView.findViewById(R.id.img_sell_house));

                    break;

                case 4:
                    ((TextView) listViewHeadView.findViewById(R.id.tv_let_title)).setText(
                        indexBean.getIndexImages().get(i).getTitle());
                    ((TextView) listViewHeadView.findViewById(R.id.tv_let_description)).setText(
                        indexBean.getIndexImages().get(i).getDescription());
                    Glide.with(getActivity())
                        .load(indexBean.getIndexImages().get(i).getImage())
                        .override(MyUtils.dip2px(getActivity(), 78),
                            MyUtils.dip2px(getActivity(), 50))
                        .into((ImageView) listViewHeadView.findViewById(R.id.img_let));
                    break;
            }
        }
        //        ArrayList<NewsInfoBean> list = new ArrayList<>();
        //        for (int i = 0; i < 6; i++) {
        //            NewsInfoBean newsInfoBean = new NewsInfoBean();
        //            newsInfoBean.setTitle("你好啊=" + i);
        //            list.add(newsInfoBean);
        //        }
        //        indexBean.setCrawNewsInfo(list);
        //社会新闻和小区新闻的教程集合
        mListCont.addAll(MyUtils.getReplaceList(indexBean));

        tvPublicNotice.SetPublicNotices(mListCont, cityId);
        if (propertyName != null) {
            tvTitle.setText(propertyName);
        } else if (MyApplication.getInstance().getUserBean() != null) {
            tvTitle.setText(MyApplication.getInstance().getUserBean().getPropertyName());

        } else {
            tvTitle.setText(indexBean.getPropertyName());

        }
        if (indexBean.getCoupon() != null) {
            startCountDownTime(MyUtils.getCountDown(indexBean.getCoupon().getEndTime()));
        }
        if (indexBean.getHuodongs() == null) {
            imgHudong.setVisibility(View.GONE);
        } else {
            imgHudong.setVisibility(View.VISIBLE);
            huodongID = indexBean.getHuodongs().getId();
            Glide.with(getActivity())
                .load(indexBean.getHuodongs().getHeadUrl())
                .placeholder(R.mipmap.tpjiazai_huodong)
                .override(MyUtils.getScreenWidth(getActivity()) - MyUtils.dip2px(getActivity(), 20),
                    MyUtils.dip2px(getActivity(), 70))
                .into(imgHudong);
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageSelected(int position) {

            currentItem = position;
            position = position % imgViewBanner.size();
            linearBannerPoint.getChildAt(position).setEnabled(true);
            linearBannerPoint.getChildAt(lastPosition).setEnabled(false);
            lastPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    private class MyAdapter extends PagerAdapter {

        private List<ImageView> imageViewsListS;

        public MyAdapter(List<ImageView> imageViewsList) {
            // TODO Auto-generated constructor stub
            this.imageViewsListS = imageViewsList;
        }

        @Override public int getCount() {

            return imageViewsListS.size();
        }

        @Override public Object instantiateItem(View arg0, int arg1) {

            ((ViewPager) arg0).addView(imageViewsListS.get(arg1));
            return imageViewsListS.get(arg1);
        }

        @Override public void destroyItem(View arg0, int arg1, Object arg2) {

            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override public Parcelable saveState() {

            return null;
        }

    }

}
