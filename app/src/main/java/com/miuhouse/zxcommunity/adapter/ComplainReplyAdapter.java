package com.miuhouse.zxcommunity.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.ComplainReply;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by khb on 2016/12/22.
 */
public class ComplainReplyAdapter extends BaseAdapter{

    private List<ComplainReply> replyList;
    private Context context;
    private boolean isFirstWuyeReply;
    private long lastTime;

    public ComplainReplyAdapter(Context context, List<ComplainReply> replyList){
        this.context = context;
        this.replyList = replyList;
        isFirstWuyeReply = true;
    }

    @Override
    public int getCount() {
        return replyList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        LinearLayout wuyeReply;
        TextView wuyeTitle;
        TextView replyButton;
        TextView wuyeReplyContent;
        LinearLayout myReply;
        TextView myReplyContent;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final ComplainReply reply = replyList.get(position);
        List<ComplainReply> copyList = new ArrayList<>();
        copyList.addAll(replyList);
        Collections.sort(copyList, new Comparator<ComplainReply>() {
            @Override
            public int compare(ComplainReply lhs, ComplainReply rhs) {
                if (lhs.getCreateTime() < rhs.getCreateTime()){
                    return 1;
                }else if (lhs.getCreateTime() == rhs.getCreateTime()){
                    return  0;
                }else {
                    return -1;
                }
            }
        });
        for (ComplainReply complainReply :
                copyList) {
            if (TextUtils.isEmpty(complainReply.getOwnerId())){
                lastTime = complainReply.getCreateTime();
                break;
            }
        }
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_complainreply, null);
            holder.wuyeReply = (LinearLayout) convertView.findViewById(R.id.wuyeReply);
            holder.wuyeTitle = (TextView) convertView.findViewById(R.id.wuyeTitle);
            holder.replyButton = (TextView) convertView.findViewById(R.id.replyButton);
            holder.wuyeReplyContent = (TextView) convertView.findViewById(R.id.wuyeReplyContent);
            holder.myReply = (LinearLayout) convertView.findViewById(R.id.myReply);
            holder.myReplyContent = (TextView) convertView.findViewById(R.id.myReplyContent);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.replyButton.setVisibility(View.GONE);
        if (TextUtils.isEmpty(reply.getOwnerId())){     //物业的回复
            holder.wuyeReply.setVisibility(View.VISIBLE);
            holder.myReply.setVisibility(View.GONE);
            holder.wuyeReplyContent.setText(reply.getContent());
//            列表是最新回复在最上
            if (lastTime == reply.getCreateTime()){      //判断是否是第一个物业回复，即最近的物业回复，如果是，显示回复按钮
                holder.replyButton.setVisibility(View.VISIBLE);
//                isFirstWuyeReply = false;
            }
            long time = reply.getCreateTime();
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
            String strTime = formatter.format(new Date(time));
            holder.wuyeTitle.setText(Html.fromHtml("物业管理处" + "<br><small><font color='#b1b1b1'>" + strTime + "</font></small></br>"));
        }else {     //用户的回复
            holder.wuyeReply.setVisibility(View.GONE);
            holder.myReply.setVisibility(View.VISIBLE);
            holder.myReplyContent.setText(reply.getContent());
        }
        holder.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivityForResult(new Intent(context, ReplyActivity.class)
//                        .putExtra("id", reply.getRepairComplainId()), REQUST_REPLY);
                if (onReplyClickListener!=null){
                    onReplyClickListener.onReplyClick(position);
                }
            }
        });
        return convertView;
    }

    public interface OnReplyClickListener{
        void onReplyClick(int position);
    }

    private OnReplyClickListener onReplyClickListener;

    public void setOnReplyClickListener(OnReplyClickListener onReplyClickListener) {
        this.onReplyClickListener = onReplyClickListener;
    }
}
