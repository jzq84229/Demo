package com.zhang.mydemo.actionbartest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

import java.lang.reflect.Field;

public class MyActionBarActivity extends BaseActivity {
    MenuItem menuItem = null;
    /** An array of strings to populate dropdown list */
    String[] actions = new String[] { "Bookmark", "Subscribe", "Share" };

    ShareActionProvider provider = null;

    @Override
    public void setContent() {
        setContentView(R.layout.activity_my_action_bar);
    }

    @Override
    public void findViews() {

    }

    @Override
    public void setData() {

    }

    @Override
    public void showContent() {
        // 通过hilde()和show()方法可以控制actionbar的隐藏和显示
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
//        mActionBar.setDisplayShowHomeEnabled(true);
//        mActionBar.setDefaultDisplayHomeAsUpEnabled(true);
//        mActionBar.hide();
//        mActionBar.show();
//        setOverflowShowingAlways();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, actions);

        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ActionBar.OnNavigationListener naviagtionListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                showToast("You selected: " + actions[itemPosition]);
                return false;
            }
        };

        mActionBar.setListNavigationCallbacks(adapter, naviagtionListener);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_action_bar, menu);
//        MenuItem menuItem = menu.findItem(R.id.menu_share);
//        MenuItemCompat.setActionProvider(menuItem, new ShareActionProvider(this));
//        MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

        // Get the ActionProvider
//        provider = (ShareActionProvider)menu.findItem(R.id.menu_share).getActionProvider();
        provider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.menu_share));
        // Initialize the share intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Text I want to share");
        provider.setShareIntent(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                showToast("home");
                break;
            case R.id.menu_settings:
                showToast("setting");
                break;
            case R.id.action_refresh:
                showToast("refresh");
                menuItem = item;
                menuItem.setActionView(R.layout.progress);
                TestTask task = new TestTask();
                task.execute("test");
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
                startContextual();
                break;
            case R.id.action_tab:
                showToast("tab fragment");
                startTabNavigationActivity();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    private class TestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            menuItem.collapseActionView();
            menuItem.setActionView(null);
        }
    }

    private void startContextual(){
        Intent i = new Intent(this, ContextualActionBarActivity.class);
        startActivity(i);
    }

    private void startTabNavigationActivity(){
        Intent i = new Intent(this, TabNavigationActivity.class);
        startActivity(i);
    }
}
