package com.zoom.music.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zoom.music.R;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FoldersActivity extends AppCompatActivity {

    private static final String CURRENT_PATH = "CURRENT_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        TextView tv = (TextView) findViewById(R.id.tv_folder_path);
        ListView lv = (ListView) findViewById(R.id.lv);

        // 当前文件夹路径是什么？
        String currentPath = null;
        try {
            currentPath = getIntent().getExtras().getString(CURRENT_PATH);
        }catch (Exception e){}
        if (currentPath == null || "".equals(currentPath)) {
            // 没有-》显示最跟目录
            currentPath = getRootDirectory();
        }
        // 有-》显示他内容
        /**
         * 0- 把当前目录设置到顶部textview里
         * 1- 获取当前文件夹的文件列表
         * 2- 通过适配把文件显示到列表里
         * 3- 监听点击事件
         */
        tv.setText(currentPath);
        final File file = new File(currentPath);
        if (file.isDirectory()) /*是否文件*/ {
            // 1-
            File[] list = file.listFiles();
            final List<File> files = Arrays.asList(list);
            // 2-
            MyFilesAdapter adap = new MyFilesAdapter(this, files);
            lv.setAdapter(adap);
            // 3-
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 通过当前点击的文件的目录、开个activity
                    if (MyFilesAdapter.isMusic(files.get(position))) {
                        /*向service 播放音乐。。。*/
                    } else {
                        Intent in = new Intent(FoldersActivity.this, FoldersActivity.class);
                        in.putExtra(CURRENT_PATH, files.get(position).getAbsolutePath());
                        startActivity(in);
                    }
                }
            });
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.home_enter, R.anim.splash_exit);
    }

    public String getRootDirectory() {

        String rootDirectory = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            rootDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            rootDirectory = Environment.getRootDirectory().getAbsolutePath();
        }
        return rootDirectory;
    }


    static class MyFilesAdapter extends BaseAdapter {

        private final List<File> data;
        private final Context ctx;

        public MyFilesAdapter (Context ctx, List<File> f) {
            this.ctx = ctx;
            this.data = f;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            File item = (File) getItem(position);

            ViewHolder vh = null;
            if (convertView == null) {
                convertView = (LayoutInflater.from(ctx)).inflate(R.layout.folder_files_list_item, parent, false);
                vh = new ViewHolder(convertView);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            // 设置文件类型图
            if (isMusic(item)) {
                vh.iv_file_type.setImageResource(R.drawable.file_music);
            } else if (isFolder(item)) {
                vh.iv_file_type.setImageResource(R.drawable.file_folder);
            } else {
                vh.iv_file_type.setImageResource(R.drawable.file_other);
            }
            // 设置文件名
            vh.tv_name.setText(item.getName());
            convertView.setTag(vh);
            return convertView;
        }

        public static boolean isMusic(File item) {

            // 通过后名.mp3, .aac // 判断是否音乐文件
            String filename = item.getName();
            String extension = filename.substring(filename.lastIndexOf(".") + 1);
            String[] types = new String[]{"mp3", "aac", "wav", "ogg"};
        /*    for (int i = 0; i < types.length; i++) {
                String type = types[0];
            }*/
            for (String type : types) {
                if(type.equals(extension.trim()))
                    return true;
            }
            return false;
        }
        private boolean isFolder(File item) {
            return item.isDirectory();
        }

        public class ViewHolder {

            public TextView tv_name;
            public ImageView iv_file_type;

            public  ViewHolder(View view){
                iv_file_type = (ImageView) view.findViewById(R.id.iv_file_type);
                tv_name = (TextView) view.findViewById(R.id.tv_file_name);
            }
        }
    }

    public interface FilePlayMusicInterface {
        public void playFile(File file);
    }

}
