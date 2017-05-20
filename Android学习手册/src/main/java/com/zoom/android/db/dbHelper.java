package com.zoom.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/27.
 */

public class dbHelper extends SQLiteOpenHelper {

    public dbHelper(Context context) {
        super(context,"android.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "creat table android ( _id integer primary key autoincrement,_title varchar, _content varchar");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
