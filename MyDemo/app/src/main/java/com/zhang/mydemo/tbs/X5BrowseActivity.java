package com.zhang.mydemo.tbs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewCallbackClient;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;
import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

public class X5BrowseActivity extends BaseActivity {
    private static final int MSG_OPEN_TEST_URL = 0;
    private static final int MSG_INIT_UI = 1;
    private final int mUrlStartNum = 0;
    private int mCurrentUrl = mUrlStartNum;
    private final int mUrlEndNum = 108;
    private MyWebView mWebView;
    private ViewGroup mViewParent;
    private ImageButton mBack;
    private ImageButton mForward;
    private ImageButton mRefresh;
    private ImageButton mExit;
    private ImageButton mHome;
    private ImageButton mTestProcesses;
    private ImageButton mTestWebviews;
    private ImageButton mMore;
    private ImageButton mClearData;
    private ImageButton mOpenFile;
    private Button mGo;
    private EditText mUrl;
    private RelativeLayout mMenu;
    private final int disable = 120;
    private final int enable = 255;

    private static final String mHomeUrl = "http://app.html5.qq.com/navi/index";
    private static final int MAX_LENGTH = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //在条件满足时开启硬件加速
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_x5_browse);

        mViewParent = (ViewGroup) findViewById(R.id.webView1);

        initBtnListeners();

        QbSdk.preInit(this);

        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
    }

    @Override
    public void setContent() {
    }

    @Override
    public void findViews() {
    }

    @Override
    public void setData() {
    }

    @Override
    public void showContent() {
    }

    private void initBtnListeners() {
        mBack = (ImageButton) findViewById(R.id.btnBack1);
        mForward = (ImageButton) findViewById(R.id.btnForward1);
        mRefresh = (ImageButton) findViewById(R.id.btnRefresh1);
        mExit = (ImageButton) findViewById(R.id.btnExit1);
        mHome = (ImageButton) findViewById(R.id.btnHome1);
        mTestProcesses = (ImageButton) findViewById(R.id.btnTestProcesses1);
        mTestWebviews = (ImageButton) findViewById(R.id.btnTestWebviews1);
        mGo = (Button) findViewById(R.id.btnGo1);
        mUrl = (EditText) findViewById(R.id.editUrl1);
        mMore = (ImageButton) findViewById(R.id.btnMore);
        mMenu = (RelativeLayout) findViewById(R.id.menuMore);
        mClearData = (ImageButton) findViewById(R.id.btnClearData);
        mOpenFile = (ImageButton) findViewById(R.id.btnOpenFile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBack.setImageAlpha(disable);
            mForward.setImageAlpha(disable);
            mHome.setImageAlpha(disable);
        }
        mHome.setEnabled(false);
        final TextView logView = (TextView) findViewById(R.id.logView1);

        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
                if (mWebView != null && mWebView.canGoBack())
                    mWebView.goBack();
            }
        });

        mForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
                if (mWebView != null && mWebView.canGoForward())
                    mWebView.goForward();
            }
        });

        mRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
                if (mWebView != null)
                    mWebView.reload();
            }
        });

        mExit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        mGo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
                String url = mUrl.getText().toString();
                if (url != null && !"".equals(url)) {
                    url = UrlUtils.resolvValidUrl(url);
                    if (url != null) {
                        mWebView.loadUrl(url);
                    }
                }
                mWebView.requestFocus();
            }
        });

        mMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mMenu.getVisibility() == View.GONE) {
                    mMenu.setVisibility(View.VISIBLE);
                    mMore.setImageDrawable(getResources().getDrawable(R.mipmap.theme_toolbar_btn_menu_fg_pressed));
                } else {
                    mMenu.setVisibility(View.GONE);
                    mMore.setImageDrawable(getResources().getDrawable(R.mipmap.theme_toolbar_btn_menu_fg_normal));
                }
            }
        });

        mClearData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
                QbSdk.clearAllWebViewCache(getApplicationContext());
                QbSdk.reset(getApplicationContext());
            }
        });

        mUrl.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                moreMenuClose();
                if (hasFocus) {
                    mGo.setVisibility(View.VISIBLE);
                    mRefresh.setVisibility(View.GONE);
                    if (null == mWebView.getUrl()) return;
                    if (mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
                        mUrl.setText("");
                        mGo.setText("取消");
                        mGo.setTextColor(0X6F0F0F0F);
                    } else {
                        mUrl.setText(mWebView.getUrl());
                        mGo.setText("进入");
                        mGo.setTextColor(0X6F0000CD);
                    }
                } else {
                    mGo.setVisibility(View.GONE);
                    mRefresh.setVisibility(View.VISIBLE);
                    String title = mWebView.getTitle();
                    if (title != null && title.length() > MAX_LENGTH)
                        mUrl.setText(title.subSequence(0, MAX_LENGTH) + "...");
                    else
                        mUrl.setText(title);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

        });

        mUrl.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                String url = null;
                if (mUrl.getText() != null) {
                    url = mUrl.getText().toString();
                }

                if (url == null
                        || mUrl.getText().toString().equalsIgnoreCase("")
                        || UrlUtils.resolvValidUrl(url) == null) {
                    mGo.setText("取消");
                    mGo.setTextColor(0X6F0F0F0F);
                } else {
                    mGo.setText("进入");
                    mGo.setTextColor(0X6F0000CD);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

        });

        mHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moreMenuClose();
                if (mWebView != null)
                    mWebView.loadUrl(mHomeUrl);
            }
        });
    }


    private boolean mNeedTestPage = false;
    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_TEST_URL:
                    if (!mNeedTestPage) {//未开启页面测试
                        return;
                    }

                    if (mCurrentUrl > mUrlEndNum) { //超过页面的最大测试数目
                        setLandScapeAndFullScreen(false);//跑完之后退出横屏和全屏
                        return;
                    }

                    //构造新的url
                    String testUrl = "file:///sdcard/outputHtml/html/"
                            + Integer.toString(mCurrentUrl) + ".html";

                    //访问新的url
                    if (mWebView != null) {
                        mWebView.loadUrl(testUrl);
                    }

                    mCurrentUrl++;
                    break;
                case MSG_INIT_UI:
                    initWebView();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private CallbackClient mCallbackClient = new CallbackClient();

    class CallbackClient implements WebViewCallbackClient {

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent, View view) {
            Log.e("grass", "MainActivity - CallbackClient -- onTouchEvent:" + motionEvent);
            return mWebView.tbs_onTouchEvent(motionEvent, view);
        }

        @Override
        public boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                    int scrollY, int scrollRangeX, int scrollRangeY,
                                    int maxOverScrollX, int maxOverScrollY,
                                    boolean isTouchEvent, View view) {
            Log.e("grass", "MainActivity - CallbackClient -- overScrollBy");
            return mWebView.tbs_overScrollBy(deltaX, deltaY, scrollX, scrollY,
                    scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
                    isTouchEvent, view);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent motionEvent, View view) {
            Log.e("grass", "MainActivity - CallbackClient -- dispatchTouchEvent");
            return mWebView.tbs_dispatchTouchEvent(motionEvent, view);
        }

        @Override
        public void computeScroll(View view) {
            Log.e("grass", "MainActivity - CallbackClient -- computeScroll");
            mWebView.tbs_computeScroll(view);
        }

        @Override
        public void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
                                   boolean clampedY, View view) {
            Log.e("grass", "MainActivity - CallbackClient -- onOverScrolled");
            mWebView.tbs_onOverScrolled(scrollX, scrollY, clampedX, clampedY, view);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent motionEvent, View view) {
            Log.e("grass", "MainActivity - CallbackClient -- onInterceptTouchEvent");
            return mWebView.tbs_onInterceptTouchEvent(motionEvent, view);
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt, View view) {
            Log.e("grass", "MainActivity - CallbackClient -- onScrollChanged");
            mWebView.tbs_onScrollChanged(l, t, oldl, oldt, view);
        }
    }

    private void initWebView() {
        // 创建WebView
        mWebView = new MyWebView(this);

        // set Callback client
        mWebView.setWebViewCallbackClient(mCallbackClient);
        mWebView.evaluateJavascript("1+1", new ValueCallback() {
            @Override
            public void onReceiveValue(Object s) {
                Log.e("0625", "value is " + s);
            }
        });

        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        // 设置Client
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
                // TODO Auto-generated method stub

                Log.e("should", "request.getUrl().toString() is " + request.getUrl().toString());

                return super.shouldInterceptRequest(view, request);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                moreMenuClose();
                // mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(view);
                /* mWebView.showLog("test Log"); */

            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {

                if (mUrl == null)
                    return;
                if (!mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
                    if (title != null && title.length() > MAX_LENGTH)
                        mUrl.setText(title.subSequence(0, MAX_LENGTH) + "...");
                    else
                        mUrl.setText(title);
                } else {
                    mUrl.setText("");
                }
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> uploadFile,
                                        String acceptType, String captureType) {
                X5BrowseActivity.this.uploadFile = uploadFile;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                X5BrowseActivity.this.startActivityForResult(
                        Intent.createChooser(
                                i,
                                getResources().getString(
                                        R.string.choose_uploadfile)), 0);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub


            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {


                new AlertDialog.Builder(X5BrowseActivity.this)
                        .setTitle("是否下载")
                        .setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Log.e("0609", "setDownloadListener 02");
                                        Toast.makeText(
                                                X5BrowseActivity.this,
                                                "fake message: i'll download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("no",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                X5BrowseActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                X5BrowseActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
            }
        });
        // 各种设置
        if (mWebView.getX5WebViewExtension() != null) {
            // Log.e("robins", "CoreVersion_FromSDK::" +
            // mWebView.getX5WebViewExtension().getQQBrowserVersion());
            mWebView.getX5WebViewExtension().setWebViewClientExtension(
                    new ProxyWebViewClientExtension() {
                        @Override
                        public Object onMiscCallBack(String method,
                                                     Bundle bundle) {
                            if (method == "onSecurityLevelGot") {
                                Toast.makeText(
                                        X5BrowseActivity.this,
                                        "Security Level Check: \nit's level is "
                                                + bundle.getInt("level"), Toast.LENGTH_SHORT)
                                        .show();
                            }
                            return null;
                        }

                        @Override
                        public boolean onTouchEvent(MotionEvent event, View view) {
                            Log.e("grass", "ProxyWebViewClientExtension - onTouchEvent");
                            return mCallbackClient.onTouchEvent(event, view);
                        }

                        // 1
                        public boolean onInterceptTouchEvent(MotionEvent ev, View view) {
                            return mCallbackClient.onInterceptTouchEvent(ev, view);
                        }

                        // 3
                        public boolean dispatchTouchEvent(MotionEvent ev, View view) {
                            return mCallbackClient.dispatchTouchEvent(ev, view);
                        }

                        // 4
                        public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                                    int scrollRangeX, int scrollRangeY,
                                                    int maxOverScrollX, int maxOverScrollY,
                                                    boolean isTouchEvent, View view) {
                            return mCallbackClient.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                                    scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent, view);
                        }

                        // 5
                        public void onScrollChanged(int l, int t, int oldl, int oldt, View view) {
                            mCallbackClient.onScrollChanged(l, t, oldl, oldt, view);
                        }

                        // 6
                        public void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
                                                   boolean clampedY, View view) {
                            mCallbackClient.onOverScrolled(scrollX, scrollY, clampedX, clampedY, view);
                        }

                        // 7
                        public void computeScroll(View view) {
                            mCallbackClient.computeScroll(view);
                        }
                    });
            mWebView.getX5WebViewExtension().invokeMiscMethod("someExtensionMethod", new Bundle());
        } else {
            TbsLog.e("robins", "CoreVersion");
        }
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();

        mWebView.loadUrl(mHomeUrl);

        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    private ValueCallback<Uri> uploadFile;

    public void openFileChooser(ValueCallback<Uri> uploadFile,
                                String acceptType, String captureType) {
        X5BrowseActivity.this.uploadFile = uploadFile;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        X5BrowseActivity.this.startActivityForResult(
                Intent.createChooser(
                        i,
                        getResources().getString(
                                R.string.choose_uploadfile)), 0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changGoForwardButton(WebView view) {
        if (view.canGoBack())
            mBack.setImageAlpha(enable);
        else
            mBack.setImageAlpha(disable);
        if (view.canGoForward())
            mForward.setImageAlpha(enable);
        else
            mForward.setImageAlpha(disable);
        if (view.getUrl() != null && view.getUrl().equalsIgnoreCase(mHomeUrl)) {
            mHome.setImageAlpha(disable);
            mHome.setEnabled(false);
        } else {
            mHome.setImageAlpha(enable);
            mHome.setEnabled(true);
        }
    }

    private void setLandScapeAndFullScreen(boolean flag) {

    }


    private void moreMenuClose() {
        if (mMenu != null && mMenu.getVisibility() == View.VISIBLE) {
            mMenu.setVisibility(View.GONE);
//            mMore.setImageDrawable(getResources().getDrawable(R.mipmap.theme_toolbar_btn_menu_fg_normal));
            mMore.setImageResource(R.mipmap.theme_toolbar_btn_menu_fg_normal);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_x5_browse, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
