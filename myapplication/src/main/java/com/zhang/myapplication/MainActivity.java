package com.zhang.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "StockQuoteClient";
    private IStockQuoteService stockService = null;
    private Button bindBtn;
    private Button callBtn;
    private Button unbindBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

//        bindBtn = (Button) findViewById(R.id.bindBtn);
//        bindBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                bindService(new Intent(IStockQuoteService.class.getName()),
////                        serConn, Context.BIND_AUTO_CREATE);
//                Intent intent = new Intent();
//                intent.setAction(IStockQuoteService.class.getName());
//                intent.setPackage(IStockQuoteService.class.getPackage().getName());
//                bindService(intent, serConn, Context.BIND_AUTO_CREATE);
//
//                bindBtn.setEnabled(false);
//                callBtn.setEnabled(true);
//                unbindBtn.setEnabled(true);
//            }
//        });
//
//        callBtn = (Button) findViewById(R.id.callBtn);
//        callBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callService();
//            }
//        });
//        callBtn.setEnabled(false);
//
//        unbindBtn = (Button) findViewById(R.id.unbindBtn);
//        unbindBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                unbindService(serConn);
//                bindBtn.setEnabled(true);
//                callBtn.setEnabled(false);
//                unbindBtn.setEnabled(false);
//            }
//        });
//        unbindBtn.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


//
//    private void callService() {
//        try {
//            double val = stockService.getQuote("ANDROID");
//            Toast.makeText(this, "Value from service is " + val, Toast.LENGTH_SHORT).show();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private ServiceConnection serConn = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            Log.v(TAG, "onServiceConnected() called");
//            stockService = IStockQuoteService.Stub.asInterface(service);
//            callService();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.v(TAG, "onServiceDisconnected() called");
//            stockService = null;
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        Log.v(TAG, "onDestroy() called");
//        if (callBtn.isEnabled()) {
//            unbindService(serConn);
//        }
//        super.onDestroy();
//    }
}
