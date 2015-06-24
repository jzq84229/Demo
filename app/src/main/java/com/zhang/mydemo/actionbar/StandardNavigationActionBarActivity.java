package com.zhang.mydemo.actionbar;

import android.app.ActionBar;
import android.os.Bundle;

public class StandardNavigationActionBarActivity extends BaseActionBarActivity {

    private static String tag = "Standard Navigation ActionBarActivity";
    public StandardNavigationActionBarActivity() {
        super(tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workwithStandardActionBar();
    }

    public void workwithStandardActionBar(){
        ActionBar bar = this.getActionBar();
        bar.setTitle(tag);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        attacheTabs(bar);
    }

    public void attacheTabs(ActionBar bar) {
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
