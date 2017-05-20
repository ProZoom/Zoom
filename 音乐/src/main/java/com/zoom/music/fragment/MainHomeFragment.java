package com.zoom.music.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.zoom.music.R;
import com.zoom.music.bean.AlbumInfo;
import com.zoom.music.bean.ArtistInfo;
import com.zoom.music.bean.MusicInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zoom.music.utils.Constant.img_res_id;
import static com.zoom.music.utils.Constant.menu_res_id;

/**
 * Created by abiguime on 2016/9/10.
 */

public class MainHomeFragment extends Fragment {

    private static final String ID = "ID";
    private GridView gridview;
    private SimpleAdapter adapter;

    private String link= "http://m5.file.xiami.com/940/2940/15102/185689_10705141_l.mp3?auth_key=6c221abe214138f4ead81112ebc865d8-1474686000-0-null";


    public static MainHomeFragment newInstance(/*int position*/) {

        Bundle args = new Bundle();
//        args.putInt(ID, position);
        MainHomeFragment fragment = new MainHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.mainhome_fragment, container, false);

        /*获取activity中的数据*/

        gridview = (GridView) rootview.findViewById(R.id.gridview);

        Object[] menu_count = {
                ((MainHomeFragment.MainHomeInterface)rootview.getContext()).getMusicInfo().size(),
                ((MainHomeFragment.MainHomeInterface)rootview.getContext()).getFavoriteMusicInfo().size(),
                0,
                ((MainHomeFragment.MainHomeInterface)rootview.getContext()).getArtistInfo().size(),
                ((MainHomeFragment.MainHomeInterface)rootview.getContext()).getAlbumInfo().size(),
                0
        };

        List<Map<String, Object>> data = new ArrayList<>();

        for (int i = 0; i < menu_res_id.length; i++) {
            Map<String, Object> mp = new HashMap<>();
            mp.put("image", img_res_id[i]);
            mp.put("count", menu_count[i]);
            mp.put("desc", getContext().getResources().getString(menu_res_id[i]));
            data.add(mp);
        }

        adapter = new SimpleAdapter(rootview.getContext(),
                data,
                R.layout.main_home_gridview_item,
                new String[]{"image", "desc", "count"},
                new int[]{R.id.img_pic, R.id.tv_desc, R.id.tv_count});

        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    /* 我的音乐 */
                    ((MainHomeInterface)getActivity()).OnMyMusicPressed();
                } else if(position==1){
                    /*我的最爱*/
                    ((MainHomeInterface)getActivity()).OnFavouriteMusicPressed();
                }else if (position == 2) {
                    /*文件夹*/
                    ((MainHomeInterface)getActivity()).OnMyFoldersPressed();
                }else if(position==3){
                    /*我的歌手*/
                    ((MainHomeInterface)getActivity()).OnMySongerPressed();
                        }else if(position==4){
                    /*我的专辑*/
                            ((MainHomeInterface)getActivity()).OnMyAlbumPressed();
                                }
            }
        });
        return rootview;
    }

    public void updateGridView(Context ctx) {

        Object[] menu_count = {
                ((MainHomeFragment.MainHomeInterface)ctx).getMusicInfo().size(),
                ((MainHomeFragment.MainHomeInterface)ctx).getFavoriteMusicInfo().size(),
                0,
                ((MainHomeFragment.MainHomeInterface)ctx).getArtistInfo().size(),
                ((MainHomeFragment.MainHomeInterface)ctx).getAlbumInfo().size(),
                0
        };

        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < menu_res_id.length; i++) {
            Map<String, Object> mp = new HashMap<>();
            mp.put("image", img_res_id[i]);
            mp.put("count", menu_count[i]);
            mp.put("desc", getContext().getResources().getString(menu_res_id[i]));
            data.add(mp);
        }

        adapter = new SimpleAdapter(ctx,
                data,
                R.layout.main_home_gridview_item,
                new String[]{"image", "desc", "count"},
                new int[]{R.id.img_pic, R.id.tv_desc, R.id.tv_count});

        gridview.setAdapter(adapter);
    }


    public interface MainHomeInterface {

        /*获取数据方法*/
        List<MusicInfo> getMusicInfo();
        List<MusicInfo> getFavoriteMusicInfo();
        List<ArtistInfo> getArtistInfo();
        List<AlbumInfo> getAlbumInfo();

        /*调用方法*/
        void OnMyMusicPressed();
        void OnMyFoldersPressed();
        void pauseMusic();
        void OnFavouriteMusicPressed();
        void OnMySongerPressed();
        void OnMyAlbumPressed();
    }

}
