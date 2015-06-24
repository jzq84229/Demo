package com.zhang.mydemo.actionbar;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Window;

public class TabNavigationActionBarActivity extends BaseActionBarActivity {
    private static String tag = "Tab Navigation ActionBarActivity";

    public TabNavigationActionBarActivity() {
        super(tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
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
