package com.zhang.mydemo.pdf;

import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;
import com.zhang.mydemo.util.ToastUtil;

import java.io.File;

public class PDFViewActivity extends BaseActivity implements OnPageChangeListener, OnDrawListener, OnLoadCompleteListener, OnErrorListener {

    private PDFView pdfView;
    private String url;
    private LinearLayout progressBar;
    private TextView mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_pdfview);
    }

    @Override
    public void findViews() {
        pdfView = (PDFView) findViewById(R.id.pdf_view);
        progressBar = (LinearLayout) findViewById(R.id.loading);
        mProgress = (TextView) findViewById(R.id.uploading_tv);
    }

    @Override
    public void setData() {
        url = "http://www.dcl-inc.com/pdf/papers-publications/042.pdf";
    }

    @Override
    public void showContent() {
        loadPDFFromAsset("042.pdf");
    }



    private void loadPDFFromAsset(String assetName){
        pdfView.fromAsset(assetName)
//                .pages(0, 2, 1, 3, 3, 3)    //all pages are displayed by default
                .enableSwipe(true)          //是否允许翻页，默认是允许翻
                .enableDoubletap(true)      //是否允许双击
                .swipeVertical(false)       //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .defaultPage(1)             //设置默认显示第1页
                .showMinimap(false)         //pdf放大的时候，是否在屏幕的右上角生成小地图
                .onDraw(this)               //绘图监听
                .onLoad(this)               //设置加载监听
                .onPageChange(this)         //设置翻页监听
                .onError(this)              //错误监听
                .load();
    }

    private void loadPDFFromFile(File file) {
        pdfView.fromFile(file)            //设置pdf文件地址
                .defaultPage(6)           //设置默认显示第1页
                .onPageChange(this)       //设置翻页监听
                .onLoad(this)             //设置加载监听
                .onDraw(this)             //绘图监听
                .showMinimap(false)       //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical( false )   //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true)        //是否允许翻页，默认是允许翻
                // .pages( 2 , 3 , 4 , 5  )  //把2 , 3 , 4 , 5 过滤掉
                .load();
    }

    private void loadPDFFromUri(Uri uri) {
        pdfView.fromUri(uri)
//                .pages(0, 2, 1, 3, 3, 3)    //all pages are displayed by default
                .enableSwipe(true)          //是否允许翻页，默认是允许翻
                .enableDoubletap(true)      //是否允许双击
                .swipeVertical(false)       //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .defaultPage(1)             //设置默认显示第1页
                .showMinimap(false)         //pdf放大的时候，是否在屏幕的右上角生成小地图
                .onDraw(this)               //绘图监听
                .onLoad(this)               //设置加载监听
                .onPageChange(this)         //设置翻页监听
                .onError(this)              //错误监听
                .load();
    }

    /**
     * 加载完成回调
     * @param nbPages  总共的页数
     */
    @Override
    public void loadComplete(int nbPages) {
        ToastUtil.showMessage("加载完成" + nbPages);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 翻页回调
     * @param page
     * @param pageCount
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
        ToastUtil.showMessage("page= " + page + " pageCount= " + pageCount);
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }

    @Override
    public void onError(Throwable t) {

    }

/*    private void downloadPDF() {
        new DownPDFAsyncTask().execute();
    }

    private class DownPDFAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            if (isCancelled()) {
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
//            mProgress.setText();

        }
    }*/


}
