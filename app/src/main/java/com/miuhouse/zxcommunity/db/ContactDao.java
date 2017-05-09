package com.miuhouse.zxcommunity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miuhouse.zxcommunity.bean.Contact;

/** 记录联系人的头像和昵称
 * Created by khb on 2016/3/23.
 */
public class ContactDao {

    public static final String TABLE_NAME = "CONTACT_INFO";
    public static final String HXID = "hxid";
    public static final String HEAD = "head";
    public static final String NICKNAME = "nickname";

    private final DatabaseHelper dbHelper;

    public ContactDao(Context context) {
        dbHelper = DatabaseHelper.getInstance();
    }

    /**
     * 增加一个联系人
     * @param id
     * @param head
     * @param nickname
     * @return
     */
    public synchronized long addContact(String id, String head, String nickname){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(HXID, id);
        cv.put(HEAD, head);
        cv.put(NICKNAME, nickname);
        return db.insert(TABLE_NAME, null, cv);
    }

    /**
     * 通过环信id，查找联系人的头像和昵称
     * @param id
     * @return
     */
    public synchronized Contact getContactById(String id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
                + " WHERE " + HXID + " = ? ",
                new String[]{id});
        if (cursor.moveToNext()){
            Contact contact = new Contact();
            contact.setHxid(id);
            contact.setHead(cursor.getString(cursor.getColumnIndex(HEAD)));
            contact.setNickname(cursor.getString(cursor.getColumnIndex(NICKNAME)));
            return contact;
        }else{
            return null;
        }
    }

    /**
     * 更新头像和昵称
     * @return
     */
    public synchronized int updateContact(String id, String head, String nickname){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(HEAD, head);
        cv.put(NICKNAME, nickname);
        int result = db.update(TABLE_NAME, cv, HXID + " = ? ", new String[]{id});
        return result;
    }

}
