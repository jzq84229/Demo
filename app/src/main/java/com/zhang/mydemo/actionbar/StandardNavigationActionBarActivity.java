package com.zhang.mydemo.actionbar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.zhang.mydemo.R;

public class StandardNavigationActionBarActivity extends BaseActionBarActivity {

    public StandardNavigationActionBarActivity(String inTag) {
        super(inTag);
    }

    @Override
    protected boolean onMenuItemSelected(MenuItem item) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_navigation_action_bar);
    }



}
