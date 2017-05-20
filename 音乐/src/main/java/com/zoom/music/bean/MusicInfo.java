/**
 * Copyright (c) www.longdw.com
 */
package com.zoom.music.bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable {



	public static final String KEY_ID = "_id";
	public static final String KEY_ALBUM_ID = "album_id";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_ARTIST = "artist";
	public static final String KEY_DATA = "_data";
//	public static final String KEY_FOLDER = "folder";
//	public static final String KEY_MUSIC_NAME_KEY = "musicnamekey";
	public static final String KEY_ARTIST_ID = "artist";
	public static final String KEY_FAVORITE = "favorite";
	public static final String KEY_MUSIC_TITLE = "title";

	/*音乐名字
	* 歌手id，歌手名
	* 文件夹
	* 音乐长度
	* 专辑id*/
	
	/** 数据库中的_id */
	public int _id = -1;
	public int songId = -1;
	public int albumId = -1;
	public int duration;
	public String musicName;
	public String artist;
	public String data;
	public String folder;
	public String musicNameKey;
	public String artist_id;
	/** 0表示没有收藏 1表示收藏 */
	public int favorite = 0;
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_ID, _id);
//		bundle.putInt(KEY_SONG_ID, songId);
		bundle.putInt(KEY_ALBUM_ID, albumId);
		bundle.putInt(KEY_DURATION, duration);
//		bundle.putString(KEY_MUSIC_NAME, musicName);
		bundle.putString(KEY_ARTIST, artist);
		bundle.putString(KEY_ARTIST_ID, artist_id);
		bundle.putString(KEY_MUSIC_TITLE, musicName);
		bundle.putString(KEY_DATA, data);
//		bundle.putString(KEY_FOLDER, folder);
//		bundle.putString(KEY_MUSIC_NAME_KEY, musicNameKey);
		bundle.putInt(KEY_FAVORITE, favorite);
		dest.writeBundle(bundle);
	}
	
	public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {

		@Override
		public MusicInfo createFromParcel(Parcel source) {
			MusicInfo music = new MusicInfo();
			Bundle bundle = new Bundle();
			bundle = source.readBundle();
			music._id = bundle.getInt(KEY_ID);
			music.musicName = bundle.getString(KEY_MUSIC_TITLE);
//			music.songId = bundle.getInt(KEY_SONG_ID);
			music.albumId = bundle.getInt(KEY_ALBUM_ID);
			music.duration = bundle.getInt(KEY_DURATION);
//			music.musicName = bundle.getString(KEY_MUSIC_NAME);
			music.artist = bundle.getString(KEY_ARTIST);
			music.artist_id = bundle.getString(KEY_ARTIST_ID);
			music.data = bundle.getString(KEY_DATA);
//			music.folder = bundle.getString(KEY_FOLDER);
//			music.musicNameKey = bundle.getString(KEY_MUSIC_NAME_KEY);
			music.favorite = bundle.getInt(KEY_FAVORITE);
			return music;
		}

		@Override
		public MusicInfo[] newArray(int size) {
			return new MusicInfo[size];
		}
	};


	public int getFavorite() {
		return favorite;
	}
	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}


	@Override
	public String toString() {
		return "MusicInfo{" +
				"_id=" + _id +
				", songId=" + songId +
				", albumId=" + albumId +
				", duration=" + duration +
				", musicName='" + musicName + '\'' +
				", artist='" + artist + '\'' +
				", data='" + data + '\'' +
				", folder='" + folder + '\'' +
				", musicNameKey='" + musicNameKey + '\'' +
				", favorite=" + favorite +
				'}';
	}
}