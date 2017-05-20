package com.zoom.music.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zoom.music.bean.MusicInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by abiguime on 2016/9/7.
 */

public class MusicDao {


    public static List<MusicInfo> queryLocalMusic (Context ctx) {

        /*获取可读或可写数据库对象*/
        SQLiteDatabase db = AppOpenHelper.getInstance(ctx);
        String sql = "select * from "+AppOpenHelper.TABLE_MUSIC;
        List<MusicInfo> musicInfos = parseCursor(db.rawQuery(sql, null));
        return musicInfos;
    }


    private static List<MusicInfo> parseCursor (Cursor cursor) {
        List<MusicInfo> msinfo = new ArrayList<>();

        while (cursor.moveToNext()) {
            MusicInfo music = new MusicInfo();
            music._id = cursor.getInt(cursor.getColumnIndex("_id"));
            music.songId = cursor.getInt(cursor.getColumnIndex("songid"));
            music.albumId = cursor.getInt(cursor.getColumnIndex("albumid"));
            music.duration = cursor.getInt(cursor.getColumnIndex("duration"));
            music.musicName = cursor.getString(cursor.getColumnIndex("musicname"));
            music.artist = cursor.getString(cursor.getColumnIndex("artist"));
            music.data = cursor.getString(cursor.getColumnIndex("data"));
            music.folder = cursor.getString(cursor.getColumnIndex("folder"));
            music.musicNameKey = cursor.getString(cursor.getColumnIndex("musicnamekey"));
            music.artist_id = cursor.getString(cursor.getColumnIndex("artist_id"));
            music.favorite = cursor.getInt(cursor.getColumnIndex("favorite"));
            msinfo.add(music);
        }
        return msinfo;
    }

}
