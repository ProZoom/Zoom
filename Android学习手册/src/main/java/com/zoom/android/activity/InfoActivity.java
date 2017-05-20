package com.zoom.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.zoom.android.R;
import com.zoom.android.bean.itemInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/27.
 */

public class InfoActivity extends Activity {
    String content = "";
    ListView listView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_info);
        listView= (ListView) findViewById(R.id.lv);
        Intent intent=getIntent();
        content=intent.getStringExtra("position");
        Toast.makeText(this,"点击："+content,Toast.LENGTH_SHORT).show();

        List<itemInfo> infos=null;


        List<Map<String, String>> data=new ArrayList<>();
        for(int i=0;i<infos.size();i++){
            Map<String, String> map=new HashMap<>();
            map.put("title", String.valueOf(infos));
           // map.put("id",);
            data.add(map);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(
                this,
                data,
                R.layout.lv_item,
                new String[]{"title"},
                new int[]{R.id.tv}
        );
        listView.setAdapter(simpleAdapter);
    }
}
