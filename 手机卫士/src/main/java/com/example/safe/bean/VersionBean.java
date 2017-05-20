package com.example.safe.bean;

/**
 * 作者：   liyang
 * 创建时间：    上午9:54
 * 描述：版本号json数据封装
 */
public class VersionBean {

    /*
    * {“version”:”1”，
    * “url”:”http://10.0.2.2:8080/xxx.apk”,
    * ”desc”:”增加了某些功能”，}
    * */

    private int Version;
    private String url;
    private String desc;


    public int getVersion() {
        return Version;
    }

    public void setVersion(int version) {
        Version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
