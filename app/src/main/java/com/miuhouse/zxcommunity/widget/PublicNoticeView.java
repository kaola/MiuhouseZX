package com.miuhouse.zxcommunity.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.NewsActivity;
import com.miuhouse.zxcommunity.bean.NewsInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kings on 2/18/2016.
 */
public class PublicNoticeView extends LinearLayout {

    private static final String TAG = "WSL";
    private Context mcontext;
    private ViewFlipper viewFlipper;
    private View scrollTitleView;

    private List<NewsInfoBean> newsTitles = new ArrayList<>();
    private long mCityId;

    public PublicNoticeView(Context context) {

        super(context);
        mcontext = context;
        init();
    }

    public PublicNoticeView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
        mcontext = context;
        init();
    }

    public PublicNoticeView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        mcontext = context;
        init();
    }

    // 初始化自定义布局
    public void bindLinearLayout() {

        scrollTitleView = LayoutInflater.from(mcontext).inflate(R.layout.ll, null);
        LayoutParams params =
            new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        addView(scrollTitleView, params);

        viewFlipper = (ViewFlipper) scrollTitleView.findViewById(R.id.flipper_scrollTitle);
        viewFlipper.setInAnimation(
            AnimationUtils.loadAnimation(mcontext, R.anim.slide_in_from_bottom));
        viewFlipper.setOutAnimation(
            AnimationUtils.loadAnimation(mcontext, R.anim.slide_in_from_top));
    }

    private void init() {

        bindLinearLayout();

    }

    /**
     * 网络请求后返回公告内容进行适配
     */

    private void bindNotices() {

        viewFlipper.removeAllViews();
        int i = 0;
        while (i < newsTitles.size()) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_news, null);
            TextView tvContent = (TextView) view.findViewById(R.id.tv_news_content);
            TextView tvCategory = (TextView) view.findViewById(R.id.tv_category);
            tvContent.setSingleLine(true);
            tvContent.setEllipsize(TextUtils.TruncateAt.END);
            tvContent.setText(newsTitles.get(i).getTitle());
            if (newsTitles.get(i).getNewsType() == 0) {
                tvCategory.setText("社会");
            } else {
                tvCategory.setText("小区");
            }

            FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            view.setOnClickListener(
                new NoticeTitleOnClickListener(mcontext, newsTitles.get(i).getNewsType()));
            viewFlipper.addView(view, lp);
            i++;
        }
        //viewFlipper自动播放,
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        if (viewFlipper.isAutoStart() && !viewFlipper.isFlipping()) {
            viewFlipper.startFlipping();
        }
    }

    /**
     * @param news 新闻列表
     */
    public void SetPublicNotices(List<NewsInfoBean> news, long cityId) {

        this.mCityId = cityId;
        newsTitles = news;
        bindNotices();
    }

    /**
     * 公告title监听
     *
     * @author wsl
     */
    class NoticeTitleOnClickListener implements OnClickListener {

        private Context context;
        private int titleid;

        public NoticeTitleOnClickListener(Context context, int whichText) {

            this.context = context;
            this.titleid = whichText;
        }

        public void onClick(View v) {
            // TODO Auto-generated method stub
            context.startActivity(
                new Intent(context, NewsActivity.class).putExtra("newType", titleid)
                    .putExtra("cityId", mCityId));
        }

    }

    /**
     * 显示notice的具体内容
     */
    public void disPlayNoticeContent(Context context, String titleid) {
        // TODO Auto-generated method stub
        Toast.makeText(context, titleid, Toast.LENGTH_SHORT).show();
    }
}
