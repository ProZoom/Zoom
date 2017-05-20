package com.zoom.wise.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李阳
 * @创建时间   2017/3/20 - 上午10:02
 * @描述  新闻中心的数据封装
 * @ 当前版本:
 */

public class NewsCenterData {

    public int retcode;

    public List<NewsData> data=new ArrayList<>();

    public class NewsData{
        public List<ViewTagData> children=new ArrayList<>();

        /**
         * @描述  新闻中的页签数据
         * @Param
         * @Return
         */
        public class ViewTagData{
            public String id;
            public String title;
            public int type;
            public String url;
        }

        public String id;

        public String title;
        public int type;

        public String url;
        public String url1;

        public String dayurl;
        public String excurl;

        public String weekurl;
    }

    public List<String> extend=new ArrayList<>();

}
