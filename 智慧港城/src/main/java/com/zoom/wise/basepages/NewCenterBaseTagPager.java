package com.zoom.wise.basepages;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zoom.wise.bean.NewsCenterData;
import com.zoom.wise.newscenterpage.BaseNewsCenterPage;
import com.zoom.wise.newscenterpage.InteractBaseNewsCenterPage;
import com.zoom.wise.newscenterpage.NewsBaseNewsCenterPage;
import com.zoom.wise.newscenterpage.PhotosBaseNewsCenterPage;
import com.zoom.wise.newscenterpage.TopicBaseNewsCenterPage;
import com.zoom.wise.utils.L;
import com.zoom.wise.utils.SpTools;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.zoom.wise.utils.Contants.NEWSCENTERURL;

/**
 * @author 李阳
 * @创建时间 2017-02-28  下午1:00
 * @描述 TODO
 * @ 当前版本:
 */
public class NewCenterBaseTagPager extends BaseTagPage {

    //新闻中心要显示的四个界面
    List<BaseNewsCenterPage> newsCenterPages=new ArrayList<>();

    private NewsCenterData	newsCenterData;

    private Gson gson;//谷歌提供的json解析器
    private String jsonCache;

    public NewCenterBaseTagPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        //1.获取本地缓存
        jsonCache = SpTools.getString(mainActivity, NEWSCENTERURL,"");
        L.i("NewCenterBaseTagPager-->jsonCache-->"+jsonCache);

        //2.获取网络数据
        x.http().get(new RequestParams(NEWSCENTERURL), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //访问数据成功
                String jsonData=result;
                L.i("NewCenterBaseTagPager-->从网络读取的数据jsonData-->"+jsonData);
                //保存到本地一份
                SpTools.setString(mainActivity,NEWSCENTERURL,jsonData);
                //解析数据
                parseJson(jsonData);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                L.i("网络请求失败--->ex-->"+ex+"\nisOnCallback-->"+isOnCallback);
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


    /**
     * @描述  解析json数据
     * @Param []
     * @Return void
     */
    private void parseJson(String jsonData) {
        //第一次打开app需要初始化
        if(gson==null) {
            gson = new Gson();
        }
        newsCenterData=gson.fromJson(jsonData, NewsCenterData.class);
        //L.i(newsCenterData.data.get(0).children.get(0).title);
        //给左侧菜单设置数据
        mainActivity.getLeftMenuFragment().setLeftMenuData(newsCenterData.data);
        //读取的数据封装到界面容器里，通过左侧菜单点击，显示不同界面
        //1.根据服务器的数据创建四个页面
        for(NewsCenterData.NewsData newsData:newsCenterData.data){
            BaseNewsCenterPage newsPage = null;
            switch (newsData.type){
                case 1://新闻
                    newsPage=new NewsBaseNewsCenterPage(mainActivity,newsCenterData.data.get(0).children);
                    break;
                case 10://专题
                    newsPage=new TopicBaseNewsCenterPage(mainActivity);
                    break;
                case 2://组图
                    newsPage=new PhotosBaseNewsCenterPage(mainActivity);
                    break;
                case 3://互动
                    newsPage=new InteractBaseNewsCenterPage(mainActivity);
                    break;
            }
            //添加新闻中心的页面到容器
            newsCenterPages.add(newsPage);
        }
        switchPage(0);
    }
    public void switchPage(int position){

        BaseNewsCenterPage baseNewsCenterPage=newsCenterPages.get(position);
        //要显示的标题
        tv_title.setText(newsCenterData.data.get(position).title);

        //要展示的内容
        fl_content.removeAllViews();
        //初始化数据
        baseNewsCenterPage.initData();
        //替换掉白纸
        fl_content.addView(baseNewsCenterPage.getRoot());//添加自己的内容到白纸上

    }
}
