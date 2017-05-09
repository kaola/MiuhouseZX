package com.miuhouse.zxcommunity.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kings on 3/16/2016.
 */
public class HistoryPassListBean extends BaseBean implements Serializable {
    private ArrayList<HistoryPassportBean> passCards;

    public ArrayList<HistoryPassportBean> getPassCards() {
        return passCards;
    }

    public void setPassCards(ArrayList<HistoryPassportBean> passCards) {
        this.passCards = passCards;
    }
}
