package com.zoom.music.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zoom.music.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zoom.music.utils.Constant.rightmenu_img_res_id;
import static com.zoom.music.utils.Constant.rightmenu_res_id;


/**
 * Created by abiguime on 2016/9/2.
 */


/* 描述fragment */
public class MenuFragment extends Fragment {

    public static MenuFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.rightmenu_fragment, container, false);

        ListView listview = (ListView) rootview.findViewById(R.id.listview);


        List<Map<String, ?>> data = new ArrayList<>();

        for (int i = 0; i < rightmenu_img_res_id.length; i++) {

            Map<String, Object> mp = new HashMap<>();
            mp.put("image", rightmenu_img_res_id[i]);
            mp.put("text", getContext().getString(rightmenu_res_id[i]));
            data.add(mp);
        }


        SimpleAdapter adap = new SimpleAdapter(rootview.getContext(),
                data,
                R.layout.menu_list_item,
                new String[]{"image", "text"},
                new int[]{R.id.menu_item_imageview,
                R.id.menu_item_textview});

        listview.setAdapter(adap);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MenuOnclick)(rootview.getContext())).menuClick(position);
            }
        });
        return rootview;
    }



    /*activity需要给我提供点击菜单的借口*/
    public interface MenuOnclick {

        public void menuClick(int i);
    }


    public static final String MENU_TAG = MenuFragment.class.getSimpleName();
}
