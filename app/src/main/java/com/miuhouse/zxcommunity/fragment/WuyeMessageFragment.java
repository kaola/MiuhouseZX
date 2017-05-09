package com.miuhouse.zxcommunity.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.LoginActivity;
import com.miuhouse.zxcommunity.activity.WuyeReplyActivity;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;

/**
 * Created by khb on 2016/12/19.
 */
public class WuyeMessageFragment extends BaseFragment implements View.OnClickListener {

    private ViewGroup emptyView;
    private SwipeRefreshLayout mRefresh;
    private RecyclerView list;
    private RelativeLayout wuyeReply;
    private TextView replyCount;

    @Override
    public void initData() {

    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_wuyemessage, null);
        wuyeReply = (RelativeLayout) view.findViewById(R.id.wuyeReply);
        replyCount = (TextView) view.findViewById(R.id.unreadWuyeReply);
        replyCount.setVisibility(View.GONE);
        wuyeReply.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wuyeReply:
                if (!MyUtils.isLoggedIn()) {
                    startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    startActivity(new Intent(context, WuyeReplyActivity.class));
                }
                break;
        }
    }

    public void refresh() {
        int count = SPUtils.getSPData(Constants.REPLYCOUNT, 0);
//        TextView replyCount = (TextView) getView().findViewById(R.id.unreadWuyeReply);
        if (count > 0){    //有未读
            replyCount.setVisibility(View.VISIBLE);
            replyCount.setText(count+"");
        }else {
            replyCount.setVisibility(View.GONE);
        }
    }

}
