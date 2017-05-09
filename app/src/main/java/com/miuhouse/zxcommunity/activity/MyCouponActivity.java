package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.MyCouponAdapter;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.bean.MyCouponListBean;
import com.miuhouse.zxcommunity.bean.MyCouponsBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.StatusCompat;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的优惠券
 * Created by kings on 1/13/2016.
 */
public class MyCouponActivity extends BaseActivity implements MyCouponAdapter.OnCheckmarkClickListener {
    private static final String TAG = "MyCouponActivity";
    private static final int EVENT = 2;
    private static final int PAGE_SIZE = 50;
    private RecyclerView recyclerMyCoupon;
    private MyCouponAdapter myCouponAdapter;
    private List<MyCouponsBean> mList = new ArrayList<>();
    private String userId;

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("我的优惠券");
        tvTitle.setTextColor(Color.parseColor("#1E2129"));
        tvTitle.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_my_coupon);
        recyclerMyCoupon = (RecyclerView) findViewById(R.id.recycler_my_coupon);
        recyclerMyCoupon.setLayoutManager(new LinearLayoutManager(this));
        myCouponAdapter = new MyCouponAdapter(this, mList);
        myCouponAdapter.setOnCheckmarkItemClickListener(this);
        recyclerMyCoupon.setAdapter(myCouponAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(recyclerMyCoupon);
    }

    @Override
    public void initData() {
        sendRequestData();
    }

    @Override
    public void initVariables() {
        userId = MyApplication.getInstance().getUserBean().getId();
    }

    @Override
    public String getTag() {
        return TAG;
    }


    private void sendRequestData() {
        String url = FinalData.URL_VALUE + "myCouponList";
        Map<String, Object> map = new HashMap<>();
        map.put("ownerId", userId);
        map.put("page", 1);
        map.put("pageSize", PAGE_SIZE);
        GsonRequest<MyCouponListBean> request = new GsonRequest<>(Request.Method.POST, url, MyCouponListBean.class, map, new Response.Listener<MyCouponListBean>() {
            @Override
            public void onResponse(MyCouponListBean myCouponListBean) {
                if (myCouponListBean.getCode() == 0 && myCouponListBean.getMyCoupons() != null)
                    mList.addAll(myCouponListBean.getMyCoupons());
                myCouponAdapter.notifyDataSetChanged();
            }
        }, new ErrorCallback());
        request.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void sendRequestDeletedCoupon(final String id) {
        String url = FinalData.URL_VALUE + "myCouponDelete";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        GsonRequest<BaseBean> request = new GsonRequest<>(Request.Method.POST, url, BaseBean.class, map, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean baseBean) {
                ToastUtils.showToast(MyCouponActivity.this,baseBean.getMsg());
            }
        }, new ErrorCallback());
        request.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onCheckItemClick(int position) {

        startActivity(new Intent(this, DetailCouponActivity.class).putExtra("id", mList.get(position).getCouponId()));
    }

    ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
            int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
            Log.i("TAG", "fromPosition=" + fromPosition);
            Log.i("TAG", "toPosition=" + toPosition);
            if (fromPosition < toPosition) {
                //分别把中间所有的item的位置重新交换
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mList, i, i - 1);
                }
            }
            myCouponAdapter.notifyItemMoved(fromPosition, toPosition);
            //返回true表示执行拖动
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Log.i("TAG", "position=" + position);
            MyCouponsBean myCouponsBean = mList.get(position);
            mList.remove(position);

            myCouponAdapter.notifyItemRemoved(position);
            showSnackbar(recyclerMyCoupon, position, myCouponsBean);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //滑动时改变Item的透明度
                final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackbar(final View inputLayout, final int position, final MyCouponsBean myCouponsBean) {
        final Snackbar snackbar = Snackbar.make(inputLayout, "删除这条优惠券吗？", Snackbar.LENGTH_LONG);
        snackbar.show();
        snackbar.setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                mList.add(position, myCouponsBean);
                myCouponAdapter.notifyDataSetChanged();
            }
        });
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                Log.i("TAG", "onDismissed=" + event);
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    sendRequestDeletedCoupon(myCouponsBean.getId());
                }
            }
        });
    }

}
