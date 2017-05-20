package com.zoom.music.fragment.intrf;


import com.zoom.music.bean.MusicInfo;

import java.util.List;

/**
 * Created by abiguime on 2016/9/15.
 */

public interface MusicFragmentInteface {

    public void play(int position, List<MusicInfo> infList);

    //
    public void setFavorite(MusicInfo info, boolean isFavorite);
}
