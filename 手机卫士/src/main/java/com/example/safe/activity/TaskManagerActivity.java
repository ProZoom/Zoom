package com.example.safe.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.safe.R;
import com.example.safe.bean.TaskBean;
import com.example.safe.engine.TastManagerEngine;
import com.example.safe.utils.L;
import com.example.safe.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：   李阳
 * 创建时间：  2017/2/6
 * 参数：
 * 描述： 进程管理Activity
 *
 */
public class TaskManagerActivity extends Activity implements View.OnClickListener {

    private TextView tv_task_num,tv_task_mem,
            tv_task_lebel;
    private ProgressBar pb_task_manager;
    private ListView lv_task_datas;

    private Button btn_Clear,btn_select_all,btn_select_op,btn_setting;

    private static final int LOADING=0;
    private static final int FINISH = 1;


    private List<TaskBean> sysTasks=new ArrayList<TaskBean>();
    private List<TaskBean> userTasks=new ArrayList<TaskBean>();

    private long availMem=0;
    private long totalMem=0;

    private myAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();//初始化界面
        initData();//设置数据
        initEvent();//初始化事件
    }

    private void initEvent() {

        lv_task_datas.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                L.i("onScroll");

                if(firstVisibleItem==0){
                    tv_task_lebel.setVisibility(View.GONE);
                }else if(firstVisibleItem>0&&firstVisibleItem<userTasks.size()+1){
                    tv_task_lebel.setVisibility(View.VISIBLE);
                    tv_task_lebel.setText("用户进程("+userTasks.size()+")");
                }else if(firstVisibleItem>userTasks.size()){
                    tv_task_lebel.setText("系统进程("+sysTasks.size()+")");
                }
            }
        });

        lv_task_datas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("position: "+position);
            }
        });
    }


    private Handler hander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOADING://加载数据进程显示
                    pb_task_manager.setVisibility(View.VISIBLE);
                    lv_task_datas.setVisibility(View.GONE);
                    tv_task_lebel.setVisibility(View.GONE);
                    break;
                case FINISH://数据加载完成后显示
                    pb_task_manager.setVisibility(View.GONE);
                    lv_task_datas.setVisibility(View.VISIBLE);

                    tv_task_num.setText("运行中的进程："+(sysTasks.size()+userTasks.size()));

                    tv_task_mem.setText("可用/总内存："+
                            Formatter.formatFileSize(getApplicationContext(),availMem)+
                            "/"+Formatter.formatFileSize(getApplicationContext(),totalMem));



                    //数据通知
                    adapter.notifyDataSetChanged();
                    break;
                default:break;
            }
        }
    };

    private void initData() {
        //子线程获取数据
        new Thread(){
            @Override
            public void run() {
                //发送加载数据进度的消息
                hander.obtainMessage(LOADING).sendToTarget();
                //加载数据
                List<TaskBean> allTaskDatas= TastManagerEngine.getAllRunningTaskInfos(getApplicationContext());
                availMem=TastManagerEngine.getAvailMemSize(getApplicationContext());
                totalMem=TastManagerEngine.getToatleMemSize(getApplicationContext());

                SystemClock.sleep(500);

                sysTasks.clear();
                userTasks.clear();
                //人发数据
                for(TaskBean taskbean:allTaskDatas){
                    if(taskbean.isSystem()){
                        sysTasks.add(taskbean);
                    }else {
                        userTasks.add(taskbean);
                    }
                }
                L.i("sysTask " +sysTasks+'\n'+"userTask " +userTasks);
                //数据加载完成
                hander.obtainMessage(FINISH).sendToTarget();
            }
        }.start();


    }

    private void initView() {
        setContentView(R.layout.activity_taskmanager);

        tv_task_num= (TextView) findViewById(R.id.tv_task_num);
        tv_task_mem= (TextView) findViewById(R.id.tv_task_mem);
        tv_task_lebel= (TextView) findViewById(R.id.tv_task_lebel);
        pb_task_manager= (ProgressBar) findViewById(R.id.pb_task_manager);
        lv_task_datas= (ListView) findViewById(R.id.lv_task_datas);

        btn_Clear= (Button) findViewById(R.id.bt_clear);
        btn_select_all= (Button) findViewById(R.id.bt_select_all);
        btn_select_op= (Button) findViewById(R.id.bt_select_op);
        btn_setting= (Button) findViewById(R.id.bt_setting);

        btn_Clear.setOnClickListener(this);
        btn_select_all.setOnClickListener(this);
        btn_select_op.setOnClickListener(this);
        btn_setting.setOnClickListener(this);


        adapter=new myAdapter();
        lv_task_datas.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_clear:
                T.show(this,"清理",0);

                break;
            case R.id.bt_select_all:
                T.show(this,"全选",0);

                break;
            case R.id.bt_select_op:
                T.show(this,"反选",0);


                break;
            case R.id.bt_setting:
                T.show(this,"设置",0);

                break;
            default:break;
        }
    }


    /**
     * 描述：数据适配器
     */
    private class myAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return userTasks.size()+1+sysTasks.size()+1;
        }

        @Override
        public Object getItem(int position) {
            TaskBean bean = null;

            if(position==0 || position==userTasks.size()+1){
                return bean;
            }

            if(position<=userTasks.size()-1){
                bean=userTasks.get(position-1);
            }else {
                bean=sysTasks.get(position-1-1-userTasks.size());
            }
            return bean;
        }

        @Override
        public long getItemId(int position) {
            return userTasks.size()+1+sysTasks.size()+1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            L.i("Task_position: "+position);
            View view=null;

            if(position==0){//用户进程标签
                TextView textView=new TextView(getApplicationContext());
                textView.setText("用户进程("+userTasks.size()+")");
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                return textView;
            }else if(position==userTasks.size()+1){//系统进程标签
                TextView textView=new TextView(getApplicationContext());
                textView.setText("系统进程("+sysTasks.size()+")");
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                return textView;
            }else {

                if (convertView != null&&convertView instanceof RelativeLayout) {
                    view = convertView;
                } else {
                    view = View.inflate(getApplicationContext(), R.layout.item_taskmanager_listview, null);
                }

                ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_taskmanager_item_icon);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_taskmanager_name_icon);
                TextView tv_local = (TextView) view.findViewById(R.id.tv_taskmanager_local_icon);
                //TextView tv_size= (TextView) view.findViewById(R.id.tv_appsize);
                //获取数据
                TaskBean bean=null;

                if(position<userTasks.size()+1){
                    bean=userTasks.get(position-1);
                }else{
                    bean=sysTasks.get(position-1-1-userTasks.size());
                }
                //设置数据
                iv_icon.setImageDrawable(bean.getIcon());
                tv_name.setText(bean.getName());
                tv_local.setText(Formatter.formatFileSize(getApplicationContext(),bean.getMemSize()));
            }
            return view;
        }
    }
}
