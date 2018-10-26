package com.zhang.mydemo.preference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

public class MainPreferenceActivity extends BaseActivity {
    private TextView tv = null;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_main_preference);
    }

    @Override
    public void findViews() {
        resources = this.getResources();
        tv = (TextView) findViewById(R.id.text1);
    }

    @Override
    public void setData() {
        setOptionText();
    }

    @Override
    public void showContent() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_preference, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_prefs){
            Intent intent = new Intent().setClass(this, FlightPreferenceActivity.class);
            this.startActivityForResult(intent, 0);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setOptionText();
    }

    private void setOptionText(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String option = prefs.getString(resources.getString(R.string.selected_flight_sort_option),
                                        resources.getString(R.string.flight_sort_option_default_value));
        String[] optionText = resources.getStringArray(R.array.flight_sort_options);
        tv.setText("option value is " + option + "(" + optionText[Integer.parseInt(option)] + ")");
    }

}
