package com.zoom.music.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;

import com.zoom.music.bean.MusicInfo;
import com.zoom.music.db.AppOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by abiguime on 2016/9/7.
 */

public class MusicUtils {


    private static final String TAG = "MusicUtils";


  /*   public static final String KEY_ID = "_id";
	public static final String KEY_ALBUM_ID = "album_id";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_TITLE = "title";
	public static final String KEY_ARTIST = "artist";

	public static final String KEY_DATA = "_data";

	public static final String KEY_FAVORITE = "favorite";*/


    private static final String[] PROJECTION = {
            MusicInfo.KEY_ID,
            MusicInfo.KEY_ALBUM_ID,
            MusicInfo.KEY_DURATION,
            MusicInfo.KEY_ARTIST,
            MusicInfo.KEY_DATA,
            MusicInfo.KEY_MUSIC_TITLE,
            MusicInfo.KEY_ARTIST_ID,
    };

    public static String albumCoverImg (Context ctx, int albumId) {

        String path = "";
        ContentResolver cr = ctx.getContentResolver();
        Uri garlbumart = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor query = cr.query(garlbumart,
                new String[]{MediaStore.Audio.AlbumColumns.ALBUM_ART},
                "_id=?",
                new String[]{""+albumId},
                null);
        while (query.moveToNext()) {
            path = query.getString(query.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART));
        }
        return path == null ? "" : path;
    }



    /* 扫描本地音乐 更新数据库 */
    public static List<MusicInfo> querySystemMusic(Context ctx) {

        ContentResolver cr = ctx.getContentResolver();
        Uri gmusic = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor query = cr.query(gmusic, PROJECTION, null, null, null);
        return parseMusic(ctx, query); /* 获取音乐列表 */
        // 想应用的本地数据库保存获取好的音乐。。。
        /*音乐保存到本地数据库里*/
        /*SqliteOpenHelper*/
    }

    private static List<MusicInfo> parseMusic(Context ctx, Cursor query) {

        List<MusicInfo> msinfo = new ArrayList<>();

        while (query.moveToNext()) {
            MusicInfo tmp = new MusicInfo();
            tmp.data = query.getString(query.getColumnIndex(MusicInfo.KEY_DATA));  /*音乐绝对目录*/
            //判断文件存不存在。。。
//            if (!(new File(tmp.data)).exists())
//                continue;
            tmp._id = query.getInt(query.getColumnIndex(MusicInfo.KEY_ID)); /* 音乐id */
            tmp.albumId = query.getInt(query.getColumnIndex(MusicInfo.KEY_ALBUM_ID)); /*音乐专辑id*/
            tmp.duration = query.getInt(query.getColumnIndex(MusicInfo.KEY_DURATION));  /*音乐时间长度*/
            tmp.artist = query.getString(query.getColumnIndex(MusicInfo.KEY_ARTIST));  /*作者名字*/
            tmp.artist_id = query.getString(query.getColumnIndex(MusicInfo.KEY_ARTIST_ID));  /*作者id*/
            tmp.musicName = query.getString(query.getColumnIndex(MusicInfo.KEY_MUSIC_TITLE));  /*音乐名称*/
            tmp.musicName=musicNameFilter(tmp.musicName);
            tmp.folder = tmp.data.substring(0, tmp.data.lastIndexOf("/")+1);  /*音乐所在文件夹*/
            if(tmp.duration>60000) msinfo.add(tmp);
        }
        return msinfo;
    }
    private static String musicNameFilter(String musicName){
        int point=-1;
        int brackets=-1;

        point=musicName.lastIndexOf(".");
        brackets=musicName.lastIndexOf("[");

        if(point!=-1){
            musicName=musicName.substring(0,point);
        }
        if(brackets!=-1){
            musicName=musicName.substring(0,brackets);
        }
        return musicName;
    }
    /**
     * 获取音乐列表
     */
    public static List<MusicInfo> queryFavoriteMusic(Context ctx) {

        List<MusicInfo> mlist = new ArrayList<>();
        SQLiteDatabase instance = AppOpenHelper.getInstance(ctx);
        Cursor query = instance.query(AppOpenHelper.TABLE_FAVORITE, AppOpenHelper.FAVORITE_TABLE_ENTRIES
                ,null,null,null,null,null);
        while (query.moveToNext()) {
            MusicInfo tmp = new MusicInfo();
            tmp._id = query.getInt(query.getColumnIndex(AppOpenHelper.FAVORITE_TABLE_ENTRIES[0]));
            tmp.favorite = query.getInt(query.getColumnIndex(AppOpenHelper.FAVORITE_TABLE_ENTRIES[1]));
            mlist.add(tmp);
        }
        return mlist;
    }


    public static void setFavorite(Context ctx, MusicInfo info, boolean isFavorite) {

        SQLiteDatabase instance = AppOpenHelper.getInstance(ctx);
        if (isFavorite) {
            // 向收藏数据库添加本音乐
            ContentValues cv = new ContentValues();
            cv.put("_id", info._id);
            cv.put("favorite", 1);
            instance.insert(AppOpenHelper.TABLE_FAVORITE, null, cv);
            return;
        } else {
            // 向收藏数据库删除本音乐
            instance.delete(AppOpenHelper.TABLE_FAVORITE, "_id = ?", new String[]{""+info._id});
        }
    }

    public static void rescan(Context ctx) {

       /* MediaScannerConnection.scanFile(ctx, new String[] {

                        "file:///storage/emulated/0"},

                null, new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri)

                    {
                        Log.d("xxx", "path scanned completed");

                    }
                });*/
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.parse("file:///storage/emulated/0"));
        ctx.sendBroadcast(intent);
    }
}
