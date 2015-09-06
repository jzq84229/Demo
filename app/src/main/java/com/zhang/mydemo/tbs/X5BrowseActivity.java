package com.zhang.mydemo.tbs;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.tencent.smtt.sdk.WebView;
import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

public class X5BrowseActivity extends BaseActivity {
    private static final int MSG_OPEN_TEST_URL = 0;
    private static final int MSG_INIT_UI = 1;
    private final int mUrlStartNum = 1;
    private int mCurrentUrl = mUrlStartNum;
    private MyWebView myWebView;
    private WebView webView;
    private ViewGroup mViewParent;
    private ImageButton mBack;
    private ImageButton mForward;
    private ImageButton mRefresh;
    private ImageButton mExit;
    private ImageButton mHome;
    private ImageButton mTestProcesses;
    private ImageButton mTextWebviews;
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
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_x5_browse);
    }

    @Override
    public void findViews() {
        webView = (WebView) findViewById(R.id.webview);
    }

    @Override
    public void setData() {

    }

    @Override
    public void showContent() {

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
