package com.example.safe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safe.R;
import com.example.safe.utils.Contents;
import com.example.safe.utils.L;
import com.example.safe.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.safe.utils.Md5Utils.md5;

/**
 * 作者：   liyang
 * 创建时间：    下午2:34
 * 描述：
 *       主界面开发
 */
public class MainActivity extends Activity {


    private GridView mGd;

    private ImageView iv_item;
    private TextView tv_item;
    private boolean passWordSetted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initridViewData();
        initEvent();

    }




    private void initView() {
        mGd= (GridView) findViewById(R.id.gd_Main);
        iv_item= (ImageView) findViewById(R.id.iv_main_item);
        tv_item= (TextView) findViewById(R.id.tv_main_item);
    }


    private void initridViewData() {

        //1.添加数据
        List<Map<String, Object>> mData=new ArrayList<>();
        for(int i=0;i< Contents.Img_MainMenu.length;i++){
            Map<String, Object> map=new HashMap();
            map.put("IMG",Contents.Img_MainMenu[i]);
            map.put("TEXT",Contents.Text_MainMenu[i]);
            mData.add(map);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(
                this,
                mData,
                R.layout.main_item,
                new String[]{"IMG", "TEXT"},
                new int[]{R.id.iv_main_item, R.id.tv_main_item}
        );
        mGd.setAdapter(simpleAdapter);

    }

    private void initEvent() {
        mGd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //手机防盗
                        L.i("0");
                        if(passWordSetted){
                            showEnterDialog();

                        }else{
                            showSettingPassDialog();
                        }
                        break;
                    case 1:
                        L.i("1");
                        break;
                    case 2:
                       //软件管理界面
                        startActivity(new Intent(MainActivity.this,AppManagerActivity.class));
                        L.i("2");
                        break;
                    case 3://进程管理
                        L.i("3");
                   startActivity(new Intent(MainActivity.this,TaskManagerActivity.class));
                        break;
                    case 4:
                        L.i("4");
                        break;
                    case 5:
                        L.i("5");
                        break;
                    case 6:
                        L.i("6");
                        break;
                    case 7:
                        L.i("7");
                        break;
                    case 8:
                        L.i("8");
                        break;
                }
            }
        });
    }

    private void showEnterDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final View view=View.inflate(this,R.layout.dialog_enter_password,null);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();

        final EditText et_enterpass= (EditText) view.findViewById(R.id.et_dialog_enter_password_one);
        Button bt_enter= (Button) view.findViewById(R.id.bt_enterPass);

        bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enterpass=et_enterpass.getText().toString().trim();
                enterpass=md5(enterpass);

                //判断密码是否正确
                if(enterpass.equals(SharedPreferenceUtils.getString(getApplicationContext(),Contents.PASSWORD,""))){
                    Toast.makeText(getApplicationContext(),"密码正确",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
               //进入手机防判断是否需要设置
                    startLostFindActivity();
                }else {
                    Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_LONG).show();
                    Vibrator vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(1000);
                    et_enterpass.setText("");

                    view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.vibration));
                }
            }
        });

    }

    private void startLostFindActivity() {
        if (SharedPreferenceUtils.getBoolean(this, Contents.ISSETTED, false)) {
            Toast.makeText(this, "进入设置向导", Toast.LENGTH_LONG).show();
            SharedPreferenceUtils.putBoolean(this, Contents.ISSETTED, true);
            startActivity(new Intent(MainActivity.this,LostFindActivity.class));
        } else {//设置过，进入界面
            Toast.makeText(this, "进入界面", Toast.LENGTH_LONG).show();
            SharedPreferenceUtils.putBoolean(this, Contents.ISSETTED, false);
            startActivity(new Intent(MainActivity.this,SetupActivity.class));
        }
    }

    private void showSettingPassDialog() {

        passWordSetted=true;

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);

        View view= View.inflate(this,R.layout.dialog_setting_password,null);
        builder.setView(view);

        final AlertDialog dialog=builder.create();
        dialog.show();




        Button bt_setPass= (Button) view.findViewById(R.id.bt_setPass);
        final EditText et_passone= (EditText) view.findViewById(R.id.et_dialog_setting_password_one);
        final EditText et_passtwo= (EditText) view.findViewById(R.id.et_dialog_setting_password_two);

        bt_setPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passone=et_passone.getText().toString().trim();

                String passtwo=et_passtwo.getText().toString().trim();

                //判断密码是否相同
                if(TextUtils.isEmpty(passone)||TextUtils.isEmpty(passtwo)){
                    Toast.makeText(getApplicationContext(),"密码不能为空", Toast.LENGTH_LONG).show();
                    L.i("密码不能为空，请重新输入");
                    return;
                }else if(!passone.equals(passtwo)){//密码不一致
                    Toast.makeText(getApplicationContext(),"密码错误，请重新输入", Toast.LENGTH_LONG).show();
                    L.i("密码不一致，请重新输入");
                    et_passone.setText("");
                    et_passtwo.setText("");
                    return;
                }else { //保存密码到share Prefernces
                    SharedPreferenceUtils.putString(getApplicationContext(),Contents.PASSWORD,md5(passone));
                    dialog.dismiss();
                }
            }
        });
    }
}
