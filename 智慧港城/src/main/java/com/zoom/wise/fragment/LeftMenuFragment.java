package com.zoom.wise.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zoom.wise.R;
import com.zoom.wise.bean.NewsCenterData;
import com.zoom.wise.utils.L;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李阳
 * @创建时间 2017-02-28  下午2:28
 * @描述 TODO
 * @ 当前版本:
 */

@ContentView(R.layout.fragment_left_content)
public class LeftMenuFragment extends BaseFragment {

    private List<NewsCenterData.NewsData> data=new ArrayList<>();//新闻中心左侧数据

    private MyAdapter adapter;

    @ViewInject(R.id.lv_leftmenu)
    private ListView lv_leftData;


    public void setLeftMenuData(List<NewsCenterData.NewsData> data){
        this.data=data;
        adapter.notifyDataSetChanged();//设置好数据后，通知界面刷新
    }

    @Override
    public void initData() {
        super.initData();
        //组织数据
        adapter=new MyAdapter();
        lv_leftData.setAdapter(adapter);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        lv_leftData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("leftMenu--listview--postion-- "+position);
                //
                mainActivity.getMainMenuFragment().leftMenuClickSwitchPages(position);
            }
        });
    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //现实数据
            TextView tv_currentView;
            if(convertView==null){
                convertView= View.inflate(mainActivity,R.layout.leftmenu_list_item,null);
            }
            tv_currentView= (TextView) convertView.findViewById(R.id.tv_base_content_title);
            //设置数据
            tv_currentView.setText(data.get(position).title);
            return convertView;
        }
    }
}
