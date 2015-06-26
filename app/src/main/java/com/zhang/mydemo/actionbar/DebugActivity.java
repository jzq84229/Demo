package com.zhang.mydemo.actionbar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.mydemo.R;

public abstract class DebugActivity extends AppCompatActivity implements IReportBack {

    protected abstract boolean onMenuItemSelected(MenuItem item);

    private static String tag = null;
    private int menuId = 0;
    private int layoutid = 0;
    private int debugTextViewId = 0;

    public DebugActivity(int inMenuId, int inLayoutid, int inDebugTextViewId, String inTag){
        tag = inTag;
        menuId = inMenuId;
        layoutid = inLayoutid;
        debugTextViewId = inDebugTextViewId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.layoutid);

        TextView tv = this.getTextView();
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_debug, menu);
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuId, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
        appendMenuItemText(item);
        if (item.getItemId() == R.id.menu_da_clear) {
            this.emptyText();
            return true;
        }
        boolean b = onMenuItemSelected(item);
        if (b == true){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    protected TextView getTextView(){
        return (TextView) this.findViewById(this.debugTextViewId);
    }

    protected void appendMenuItemText(MenuItem menuItem){
        String title = menuItem.getTitle().toString();
        appendText("MenuItem:" + title);
    }

    protected void emptyText(){
        TextView tv = getTextView();
        tv.setText("");
    }

    protected void appendText(String s){
        TextView tv = getTextView();
        tv.setText(s + "\n" + tv.getText());
        Log.d(tag, s);
    }

    public void reportBack(String tag, String message){
        this.appendText(tag + ":" + message);
        Log.d(tag, message);
    }

    public void reportTransient(String tag, String message) {
        String s = tag + ":" + message;
        Toast mToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        mToast.show();
        reportBack(tag, message);
        Log.d(tag, message);
    }




}
