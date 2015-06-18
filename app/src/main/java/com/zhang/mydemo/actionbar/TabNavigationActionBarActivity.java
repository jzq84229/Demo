package com.zhang.mydemo.actionbar;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.zhang.mydemo.R;

public class TabNavigationActionBarActivity extends BaseActionBarActivity {
    @Override
    protected boolean onMenuItemSelected(MenuItem item) {
        return false;
    }

    private static String tag = "Tab Navigation ActionBarActivity";

    public TabNavigationActionBarActivity(String inTag) {
        super(inTag);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workwithTabbedActionBar();
    }

    public void workwithTabbedActionBar(){
        ActionBar bar = this.getActionBar();
        bar.setTitle(tag);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        TabListener tl = new TabListener(this, this);

        ActionBar.Tab tab1 = bar.newTab();
        tab1.setText("Tab1");
        tab1.setTabListener(tl);
        bar.addTab(tab1);

        ActionBar.Tab tab2 = bar.newTab();
        tab2.setText("Tab2");
        tab2.setTabListener(tl);
        bar.addTab(tab2);
    }

}
