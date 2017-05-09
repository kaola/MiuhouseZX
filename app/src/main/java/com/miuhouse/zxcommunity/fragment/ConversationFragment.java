package com.miuhouse.zxcommunity.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.utils.MyUtils;

import java.util.List;

/**
 * Created by khb on 2016/3/16.
 */
public class ConversationFragment extends BaseFragment {

    private EaseConversationList mEaseConversationList;
    private ConversationListItemClickListener conversationListItemClickListener;

    private final static int REFRESH = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH:
                    mEaseConversationList.refresh();

//                    Collection<EMConversation> values = EMClient.getInstance().chatManager().getAllConversations().values();
                    if(MyUtils.isEmptyList(mEaseConversationList.getConversations())){
                        viewGroup.setVisibility(View.VISIBLE);
                    }else{
                        viewGroup.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
    private List<EMConversation> list;
    private ViewGroup viewGroup;

    @Override
    public void initData() {

    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_conversation, null);
        mEaseConversationList = (EaseConversationList) view.findViewById(R.id.conversationList);
//        list = mEaseConversationList.getConversations();
        mEaseConversationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (conversationListItemClickListener != null) {
                    EMConversation emConversation = mEaseConversationList.getItem(position);
                    conversationListItemClickListener.onListItemClicked(emConversation);
                }
            }
        });
        viewGroup = (ViewGroup) view.findViewById(R.id.empty);
        if(MyUtils.isEmptyList(mEaseConversationList.getConversations())){
            viewGroup.setVisibility(View.VISIBLE);
        }else{
            viewGroup.setVisibility(View.GONE);
        }
        return view;
    }

    public void refresh(){
        handler.sendEmptyMessage(REFRESH);
    }

    public interface ConversationListItemClickListener {
        /**
         * 会话listview item点击事件
         * @param conversation 被点击item所对应的会话
         */
        void onListItemClicked(EMConversation conversation);
    }

    /**
     * 设置listview item点击事件
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(ConversationListItemClickListener listItemClickListener){
        conversationListItemClickListener = listItemClickListener;
    }
}
