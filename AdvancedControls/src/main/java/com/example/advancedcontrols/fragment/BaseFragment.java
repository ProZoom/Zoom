package com.example.advancedcontrols.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.advancedcontrols.activity.MainActivity;

/**
 * @author 李阳
 * @创建时间 2017/4/21 - 下午4:29
 * @描述
 * @ 当前版本:
 */

public abstract class BaseFragment extends Fragment {


    public MainActivity mainActivity;

    private View root;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取Fragment所在的Activity
        mainActivity=(MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //绑定布局
        root = inflater.inflate(bindView(),container,false);

        return root;
    }

    /**
     * @描述 绑定布局
     * @Param
     * @Return
     */
    public abstract int bindView();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    public void initView() {

    }

    /**
     * @描述 初始化事件
     * @Param
     * @Return
     */
    public void initEvent() {
    }

    /**
     * @描述 初始化数据
     * @Param
     * @Return
     */
    public void initData() {
    }

    public View getRootView(){
        return root;
    }
}
