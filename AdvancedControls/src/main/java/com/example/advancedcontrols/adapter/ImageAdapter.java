package com.example.advancedcontrols.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.advancedcontrols.R;
import com.example.advancedcontrols.utils.L;
import com.example.advancedcontrols.utils.image3chche.ImageCache;

/**
 * @author 李阳
 * @创建时间 2017/5/11 - 下午1:41
 * @描述
 * @ 当前版本:
 */

public class ImageAdapter extends ArrayAdapter<String> {

    private ImageCache manager;

    //记录每个子项的高度
    private int mItemHeight = 0;

    public ImageAdapter(Context context, int textViewResourceId, String[] objects, GridView photoWall) {
        super(context, textViewResourceId, objects);

        manager = ImageCache.getInstanse(context);
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String url = getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.photo_layout, null);
        } else {
            view = convertView;
        }

        final ImageView imageView = (ImageView) view.findViewById(R.id.photo);

        if (imageView.getLayoutParams().height != mItemHeight) {
            imageView.getLayoutParams().height = mItemHeight;
        }

        // 给ImageView设置一个Tag，保证异步加载图片时不会乱序
        imageView.setTag(url);
        imageView.setImageResource(R.mipmap.empty_photo);
        L.i(">>>>>>>>>>",">>>>>>"+imageView.getTag());
        manager.loadImages(imageView, url, false);
        return view;
    }


    //设置item子项的高度
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        notifyDataSetChanged();
    }
}
