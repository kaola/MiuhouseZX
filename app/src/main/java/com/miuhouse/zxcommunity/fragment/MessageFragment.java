package com.miuhouse.zxcommunity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.ChatActivity;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.db.NotificationDao;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.widget.MyDot;

/**
 * Created by kings on 1/6/2016.
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private EaseConversationList mEList;
    private ConversationListItemClickListener conversationListItemClickListener;
    private TextView optionLeft;
    private TextView optionRight;
    //    private ConversationFragment mConversationFragment;
    private WuyeMessageFragment wyMessageFragment;
    private NotificationFragment mNotificationFragment;
    private FragmentManager mChildFragmentManager;
    private MyDot unreadNotification;

    private MyDot unreadConversation;
    private final static int REFRESH_CONVERSATION = 1;
    private static final int REFRESH_NOTIFICATION = 2;
    private final static int REFRESH_WUYEREPLY = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_CONVERSATION:
                    showUnreadC();
//                    getConversationFragment().refresh();
                    break;
                case REFRESH_NOTIFICATION:
                    mNotificationFragment.refresh();
                    break;
                case REFRESH_WUYEREPLY:
                    wyMessageFragment.refresh();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void showUnreadC() {
        if (EMClient.getInstance().chatManager().getUnreadMsgsCount() > 0) {
            unreadConversation.setVisibility(View.VISIBLE);
        } else {
            unreadConversation.setVisibility(View.GONE);
        }
    }

    private void showUnreadN() {
        int unread = new NotificationDao(activity)
                .getUnreadDataCountByPropertyId(
                        SPUtils.getSPData(Constants.PROPERTYID, 0L));
        if (unread > 0) {
            unreadNotification.setVisibility(View.VISIBLE);
        } else {
            unreadNotification.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = activity.getLayoutInflater().inflate(R.layout.rounded_image, null);
        Toolbar title = (Toolbar) view.findViewById(R.id.titlebar);
        ((AppCompatActivity) activity).setSupportActionBar(title);
        title.setTitle("");
        optionLeft = (TextView) view.findViewById(R.id.tv_option_left);
        optionRight = (TextView) view.findViewById(R.id.tv_option_right);
        unreadNotification = (MyDot) view.findViewById(R.id.unreadNotification);
        unreadConversation = (MyDot) view.findViewById(R.id.unreadConversation);
        showUnreadC();
        showUnreadN();

        optionLeft.setOnClickListener(this);
        optionRight.setOnClickListener(this);
        selectOption(optionLeft, true);
        selectOption(optionRight, false);
        mChildFragmentManager = getChildFragmentManager();
        mNotificationFragment = (NotificationFragment) FragmentFactory.getFragment(Constants.NOTIFICATION);
        wyMessageFragment = (WuyeMessageFragment) FragmentFactory.getFragment(Constants.WUYEREPLY);
        showChildeFragment();
        return view;
    }

    private void showChildeFragment() {
        mChildFragmentManager.beginTransaction()
                .add(R.id.messageContainer, mNotificationFragment)
//                .add(R.id.messageContainer, mConversationFragment)
//                .hide(mConversationFragment)
                .show(mNotificationFragment).commit();
//        当未登录时，先不初始化聊天记录页面
//        算了，一起初始化吧
//        if (MyUtils.isLoggedIn()){
        mChildFragmentManager.beginTransaction()
                .add(R.id.messageContainer, wyMessageFragment)
                .hide(wyMessageFragment).commit();
//        }
    }

//    获取聊天对话记录列表
    private ConversationFragment getConversationFragment() {
        ConversationFragment fragment = (ConversationFragment) FragmentFactory.getFragment(Constants.CONVERSATION);
        fragment.setConversationListItemClickListener(new ConversationFragment.ConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                String nickname = conversation.getLastMessage().getStringAttribute(Constants.NICKNAME, null);
                if (!MyUtils.isLoggedIn()) {
                    if (!conversation.getLastMessage().getFrom().equals(MyApplication.getInstance().getIMEI())) {
                        SPUtils.saveSPData("chatnickname", nickname);
                    }
                } else {
                    if (!conversation.getLastMessage().getFrom().equals(Constants.EASEACCOUNTHEAD + AccountDBTask.getUserBean().getId())) {
//                    将用户的环信id和会话发出者的环信id比较，
//                    若不相等，说明此消息是对方发送给用户
//                    获取的nickname就是对方的，此时将nickname保存到本地
                        SPUtils.saveSPData("chatnickname", nickname);
                    }
                }
                startActivity(new Intent(activity, ChatActivity.class)
                                .putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName())
                                .putExtra("nickname", SPUtils.getSPData("chatnickname", null))
                );
            }
        });
        return fragment;
    }

    public int dp2px(int dp) {
        WindowManager wm = (WindowManager) getActivity().getBaseContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        return (int) (dp * displaymetrics.density + 0.5f);
    }

    public void refresh(int tag) {
        refreshUI(tag);
    }

    private void refreshUI(int tag) {
        if (tag == Constants.CONVERSATION) {
            handler.sendEmptyMessage(REFRESH_CONVERSATION);
        } else if (tag == Constants.NOTIFICATION){
            handler.sendEmptyMessage(REFRESH_NOTIFICATION);
        } else if (tag == Constants.WUYEREPLY){
            handler.sendEmptyMessage(REFRESH_WUYEREPLY);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_option_left:
                if (!optionLeft.isSelected()) {
                    selectOption(optionRight, false);
                    selectOption(optionLeft, true);
                    mChildFragmentManager.beginTransaction()
                            .hide(wyMessageFragment)
//                            .hide(getConversationFragment())
                            .show(mNotificationFragment)
                            .commit();
                }
                break;
            case R.id.tv_option_right:
//                    if (!MyUtils.isLoggedIn()){
//                        ToastUtils.showToast(activity, getResources().getString(R.string.user_not_login));
//                        startActivity(new Intent(activity, LoginActivity.class));
//                        return ;
//                    }
                if (!optionRight.isSelected()) {
                    selectOption(optionRight, true);
                    selectOption(optionLeft, false);
                    mChildFragmentManager.beginTransaction()
                            .hide(mNotificationFragment)
                            .show(wyMessageFragment)
//                            .show(getConversationFragment())
                            .commit();
                }
                break;
        }
    }

    private void selectOption(TextView tv, boolean isSelected) {
        if (isSelected) {
            tv.setTextColor(getResources().getColor(R.color.login_bg_red));
        } else {
            tv.setTextColor(getResources().getColor(R.color.white_pure));
        }
        tv.setSelected(isSelected);
    }

    public interface ConversationListItemClickListener {
        /**
         * 会话listview item点击事件
         *
         * @param conversation 被点击item所对应的会话
         */
        void onListItemClicked(EMConversation conversation);
    }

    /**
     * 设置listview item点击事件
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(ConversationListItemClickListener listItemClickListener) {
        this.conversationListItemClickListener = listItemClickListener;
    }

}
