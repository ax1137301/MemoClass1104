package com.example.memoclass1104;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DbAdapter {
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "date";
    public static final String KEY_MEMO = "memo";
    public static final String KEY_REMIND = "remind";
    public static final String KEY_BGCOLOR = "bgcolor";
    public static final String TABLE_NAME = "memo";
    private Dbhelper mDbhelper;
    private SQLiteDatabase mdb;
    private final Context mCnx;
    public ContentValues values;


    public DbAdapter(Context mCnx) {
        this.mCnx = mCnx;
        open();
    }

    public void open() {
        mDbhelper = new Dbhelper(mCnx);
        mdb = mDbhelper.getWritableDatabase();
    }
    //新增便條方法
    public long CreateMemo(String date, String memo, String remind, String bgcolor) {
        long id = 0;
        try {
            values = new ContentValues();
            values.put(KEY_DATE, date);
            values.put(KEY_MEMO, memo);
            values.put(KEY_BGCOLOR, bgcolor);
            values.put(KEY_REMIND, remind);
            id = mdb.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mdb.close();
            Toast.makeText(mCnx, "新增成功!!", Toast.LENGTH_SHORT).show();
        }
        return id;
    }//CreateMemo結束


    public Cursor list_mo() {
        // 設定Cursor的表格欄位
        Cursor mCursor = mdb.query(TABLE_NAME, new String[]{KEY_ID, KEY_DATE, KEY_MEMO, KEY_REMIND, KEY_BGCOLOR},
                null, null, null, null, null);
        // 如果Cursor不是空的，就移動到第一筆
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor queryByID(int item_id) {
        Cursor mCursor = mdb.query(TABLE_NAME, new String[]{KEY_ID, KEY_DATE, KEY_MEMO, KEY_REMIND, KEY_BGCOLOR},
                KEY_ID + "=" + item_id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //更新便條方法
    public long UpdateMemo(int id,String date, String memo, String remind, String bgcolor) {
        long Update = 0;
        try {
            //將資料丟到contentvalues
            ContentValues values = new ContentValues();
            values.put(KEY_DATE, date);
            values.put(KEY_MEMO, memo);
            values.put(KEY_BGCOLOR, bgcolor);
            values.put(KEY_REMIND, remind);
            String [] args = {Integer.toString(id)};
            id = mdb.update(TABLE_NAME,values,"id=?",args);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mdb.close();
            Toast.makeText(mCnx, "更新成功!!", Toast.LENGTH_SHORT).show();
        }
        return id;
    }//UpdateMemo結束
    //刪除便條方法
    public boolean deleteMemo(int id){
        String [] args = {Integer.toString(id)};
        mdb.delete(TABLE_NAME,"id=?",args);
        return true;
    }//deleteMemo結束
}
