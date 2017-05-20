package com.zoom.music.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zoom.music.activity.MainActivity;
import com.zoom.music.bean.MusicInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abiguime on 2016/9/19.
 */

public class QQMediaPlayerService extends Service {


    public static final String PLAYER_SP = "PLAYER_SP";
    public static final String CURRENT_MUSIC = "current_music";
    public static final String CURRENT_LIST = "current_list";
    public static final String CURRENT_DURATION = "current_duration";
    public static final String POSITION = "current_position";

    // 播放音乐对象
    MusicPlayerInterface.MyMediaPlayer mp = null;

    // 绑定接口
    private IBinder mybinder = null;


    // 是否在播放
    public boolean isPlaying = false;


    // GSON
    Gson gson = new Gson();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mybinder == null)
            mybinder = new MusicPlayerInterface();
        return mybinder;
    }


    public class MusicPlayerInterface extends Binder {

            /* - 通过路劲或者音乐id，播放音乐播放音乐
            - 暂停音乐。
            - next 播放下一个、 prev 上一个
            - 启动随机播放---*/
        // ------------------------------------------

        private List<MusicInfo> pathlist;
        private int position;

        public MusicPlayerInterface() {
            if (mp == null)
                mp = new MyMediaPlayer();
        }

        /**
         * - position -> 音乐在列表中的位置
         * - pathlist -> 正在播放的音乐列表
         */
        public void play(int pos, List<MusicInfo> pathlist){

            this.position = pos;
            this.pathlist = pathlist;
          /*  if (mp != null && mp.isPlaying()) {
                pause();
            }*/
            /*
            * 1- 播放摸个列表下的某个音乐文件时
            *       - 把当前文件和列表的信息，保存到sharedpreferences？
            *               - 以后重启app，也可以接着播放本音乐
            *
            * 2- 每一次暂停音乐时
            *       1- 保存当前已经播放的进度——》保存到sharedpreferences
            *       2- 释放mediaplayer
            *
            * 3- 重新或者继续播放时、如果是同一音乐文件获取目前的进度，如果不是，那就播放新的音乐文件。
            *
            * */
            SharedPreferences sp = getSharedPreferences(PLAYER_SP, MODE_PRIVATE);
//            editor.putString("current_list", )

            int duration = 0;
            String currentMusic = sp.getString(CURRENT_MUSIC, "");
            String currentList = sp.getString(CURRENT_LIST, "");
            if (currentMusic.equals(gson.toJson(pathlist.get(pos)))/*是否同样的音乐文件*/ &&
                    currentList.equals(gson.toJson(pathlist))
                    ) {
                // 继续播放 -->  获取目前的进度
                duration = sp.getInt(CURRENT_DURATION, 0);
            }
            try {
                if (mp != null) {
                    if (mp.isPlaying())
                        mp.pause();
                    mp.release();
                    mp = null; // 释放mp
                }
                // 播放
                mp = new MyMediaPlayer();
                String file  = "file:///"+pathlist.get(pos).data;
                mp.setDataSource(file);
                mp.prepare();
                mp.seekTo(duration);// 把进度跳到应该的位置
                mp.start();
                mT("播放 - "+pathlist.get(position).musicName);
                sendPlayingBroadcast(MainActivity.IPLAY);
                isPlaying  =true;
                SharedPreferences.Editor editor = sp.edit();
                // GSON
                editor.putString(CURRENT_MUSIC, gson.toJson(pathlist.get(pos)));
                editor.putString(CURRENT_LIST, gson.toJson(pathlist));
                editor.putInt(CURRENT_DURATION, duration);
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* 暂停音乐*/
        public void pause (){

            SharedPreferences sp = getSharedPreferences(PLAYER_SP, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            // GSON
            editor.putString(CURRENT_MUSIC, gson.toJson(pathlist.get(position)));
            editor.putString(CURRENT_LIST, gson.toJson(pathlist));
            editor.putInt(CURRENT_DURATION, mp.getCurrentPosition());
            editor.commit();
            mp.pause();
            mp.release();
            mp = null;
            sendPlayingBroadcast(MainActivity.IPAUSE);
            /*mp.pause();
            isPlaying  = false;
            // 保存当前进度。*/
        }

        /*播放下一个*/
        public void playNext() {
            if (position == pathlist.size()-1) {
                position = 0;
            } else {
                position++;
            }
            play(position, pathlist);
        }

        /*播放上一个*/
        public void playPrev () {
            if (position == 0) {
                position = pathlist.size()-1;
            } else {
                position--;
            }
            play(position, pathlist);
        }

        public int getTotalProgress() {
            try {
                if (mp != null)
                return mp.getDuration();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        public int getProgress() {
            try {
                if (mp != null)
               return mp.getCurrentPosition();
            } catch (Exception e) {
            e.printStackTrace();
            }
            return 0;
        }

        public class MyMediaPlayer extends MediaPlayer {

            @Override
            public void setOnCompletionListener(OnCompletionListener listener) {
                super.setOnCompletionListener(listener);
                // 当音乐播放完成，下一个
                playNext();
            }
        }

        private void sendPlayingBroadcast(String s) {
            Intent intent = new Intent(s);
            intent.putParcelableArrayListExtra(CURRENT_LIST, (ArrayList<? extends Parcelable>) pathlist);
            intent.putExtra(POSITION, position);
            intent.putExtra("MUSICINFO", pathlist.get(position));
            sendBroadcast(intent);
        }

    }


    private void mT(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("xxx", "服务启动");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("xxx", "服务onDestroy");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d("xxx", "服务onRebind");
    }

}
