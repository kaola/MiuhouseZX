package com.miuhouse.zxcommunity.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.bean.Notification;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by khb on 2016/3/18.
 */
public class NotificationDetailActivity extends BaseActivity{

    private TextView mNotificationTitle;
    private TextView mTime;
    private WebView mNotificationContent;
    private TextView mSender;

    public final static String NOTIFICATION = "notification";
    private Notification notification;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.titlebar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back_black);
        setSupportActionBar(toolbar);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("公告");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_notificationdetail);
        mNotificationTitle = (TextView) findViewById(R.id.notificationTitle);
        mTime = (TextView) findViewById(R.id.time);
        mSender = (TextView) findViewById(R.id.tvSenderName);
        mNotificationContent = (WebView) findViewById(R.id.notificationContent);
        if (notification!= null){
            mNotificationTitle.setText(notification.getTitle());
            long sendTime = Long.parseLong(notification.getSendTime());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formatTime = format.format(new Date(sendTime));
            mTime.setText(formatTime);
//            showContent();
        }
    }

    private void showContent() {
        String html = "<html><head><meta http-equiv=\"content-type\" content=\"textml\"; charset=UTF-8\" /><style type=\"text/css\"></style></head>"
                + "<body>%s</body></html>";
        String htmlStr = String.format(html, notification.getContent());
        mNotificationContent.loadDataWithBaseURL(null, htmlStr, "text/html", "utf-8", null);
    }

    @Override
    public void initData() {
        notification = (Notification) getIntent().getSerializableExtra(NOTIFICATION);
        getDetail();

    }

    private void getDetail() {
        String url = FinalData.URL_VALUE + "noticeInfo";
        Map<String, Object> map = new HashMap<>();
        map.put("id", notification.getId());
        GsonRequest<NoticeBean> request = new GsonRequest<>(Request.Method.POST, url, NoticeBean.class, map,
                new Response.Listener<NoticeBean>() {
                    @Override
                    public void onResponse(NoticeBean noticeBean) {
                        if (noticeBean.getCode() == 0){
                            if (noticeBean.getNoticeMsg() != null){
                                notification = noticeBean.getNoticeMsg();
                                showContent();
                            }else{
                                showToast("未找到公告内容");
                            }
                        }else{
                            showToast("未找到公告内容");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast("查找公告时出错");
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private class NoticeBean extends BaseBean{
        Notification noticeMsg;

        public Notification getNoticeMsg() {
            return noticeMsg;
        }

        public void setNoticeMsg(Notification noticeMsg) {
            this.noticeMsg = noticeMsg;
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
