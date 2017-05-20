package com.zoom.music.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zoom.music.R;
import com.zoom.music.bean.AlbumInfo;
import com.zoom.music.bean.ArtistInfo;
import com.zoom.music.bean.MusicInfo;
import com.zoom.music.fragment.MainHomeFragment;
import com.zoom.music.fragment.MenuFragment;
import com.zoom.music.fragment.MusicListFragment;
import com.zoom.music.fragment.intrf.MusicFragmentInteface;
import com.zoom.music.service.QQMediaPlayerService;
import com.zoom.music.utils.MusicUtils;
import com.zoom.music.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zoom.music.utils.BasicUtils.mT;
import static com.zoom.music.utils.Constant.menu_res_id;


public class MainActivity extends SlidingFragmentActivity implements MenuFragment.MenuOnclick,
        MainHomeFragment.MainHomeInterface, MusicFragmentInteface {


    private static final int PROP_NUMB = 5;
    private static final int UPDATE_UI = 90;
    public static final String IPAUSE = "com.zoom.music.IPAUSE";
    public static final String IPLAY = "com.zoom.music.IPLAY";
    private ViewPager viewpager;


    List<MusicInfo> favoriteMusicList;
    List<MusicInfo> musicInfoList;
    List<ArtistInfo> artistInfoList;
    List<AlbumInfo> albumInfoList;
    private MainViewPagerAdapter adaap;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_UI:
                    updateUI();
                    break;
            }
        }
    };
    private MyPausePlayReceiver brodcastReceiver;
    private Gson gson = new Gson();
    private MThread currentThread = null; // 获取进度的子线程

    @Override
    protected void onStart() {
        super.onStart();
        /* 注册广播*/
        brodcastReceiver = new MyPausePlayReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(IPAUSE);
        filter.addAction(IPLAY);
        registerReceiver(brodcastReceiver, filter);
    }

    // 服务的intent
    private Intent checkAndStartIntent;

    /* 链接服务对象*/
    private ServiceConnection conn = null;

    /* 底部菜单的视图 */
    TextView tv_title, tv_artist;
    TextView tv_left_progress, tv_right_progress;
    ImageView headicon_iv;

    ProgressBar playback_seekbar;
    ImageButton btn_pause, btn_play, btn_playNext,btn_menu2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAndStartService();

        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.right_menu); /*菜单内容*/
        getSlidingMenu().setTouchModeAbove(View.FOCUSABLES_TOUCH_MODE);//全屏
        //设置侧边栏的位置：右边
        getSlidingMenu().setMode(SlidingMenu.RIGHT);
        //设置预留的屏幕宽度
        getSlidingMenu().setBehindOffset(500);

        /*设置边栏菜单*/
        initMenu();
        initMainView();
        initBottomPlayerViews();
    }

    private void initBottomPlayerViews() {

        tv_title = (TextView) findViewById(R.id.musicname_tv2);
        tv_artist = (TextView) findViewById(R.id.artist_tv2);
       tv_left_progress = (TextView) findViewById(R.id.position_tv2);
        tv_right_progress = (TextView) findViewById(R.id.duration_tv2);
        btn_pause = (ImageButton) findViewById(R.id.btn_pause2);
        btn_play = (ImageButton) findViewById(R.id.btn_play2);
        btn_playNext = (ImageButton) findViewById(R.id.btn_playNext2);
        btn_menu2= (ImageButton) findViewById(R.id.btn_menu2);
        playback_seekbar = (ProgressBar) findViewById(R.id.playback_seekbar2);
        headicon_iv = (ImageView) findViewById(R.id.headicon_iv);

        btn_menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,MusicInfoActivity.class);
//                overridePendingTransition(R.anim.home_enter,R.anim.splash_exit);
//                startActivity(intent);
            }
        });

        /*歌曲详情表*/
        headicon_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,MusicInfoActivity.class);
