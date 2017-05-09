package com.miuhouse.zxcommunity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miuhouse.zxcommunity.application.MyApplication;

/**
 * Created by kings on 1/5/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper singleton = null;
    private static final String DATABASE_NAME = "community.db";
    private static final int DATABASE_VERSION = 4;
    private static final String CREATE_ACCOUNT_TABLE_SQL = "create table " + AccountTable.TABLE_NAME + "("
            + AccountTable.ID + " text primary key,"
            + AccountTable.NAME + " text,"
            + AccountTable.NICK_NAME + " text,"
            + AccountTable.HEAD_URL + " text,"
            + AccountTable.MOBILE + " text,"
            + AccountTable.BUILD + " text,"
            + AccountTable.PROPERTYID + " text,"
            + AccountTable.PROPERTY_NAME + " text,"
            + AccountTable.UNIT + " text,"
            + AccountTable.STATUS+" integer"
            + ");";
    private static final String CREATE_NOTIFICATION_TABLE_SQL = "create table " + NotificationDao.TABLE_NAME + "("
            + NotificationDao.ID + " text primary key not null,"
            + NotificationDao.TITLE + " text not null, "
            + NotificationDao.DESCRIPTION + " text, "
            + NotificationDao.CONTENT + " text, "
            + NotificationDao.CREATETIME + " text not null, "
            + NotificationDao.SENDTIME + " text not null, "
            + NotificationDao.STATE + " integer not null, "
            + NotificationDao.PROPERTYID + " integer not null "
            + ");";
    private static final String CREATE_CONTACT_TABLE_SQL = "create table " + ContactDao.TABLE_NAME + "("
            + ContactDao.HXID + " integer primary key not null, "
            + ContactDao.HEAD + " text, "
            + ContactDao.NICKNAME + " text "
            +")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_TABLE_SQL);
        db.execSQL(CREATE_NOTIFICATION_TABLE_SQL);
        db.execSQL(CREATE_CONTACT_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion > 1) {
            deleteAllTable(db);
            onCreate(db);
        }
    }

    public static synchronized DatabaseHelper getInstance() {
        if (singleton == null) {
            singleton = new DatabaseHelper(MyApplication.getInstance());
        }
        return singleton;
    }

    private void deleteAllTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + AccountTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NotificationDao.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ContactDao.TABLE_NAME);
        // deleteAllTableExceptAccount(db);

    }
}
