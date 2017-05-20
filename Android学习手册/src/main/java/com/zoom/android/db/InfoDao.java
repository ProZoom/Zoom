package com.zoom.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zoom.android.bean.itemInfo;





public class InfoDao {

    dbHelper dbHelper;


    public InfoDao(Context context) {
        dbHelper = new dbHelper(context);
    }

    public  void add(itemInfo itemInfo){
        //拿到 工具类的实例 , 然后去操作 数据库
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        db.execSQL("insert into android values(null,?,?)", new String[]{com.zoom.android.bean.itemInfo.getTitle(), com.zoom.android.bean.itemInfo.getContent()});
    }
    public  void delete(String id){
        //拿到 工具类的实例 , 然后去操作 数据库
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        db.execSQL("delete from basic where _id=?",new Object[]{id});
    }

    public  void update(itemInfo itemInfo){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update android set _id=?,_title=? where _content=?", new Object[]{com.zoom.android.bean.itemInfo.getId(), com.zoom.android.bean.itemInfo.getTitle(), com.zoom.android.bean.itemInfo.getContent()});
    }

    public  itemInfo find(String id){
        SQLiteDatabase db= dbHelper.getReadableDatabase();

        Cursor cursor=db.rawQuery("select * from basic where _id=?", new String[]{id});

        boolean result=cursor.moveToNext();
        itemInfo itemInfo=null;
        if(result){
            String _id=cursor.getString(cursor.getColumnIndex("_id"));
            String title=cursor.getString(cursor.getColumnIndex("_title"));
            String content=cursor.getString(cursor.getColumnIndex("_content"));
            itemInfo=new itemInfo(_id,title,content);
        }
        cursor.close();
        return itemInfo;
    }
}
