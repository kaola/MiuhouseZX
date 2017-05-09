package com.miuhouse.zxcommunity.fragment;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.miuhouse.zxcommunity.db.AccountDBTask;
import com.miuhouse.zxcommunity.db.ContactDao;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;

/**
 * Created by khb on 2016/3/23.
 */
public class ChatFragment extends EaseChatFragment {

    private final ContactDao contactDao;
    private String head;
    private String nickname;

    public ChatFragment() {
        super();
        contactDao = new ContactDao(getContext());
//        contactDao.getContactById(toChatUsername)
    }

    @Override
    protected void setUpView() {
        super.setUpView();
    }

    /**
     * message消息扩展里设置用户自己的头像和昵称
     * @param message
     */
    private void setMessage(EMMessage message){
//        head = contactDao.getContactById(toChatUsername).getHead();
//        nickname = contactDao.getContactById(toChatUsername).getNickname();
        if (!MyUtils.isLoggedIn()) {
            nickname = SPUtils.getSPData(Constants.TEMPNAME, null);
            head = "";
        }else{
            nickname = AccountDBTask.getUserBean().getNickName();
//            if (nickname == null){
//                nickname = SPUtils.getSPData(Constants.TEMPNAME, null);
//            }
            head = AccountDBTask.getUserBean().getHeadUrl();
        }
        message.setAttribute(Constants.HEAD, head);
        message.setAttribute(Constants.NICKNAME, nickname);
//        message.setAttribute("senderHeader", AccountDBTask.getUserBean().getHeadUrl());
    }

    @Override
    protected void sendTextMessage(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        setMessage(message);
        sendMessage(message);
    }

    @Override
    protected void sendBigExpressionMessage(String name, String identityCode){
        EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        setMessage(message);
        sendMessage(message);
    }

    @Override
    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        setMessage(message);
        sendMessage(message);
    }

    @Override
    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        setMessage(message);
        sendMessage(message);
    }

    @Override
    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        setMessage(message);
        sendMessage(message);
    }

    @Override
    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        setMessage(message);
        sendMessage(message);
    }

    @Override
    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        setMessage(message);
        sendMessage(message);
    }
}
