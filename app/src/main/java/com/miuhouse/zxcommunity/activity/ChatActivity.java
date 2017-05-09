package com.miuhouse.zxcommunity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.hyphenate.util.EasyUtils;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.fragment.ChatFragment;
import com.miuhouse.zxcommunity.utils.Constants;

/**
 * 聊天页面，需要fragment的使用{@link #EaseChatFragment}
 *
 */
public class ChatActivity extends BaseActivity{
    public static ChatActivity activityInstance;
    private ChatFragment chatFragment;
    String toChatUsername;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        activityInstance = this;
        //聊天人或群id
        toChatUsername = getIntent().getExtras().getString("userId");
        String nickname = getIntent().getStringExtra("nickname");
        //可以直接new EaseChatFratFragment使用
        chatFragment = new ChatFragment();
//        chatFragment = new EaseChatFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
        args.putString(Constants.EXTRA_USER_ID, toChatUsername);
        args.putString("nickname", nickname);
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        
    }

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
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initVariables() {

    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }
}
