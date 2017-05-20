package com.zoom.music.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zoom.music.bean.AlbumInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */

public class AlbumDao {
    public static List<AlbumInfo> queryLocalAlbum (Context ctx) {

        /*获取可读或可写数据库对象*/
        SQLiteDatabase db = AppOpenHelper.getInstance(ctx);
        String sql = "select * from "+AppOpenHelper.TABLE_MUSIC;
        List<AlbumInfo> albumInfos = parseCursor(db.rawQuery(sql, null));
        return albumInfos;
    }


    private static List<AlbumInfo> parseCursor (Cursor cursor) {
        List<AlbumInfo> msinfo = new ArrayList<>();

        while (cursor.moveToNext()) {
            AlbumInfo album = new AlbumInfo();

            msinfo.add(album);
        }
        return msinfo;
    }
}
