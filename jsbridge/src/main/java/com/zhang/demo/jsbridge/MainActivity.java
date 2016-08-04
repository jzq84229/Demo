package com.zhang.demo.jsbridge;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import com.zhang.demo.jsbridge.lib.BridgeHandler;
import com.zhang.demo.jsbridge.lib.BridgeWebView;
import com.zhang.demo.jsbridge.lib.CallBackFunction;
import com.zhang.demo.jsbridge.lib.DefaultHandler;

public class MainActivity extends AppCompatActivity {
    private BridgeWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initWebView();
    }

    private void init() {
        webView = (BridgeWebView) findViewById(R.id.web_view);
    }

    private void initWebView() {
        webView.setDefaultHandler(new DefaultHandler());

        webView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            }
        });
        webView.setWebViewClient();

        webView.loadUrl("file:///android_asset/demo.html");
//        webView.loadUrl("http://192.168.1.40/zz2/jl.html");


        webView.registerHandler("getUserInfo", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
//                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }

        });
    }
}
