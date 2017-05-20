package com.example.advancedcontrols.activity.FallingView;

import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.advancedcontrols.R;
import com.example.advancedcontrols.activity.BaseActivity;
import com.example.advancedcontrols.ui.fallingview.FallingView;
import com.example.advancedcontrols.utils.L;

/**
 * @author 李阳
 * @创建时间 2017/5/3 - 下午8:51
 * @描述
 * @ 当前版本:
 */

public class FlakeFallingView extends BaseActivity implements SeekBar.OnSeekBarChangeListener {


    private ImageView iv_bg;
    private SeekBar sb_size;
    private SeekBar sb_speed;
    private SeekBar sb_density;

    private FallingView mFallingView;


    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_flake_fallingview);

        iv_bg= (ImageView) findViewById(R.id.iv_bg);
        sb_size= (SeekBar) findViewById(R.id.sb_size);
        sb_speed= (SeekBar) findViewById(R.id.sb_speed);
        sb_density= (SeekBar) findViewById(R.id.sb_density);

        mFallingView= (FallingView) findViewById(R.id.fv_text);

    }

    @Override
    public void initData() {
        super.initData();
        iv_bg.setImageResource(R.mipmap.bg);
    }

    @Override
    public void initListenerEvent() {
        super.initListenerEvent();

        sb_size.setOnSeekBarChangeListener(this);
        sb_speed.setOnSeekBarChangeListener(this);
        sb_density.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.sb_size:
                L.i("Scale--->"+progress);
                mFallingView.setScale(progress);//设置碎片的大小，数值越大，碎片越小，默认值是3
                break;
            case R.id.sb_speed:
                L.i("speed--->"+progress);

                mFallingView.setDensity(progress);//设置密度，数值越大，碎片越密集

                break;
            case R.id.sb_density:
                L.i("density--->"+progress);

                mFallingView.setDelay(progress);//设置碎片飘落的速度，数值越大，飘落的越慢，默认值是10
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
