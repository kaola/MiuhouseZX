package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.bean.Notification;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.List;

/**
 * Created by khb on 2016/3/17.
 */
public class NotificationAdapter extends BaseRVAdapter<Notification>{

    public NotificationAdapter(Activity activity, List<Notification> mDataList) {
        super(activity, mDataList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationItemHolder(mLayoutInflater.inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NotificationItemHolder mHolder = (NotificationItemHolder) holder;
        Notification notification = mList.get(position);
        mHolder.time.setText(MyUtils.checkTime(Long.parseLong(notification.getSendTime())));
//        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD HH-mm-ss");
//        try {
//            mHolder.time.setText(MyUtils.checkTime(format.parse(notification.getSendTime()).getTime()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        mHolder.title.setText(notification.getTitle());
        mHolder.desc.setText(notification.getDescription());
    }

    public class NotificationItemHolder extends RecyclerView.ViewHolder{
        private TextView time;
        private TextView title;
        private TextView desc;
        private RelativeLayout container;

        public NotificationItemHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);
            container = (RelativeLayout) itemView.findViewById(R.id.notificationBreif);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNotificationClickListener != null){
                        mOnNotificationClickListener.onNotificationClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }

    public static interface OnNotificationClickListener {
        void onNotificationClick(View view, int position);
    }

    private OnNotificationClickListener mOnNotificationClickListener;

    public void setOnNotificationClickListener(OnNotificationClickListener mOnNotificationClickListener){
        this.mOnNotificationClickListener = mOnNotificationClickListener;
    }

}
