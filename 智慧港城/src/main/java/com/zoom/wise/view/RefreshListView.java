package com.zoom.wise.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoom.wise.R;
import com.zoom.wise.utils.DensityUtils;
import com.zoom.wise.utils.L;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * @author 李阳
 * @创建时间 2017/3/31 - 上午7:15
 * @描述
 * @ 当前版本:
 */

public class RefreshListView extends ListView {

    private int currentState=0;
    private static final int PULL_DOWN = 1;
    private static final int RELEASE_STATE = 2;
    private static final int REFRESHING = 3;
    private static final int REFRESHED=4;

    private LinearLayout headView;//

    private LinearLayout ll_listview_refresh_head;

    private RelativeLayout rl_listview_refresh_bottom;

    private TextView tv_listview_refresh_head;

    private ImageView iv_arrow;

    private ProgressBar pb_listview_refresh_head;

    private RotateAnimation up_ra;
    private RotateAnimation down_ra;

    float down_Y = -1;
    float up_Y;
    float move_Y;

    private int headHeight;
    private View lunboView;
    private int listViewOnScreenY;
    private int lunboViewOnScreenY;


    public RefreshListView(Context context) {
        super(context);
        initView();
        initAnimation();
    }




    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAnimation();

    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initAnimation();

    }


    private void initView() {
        initHead();
        initBottom();
    }

    private void initHead() {
        headView = (LinearLayout) View.inflate(getContext(), R.layout.listview_refresh_container, null);

        ll_listview_refresh_head = (LinearLayout) headView.findViewById(R.id.rl_listview_refresh_head);

        tv_listview_refresh_head = (TextView) headView.findViewById(R.id.tv_listview_refresh_head);
        iv_arrow = (ImageView) headView.findViewById(R.id.iv_listview_refresh_head);
        pb_listview_refresh_head = (ProgressBar) headView.findViewById(R.id.pb_listview_refresh_head);

        int[] local = new int[2];
        ll_listview_refresh_head.getLocationOnScreen(local);
        L.i("headView--->outLocation--->" + local[1]);

        //测量控件高度
        ll_listview_refresh_head.measure(0, 0);
        headHeight = ll_listview_refresh_head.getMeasuredHeight();//像素值px
        L.i("headheught--->" + headHeight + "\n" + "headheight--->" + DensityUtils.px2dip(getContext(), headHeight));
        //隐藏刷新头
        headView.setPadding(0, -headHeight, 0, 0);
        //加载到listview中
        addHeaderView(headView);
    }

    private void initBottom() {
        View bottomView = View.inflate(getContext(), R.layout.listview_refresh_bottom, null);

        rl_listview_refresh_bottom = (RelativeLayout) headView.findViewById(R.id.rl_listview_refresh_bottom);

        bottomView.setPadding(0, 0, 0, -DensityUtils.dip2px(getContext(), 60));
        //加载到listview中
        addFooterView(bottomView);
    }

    private void initAnimation() {
        up_ra = new RotateAnimation(0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        up_ra.setDuration(500);
        up_ra.setFillAfter(true);//停留在动画结束的状态

        down_ra = new RotateAnimation(-180, -360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        down_ra.setDuration(500);
        down_ra.setFillAfter(true);//停留在动画结束的状态
    }


    public void addLunBoView(View view) {
        lunboView = view;
        headView.addView(lunboView);
    }


    public boolean isLunboFullShow() {
        int[] outLocation = new int[2];//x,y轴坐标
        //
        if (listViewOnScreenY == 0) {
            this.getLocationOnScreen(outLocation);
            listViewOnScreenY = outLocation[1];
            L.i("RefreshListView--->outLocation--->" + listViewOnScreenY);
        }
        lunboView.getLocationOnScreen(outLocation);
        lunboViewOnScreenY = outLocation[1];
        L.i("lunboView--->outLocation--->" + lunboViewOnScreenY);
        if (lunboViewOnScreenY < listViewOnScreenY) {//没有显示完全
            L.i("lunboView--->没有显示完全");
            return false;
        } else {
            L.i("lunboView--->显示完全");
            return true;
        }
    }


    /**
     * @描述 重写此方法，实现刷新功能
     * @Param
     * @Return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        float scrollYDis=0;

        switch (ev.getAction()) {
            case ACTION_DOWN:
                down_Y = ev.getY();//按下时y坐标
                break;
            case ACTION_MOVE:
                if (!isLunboFullShow()) {//轮播图没有完全显示
                    break;
                }
                if (down_Y == -1) {//按下时没有获取坐标
                    down_Y = ev.getY();
                }
                move_Y = ev.getY();
                float d_Y = move_Y - down_Y;
                L.i("d_Y----->" + d_Y);
                if (d_Y > 0 && getFirstVisiblePosition() == 0) {
                    L.i("向下滑动");
                    scrollYDis = -headHeight + d_Y;
                    L.i("a----->" + scrollYDis);
                    headView.setPadding(0, (int) scrollYDis, 0, 0);
                    if (scrollYDis <0 && currentState!=PULL_DOWN) {//刷新范围
                        currentState=PULL_DOWN;
                        refreshState();
                    } else if(scrollYDis>=0&&currentState!=RELEASE_STATE){
                        currentState=RELEASE_STATE;
                        refreshState();
                    }
                    return true;
                }
                break;
            case ACTION_UP:
                down_Y = -1;
                up_Y = ev.getY();
                if(currentState==PULL_DOWN){
                    currentState = PULL_DOWN; //改变状态为正在刷新数据的状态
                    refreshState(); //刷新界面
                    headView.setPadding(0, -headHeight, 0, 0);
                }else if(currentState==RELEASE_STATE){
                    headView.setPadding(0, 0, 0, 0);
                    //刷新数据
                    currentState = REFRESHING; //改变状态为正在刷新数据的状态
                    refreshState(); //刷新界面

                }else if(currentState==REFRESHING){
                    currentState = REFRESHED; //改变状态为正在刷新数据的状态
                    refreshState(); //刷新界面
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshState() {
        switch (currentState) {
            case PULL_DOWN:// 下拉刷新
                //改变文件
                pb_listview_refresh_head.setVisibility(View.GONE);
                tv_listview_refresh_head.setText("下拉刷新");
                iv_arrow.startAnimation(down_ra);
                break;
            case RELEASE_STATE:// 松开刷新
                tv_listview_refresh_head.setText("释放立即刷新");
                iv_arrow.startAnimation(up_ra);
                break;
            case REFRESHING://正在刷新状态
                iv_arrow.clearAnimation();//清除所有动画
                iv_arrow.setVisibility(View.GONE);//隐藏箭头
                pb_listview_refresh_head.setVisibility(View.VISIBLE);//显示进度条
                tv_listview_refresh_head.setText("正在刷新数据");
                //刷新动作

                //SystemClock.sleep(3000);
            case REFRESHED://刷新成功
                iv_arrow.clearAnimation();//清除所有动画
                iv_arrow.setVisibility(View.GONE);//隐藏箭头
                //pb_loading.setVisibility(View.VISIBLE);//显示进度条
                tv_listview_refresh_head.setText("刷新成功");
                headView.setPadding(0, -headHeight, 0, 0);

            default:
                break;
        }
    }
}
