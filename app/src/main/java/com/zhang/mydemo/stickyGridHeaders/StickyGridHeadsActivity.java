package com.zhang.mydemo.stickyGridHeaders;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;

/**
 * 测试StickyGridHeads的使用
 * 参考实现http://blog.csdn.net/xiaanming/article/details/20481185
 */
public class StickyGridHeadsActivity extends BaseActivity {
    private ProgressDialog mProgressDialog;
    /**
     * 图片扫描器
     */
    private ImageScanner mScanner;
    private StickyGridHeadersGridView mGridView;

    /**
     * 没有HeadId的List
     */
    private List<GridItem> mGridList = new ArrayList<>();
    private static int section = 1;
    private Map<String, Integer> sectionMap = new HashMap<>();

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_grid_heads);

        mGridView = (StickyGridHeadersGridView) findViewById(R.id.asset_grid);
        mGridView.setAreHeadersSticky(false);
        mGridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));
        mScanner = new ImageScanner(this);

        mScanner.scanImages(new ImageScanner.ScanCompleteCallBack() {
            {
                mProgressDialog = ProgressDialog.show(StickyGridHeadsActivity.this, null, "正在加载……");
            }

            @Override
            public void scanComplete(Cursor cursor) {
                //关闭进度条
                mProgressDialog.dismiss();

                if (cursor == null) {
                    return;
                }

                while (cursor.moveToNext()) {
                    //获取图片的路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    //获取图片的添加到系统的毫秒数
                    long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                    GridItem mGridItem = new GridItem(path, parseTimeToYMD(time, "yyyy年MM月dd日"));
                    mGridList.add(mGridItem);
                }
                cursor.close();
                Collections.sort(mGridList, new YMDComparator());

                for (ListIterator<GridItem> it = mGridList.listIterator(); it.hasNext(); ) {
                    GridItem mGridItem = it.next();
                    String ym = mGridItem.getTime();
                    if (!sectionMap.containsKey(ym)) {
                        mGridItem.setSection(section);
                        sectionMap.put(ym, section);
                        section++;
                    } else {
                        mGridItem.setSection(sectionMap.get(ym));
                    }
                }
                mGridView.setAdapter(new StickyGridAdatper(StickyGridHeadsActivity.this, mGridList, mGridView));

//                //给GridView的item的数据生成HeaderId
//                List<GridItem> hasHeadIdList = generateHeaderId(mGridList);
//                //排序
//                Collections.sort(hasHeadIdList, new YMDComparator());
//                mGridView.setAdapter(new StickyGridAdatper(StickyGridHeadsActivity.this, hasHeadIdList, mGridView));
            }
        });
    }*/

    @Override
    public void setContent() {
        setContentView(R.layout.activity_sticky_grid_heads);
    }

    @Override
    public void findViews() {
        mGridView = (StickyGridHeadersGridView) findViewById(R.id.asset_grid);
        mGridView.setAreHeadersSticky(false);
        mGridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);
//        mActionBar.setDefaultDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setData() {
        mScanner = new ImageScanner(this);

        mScanner.scanImages(new ImageScanner.ScanCompleteCallBack() {
            {
                mProgressDialog = ProgressDialog.show(StickyGridHeadsActivity.this, null, "正在加载……");
            }

            @Override
            public void scanComplete(Cursor cursor) {
                imageScanComplete(cursor);
            }
        });
    }

    @Override
    public void showContent() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        switch (item.getItemId()) {
            case R.id.home:
                showToast("home");
                break;
            case R.id.menu_settings:
                showToast("setting");
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void imageScanComplete(Cursor cursor){
        //关闭进度条
        mProgressDialog.dismiss();

        if (cursor == null) {
            return;
        }

        while (cursor.moveToNext()) {
            //获取图片的路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //获取图片的添加到系统的毫秒数
            long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
            GridItem mGridItem = new GridItem(path, parseTimeToYMD(time, "yyyy年MM月dd日"));
            mGridList.add(mGridItem);
        }
        cursor.close();
        Collections.sort(mGridList, new YMDComparator());

        for (ListIterator<GridItem> it = mGridList.listIterator(); it.hasNext(); ) {
            GridItem mGridItem = it.next();
            String ym = mGridItem.getTime();
            if (!sectionMap.containsKey(ym)) {
                mGridItem.setSection(section);
                sectionMap.put(ym, section);
                section++;
            } else {
                mGridItem.setSection(sectionMap.get(ym));
            }
        }
        mGridView.setAdapter(new StickyGridAdatper(StickyGridHeadsActivity.this, mGridList, mGridView));

//                //给GridView的item的数据生成HeaderId
//                List<GridItem> hasHeadIdList = generateHeaderId(mGridList);
//                //排序
//                Collections.sort(hasHeadIdList, new YMDComparator());
//                mGridView.setAdapter(new StickyGridAdatper(StickyGridHeadsActivity.this, hasHeadIdList, mGridView));

    }

    /**
     * 对GridView的Item生成HeaderId, 根据图片的添加时间的年、月、日来生成HeaderId
     * 年、月、日相等HeaderId就相同
     * @param mGridList
     * @return
     */
//    private List<GridItem> generateHeaderId(List<GridItem> mGridList){
//        Map<String, Integer> mHeaderIdMap = new HashMap<String, Integer>();
//        int mHeaderId = 1;
//        List<GridItem> hasHeaderIdList;
//        for (ListIterator<GridItem> it = mGridList.listIterator(); it.hasNext();) {
//            GridItem mGridItem = it.next();
//            String ymd = mGridItem.getTime();
//            if (!mHeaderIdMap.containsKey(ymd)) {
//                mGridItem.setHeaderId(mHeaderId);
//                mHeaderIdMap.put(ymd, mHeaderId);
//                mHeaderId++;
//            } else {
//                mGridItem.setHeaderId(mHeaderIdMap.get(ymd));
//            }
//        }
//        hasHeaderIdList = mGridList;
//
//        return hasHeaderIdList;
//    }

    /**
     * 将毫秒数装换成pattern这个格式，我这里是转换成年月日
     * @param time
     * @param pattern
     * @return
     */
    public static String parseTimeToYMD(long time, String pattern) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time * 1000));
    }



}
