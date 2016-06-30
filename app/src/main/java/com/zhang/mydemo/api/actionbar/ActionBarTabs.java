package com.zhang.mydemo.api.actionbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;
import com.zhang.mydemo.util.ToastUtil;

public class ActionBarTabs extends BaseActivity implements View.OnClickListener {
    private Button btnAddTab;
    private Button btnRemoveLastTab;
    private Button btnToggleTabs;
    private Button btnRemoveAllTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_action_bar_tabs);
    }

    @Override
    public void findViews() {
        btnAddTab = (Button) findViewById(R.id.btn_add_tab);
        btnRemoveLastTab = (Button) findViewById(R.id.btn_remove_tab);
        btnToggleTabs = (Button) findViewById(R.id.btn_toggle_tabs);
        btnRemoveAllTabs = (Button) findViewById(R.id.btn_remove_all_tabs);
    }

    @Override
    public void setData() {
        btnAddTab.setOnClickListener(this);
        btnRemoveLastTab.setOnClickListener(this);
        btnToggleTabs.setOnClickListener(this);
        btnRemoveAllTabs.setOnClickListener(this);
    }

    @Override
    public void showContent() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar_tabs, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_tab:
                onAddTab();
                break;
            case R.id.btn_remove_tab:
                onRemoveLastTab();
                break;
            case R.id.btn_toggle_tabs:
                onToggleTabs();
                break;
            case R.id.btn_remove_all_tabs:
                onRemoveAllTabs();
                break;
        }
    }

    private void onAddTab(){
        final ActionBar bar = getSupportActionBar();
        final int tabCount = bar.getTabCount();
        final String text = "Tab " + tabCount;
        bar.addTab(bar.newTab()
                .setText(text)
                .setTabListener(new TabListener(new TabContentFragment(text))));
    }

    private void onRemoveLastTab(){
        final ActionBar bar = getSupportActionBar();
        if (bar.getTabCount() > 0) {
            bar.removeTabAt(bar.getTabCount() - 1);
        }
    }

    private void onToggleTabs() {
        final ActionBar bar = getSupportActionBar();
        if (bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
        } else {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        }
    }

    private void onRemoveAllTabs() {
        getSupportActionBar().removeAllTabs();
    }



















    private class TabListener implements ActionBar.TabListener{
        private TabContentFragment mFragment;

        public TabListener(TabContentFragment mFragment) {
            this.mFragment = mFragment;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.add(R.id.fragment_content, mFragment, mFragment.getText());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.remove(mFragment);
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//            ToastUtil.showToast(ActionBarTabs.this, "Reselected!", Toast.LENGTH_SHORT);
            ToastUtil.showMessage("Reselected!");
        }
    }

    private class TabContentFragment extends Fragment{
        private String mText;

        public TabContentFragment(String mText) {
            this.mText = mText;
        }

        public String getText(){
            return mText;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View fragView = inflater.inflate(R.layout.action_bar_tab_content, container, false);
            TextView text = (TextView) fragView.findViewById(R.id.text);
            text.setText(mText);
            return fragView;
        }
    }
}
