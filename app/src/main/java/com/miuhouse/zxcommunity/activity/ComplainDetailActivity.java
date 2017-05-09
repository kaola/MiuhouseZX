package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.photo.GalleryActivity;
import com.miuhouse.zxcommunity.adapter.ComplainReplyAdapter;
import com.miuhouse.zxcommunity.adapter.ListItemImagesAdapter;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.bean.Complain;
import com.miuhouse.zxcommunity.bean.ComplainReply;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.widget.MyGridView;
import com.miuhouse.zxcommunity.widget.MyListView;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khb on 2016/12/21.
 */
public class ComplainDetailActivity extends BaseActivity {


    private TextView time;
    private TextView type;
    private TextView topic;
    private MyGridView images;
    private MyListView replyList;
    private ListItemImagesAdapter adapter;
    private ArrayList<String> imagesList;
    private List<ComplainReply> complainReplyList;
    private ComplainReplyAdapter replyAdapter;
    private String id;

    private final static int REQUEST_REPLY = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action:
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
        title.setText("详情");
        title.setTextColor(Color.parseColor("#1E2129"));
        title.setClickable(true);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_complaindetail);
        time = (TextView) findViewById(R.id.time);
        type = (TextView) findViewById(R.id.type);
        topic = (TextView) findViewById(R.id.topic);
        images = (MyGridView) findViewById(R.id.images);
        replyList = (MyListView) findViewById(R.id.complainList);
        imagesList = new ArrayList<>();
        adapter = new ListItemImagesAdapter(activity, imagesList);
        images.setAdapter(adapter);
        complainReplyList = new ArrayList<>();
        replyAdapter = new ComplainReplyAdapter(activity, complainReplyList);
        replyAdapter.setOnReplyClickListener(new ComplainReplyAdapter.OnReplyClickListener() {
            @Override
            public void onReplyClick(int position) {
                startActivityForResult(new Intent(activity, ReplyActivity.class)
                        .putExtra("id", complainReplyList.get(position).getRepairComplainId()),
                        REQUEST_REPLY);
            }
        });
        replyList.setAdapter(replyAdapter);
        getDetail();

    }

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }

    private void getDetail(){
        String url = FinalData.URL_VALUE + "repairComplainInfo";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        GsonRequest<ComplainDetailsBean> req = new GsonRequest<>(Request.Method.POST,
                url, ComplainDetailsBean.class, map,
                new Response.Listener<ComplainDetailsBean>() {
                    @Override
                    public void onResponse(ComplainDetailsBean complainDetailsBeans) {
                        setView(complainDetailsBeans.getRepairComplain(),
                                complainDetailsBeans.getRepairReplies());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast("服务器错误");
                    }
        }) ;
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    private void setView(Complain complain, List<ComplainReply> replys){
        setType(complain);
        setTime(complain);
        topic.setText(complain.getContent());
        if (!MyUtils.isEmptyList(complain.getImages())){
            imagesList.clear();
            imagesList.addAll(complain.getImages());
            if (imagesList.size()>1) {
                images.setNumColumns(3);
                images.setHorizontalSpacing(20);
            }else if (imagesList.size() == 1){
                images.setNumColumns(1);
                images.setHorizontalSpacing(20);
            }
            images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(activity, GalleryActivity.class)
                            .putStringArrayListExtra("imgPath", imagesList)
                            .putExtra("index", position));
                }
            });
            adapter.notifyDataSetChanged();
        }

        if (!MyUtils.isEmptyList(replys)){
            complainReplyList.clear();
            complainReplyList.addAll(replys);
            replyAdapter.notifyDataSetChanged();
        }

    }

    private void setType(Complain complain) {
        String strType = null;
        switch (complain.getType()){
            case Complain.REPAIR:
                strType = "报修";
                break;
            case Complain.COMPLAIN:
                strType = "投诉";
                break;
            case Complain.ADVICE:
                strType = "建议";
                break;
            case Complain.LIKE:
                strType = "表扬";
                break;

        }
        type.setText(strType);
    }

    private void setTime(Complain complain) {
        long time = complain.getCreateTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
        String strTime = formatter.format(new Date(time));
        this.time.setText(strTime);
    }

    class ComplainDetailsBean extends BaseBean{
        Complain repairComplain;
        List<ComplainReply> repairReplies;

        public Complain getRepairComplain() {
            return repairComplain;
        }

        public void setRepairComplain(Complain repairComplain) {
            this.repairComplain = repairComplain;
        }

        public List<ComplainReply> getRepairReplies() {
            return repairReplies;
        }

        public void setRepairReplies(List<ComplainReply> repairReplies) {
            this.repairReplies = repairReplies;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REPLY){
            if (resultCode == RESULT_OK){
                getDetail();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
