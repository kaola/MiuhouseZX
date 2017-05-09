package com.miuhouse.zxcommunity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miuhouse.zxcommunity.bean.Notification;

import java.util.ArrayList;
import java.util.List;

/** 公告Dao
 * Created by khb on 2016/3/17.
 */
public class NotificationDao {
    public static final String TABLE_NAME = "NotificationTable";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String CONTENT = "content";
    public static final String CREATETIME = "createtime";
    public static final String SENDTIME = "sendtime";
    public static final String PROPERTYID = "propertyid";
    public static final String PUSHTYPE = "pushtype";
    public static final String STATE = "state";

    public static final int READ = 1;
    public static final int UNREAD = 0;

    private final DatabaseHelper dbHelper;

    public NotificationDao(Context context){
        dbHelper = DatabaseHelper.getInstance();
    }

    /**
     * 检查数据库中是否有此数据
     * @param createtime 消息在后台创建的时间
     * @return
     */
    public synchronized  boolean checkIfExistsByTime(String createtime){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM "
                + NotificationDao.TABLE_NAME
                + " WHERE " + CREATETIME + " = ?", new String[]{createtime});
        boolean flag = cursor.moveToNext();
        cursor.close();
        db.close();
        return flag;
    }

    /**
     * 数据库中添加一条数据
     * @param notification
     * @return
     */
    public synchronized long addData(Notification notification){
        if(checkIfExistsByTime(notification.getCreateTime() + "")){
//			-1是insert方法发生错误的代码，为不重复，这里返回-2
            return -2;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, notification.getId());
        cv.put(TITLE, notification.getTitle());
        cv.put(DESCRIPTION, notification.getDescription());
        cv.put(CONTENT, notification.getContent());
        cv.put(CREATETIME, notification.getCreateTime());
        cv.put(SENDTIME, notification.getSendTime());
        cv.put(STATE, notification.isRead() ? READ : UNREAD);
//        cv.put(PUSHTYPE, notification.getPushType());
        cv.put(PROPERTYID, notification.getPropertyId());
        return db.insert(TABLE_NAME, null, cv);
    }

    /**
     * 将公告设为已读
     * @param notification
     */
    public synchronized int updateData(Notification notification){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TITLE, notification.getTitle());
        cv.put(DESCRIPTION, notification.getDescription());
        cv.put(CONTENT, notification.getContent());
        cv.put(CREATETIME, notification.getCreateTime());
        cv.put(SENDTIME, notification.getSendTime());
        cv.put(STATE, READ);
//        cv.put(PUSHTYPE, notification.getPushType());
        cv.put(PROPERTYID, notification.getPropertyId());
        return db.update(TABLE_NAME, cv, ID + " = ? ", new String[]{notification.getId() + ""});
    }

    /**
     * 获取数据列表
     * @return
     */
    public synchronized List<Notification> getDataListByPropertyId(long propertyId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_NAME
                + " WHERE " + PROPERTYID + " = " + propertyId
                + " ORDER BY "
                + SENDTIME
                + " DESC ", null);
        List<Notification> list = new ArrayList<>();
        if(db.isOpen()){
            while (cursor.moveToNext()){
                Notification notification = new Notification();
                notification.setId(cursor.getString(cursor.getColumnIndex(ID)));
                notification.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                notification.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                notification.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                notification.setCreateTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATETIME))));
                notification.setSendTime(cursor.getString(cursor.getColumnIndex(SENDTIME)));
                notification.setIsRead(cursor.getInt(cursor.getColumnIndex(STATE)) == READ ? true : false);
//                notification.setPushType(cursor.getInt(cursor.getColumnIndex(PUSHTYPE)));
                notification.setPropertyId(cursor.getInt(cursor.getColumnIndex(PROPERTYID)));
                list.add(notification);
            }
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 获取未读消息数
     * @return
     */
    public synchronized int getUnreadDataCountByPropertyId(long propertyId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_NAME
                + " WHERE " + STATE + " = " + UNREAD
                + " AND "
                + PROPERTYID + " = " + propertyId, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return  count;
    }

    /**
     * 获取所有消息数
     * @return
     */
    public  synchronized  int getDataCountByPropertyId(long propertyId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_NAME
                + " WHERE " + PROPERTYID + " = " + propertyId, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    /**
     * 数据库中删除一条数据
     * @param id
     */
    public synchronized void deleteData(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete(TABLE_NAME, ID + " = ? ", new String[]{id + ""});
    }

    /**
     * 清空数据库
     */
    public synchronized void deleteAllData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME, null );
        db.execSQL("update sqlite_sequence set seq=0 where name = '" + TABLE_NAME + "'");
    }

    /**
     * 根据创建时间查找数据库中的消息
     * @param createtime
     * @return
     */
    public synchronized Notification getDataByCreateTime(long createtime){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_NAME
                + " WHERE " + CREATETIME + " = ? ", new String[]{createtime + ""});
        Notification notification = null;
        if (cursor.moveToNext()){
            notification = new Notification();
            notification.setId(cursor.getString(cursor.getColumnIndex(ID)));
            notification.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            notification.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
            notification.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
            notification.setCreateTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATETIME))));
            notification.setSendTime(cursor.getString(cursor.getColumnIndex(SENDTIME)));
            notification.setIsRead(cursor.getInt(cursor.getColumnIndex(STATE)) == READ ? true : false);
//                notification.setPushType(cursor.getInt(cursor.getColumnIndex(PUSHTYPE)));
            notification.setPropertyId(cursor.getInt(cursor.getColumnIndex(PROPERTYID)));
        }
        return notification;
    }

    /**
     * 根据id查找数据库中的消息
     * @param id
     * @return
     */
    public synchronized Notification getDataById(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + TABLE_NAME
                + " WHERE " + ID + " = ? ", new String[]{id + ""});
        Notification notification = null;
        if (cursor.moveToNext()){
            notification = new Notification();
            notification.setId(cursor.getString(cursor.getColumnIndex(ID)));
            notification.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            notification.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
            notification.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
            notification.setCreateTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(CREATETIME))));
            notification.setSendTime(cursor.getString(cursor.getColumnIndex(SENDTIME)));
            notification.setIsRead(cursor.getInt(cursor.getColumnIndex(STATE)) == READ ? true : false);
//                notification.setPushType(cursor.getInt(cursor.getColumnIndex(PUSHTYPE)));
            notification.setPropertyId(cursor.getInt(cursor.getColumnIndex(PROPERTYID)));
        }
        return notification;
    }
}
