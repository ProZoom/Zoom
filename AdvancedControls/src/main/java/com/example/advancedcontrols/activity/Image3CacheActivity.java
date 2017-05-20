package com.example.advancedcontrols.activity;

import android.view.ViewTreeObserver;
import android.widget.GridView;

import com.example.advancedcontrols.R;
import com.example.advancedcontrols.adapter.ImageAdapter;
import com.example.advancedcontrols.utils.ImageResource;
import com.example.advancedcontrols.utils.L;
import com.example.advancedcontrols.utils.image3chche.ImageCache;

/**
 * @author 李阳
 * @创建时间 2017/5/11 - 上午8:24
 * @描述
        什么是图片的三级缓存
        1、内存缓存 优先加载，速度最快
        2、本地缓存 次优先加载 速度稍快
        3、网络缓存 最后加载 速度由网络速度决定（浪费流量）
 * @ 当前版本:
 */

public class Image3CacheActivity extends BaseActivity {


    private GridView gv_photos;

    //GridView的适配器
    private ImageAdapter mAdapter;

    private int mImageThumbSize;

    private int mImageThumbSpacing;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_image3cache);

        gv_photos= (GridView) findViewById(R.id.gv_photos);

    }


    @Override
    public void initData() {
        super.initData();

        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);

        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        mAdapter = new ImageAdapter(this, 0, ImageResource.imageThumbUrls, gv_photos);

        gv_photos.setAdapter(mAdapter);
    }


    @Override
    public void initListenerEvent() {
        super.initListenerEvent();
        //
        gv_photos.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final int numColumns = (int) Math.floor(gv_photos.getWidth() / (mImageThumbSize + mImageThumbSpacing));
                L.i("numColumns------>"+numColumns);
                if (numColumns > 0) {
                    int columnWidth = (gv_photos.getWidth() / numColumns) - mImageThumbSpacing;

                    mAdapter.setItemHeight(columnWidth);

                    gv_photos.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageCache.getInstanse(this).onDestroy();
    }
}
