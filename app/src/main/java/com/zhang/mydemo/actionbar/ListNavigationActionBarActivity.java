package com.zhang.mydemo.actionbar;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

public class ListNavigationActionBarActivity extends BaseActionBarActivity {

    private static String tag = "List Navigation ActionBarActivity";
    public ListNavigationActionBarActivity() {
        super(tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workwithListActionBar();
    }

    public void workwithListActionBar(){
        ActionBar bar = this.getSupportActionBar();
        bar.setTitle(tag);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        bar.setListNavigationCallbacks(new SimpleSpinnerArrayAdapter(this),
                new ListListener(this,this));
    }

}
