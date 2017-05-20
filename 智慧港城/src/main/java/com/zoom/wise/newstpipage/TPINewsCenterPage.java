package com.zoom.wise.newstpipage;


import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zoom.wise.R;
import com.zoom.wise.activity.MainActivity;
import com.zoom.wise.bean.NewsCenterData;
import com.zoom.wise.bean.TpiNewsData;
import com.zoom.wise.utils.DensityUtils;
import com.zoom.wise.utils.L;
import com.zoom.wise.utils.SpTools;
import com.zoom.wise.view.RefreshListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.zoom.wise.utils.Contants.Absolute_path;


public class TPINewsCenterPage
{

    //注解所有组件
    @ViewInject(R.id.vp_lunbo)
    private ViewPager vp_lunbo ; //轮播图的显示组件

    @ViewInject(R.id.tv_lunbo_title)
    private TextView tv_pic_desc;//图片的描述信息

    @ViewInject(R.id.ll_lunbo_point)
    private LinearLayout ll_points;//轮播图的每张图片对应的点组合

    @ViewInject(R.id.lv_tpi)
    private RefreshListView lv_listnews;// 显示列表新闻的组件

    //数据
    private MainActivity mainActivity;
    private View root;
    private NewsCenterData.NewsData.ViewTagData viewTagData;//页签对应的数据
    private Gson gson;
    private String jsonCache;


    private List<TpiNewsData.TPIData.lunbo_news> lunboDatas = new ArrayList<>();    //轮播图的数据

    private LunBoAdapter lunboAdapter ;  //轮播图的适配器

    private int	picSelectIndex;

    private LunBoTask	lunboTask;

    private List<TpiNewsData.TPIData.listView_News>	listNews = new ArrayList<>();    //新闻列表的数据

    private ListNewsAdapter	listNewsAdapter;

    public TPINewsCenterPage(MainActivity mainActivity, NewsCenterData.NewsData.ViewTagData viewTagData){
        this.mainActivity = mainActivity;
        this.viewTagData = viewTagData;
        gson = new Gson();
        //轮播的任务
        lunboTask = new LunBoTask();

        initView();//初始化界面
        initData();//初始化数据
        initEvent();//初始化事件
    }

    private void initView() {
        //页签对应页面的根布局
        root = View.inflate(mainActivity, R.layout.tpi_news_content, null);
        x.view().inject(this, root);

        View lunBoPic = View.inflate(mainActivity, R.layout.tpi_news_lunbopic, null);

        x.view().inject(this, lunBoPic);

        //把轮播图加到listView中
        lv_listnews.addLunBoView(lunBoPic);
    }

    private void initData() {
        //轮播图的适配器
        lunboAdapter  = new LunBoAdapter();
        //给轮播图
        vp_lunbo.setAdapter(lunboAdapter );

        //新闻列表的适配器
        listNewsAdapter = new  ListNewsAdapter();
        //设置新闻列表适配
        lv_listnews.setAdapter(listNewsAdapter);

        //从本地获取数据
        jsonCache = SpTools.getString(mainActivity, viewTagData.url, "");
        if (!TextUtils.isEmpty(jsonCache)) {
            //有数据，解析数据
            TpiNewsData newsData = parseJson(jsonCache);
            //处理数据
            processData(newsData);
        }

        getDataFromNet();//从网络获取数据
    }


