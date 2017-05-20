package com.example.safe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.safe.R;
import com.example.safe.utils.Contents;
import com.example.safe.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;


public class SetupActivity extends Activity {


    private ViewPager viewPager;
    private List<View> viewList;

    private Button btn_bind_or_unbind,btn_select_safe_phone;
    private ImageView iv_lock;
    private View view1,view2,view3,view4;
    private EditText et_safe_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        initPagerView();
        initView();
        initEvent();

    }

    private void initEvent() {

        btn_bind_or_unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(
                        SharedPreferenceUtils.getString(getApplicationContext(),Contents.SIM,""))){
                    //没有绑定Sim卡
                    //绑定Sim卡，存储Sim卡信息
                    //获取Sim卡信息
                    TelephonyManager tm= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = tm.getSimSerialNumber();
                    //保存Sim信息
                    SharedPreferenceUtils.putString(getApplicationContext(), Contents.SIM,simSerialNumber);

                    //改变图标
                    iv_lock.setImageResource(R.mipmap.lock);
                }else {
                    //绑定Sim卡,取消绑定
                    SharedPreferenceUtils.putString(getApplicationContext(),Contents.SIM,"");
                    //改变图标
                    iv_lock.setImageResource(R.mipmap.unlock);
                }
            }
        });



        btn_select_safe_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String safeName=et_safe_phone.getText().toString().trim();


                if(TextUtils.isEmpty(safeName)){
                    //号码为空
                    Toast.makeText(getApplicationContext(),"安全号码为空",Toast.LENGTH_LONG).show();

                }else{
                    //号码不为空
                    SharedPreferenceUtils.putString(
                    getApplicationContext(),
                            Contents.SAFE_PHONE,
                            safeName);
                    viewPager.setCurrentItem(viewList.size()-1);
                }
            }
        });
    }

    private void initView() {
        btn_bind_or_unbind= (Button) view2.findViewById(R.id.btn_bind_or_unbind);
        iv_lock= (ImageView) view2.findViewById(R.id.iv_lock);

        btn_select_safe_phone= (Button) view3.findViewById(R.id.btn_select_safe_phone);
        et_safe_phone= (EditText) view3.findViewById(R.id.et_safe_phone);



        iv_lock.setImageResource(R.mipmap.unlock);


    }

    private void initPagerView() {

        viewPager= (ViewPager) findViewById(R.id.vp_setup);

        LayoutInflater inflater=getLayoutInflater();

        view1=inflater.inflate(R.layout.activity_setup_1,null);
        view2=inflater.inflate(R.layout.activity_setup_2,null);
        view3=inflater.inflate(R.layout.activity_setup_3,null);
        view4=inflater.inflate(R.layout.activity_setup_4,null);

        viewList=new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);

        PagerAdapter pagerAdapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };

        viewPager.setAdapter(pagerAdapter);
    }

}
