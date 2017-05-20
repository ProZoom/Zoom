package com.zoom.music.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abiguime on 2016/9/7.
 */

public class AppOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1; /*数据库版本*/
    private static final String DB_NAME = "musicstore_new"; /*数据库名称*/

    public static final String TABLE_ALBUM = "album_info"; /*专辑表名字*/
    public static final String TABLE_ARTIST = "artist_info"; /*歌手表名字*/
    public static final String TABLE_MUSIC = "music_info"; /*音乐表*/
    public static final String TABLE_FOLDER = "folder_info"; /*文件夹表*/
    public static final String TABLE_FAVORITE = "favorite_info"; /*收藏表*/

    private static AppOpenHelper appOpenHelper;
    private static SQLiteDatabase mdB;


    public static final String[] FAVORITE_TABLE_ENTRIES = {"_id", "favorite"};

    public static SQLiteDatabase getInstance (Context ctx) {
        synchronized (AppOpenHelper.class) {
            mdB = getHelper(ctx).getWritableDatabase();
        }
        return mdB;
    }


    public static AppOpenHelper getHelper (Context contxt) {
        if (appOpenHelper == null) {
            synchronized (AppOpenHelper.class) {
                appOpenHelper = new AppOpenHelper(contxt);
            }
        }
        return appOpenHelper ;
    }


    private AppOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*创建数据库表 （SQL）*/
        db.execSQL("create table "
                + TABLE_MUSIC
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " songid integer, albumid integer, duration integer, musicname varchar(10), "
                + "artist char, data char, folder char, musicnamekey char, artistkey char, favorite integer)");
        db.execSQL("create table "
                + TABLE_ALBUM
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "album_name char, album_id integer, number_of_songs integer, album_art char)");
        db.execSQL("create table "
                + TABLE_ARTIST
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, artist_name char, number_of_tracks integer)");
        db.execSQL("create table "
                + TABLE_FOLDER
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, folder_name char, folder_path char)");
      /*  db.execSQL("create table "
                + TABLE_FAVORITE
                + " (_id integer,"
                + " songid integer, albumid integer, duration integer, musicname varchar(10), "
                + "artist char, data char, folder char, musicnamekey char, artistkey char, favorite integer)");
  */
        db.execSQL("create table "
                + TABLE_FAVORITE
                + " (_id integer, favorite integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        if (oldVersion<newVersion) {
            /*数据库该更新*/
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_MUSIC);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_ALBUM);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_ARTIST);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_FOLDER);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_FAVORITE);
        }
        onCreate(db);
    }
}