//                overridePendingTransition(R.anim.home_enter,R.anim.splash_exit);
//                startActivity(intent);
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(cPosition, cInfo);
            }
        });
        btn_playNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayerInterface.playNext();
            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMusic();
            }
        });

        // 获取目前在播放的音乐
        SharedPreferences sp = getSharedPreferences(QQMediaPlayerService.PLAYER_SP, MODE_PRIVATE);

        String currentMusic = sp.getString(QQMediaPlayerService.CURRENT_MUSIC, "");
        String currentList = sp.getString(QQMediaPlayerService.CURRENT_LIST, "");
        if (!currentMusic.equals("")/*是否同样的音乐文件*/ &&
                !currentList.equals("")
                ) {
            cInfo =  gson.fromJson(currentList,
                    new TypeToken<List<MusicInfo>>(){}.getType());
            MusicInfo tmp = gson.fromJson(currentMusic,
                    MusicInfo.class);
            cPosition = -1;
            for (int i = 0; i < cInfo.size(); i++) {
                if (cInfo.get(i).equals(tmp))
                    cPosition = i;
            }
            if (cPosition == -1)
                cPosition = 0;
        }
        try {
            MusicInfo cp = cInfo.get(cPosition);
            tv_left_progress.setText("00:00");
            tv_right_progress.setText(Utils.fromMilliToSecond(cp.duration));
            tv_artist.setText(cInfo.get(cPosition).artist);
            tv_title.setText(cInfo.get(cPosition).musicName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkAndStartService() {
        checkAndStartIntent = new Intent(this, QQMediaPlayerService.class);
        startService(checkAndStartIntent);
    }


    private void initMainView() {

        /*从引导页获取数据*/
        // 1- 音乐列表
        musicInfoList = getIntent().getParcelableArrayListExtra(SplashActivity.MY_MUSIC_TAG);
        // 2- 歌手信息列表
        artistInfoList = getIntent().getParcelableArrayListExtra(SplashActivity.MY_ARTIST_TAG);
        // 3- 专辑列表
        albumInfoList = getIntent().getParcelableArrayListExtra(SplashActivity.MY_ALBUM_TAG);
        // 4- 收藏音乐列表
        favoriteMusicList = getIntent().getParcelableArrayListExtra(SplashActivity.MY_FAVORITE_TAG);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        adaap = new MainViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adaap);
    }


    private void initMenu() {
        /*将framelayout添加右侧栏。。。*/
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.add(R.id.menu_frame, MenuFragment.newInstance(), MenuFragment.MENU_TAG);
        trans.commit();
    }


    @Override
    public void menuClick(int i) {
        /**/
        if (i == 0){
            MusicUtils.rescan(this); //
        } else if (i == 5) {
        }
        else
            mT(this,"点击—— "+getResources().getString(menu_res_id[i]));
    }


    @Override
    public List<MusicInfo> getMusicInfo() {
        return musicInfoList;
    }

    @Override
    public List<ArtistInfo> getArtistInfo() {
        return artistInfoList;
    }

    @Override
    public List<AlbumInfo> getAlbumInfo() {
        return albumInfoList;
    }

    @Override
    public List<MusicInfo> getFavoriteMusicInfo() {
        return favoriteMusicList;
    }


    @Override
    public void OnMyMusicPressed() {
            /*切换fragment
            * 1- 把数据创给fragment、
            * 2- 再切换fragment */
        MusicListFragment frg = (MusicListFragment) adaap.getItem(MainViewPagerAdapter.POSITION_1_MUSICLIST);
        mT(this,"切换了");
        frg.updateData(getMusicInfo());
        // 等500毫秒再切换fragment
        viewpager.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewpager.setCurrentItem(MainViewPagerAdapter.POSITION_1_MUSICLIST);
                    }
                });
            }
        }, 500);
    }

    @Override
    public void OnMyFoldersPressed() {
        Intent in = new Intent(MainActivity.this, FoldersActivity.class);
        startActivity(in);
    }
    @Override
    public void pauseMusic() {
        if (musicPlayerInterface != null)
            musicPlayerInterface.pause();
    }

    @Override
    public void OnFavouriteMusicPressed() {
        mT(this,"我的最爱");
        MusicListFragment frg = (MusicListFragment) adaap.getItem(MainViewPagerAdapter.POSITION_1_MUSICLIST);
        mT(this,"切换了");
        frg.updateData(favoriteMusicList);
        // 等500毫秒再切换fragment
        viewpager.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewpager.setCurrentItem(MainViewPagerAdapter.POSITION_1_MUSICLIST);
                    }
                });
            }
        }, 500);
    }

    @Override
    public void OnMySongerPressed() {
        mT(this,"我的歌手");
        MusicListFragment frg = (MusicListFragment) adaap.getItem(MainViewPagerAdapter.POSITION_1_MUSICLIST);
        //frg.updateData(getArtistInfo());
        // 等500毫秒再切换fragment
        viewpager.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewpager.setCurrentItem(MainViewPagerAdapter.POSITION_1_MUSICLIST);
                    }
                });
            }
        }, 500);
    }

    @Override
    public void OnMyAlbumPressed() {
        mT(this,"我的专辑");
        MusicListFragment frg = (MusicListFragment) adaap.getItem(MainViewPagerAdapter.POSITION_1_MUSICLIST);
        //frg.updateData(getAlbumInfo());
        // 等500毫秒再切换fragment
        viewpager.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewpager.setCurrentItem(MainViewPagerAdapter.POSITION_1_MUSICLIST);
                    }
                });
            }
        }, 500);
    }

    // 正在播放的媒体并媒体在列表中的id。
    int cPosition = -1;
    List<MusicInfo> cInfo = null;

    // 绑定之后从服务获取的接口
    QQMediaPlayerService.MusicPlayerInterface musicPlayerInterface = null;

    @Override
    public void play(final int position, final List<MusicInfo> infList) {

        // 有没有绑定到服务上？
        if (checkAndStartIntent == null) {
            checkAndStartIntent = new Intent(this, QQMediaPlayerService.class);
        }
        if (musicPlayerInterface == null) {
            bindService(checkAndStartIntent,  new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    // 获取服务接口。
                    conn = this;
                    musicPlayerInterface = (QQMediaPlayerService.MusicPlayerInterface) service;
                    musicPlayerInterface.play(position, infList);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    musicPlayerInterface = null;
                }
            }, BIND_NOT_FOREGROUND);
        } else {
            musicPlayerInterface.play(position, infList);
        }
        cPosition = position;
        cInfo = infList;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn!=null)
            unbindService(conn);
        unregisterReceiver(brodcastReceiver);
    }

    @Override
    public void setFavorite(final MusicInfo info, final boolean isFavorite) {

        // true -> 收藏
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (musicInfoList) {
                    synchronized (favoriteMusicList) {
                        // 向actvity aRRAY更新状态
                        if (isFavorite) {
                            // 1- 向musicInfoList把本音乐文件favorite字段改成1
                            // 2- 向收藏列表音乐添加本音乐文件
                            for (int i = 0; i < musicInfoList.size(); i++) {
                                if (musicInfoList.get(i)._id == info._id) {
                                    musicInfoList.get(i).setFavorite(1);
                                    favoriteMusicList.add(musicInfoList.get(i));
                                    break;
                                }
                            }
                        } else {
                            // 1- 向musicInfoList把本音乐文件favorite字段改成0
                            // 2- 向收藏列表音乐删除本音乐文件
                            for (int i = 0; i < musicInfoList.size(); i++) {
                                if (musicInfoList.get(i)._id == info._id) {
                                    musicInfoList.get(i).setFavorite(0);
                                    for (int k = 0; k < favoriteMusicList.size(); k++) {
                                        if (favoriteMusicList.get(k)._id == info._id) {
                                            favoriteMusicList.remove(k);
                                            break;
                                        }
                                    }
                                    ;
                                    break;
                                }
                            }
                        }
                        // 更新MainFragmentHome--》UI
                        // 删除目前的适配里的所有fragment
                        mHandler.sendEmptyMessage(UPDATE_UI);
                    }
                }
                MusicUtils.setFavorite(MainActivity.this, info, isFavorite);
                //
            }
        }).start();
        // false -> 取消收藏
    }

    private void updateUI() {
        // 更新菜单
        ((MainHomeFragment)(adaap.getItem(MainViewPagerAdapter.POSITION_0_HOME)))
                .updateGridView(MainActivity.this);
        //
        adaap.setMusicListFragmentToNUll();
    }

    /*更新底部播放功能*/
    private void updateBottomPlayer(MusicInfo inf, boolean isPlaying) {
        if (isPlaying) {
            tv_title.setText(inf.musicName);
            tv_artist.setText(inf.artist);
            tv_right_progress.setText(Utils.fromMilliToSecond(inf.duration));
            btn_play.setVisibility(View.INVISIBLE);
            btn_pause.setVisibility(View.VISIBLE);
            // 启动获取播放进度的子线程
        } else {
            btn_pause.setVisibility(View.INVISIBLE);
            btn_play.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public class MainViewPagerAdapter extends FragmentPagerAdapter {

        public static final int POSITION_0_HOME = 0, POSITION_1_MUSICLIST = 1, POSITION_2_XXX = 2;

        public MainViewPagerAdapter(FragmentManager fm) {
            super(fm);
            frg = new HashMap<>();
        }

        Map<Integer, Fragment> frg;

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: /*显示主页面*/
                    if (frg.get(position)  == null) {
                        frg.put(position, MainHomeFragment.newInstance());
                    }
                    break;
                case 1: /*显示音乐列表*/
                    if (frg.get(position)  == null) {
                        frg.put(position, MusicListFragment.newInstance(new ArrayList<MusicInfo>()));
                    }
                    break;
                case 2: /*显示歌手。。。*/
                    if (frg.get(position)  == null) {
                        frg.put(position, MusicListFragment.newInstance(new ArrayList<MusicInfo>()));
                    }
                    break;
                case 3: /*显示歌手。。。*/
                    if (frg.get(position)  == null) {
                        frg.put(position, MusicListFragment.newInstance(new ArrayList<MusicInfo>()));
                    }
                    break;
                case 4: /*显示歌手。。。*/
                    if (frg.get(position)  == null) {
                        frg.put(position, MusicListFragment.newInstance(new ArrayList<MusicInfo>()));
                    }
                    break;
                case 5: /*显示歌手。。。*/
                    if (frg.get(position)  == null) {
                        frg.put(position, MusicListFragment.newInstance(new ArrayList<MusicInfo>()));
                    }
                    break;
            }
            return frg.get(position);
        }

        @Override
        public int getCount() {
            return PROP_NUMB;
        }

        // 音乐列表fragment设成null
        public void setMusicListFragmentToNUll() {
          /*  if (frg!=null)
                frg.put(POSITION_1_MUSICLIST, MusicListFragment.newInstance(null));
       */
        }
    }


    public class MyPausePlayReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MusicInfo inf = intent.getExtras().getParcelable("MUSICINFO");
            updateBottomPlayer(inf, intent.getAction().equals(IPLAY));
            if (intent.getAction().equals(IPLAY)) {
                // 获取音乐列表
                cInfo = intent.getExtras().getParcelableArrayList(QQMediaPlayerService.CURRENT_LIST);
                // 获取当前播放位置
                cPosition = intent.getExtras().getInt(QQMediaPlayerService.POSITION);
                //
                final MusicInfo xxx = cInfo.get(cPosition);
                final String picPath = MusicUtils.albumCoverImg(MainActivity.this, xxx.albumId);
                if (!"".equals(picPath.trim())) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                            headicon_iv.setImageBitmap(bitmap);
                        }
                    });
                } else {
                    headicon_iv.setImageResource(R.mipmap.img_album_background);
                }
                // 启动子线程
                currentThread = new MThread();
                currentThread.start();
            } else { /* 音乐暂停之后，把获取进度的子线程停止*/
                if(currentThread!= null)
                    currentThread.isPlaying = false;
            }
        }
    }


    /* 获取进度子线程 */
    class MThread extends Thread implements Runnable{
        public boolean isPlaying = true;

        public MThread() {
        }

        @Override
        public void run() {
            final int totalDuration = musicPlayerInterface.getTotalProgress();
            while(this.isPlaying) {
                try {
                    Thread.sleep(1000);
                    if (isPlaying) {
                        // 当前进度s
                        final int duration = musicPlayerInterface.getProgress();
                        // 媒体总共长度
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("xxx", "duration --> "+duration+" - totalduration --> "+totalDuration);
                                playback_seekbar.setMax(totalDuration);
                                playback_seekbar.setProgress(duration);
                                tv_left_progress.setText(Utils.fromMilliToSecond(duration));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
