package com.example.advancedcontrols.basePages;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.advancedcontrols.R;
import com.example.advancedcontrols.utils.L;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import static com.example.advancedcontrols.utils.Contants.settingItemTitle;

/**
 * @author 李阳
 * @创建时间 2017/4/22 - 上午10:41
 * @描述
 * @ 当前版本:
 */

public class SettingBaseTagPage extends baseTagPage {

    private ListView lv_setting;

    ArrayAdapter<String> adapter;


    public SettingBaseTagPage(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("Setting");
        View view=View.inflate(mainActivity, R.layout.fragment_main_setting, null);
        fl_main.addView(view);
        lv_setting= (ListView) view.findViewById(R.id.lv_setting);


        //装备数据
        adapter = new ArrayAdapter<String>(
                mainActivity,
                R.layout.list_item,
                settingItemTitle);
        lv_setting.getDivider();
        lv_setting.addHeaderView(new View(mainActivity));
        lv_setting.setDivider(null);//设置ListView分割线
        lv_setting.setAdapter(adapter);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        lv_setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("您点击了第 "+position+" 条目---> "+settingItemTitle[position]);

                int currentType = adapter.getItemViewType(position);
                //if(currentType==)

                switch (position){
                    case 2:
                        showShare(mainActivity,null,true);
                        break;
                }
            }
        });
    }

    public static void showShare(Context context, String platformToShare, boolean showContentEdit) {
        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }
        oks.setTheme(OnekeyShareTheme.CLASSIC);//界面风格设置为九宫格模式
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        //oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
        oks.setTitle("ShareSDK--Title");
        oks.setTitleUrl("http://mob.com");
        //oks.setText(context.getString(R.string.app_share_text));
        //oks.setImagePath("/sdcard/test-pic.jpg");  //分享sdcard目录下的图片
        oks.setImageUrl("http://f1.webshare.mob.com/dimgs/1c950a7b02087bf41bc56f07f7d3572c11dfcf36.jpg");
        oks.setUrl("http://www.mob.com"); //微信不绕过审核分享链接
        //oks.setFilePath(testVideo);  //filePath用于视频分享
        //oks.setComment(context.getString(R.string.app_share_comment)); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
        oks.setSite("ShareSDK");  //QZone分享完之后返回应用时提示框上显示的名称
        oks.setSiteUrl("http://mob.com");//QZone分享参数
        oks.setVenueName("ShareSDK");
        oks.setVenueDescription("This is a beautiful place!");
        oks.setLatitude(23.169f);
        oks.setLongitude(112.908f);
        // 将快捷分享的操作结果将通过OneKeyShareCallback回调
        // oks.setCallback(new OneKeyShareCallback());
        // 去自定义不同平台的字段内容
        // oks.setShareContentCustomizeCallback(new
        // ShareContentCustomizeDemo());
        // 启动分享
        oks.show(context);
    }
}
