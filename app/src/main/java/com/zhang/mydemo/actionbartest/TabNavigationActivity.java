package com.zhang.mydemo.actionbartest;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zhang.mydemo.R;

public class TabNavigationActivity extends AppCompatActivity implements AFragment.OnFragmentInteractionListener {
    private static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_navigation);

        appContext = getApplicationContext();
        ActionBar actionBar = getSupportActionBar();
        // 设置action bar 的 navigation mode
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 添加 action bar 的tabs
        ActionBar.Tab playerTab = actionBar.newTab().setText("tab A");
        ActionBar.Tab stationsTab = actionBar.newTab().setText("tab B");

        // 实例化 fragment action bar 是用 fragment 来显示的
        Fragment PlayerFragment = new AFragment();
        Fragment StationsFragment = new BFragment();

        // 对 tabs 设置监听事件
        playerTab.setTabListener(new MyTabsListener(PlayerFragment));
        stationsTab.setTabListener(new MyTabsListener(StationsFragment));

        // 最后把 tabs 加入监听事件
        actionBar.addTab(playerTab);
        actionBar.addTab(stationsTab);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_settings:
                showToast("setting");
                break;
            case R.id.action_refresh:
                showToast("refresh");
                break;
            case R.id.action_about:
                showToast("about");
                break;
            case R.id.action_edit:
                showToast("edit");
                break;
            case R.id.action_email:
                showToast("email");
                break;
            case R.id.action_contextual:
                showToast("contextual");
                break;
            case R.id.action_tab:
                showToast("tab fragment");
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Toast toast;
    private void showToast(String str) {
        if (toast == null) {
            toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        } else {
            toast.setText(str);
        }
        toast.show();

    }

    private class MyTabsListener implements ActionBar.TabListener {
        public Fragment fragment;

        public MyTabsListener(Fragment fragment) {
            this.fragment = fragment;
        }

        // 重复两次以上点击 tab
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            showToast("Reselected!");
        }

        // 就点击一次
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {// ft 用来控制 fragment
            ft.replace(R.id.fragment_container, fragment);
        }

        // 不点击
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
