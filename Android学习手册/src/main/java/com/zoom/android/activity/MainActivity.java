package com.zoom.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.zoom.android.R;
import com.zoom.android.bean.itemInfo;
import com.zoom.android.db.InfoDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zoom.android.utils.Constant.img_res;
import static com.zoom.android.utils.Constant.str_res;


public class MainActivity extends Activity {

    ViewPager viewPager = null;
    View view_info = null, view_main = null;
    GridView gd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemInfo item=new itemInfo("4","wwww","ddddd");
         InfoDao infodao=new InfoDao(this);
        infodao.add(item);
        Log.i("ddd","sss");
        //获取xml资源
        getResourcesFromXml();
        //初始化主界面
        initViewPager();
        //初始化gridview
        initGridView();
    }

    private void getResourcesFromXml() {
        viewPager= (ViewPager) findViewById(R.id.viewpager);
    }

    private void initViewPager() {
        LayoutInflater inflater=getLayoutInflater();
        view_info=inflater.inflate(R.layout.view_info,null);
        view_main=inflater.inflate(R.layout.view_main,null);
        gd= (GridView) view_main.findViewById(R.id.gd);
        final ArrayList<View> viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view_main);
        //viewList.add(view_info);
        PagerAdapter pagerAdapter=new PagerAdapter() {
            //返回要滑动的VIew的个数
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            //做了两件事，第一：将当前视图添加到container中，第二：返回当前View
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            //从当前container中删除指定位置（position）的View;
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
                container.removeView(viewList.get(position));
            }
        };

        viewPager.setAdapter(pagerAdapter);
    }

    private void initGridView() {

        List<Map<String, Object>> data=new ArrayList<>();
        for(int i=0;i<img_res.length;i++){
            Map<String,Object> map=new HashMap<>();
            map.put("img",img_res[i]);
            map.put("text",str_res[i]);
            data.add(map);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(
                this,
                data,
                R.layout.grid_item,
                new String[]{"img","text"},
                new int[]{R.id.img,R.id.tv}
        );
        gd.setAdapter(simpleAdapter);

        gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(),"点击："+position,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,InfoActivity.class);
                intent.putExtra("position",Integer.toString(position));
                startActivity(intent);
            }
        });
    }


}
