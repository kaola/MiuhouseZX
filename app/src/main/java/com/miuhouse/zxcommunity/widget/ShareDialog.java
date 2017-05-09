package com.miuhouse.zxcommunity.widget;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ShareDialog extends Dialog implements OnClickListener {
    private Activity context;
    private String shareUrl;
    private String shareTitle;
    private String shareImageUrl;


    public interface OnSharePlatformClick {
        void onPlatformClick(int id);
    }

    private OnSharePlatformClick mListener;

    public ShareDialog(Activity context, String url, String title, String shareImageUrl) {

        this(context, R.style.dialog_bottom);
        this.context = context;
        this.shareUrl = url;
        this.shareTitle = title;
        this.shareImageUrl = shareImageUrl;

    }

//    public ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//        //
//        init();
//    }

    public ShareDialog(Context context, int theme) {
        super(context, theme);
        init();
        //
    }

    private void init() {
        setCancelable(false);
        View shareView = getLayoutInflater().inflate(R.layout.dialog_cotent_share, null);
        shareView.findViewById(R.id.ly_share_qq).setOnClickListener(this);
        shareView.findViewById(R.id.ly_share_message).setOnClickListener(this);
        shareView.findViewById(R.id.ly_share_email).setOnClickListener(this);
        // shareView.findViewById(R.id.ly_share_sina_weibo).setOnClickListener(this);
        shareView.findViewById(R.id.ly_share_weichat).setOnClickListener(this);
        shareView.findViewById(R.id.ly_share_weichat_circle).setOnClickListener(this);

        super.setContentView(shareView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);
    }

    public void setOnPlatformClickListener(OnSharePlatformClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View v) {

        final int id = v.getId();
        // if (mListener != null) {
        // mListener.onPlatformClick(id);
        // }
        switch (id) {
            case R.id.ly_share_weichat_circle:
                shareToWeiChatCircle();
                break;
            case R.id.ly_share_weichat:
                shareToWeiChat();
                break;
            // case R.id.ly_share_sina_weibo:
            // shareToSinaWeibo();
            // break;
            case R.id.ly_share_qq:
                shareToQQ();
                break;
            case R.id.ly_share_message:
                addSMS();
                break;
            case R.id.ly_share_email:
                addEmail();
                break;
            default:
                break;
        }
        dismiss();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        this.dismiss();
    }

    @SuppressWarnings("deprecation")
    private void shareToWeiChatCircle() {

        ShareAction shareAction = new ShareAction(context)
                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                    }
                });
        shareAction.withText(shareTitle)
                .withTargetUrl(shareUrl)
                .withTitle(shareTitle);
        if (!MyUtils.isEmpty(shareImageUrl)) {
            shareAction.withMedia(new UMImage(context, shareImageUrl));
        } else {
            shareAction.withMedia(new UMImage(context, R.mipmap.ic_launcher));
        }
        shareAction.share();
    }

    @SuppressWarnings("deprecation")
    private void shareToWeiChat() {
        // 添加微信平台
        ShareAction weiChatShareAction = new ShareAction(context)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                    }
                });
        weiChatShareAction.withText(shareTitle)
                .withTargetUrl(shareUrl)
                .withTitle(shareTitle);
        if (!MyUtils.isEmpty(shareImageUrl)) {
            weiChatShareAction.withMedia(new UMImage(context, shareImageUrl));
        } else {
            weiChatShareAction.withMedia(new UMImage(context, R.mipmap.ic_launcher));
        }
        weiChatShareAction.share();

    }


    protected void shareToQQ() {
        ShareAction qqShareAction = new ShareAction(context)
                .setPlatform(SHARE_MEDIA.QQ)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                    }
                });
        Log.i("TAG", "shareImageUrl=" + shareImageUrl);
        qqShareAction.withText(shareTitle)
                .withTargetUrl(shareUrl)
                .withTitle(shareTitle);
        if (!MyUtils.isEmpty(shareImageUrl)) {
            qqShareAction.withMedia(new UMImage(context, shareImageUrl));
        }
        qqShareAction.share();
    }

    //
//    /**
//     * 添加短信平台</br>
//     */
    private void addSMS() {

        new ShareAction(context)
                .setPlatform(SHARE_MEDIA.SMS)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                    }
                })
                .withText(shareTitle)
                .withTargetUrl(shareUrl)
                .withTitle(shareTitle)
                .share();
    }

    //
//    /**
//     * 添加Email平台</br>
//     */
    private void addEmail() {
//        // 添加email

        new ShareAction(context)
                .setPlatform(SHARE_MEDIA.EMAIL)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                    }
                })
                .withText(shareTitle)
                .withTargetUrl(shareUrl)
                .withTitle(shareTitle)
                .share();
    }
}