    private void initEvent() {
        //给轮播图添加页面切换事件
        vp_lunbo.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                picSelectIndex = position;
                setPicDescAndPointSelect(picSelectIndex);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub

            }
        });
    }


    private void getDataFromNet() {

        x.http().get(new RequestParams(Absolute_path + viewTagData.url), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String jsonData =result;
                L.i("TPINewsCenterPage-->从网络读取的数据jsonData-->\n"+jsonData);
                //将数据保存在本地
                SpTools.setString(mainActivity,viewTagData.url,jsonData);
                //解析数据
                TpiNewsData newsDatas = parseJson(jsonData);
                //数据处理
                processData(newsDatas);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.i(Absolute_path+viewTagData.url+"---->onFailure");
                //访问数据失败
                if(!TextUtils.isEmpty(jsonCache)){//有本地数据，从本地数据显示
                    L.i("NewCenterBaseTagPager-->jsonCache-->访问网络失败，读取本地数据");
                    parseJson(jsonCache);
                }else {
                    L.i("网络连接失败，没有本地数据，请打开请连接网络");
                    Toast.makeText(mainActivity.getApplicationContext(),"首次打开请连接网络", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private TpiNewsData parseJson(String jsonData){
        //解析json数据
        TpiNewsData tpiNewsData = gson.fromJson(jsonData, TpiNewsData.class);
        return tpiNewsData;
    }

    private void processData(TpiNewsData newsData){
        //完成数据的处理

        //1.设置轮播图的数据
        setLunBoData(newsData);

        //2.轮播图对应的点处理
        initPoints();//初始化轮播图的点

        //3. 设置图片描述和点的选中效果
        setPicDescAndPointSelect(picSelectIndex);

        //4. 开始轮播图
        lunboTask.startLunbo();

        //5. 新闻列表的数据
        setListViewNews(newsData);
    }

    private void setLunBoData(TpiNewsData newsData) {
        //获取轮播图的数据
        lunboDatas = newsData.data.topnews;
        lunboAdapter.notifyDataSetChanged();//更新界面
    }

    private void setListViewNews(TpiNewsData newsData) {

        listNews  = newsData.data.news;
        //更新界面
        listNewsAdapter.notifyDataSetChanged();
    }

    private class LunBoTask extends Handler implements Runnable{

        public void stopLunbo(){
            //移除当前所有的任务
            removeCallbacksAndMessages(null);
        }
        public void startLunbo(){
            stopLunbo();
            postDelayed(this, 2000);
        }
        @Override
        public void run() {
            //控制轮播图的显示
            vp_lunbo.setCurrentItem((vp_lunbo.getCurrentItem() + 1) % vp_lunbo.getAdapter().getCount());
            postDelayed(this, 2000);
        }

    }

    private void setPicDescAndPointSelect(int picSelectIndex) {
        //设置描述信息
        tv_pic_desc.setText(lunboDatas.get(picSelectIndex).title);

        //设置点是否是选中
        for (int i = 0; i < lunboDatas.size(); i++) {
            ll_points.getChildAt(i).setEnabled(i == picSelectIndex);
        }

    }

    private void initPoints() {
        //清空以前存在的点
        ll_points.removeAllViews();
        //轮播图有几张 加几个点
        for (int i = 0; i < lunboDatas.size() ; i++) {
            View v_point = new View(mainActivity);
            //设置点的背景选择器
            v_point.setBackgroundResource(R.drawable.point_seletor);
            v_point.setEnabled(false);//默认是默认的灰色点

            //设置点的大小
            LayoutParams params = new LayoutParams(DensityUtils.dip2px(mainActivity,5), DensityUtils.dip2px(mainActivity,5));
            //设置点与点直接的间距
            params.leftMargin = DensityUtils.dip2px(mainActivity,10);

            //设置参数
            v_point.setLayoutParams(params);
            ll_points.addView(v_point);
        }
    }

    private class ListNewsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listNews.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(mainActivity, R.layout.tpi_news_listview_item, null);

                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_tpi_news_num_item);
                holder.iv_newspic = (ImageView) convertView.findViewById(R.id.iv_tpi_news_pic_item);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_tpi_news_title_item);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_tpi_news_data_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //设置数据
            TpiNewsData.TPIData.listView_News tpiNewsData_Data_ListNewsData = listNews.get(position);
            //设置标题
            holder.tv_title.setText(tpiNewsData_Data_ListNewsData.title);

            //设置时间
            holder.tv_time.setText(tpiNewsData_Data_ListNewsData.pubdate);

            //设置图片
            x.image().bind(holder.iv_newspic,tpiNewsData_Data_ListNewsData.listimage);
           //bitmapUtils.display(holder.iv_newspic, tpiNewsData_Data_ListNewsData.listimage);

            return convertView;
        }

    }

    private class ViewHolder{
        ImageView iv_newspic;
        TextView tv_title;
        TextView tv_time;
        ImageView iv_icon;
    }

    private class LunBoAdapter extends PagerAdapter{

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv_lunbo_pic = new ImageView(mainActivity);
            iv_lunbo_pic.setScaleType(ScaleType.FIT_XY);

            //设备默认的图片,网络缓慢
            iv_lunbo_pic.setImageResource(R.mipmap.home_scroll_default);

            //给图片添加数据
            TpiNewsData.TPIData.lunbo_news tpiNewsData_Data_LunBoData = lunboDatas.get(position);

            //图片的url
            String topimageUrl = tpiNewsData_Data_LunBoData.topimage;

            //把url的图片给iv_lunbo_pic
            //异步加载图片，并且显示到组件中
            x.image().bind(iv_lunbo_pic,topimageUrl);

            //给图片添加触摸事件
            iv_lunbo_pic.setOnTouchListener(new OnTouchListener() {

                private float	downX;
                private float	downY;
                private long	downTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://按下停止轮播
                            System.out.println("按下");
                            downX = event.getX();
                            downY = event.getY();
                            downTime = System.currentTimeMillis();
                            lunboTask.stopLunbo();
                            break;
                        case MotionEvent.ACTION_CANCEL://事件取消
                            lunboTask.startLunbo();
                            break;
                        case MotionEvent.ACTION_UP://松开
                            float upX = event.getX();
                            float upY = event.getY();

                            if (upX == downX && upY == downY) {
                                long upTime = System.currentTimeMillis();
                                if (upTime - downTime < 500) {
                                    //点击
                                    lunboPicClick("被单击了。。。。。");
                                }
                            }
                            System.out.println("松开");
                            lunboTask.startLunbo();//开始轮播
                            break;
                        default:
                            break;
                    }
                    return true;
                }

                private void lunboPicClick(Object data) {
                    //处理图片的点击事件
                    System.out.println(data);

                }
            });
            container.addView(iv_lunbo_pic);

            return iv_lunbo_pic;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return lunboDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == object;
        }

    }

    public View getRoot(){
        return root;
    }
}
