package com.miuhouse.zxcommunity.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.miuhouse.zxcommunity.bean.UserBean;

/**
 * Created by kings on 1/7/2016.
 */
public class AccountDBTask {
    private AccountDBTask() {

    }

    private static SQLiteDatabase getWsd() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        return databaseHelper.getWritableDatabase();
    }

    private static SQLiteDatabase getRsd() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        return databaseHelper.getReadableDatabase();
    }

    /**
     * 保存用户资料
     *
     * @param userBean
     */
    public static void saveUserBean(UserBean userBean) {
        if (getWsd().isOpen()) {
            ContentValues values = new ContentValues();
            values.put(AccountTable.ID, userBean.getId());
            values.put(AccountTable.NICK_NAME, userBean.getNickName());
            values.put(AccountTable.NAME, userBean.getName());
            values.put(AccountTable.HEAD_URL, userBean.getHeadUrl());
            values.put(AccountTable.BUILD, userBean.getBuild());
            values.put(AccountTable.PROPERTYID, userBean.getPropertyId());
            values.put(AccountTable.MOBILE, userBean.getMobile());
            values.put(AccountTable.PROPERTY_NAME, userBean.getPropertyName());
            values.put(AccountTable.UNIT, userBean.getUnit());
            values.put(AccountTable.STATUS, userBean.getStatus());
            String[] whereArgs = {userBean.getId()};
            if (getSelectCursor().getCount() == 0) {
                getWsd().insert(AccountTable.TABLE_NAME, null, values);

            } else {
                Log.i("TAG", "update");
                getWsd().update(AccountTable.TABLE_NAME, values, AccountTable.ID + "=?", whereArgs);

            }
        }
    }

    /**
     * 获取用户资料
     *
     * @return
     */
    public static UserBean getUserBean() {

        Cursor c = getSelectCursor();

        if (c.moveToNext()) {
            UserBean userBean = new UserBean();
            int colId = c.getColumnIndex(AccountTable.ID);
            userBean.setId(c.getString(colId));

            colId = c.getColumnIndex(AccountTable.NAME);
            userBean.setName(c.getString(colId));

            colId = c.getColumnIndex(AccountTable.NICK_NAME);
            userBean.setNickName(c.getString(colId));

            colId = c.getColumnIndex(AccountTable.HEAD_URL);
            userBean.setHeadUrl(c.getString(colId));

            colId = c.getColumnIndex(AccountTable.BUILD);
            userBean.setBuild(c.getString(colId));

            colId = c.getColumnIndex(AccountTable.MOBILE);
            userBean.setMobile(c.getString(colId));

            colId = c.getColumnIndex(AccountTable.PROPERTYID);
            userBean.setPropertyId(c.getLong(colId));

            colId = c.getColumnIndex(AccountTable.PROPERTY_NAME);
            userBean.setPropertyName(c.getString(colId));
            colId = c.getColumnIndex(AccountTable.UNIT);
            userBean.setUnit(c.getString(colId));
            colId = c.getColumnIndex(AccountTable.STATUS);
            userBean.setStatus(c.getInt(colId));
            return userBean;
        }
        return null;
    }

    private static Cursor getSelectCursor() {
        String sql = "select * from " + AccountTable.TABLE_NAME;

        return getRsd().rawQuery(sql, null);
    }

    public static void updateNickName(String uid, String nickName, String bigLetter) {
        if (getWsd().isOpen()) {
            ContentValues values = new ContentValues();
            values.put(bigLetter, nickName);
            String[] whereArgs = {uid};
            getWsd().update(AccountTable.TABLE_NAME, values, AccountTable.ID + "=?", whereArgs);
        }
    }

    public static void updateStatus(String uid, int status) {
        if (getWsd().isOpen()) {
            ContentValues values = new ContentValues();
            values.put(AccountTable.STATUS, status);
            String[] whereArgs = {uid};
            getWsd().update(AccountTable.TABLE_NAME, values, AccountTable.ID + "=?", whereArgs);
        }
    }
    public static void clear() {
        String sql = "delete from " + AccountTable.TABLE_NAME;

        getWsd().execSQL(sql);
    }

}
