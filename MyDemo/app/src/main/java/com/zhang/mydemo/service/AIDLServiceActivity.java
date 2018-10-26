package com.zhang.mydemo.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhang.myapplication.IStockQuoteService;
import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

public class AIDLServiceActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = AIDLServiceActivity.class.getSimpleName();
    private Button bindBtn;
    private Button callBtn;
    private Button unbindBtn;

    private IStockQuoteService stockService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_aidlservice);
    }

    @Override
    public void findViews() {
        bindBtn = (Button) findViewById(R.id.bindBtn);
        callBtn = (Button) findViewById(R.id.callBtn);
        unbindBtn = (Button) findViewById(R.id.unbindBtn);

        bindBtn.setOnClickListener(this);
        callBtn.setOnClickListener(this);
        unbindBtn.setOnClickListener(this);
    }

    @Override
    public void setData() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bindBtn:
//                bindService(new Intent(IStockQuoteService.class.getName()),
////                        serConn, Context.BIND_AUTO_CREATE);
                Intent intent = new Intent();
                intent.setAction(IStockQuoteService.class.getName());
                intent.setPackage(IStockQuoteService.class.getPackage().getName());
                bindService(intent, serConn, Context.BIND_AUTO_CREATE);

                bindBtn.setEnabled(false);
                callBtn.setEnabled(true);
                unbindBtn.setEnabled(true);
                break;
            case R.id.callBtn:
                callService();
                break;
            case R.id.unbindBtn:
                unbindService(serConn);
                bindBtn.setEnabled(true);
                callBtn.setEnabled(false);
                unbindBtn.setEnabled(false);
                break;
        }
    }

    private void callService() {
        try {
            double val = stockService.getQuote("ANDROID");
            Toast.makeText(this, "Value from service is " + val, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection serConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, "onServiceConnected() called");
            stockService = IStockQuoteService.Stub.asInterface(service);
            callService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(TAG, "onServiceDisconnected() called");
            stockService = null;
        }
    };
}
