package com.zoom.wise.bean;

import java.util.List;

/**
 * @author 李阳
 * @创建时间 2017/3/23 - 下午10:28
 * @描述
 * @ 当前版本:
 */

public class TpiNewsData {


    public int retcode;
    public TPIData data;

    public class TPIData{
        public String countcommenturl;
        public String more;
        public String title;

        public List<listView_News> news;
        public class listView_News{
            public boolean comment;
            public String commentlist;
            public String commenturl;
            public String id;
            public String listimage;
            public String pubdate;
            public String title;
            public String type;
            public String url;
        }


        public List<TPI_topic> topic;
        private class TPI_topic {
            public String description;
            public String id;
            public String listimage;
            public String sort;
            public String title;
            public String url;
        }


        public List<lunbo_news> topnews;
        public class lunbo_news{
            public boolean comment;
            public String commentlist;
            public String commenturl;
            public String id;
            public String pubdate;
            public String title;
            public String topimage;
            public String type;
            public String url;
        }
    }
}
