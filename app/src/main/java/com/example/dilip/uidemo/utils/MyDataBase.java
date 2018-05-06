package com.example.dilip.uidemo.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.annotations.Expose;

public class MyDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ui_demo";
    private static final int DATABASE_VERSION = 6;
    public static final String KEY_SPECIFICATION = "specialization";
    public static final String KEY_DESCRIPTION = "descriptions";
    public static final String KEY_IMAGE = "image_path";
    public static final String  KEY_ID = "cus_id";

    public static final String KEY_FLAG = "flag";

    private static final String CREATE_TABLE =
            "create table submission_data (id INTEGER PRIMARY KEY autoincrement,cus_id INTEGER,specialization text not null,descriptions text not null,image_path blob not null);";

    public static final String TABLE_SUBMISSION_NAME = "submission_data";

    Context mContext;
    public MyDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long insertSubmissionData(int id,String specialization,String description,byte[] image) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, id);
        initialValues.put(KEY_SPECIFICATION, specialization);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_IMAGE, image);
        return getWritableDatabase().insert(TABLE_SUBMISSION_NAME, null, initialValues);
    }

    public Cursor getItems() {
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "select * from " + TABLE_SUBMISSION_NAME;
        Cursor cursor = database.rawQuery(sql, null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS submission_data");
    }

    public void fetchData(DataFetchListner listener) {
        DataFetcher fetcher = new DataFetcher(listener, this.getWritableDatabase(),mContext);
        fetcher.start();
    }
}
