package com.zoom.music.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import com.zoom.music.R;
import com.zoom.music.bean.AlbumInfo;
import com.zoom.music.bean.ArtistInfo;
import com.zoom.music.bean.MusicInfo;
import com.zoom.music.utils.AlbumUtils;
import com.zoom.music.utils.ArtistUtils;
import com.zoom.music.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zoom.music.utils.BasicUtils.mT;


public class SplashActivity extends Activity {

    private static final long WAITING_TIME = 3000;
    private static final String TAG = SplashActivity.class.getSimpleName();
    public static final String MY_MUSIC_TAG = "MY_MUSIC_TAG" ;
    public static final String MY_ARTIST_TAG = "MY_ARTIST_TAG" ;
    public static final String MY_ALBUM_TAG = "MY_ALBUM_TAG" ;
    public static final String MY_FAVORITE_TAG = "MY_FAVORITE_TAG";

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        afterHavingPermission(); // 调到mainactivity里去
    }

    // 权限获取好之后，要进行的方法
    private void afterHavingPermission () {

       mT(this,"afterHavingPermission");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            /*获取、
            *        - 我的音乐，专辑，文件夹。。。  媒体内容提供者*/
            // 获取我的音乐列表
            final List<MusicInfo> musicList = MusicUtils.querySystemMusic(SplashActivity.this);
            final List<AlbumInfo> albumList = AlbumUtils.querySystemAlbums(SplashActivity.this);
            final List<ArtistInfo> artistList = ArtistUtils.querySystemAlbums(SplashActivity.this);
            // 从本应用数据库获取收藏过的音乐文件
            final List<MusicInfo> favoriteMusicList = MusicUtils.queryFavoriteMusic(SplashActivity.this);
            for (int i = 0; i < musicList.size(); i++) {
                for (int j = 0; j < favoriteMusicList.size(); j++) {
                    if (musicList.get(i)._id == favoriteMusicList.get(j)._id) {
                        musicList.get(i).setFavorite(1);
                    }
                }
            }
        /*过一段时间调到mainactivity里去*/
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putParcelableArrayListExtra(MY_MUSIC_TAG, (ArrayList<? extends Parcelable>) musicList);
                intent.putParcelableArrayListExtra(MY_ARTIST_TAG, (ArrayList<? extends Parcelable>) artistList);
                intent.putParcelableArrayListExtra(MY_ALBUM_TAG, (ArrayList<? extends Parcelable>) albumList);
                intent.putParcelableArrayListExtra(MY_FAVORITE_TAG, (ArrayList<? extends Parcelable>) favoriteMusicList);
                startActivity(intent); /* 调到 主页去 */
                overridePendingTransition(R.anim.home_enter,R.anim.splash_exit);
                finish();  /* 毁灭当前activity */
            }
        },WAITING_TIME);
    }


}
