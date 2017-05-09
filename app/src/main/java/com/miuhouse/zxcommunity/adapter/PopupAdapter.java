package com.miuhouse.zxcommunity.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;

import java.util.List;

/**
 * Created by khb on 2016/1/11.
 */
public class PopupAdapter extends BaseRVAdapter<String> {
    public PopupAdapter(Activity mContext, List<String> mList) {
        super(mContext, mList);
    }

    private OnPopupItemClickListener mPopupItemClickListener;

    public interface OnPopupItemClickListener{
        void onClick(View view, int position);
    }

    public void setOnPopupItemClickListener(OnPopupItemClickListener mPopupItemClickListener){
        this.mPopupItemClickListener = mPopupItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PopupHolder(mLayoutInflater.inflate(R.layout.item_popup, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String itemName = mList.get(position);
        PopupHolder mHolder = (PopupHolder) holder;
        mHolder.item.setText(itemName);
    }

    public class PopupHolder extends BaseViewHolder{

        public TextView item;

        public PopupHolder(final View itemView) {
            super(itemView);

            item = (TextView) itemView.findViewById(R.id.popupTitle);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPopupItemClickListener != null){
                        mPopupItemClickListener.onClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }
}
