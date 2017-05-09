package com.miuhouse.zxcommunity.activity;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.fragment.BuyHouseFragment;
import com.miuhouse.zxcommunity.fragment.RentingHouseFrxagment;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.widget.StatusCompat;

/**
 * 我的需求
 * Created by kings on 1/20/2016.
 */
public class NeedHouseAactivity extends BaseActivity {
    public static final String TAG="NeedHouseAactivity";
    private TextView tvBuyHouse;
    private TextView tvRenting;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private BuyHouseFragment buyHouseFragment;
    private RentingHouseFrxagment rentingHouseFrxagment;
    private int tag;

    @Override
    public void initView() {
        setContentView(R.layout.activity_need_home);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        if (tag == Constants.SELL) {
            buyHouseFragment = new BuyHouseFragment();
            ft.replace(R.id.fl_make_money_container, buyHouseFragment);
        }else{
            rentingHouseFrxagment = new RentingHouseFrxagment();
            ft.replace(R.id.fl_make_money_container, rentingHouseFrxagment);
        }
        ft.commit();
    }

    @Override
    public void initTitle() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.titlebar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(mToolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(tag == Constants.SELL ? "我要买房":"我要租房" );

    }


    @Override
    public void initData() {
        tag = getIntent().getIntExtra("tag", Constants.SELL);
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return TAG;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
