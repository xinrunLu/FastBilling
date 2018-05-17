package com.luxinrun.fastbilling.assistent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase db = null;
    Cursor cursor;

    public DBHelper(Context context) {
        super(context, "fast_billing_database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("create table if not exists " + "fast_billing_tab"
                + "(" + "_id" + " integer primary key,"
                + "date_time" + " text,"
                + "exp_or_income_num" + " text,"
                + "exp_or_income_title" + " text,"
                + "classify_num" + " text,"
                + "classify_title" + " text,"
                + "money" + " text,"
                + "summary" + " text,"
                + "location" + " text)");
    }

    //插入一条数据
    public void insert(ContentValues values) {
        db = getWritableDatabase();
        db.insert("fast_billing_tab", "_id", values);
        db.close();
    }

    public void delet(String id) {
        db = getWritableDatabase();
        db.delete("fast_billing_tab", "_id = ?", new String[]{id});
    }

    // 查询数据库所有数据按照日期降序排列
    public ArrayList<Map<String, Object>> cursorList() {
        db = getReadableDatabase();
        Cursor cursor = db.query("fast_billing_tab", null, null, null, null, null, "date_time" + " DESC");
        ArrayList<Map<String, Object>> cursorData = new ArrayList<Map<String, Object>>();
        while (cursor.moveToNext()) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
            item.put("date_time", cursor.getString(cursor.getColumnIndex("date_time")));
            item.put("exp_or_income_num", cursor.getString(cursor.getColumnIndex("exp_or_income_num")));
            item.put("exp_or_income_title", cursor.getString(cursor.getColumnIndex("exp_or_income_title")));
            item.put("classify_num", cursor.getString(cursor.getColumnIndex("classify_num")));
            item.put("classify_title", cursor.getString(cursor.getColumnIndex("classify_title")));
            item.put("money", cursor.getString(cursor.getColumnIndex("money")));
            item.put("summary", cursor.getString(cursor.getColumnIndex("summary")));
            item.put("location", cursor.getString(cursor.getColumnIndex("location")));
            cursorData.add(item);
        }
        cursor.close();
        db.close();
        return cursorData;
    }

    // 查询数据库某月收入或者支出
    public ArrayList<Map<String, Object>> cursorMonth(String yearmonth, String expORincome) {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from fast_billing_tab where strftime('%Y%m', date_time) = ? AND exp_or_income_num = ? order by date_time desc",
                new String[]{yearmonth, expORincome});
        ArrayList<Map<String, Object>> cursorData = new ArrayList<Map<String, Object>>();
        while (cursor.moveToNext()) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
            item.put("date_time", cursor.getString(cursor.getColumnIndex("date_time")));
            item.put("exp_or_income_num", cursor.getString(cursor.getColumnIndex("exp_or_income_num")));
            item.put("exp_or_income_title", cursor.getString(cursor.getColumnIndex("exp_or_income_title")));
            item.put("classify_num", cursor.getString(cursor.getColumnIndex("classify_num")));
            item.put("classify_title", cursor.getString(cursor.getColumnIndex("classify_title")));
            item.put("money", cursor.getString(cursor.getColumnIndex("money")));
            item.put("summary", cursor.getString(cursor.getColumnIndex("summary")));
            item.put("location", cursor.getString(cursor.getColumnIndex("location")));
            cursorData.add(item);
        }
        cursor.close();
        db.close();
        return cursorData;
    }

    // 查询数据库某月收入或者支出
    public ArrayList<Map<String, Object>> cursorDateSlot(String startdate, String enddate, String expORincome) {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from fast_billing_tab where date_time BETWEEN ? AND ? AND exp_or_income_num = ? order by date_time desc",
                new String[]{startdate, enddate, expORincome});
        ArrayList<Map<String, Object>> cursorData = new ArrayList<Map<String, Object>>();
        while (cursor.moveToNext()) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
            item.put("date_time", cursor.getString(cursor.getColumnIndex("date_time")));
            item.put("exp_or_income_num", cursor.getString(cursor.getColumnIndex("exp_or_income_num")));
            item.put("exp_or_income_title", cursor.getString(cursor.getColumnIndex("exp_or_income_title")));
            item.put("classify_num", cursor.getString(cursor.getColumnIndex("classify_num")));
            item.put("classify_title", cursor.getString(cursor.getColumnIndex("classify_title")));
            item.put("money", cursor.getString(cursor.getColumnIndex("money")));
            item.put("summary", cursor.getString(cursor.getColumnIndex("summary")));
            item.put("location", cursor.getString(cursor.getColumnIndex("location")));
            cursorData.add(item);
        }
        cursor.close();
        db.close();
        return cursorData;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
