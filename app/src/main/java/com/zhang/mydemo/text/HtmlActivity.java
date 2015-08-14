package com.zhang.mydemo.text;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

public class HtmlActivity extends BaseActivity {
    private TextView mTextView;
    static Context ctx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_html);
    }

    @Override
    public void findViews() {
        mTextView = (TextView) findViewById(R.id.tv_text_view);
    }

    @Override
    public void setData() {
        ctx = this;

        String htmlLinkText = "<a href=\"/\" mce_href=\"http://www.baidu.com/\"><u>我的CSDN博客 </u></a>";
        mTextView.setText(Html.fromHtml(htmlLinkText));
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = mTextView.getText();
        if(text instanceof Spannable){
            int end = text.length();
            Spannable sp = (Spannable)mTextView.getText();
            URLSpan[] urls=sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style=new SpannableStringBuilder(text);
            style.clearSpans();//should clear old spans
            for(URLSpan url : urls){
                MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
                style.setSpan(myURLSpan,sp.getSpanStart(url),sp.getSpanEnd(url),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            mTextView.setText(style);
        }
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

    private static class MyURLSpan extends ClickableSpan {

        private String mUrl;
        MyURLSpan(String url) {
            mUrl =url;
        }
        @Override
        public void onClick(View widget) {
            // TODO Auto-generated method stub
            Toast.makeText(ctx, "hello!", Toast.LENGTH_LONG).show();
        }
    }
}
