package com.zoom.music.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.zoom.music.bean.AlbumInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abiguime on 2016/9/11.
 */
public class AlbumUtils {


    private static final String TAG = AlbumUtils.class.getSimpleName();

    /*获取系统专辑列表*/
    public static List<AlbumInfo> querySystemAlbums(Context ctx) {

        List<AlbumInfo> inf = new ArrayList<>();

        ContentResolver cr = ctx.getContentResolver();
        Uri gmusic = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor query = cr.query(gmusic, PROJECTION, null, null, null);
        parseAlbums(inf, query);
        return inf;
    }

    private static void parseAlbums(List<AlbumInfo> inf, Cursor query) {

        while(query.moveToNext()) {
            AlbumInfo tmp = new AlbumInfo();
            tmp.album_art = query.getString(query.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART)); /* 专辑图 */
            tmp.number_of_songs = query.getInt(query.getColumnIndex(MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS)); /* 专辑图 */
            tmp.album_name = query.getString(query.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM)); /* 专辑图 */
            inf.add(tmp);
        }
    }


    private static final String[] PROJECTION = {
//            MediaStore.Audio.AlbumColumns.ALBUM_ID,
            MediaStore.Audio.AlbumColumns.ALBUM_ART,
            MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS,
            MediaStore.Audio.AlbumColumns.ALBUM,
    };



/*    *//* 扫描本地音乐 更新数据库 *//*
    public static List<MusicInfo> querySystemMusic(Context ctx) {

        ContentResolver cr = ctx.getContentResolver();
        Uri gmusic = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor query = cr.query(gmusic, PROJECTION, null, null, null);
        return parseMusic(query); *//* 获取音乐列表 *//*
        // 想应用的本地数据库保存获取好的音乐。。。
        *//*音乐保存到本地数据库里*//*
        *//*SqliteOpenHelper*//*
    }*/

}
