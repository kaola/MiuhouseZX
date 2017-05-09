package com.miuhouse.zxcommunity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by khb on 2016/1/26.
 */
public class EquipCheckableAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;

    public List<String> getmCheckedList() {
        return mCheckedList;
    }

    private List<String> mCheckedList;

    public EquipCheckableAdapter(Context context, List<String> list, List<String> mCheckedList){
        super();
        mContext = context;
        mList = list;
        if (MyUtils.isEmptyList(mCheckedList)) {
            this.mCheckedList = new ArrayList<>();
        }else{
            this.mCheckedList = mCheckedList;
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {

        if (mCheckedList.contains(mList.get(position))){
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        LinearLayout container;
        ImageView checkIcon;
        TextView equipName;
        boolean isChecked;
    }

    @Override
    public View getView(final int position, View  convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_equip_checkable, null);
            holder.container = (LinearLayout) convertView.findViewById(R.id.equipCheckable);
            holder.checkIcon = (ImageView) convertView.findViewById(R.id.checkIcon);
            holder.equipName = (TextView) convertView.findViewById(R.id.equipName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String equipName = mList.get(position);
        holder.equipName.setText(equipName);
        setChecked(holder);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isChecked){
                    mCheckedList.remove(equipName);
                }else {
                    mCheckedList.add(equipName);
                }
                setCheckedClick(holder);
            }
        });
        checkIfWasChecked(holder);

        return convertView;
    }

    private void setChecked(ViewHolder holder){
        if (holder.isChecked){
            holder.checkIcon.setImageResource(R.mipmap.ico_house_fuxuan_checked);
        }else {
            holder.checkIcon.setImageResource(R.mipmap.ico_house_fuxuan);
        }
    }

    /**
     * 点击item后设置是否被选中
     * @param holder
     */
   private void setCheckedClick(ViewHolder holder){
       if (holder.isChecked){
           holder.checkIcon.setImageResource(R.mipmap.ico_house_fuxuan);
           holder.isChecked = false;
       }else {
           holder.checkIcon.setImageResource(R.mipmap.ico_house_fuxuan_checked);
           holder.isChecked = true;
       }
   }

    /**
     * 如果是修改租房信息，则之前的列表里可能已经有配套设施的信息，此时需检查，如果属于原来已有的设施，则设为选中
     * @param holder
     */
    private void checkIfWasChecked(ViewHolder holder){
        if (mCheckedList.contains(holder.equipName.getText().toString())){
            holder.isChecked = true;
            setChecked(holder);
            notifyDataSetChanged();
        }
    }
}
