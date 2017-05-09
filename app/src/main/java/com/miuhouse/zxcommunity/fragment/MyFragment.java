package com.miuhouse.zxcommunity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.BrowseActivity;
import com.miuhouse.zxcommunity.activity.FeedbackActivity;
import com.miuhouse.zxcommunity.activity.LoginActivity;
import com.miuhouse.zxcommunity.activity.MyCouponActivity;
import com.miuhouse.zxcommunity.activity.MyHousesActivity;
import com.miuhouse.zxcommunity.activity.SettingActivity;
import com.miuhouse.zxcommunity.activity.user.UserInfoActivity;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.UserBean;
import com.miuhouse.zxcommunity.bean.UserBeanInfo;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.NotCheckedPopupWindow;
import com.miuhouse.zxcommunity.widget.RoundTextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kings on 1/6/2016.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_CODE = 2;
    private RelativeLayout relativeMeAccount;
    private View view;
    private AppCompatActivity context;
    private String userId;
    private TextView tvTitle;
    private TextView tvName;
    private ImageView imgAddress;
    private ImageView imgAvatar;
    private RoundTextView tvHind;
    private UserBean user;

    /**
     * 登录成功
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(LoginActivity.INTENT_ACTION_USER_CHANGE)) {
                user = AccountDBTask.getUserBean();
            } else if (action.equals(Constants.INTENT_ACTION_LOGOUT)) {
                user = null;
            }
            fillView();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(LoginActivity.INTENT_ACTION_USER_CHANGE);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    /**
     * initData 在initView 的前面
     */
    @Override
    public void initData() {

    }

    public void getIsCheck(String userId) {
        String urlPath = FinalData.URL_VALUE + "isCheck";
        Map<String, Object> params = new HashMap<>();
        params.put("ownerId", userId);
        GsonRequest<UserBeanInfo> custom =
            new GsonRequest<>(Request.Method.POST, urlPath, UserBeanInfo.class, params,
                getListener(), getErrorListener());
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(custom);
    }

    private Response.Listener<UserBeanInfo> getListener() {
        return new Response.Listener<UserBeanInfo>() {
            @Override
            public void onResponse(UserBeanInfo userBean) {
                if (userBean.getUserInfo() != null) {
                    if (userBean.getCode() == 0) {
                        AccountDBTask.saveUserBean(userBean.getUserInfo());
                        MyApplication.getInstance().setmUserBean(userBean.getUserInfo());
                        user = userBean.getUserInfo();
                    }
                } else {
                    ToastUtils.showToast(context, userBean.getMsg());
                }
                fillView();
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError) {
                    ToastUtils.showToast(context, "请求超时");
                } else if (volleyError instanceof NoConnectionError) {
                    ToastUtils.showToast(context, "没有网络连接");
                } else if (volleyError instanceof ServerError) {
                    ToastUtils.showToast(context, "服务器异常 登录失败");
                }
                fillView();
            }
        };
    }

    @Override
    public View initView(LayoutInflater inflater) {

        view = inflater.inflate(R.layout.fragment_my, null);
        //        Toolbar mToolbar = ((MainActivity) getActivity()).getmToolbar();
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.titlebar);

        tvTitle = (TextView) mToolbar.findViewById(R.id.title);
        imgAddress = (ImageView) mToolbar.findViewById(R.id.img_address);
        mToolbar.setTitle("");
        context.setSupportActionBar(mToolbar);
        //        StatusCompat.compat(context, StatusCompat.COLOR_TITLE);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setClickable(true);
        imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvHind = (RoundTextView) view.findViewById(R.id.tv_hint);
        relativeMeAccount = (RelativeLayout) view.findViewById(R.id.relative_me_account);
        view.findViewById(R.id.relative_me_coupon).setOnClickListener(this);
        view.findViewById(R.id.relative_me_feedback).setOnClickListener(this);
        view.findViewById(R.id.relative_setting).setOnClickListener(this);
        relativeMeAccount.setOnClickListener(this);
        view.findViewById(R.id.relative_sell_house).setOnClickListener(this);
        view.findViewById(R.id.relative_let_house).setOnClickListener(this);
        view.findViewById(R.id.relative_me_share).setOnClickListener(this);
        view.findViewById(R.id.relative_me_instrustion).setOnClickListener(this);
        //填充view
        user = AccountDBTask.getUserBean();
        if (user != null) {
            userId = user.getId();
            getIsCheck(userId);
        } else {
            fillView();
        }
        return view;
    }

    /**
     * 填充View
     */
    private void fillView() {
        if (user != null) {
            Glide.with(getActivity())
                .load(user.getHeadUrl())
                .centerCrop()
                .override(MyUtils.dip2px(getActivity(), 55), MyUtils.dip2px(getActivity(), 55))
                .into(imgAvatar);
            tvName.setText(user.getNickName());
            tvHind.setText(userStatus(user.getStatus()));
        } else {
            Glide.with(getActivity())
                .load(R.mipmap.me_toux_moren)
                .centerCrop()
                .override(MyUtils.dip2px(getActivity(), 55), MyUtils.dip2px(getActivity(), 55))
                .into(imgAvatar);
            tvName.setText("未登录");
            tvHind.setText("登录/注册");
        }
    }

    private String userStatus(int status) {
        String strStatus = null;
        switch (status) {
            case UserBean.UNCHECKED:
                strStatus = "审核中";
                tvHind.getDelegate()
                    .setBackgroundColor(getResources().getColor(R.color.login_bg_red));
                break;
            case UserBean.CHECKED:
                strStatus = "资料已验证";
                tvHind.getDelegate().setBackgroundColor(getResources().getColor(R.color.text_a6));
                break;
            case UserBean.CHECKED_NOT_OK:
                strStatus = "资料未通过验证";
                tvHind.getDelegate()
                    .setBackgroundColor(getResources().getColor(R.color.login_bg_red));

                break;
            case UserBean.BANNED:
                strStatus = "禁用";
                tvHind.getDelegate()
                    .setBackgroundColor(getResources().getColor(R.color.login_bg_red));
                break;
            case UserBean.NEED_CHECKED:
                strStatus = "资料未认证";
                tvHind.getDelegate()
                    .setBackgroundColor(getResources().getColor(R.color.login_bg_red));
                break;
        }
        return strStatus;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (AppCompatActivity) context;
    }

    /**
     * fragment 使用activity toolbar 当当前fragment可见的时候 title换成"我"
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            tvTitle.setText("我");
            imgAddress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 当用户尚未登录时 点击进入登录界面
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.relative_me_account:

                isGotoLoginActivity(UserInfoActivity.class);
                break;
            case R.id.relative_me_coupon:
                isGotoLoginActivity(MyCouponActivity.class);

                break;
            case R.id.relative_me_feedback:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.relative_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.relative_sell_house:
                if (MyUtils.isLoggedIn()
                    && AccountDBTask.getUserBean().getStatus() != UserBean.CHECKED) {
                    showCheck();
                    return;
                }
                startActivity(new Intent(activity, MyHousesActivity.class)
                    .putExtra(MyHousesActivity.TAG_SELL_OR_RENT, Constants.SELL)
                    .putExtra(MyHousesActivity.USERID, userId));
                break;
            case R.id.relative_let_house:
                if (MyUtils.isLoggedIn()
                    && AccountDBTask.getUserBean().getStatus() != UserBean.CHECKED) {
                    showCheck();
                    return;
                }
                startActivity(new Intent(activity, MyHousesActivity.class)
                    .putExtra(MyHousesActivity.TAG_SELL_OR_RENT, Constants.LEASE)
                    .putExtra(MyHousesActivity.USERID, userId));
                break;
            case R.id.relative_me_share:
                MyUtils.handleShare(getActivity(),
                    "http://a.app.qq.com/o/simple.jsp?pkgname=com.miuhouse.zxcommunity",
                    "瞄房正兴社区", null);
                break;
            case R.id.relative_me_instrustion:
                Intent intent = new Intent(activity, BrowseActivity.class);
                intent.putExtra(BrowseActivity.BROWSER_KEY, FinalData.URL_EXPLAIN);
                intent.putExtra("title", "使用说明");
                startActivity(intent);
                break;
        }
    }

    private void isGotoLoginActivity(Class activity) {

        if (user != null) {
            startActivity(new Intent(getActivity(), activity));
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            //            startActivityForResult(new Intent(getActivity(), LoginActivity.class), REQUEST_CODE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Log.i("TAG", "resultCode");
                initData();
            }
        }
    }

    private void showCheck() {

        final NotCheckedPopupWindow popup = new NotCheckedPopupWindow(activity, view);
        popup.setOnMainClickListener(new NotCheckedPopupWindow.OnMainClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(activity, UserInfoActivity.class));
                popup.dismiss();
            }
        });
        popup.show();
    }
}
