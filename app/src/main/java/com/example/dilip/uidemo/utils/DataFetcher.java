package com.example.dilip.uidemo.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataFetcher extends Thread{

    private final DataFetchListner mListener;
    private final SQLiteDatabase mDb;
    private Context context;

    public DataFetcher(DataFetchListner mListener, SQLiteDatabase mDb,Context context) {
        this.mListener = mListener;
        this.mDb = mDb;
        this.context= context;
    }

    @Override
    public void run() {
        //Select all data
        MyDataBase myDataBase = new MyDataBase(context);
        CheckNetwork checkNetwork = new CheckNetwork();

       /* Cursor cursor = myDataBase.getItems();
        while (cursor.moveToFirst()){
            do {
                ItemModel model = new ItemModel();
                model.setId(cursor.getInt(cursor.getColumnIndex(myDataBase.KEY_ID)));
                model.setImage(checkNetwork.getBitmapFromByte(cursor.getBlob(cursor.getColumnIndex(myDataBase.KEY_IMAGE))));
                model.setDescriptions(cursor.getString(cursor.getColumnIndex(myDataBase.KEY_DESCRIPTION)));
                model.setSpecialization(cursor.getString(cursor.getColumnIndex(myDataBase.KEY_SPECIFICATION)));

                publishFlower(model);

            }while (cursor.moveToNext());
            cursor.close();
        }*/

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + MyDataBase.TABLE_SUBMISSION_NAME, null);

        final List<ItemModel> dataList = new ArrayList<>();

        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do {
                    ItemModel data = new ItemModel();

                    data.setImage(checkNetwork.getBitmapFromByte(cursor.getBlob(cursor.getColumnIndex(MyDataBase.KEY_IMAGE))));
                    data.setSpecialization(cursor.getString(cursor.getColumnIndex(MyDataBase.KEY_SPECIFICATION)));
                    data.setDescriptions(cursor.getString(cursor.getColumnIndex(MyDataBase.KEY_DESCRIPTION)));
                    data.setId(cursor.getInt(cursor.getColumnIndex(MyDataBase.KEY_ID)));

                    Log.e("VALUE_Got",data.getDescriptions());

                    dataList.add(data);
                    publishFlower(data);
                } while (cursor.moveToNext());
            }
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onHideDialog();

            }
        });

    }
    // used to pass all data
    public void publishFlower(final ItemModel data) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // passing data through onDeliverData()
                mListener.onDeliverData(data);
            }
        });
    }
}
