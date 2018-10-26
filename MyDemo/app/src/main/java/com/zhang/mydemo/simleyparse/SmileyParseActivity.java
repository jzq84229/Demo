package com.zhang.mydemo.simleyparse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

public class SmileyParseActivity extends BaseActivity {
    private TextView tv;
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_smiley_parse);
    }

    @Override
    public void findViews() {
        tv = (TextView) findViewById(R.id.tv_text_view);
        tv2 = (TextView) findViewById(R.id.tv_text_view2);
    }

    @Override
    public void setData() {
        String str = "高兴[高兴]" + "悲伤[悲伤]" + "眨眼[眨眼]" + "吐舌[吐舌]";
        tv.setText("原文：" + str);

        SmileyParser.init(this);
        SmileyParser parser = SmileyParser.getInstance();
        tv2.setText(parser.addSmileySpans("SmileyParse类处理后：" + str));
    }

    @Override
    public void showContent() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default, menu);
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
}
