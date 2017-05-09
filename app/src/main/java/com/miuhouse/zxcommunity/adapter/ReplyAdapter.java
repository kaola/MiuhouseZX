package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.ComplainDetailActivity;
import com.miuhouse.zxcommunity.bean.ComplainReply;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by khb on 2016/12/23.
 */
public class ReplyAdapter extends BaseRVAdapter<ComplainReply> {
    public ReplyAdapter(Activity mContext, List<ComplainReply> mList) {
        super(mContext, mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReplyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_reply, null));
    }

    class ReplyHolder extends RecyclerView.ViewHolder{

        TextView reply_a;
        TextView reply_b;
        LinearLayout content;

        public ReplyHolder(View itemView) {
            super(itemView);
            reply_a = (TextView) itemView.findViewById(R.id.reply_a);
            reply_b = (TextView) itemView.findViewById(R.id.reply_b);
            content = (LinearLayout) itemView.findViewById(R.id.content);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReplyHolder mholder = (ReplyHolder) holder;
        final ComplainReply reply = mList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("（MM-dd HH:mm）");
        mholder.reply_a.setText(Html.fromHtml("<font color='#40beff'>物业回复：</font>   "
                + "<small><font color='#848484'>"+format.format(new Date(reply.getCreateTime())) + "</font></small>"));
        mholder.reply_b.setText(reply.getContent());
        mholder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ComplainDetailActivity.class)
                    .putExtra("id", reply.getRepairComplainId()));
            }
        });
    }
}
