package com.zhang.mydemo.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.BaseApplication;
import com.zhang.mydemo.R;
import com.zhang.mydemo.common.album.AlbumHelper;
import com.zhang.mydemo.common.album.ImageBucket;
import com.zhang.mydemo.common.album.ImageItem;
import com.zhang.mydemo.common.viewholder.GridItemHolder;
import com.zhang.mydemo.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseActivity {
//    private ListView mListView;
    private GridView mGridView;
    private View bottom;

    private MyAdapter adapter;
    private BaseApplication app;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private List<ImageItem> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_test);
    }

    @Override
    public void findViews() {
        mGridView = (GridView) findViewById(R.id.gridview);
        bottom = findViewById(R.id.bottom_view);
    }

    @Override
    public void setData() {
        app = (BaseApplication) getApplication();
        mImageLoader = ImageLoader.getInstance();
        options = app.getImageOptions();

        adapter = new MyAdapter();
        mGridView.setAdapter(adapter);
    }

    @Override
    public void showContent() {
        initListData();
    }

    private void initListData(){
        AlbumHelper albumHelper = AlbumHelper.getHelper();
        albumHelper.init(getApplicationContext());
        List<ImageBucket> bucketList = albumHelper.getImagesBucketList(true, false);
        list.clear();
        if (!bucketList.isEmpty()) {
            for (ImageBucket bucket : bucketList) {
                list.addAll(bucket.getImageList());
            }
        }
        if (!list.isEmpty()) {
            adapter.setList(list);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (bottom.getVisibility() == View.VISIBLE) {
                bottom.setVisibility(View.GONE);
            } else {
                bottom.setVisibility(View.VISIBLE);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

















    private class MyAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private List<ImageItem> itemList = new ArrayList<>();

        public MyAdapter() {
            inflater = getLayoutInflater();
        }

        public void setList(List<ImageItem> itemList){
            this.itemList = itemList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public ImageItem getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageItem item = getItem(position);
            GridItemHolder holder = null;
            if (convertView == null) {
                holder = new GridItemHolder();
                convertView = inflater.inflate(R.layout.layout_square_grid_item, parent, false);
                holder.mImageView = (ImageView) convertView.findViewById(R.id.imageview);
                convertView.setTag(holder);
            } else {
                holder = (GridItemHolder) convertView.getTag();
            }

            mImageLoader.displayImage(Utils.getImageFileUrl(item.getImagePath()), holder.mImageView, options);
            return convertView;
        }
    }
}
