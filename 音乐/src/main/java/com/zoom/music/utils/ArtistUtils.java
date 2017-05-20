package com.zoom.music.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.zoom.music.bean.ArtistInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abiguime on 2016/9/11.
 */
public class ArtistUtils {
    private static final String[] PROJECTION = {
            MediaStore.Audio.ArtistColumns.ARTIST,
            MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS,
    } ;

    public static List<ArtistInfo> querySystemAlbums(Context ctx) {

        List<ArtistInfo> inf = new ArrayList<>();
        ContentResolver cr = ctx.getContentResolver();
        Uri gmusic = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor query = cr.query(gmusic, PROJECTION, null, null, null);
        parseAlbums(inf, query);
        return inf;
    }

    private static void parseAlbums(List<ArtistInfo> inf, Cursor query) {

        while(query.moveToNext()) {
            ArtistInfo tmp = new ArtistInfo();
            tmp.artist_name = query.getString(query.getColumnIndex(MediaStore.Audio.ArtistColumns.ARTIST)); /* 专辑图 */
            tmp.number_of_tracks = query.getInt(query.getColumnIndex(MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS)); /* 专辑图 */
            inf.add(tmp);
        }
    }
}
