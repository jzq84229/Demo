package com.zhang.mydemo.text;

import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

public class LinkActivity extends BaseActivity {
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    @Override
    public void setContent() {
        setContentView(R.layout.activity_link);
    }

    @Override
    public void findViews() {
        tv1 = (TextView) findViewById(R.id.link_tv1);
        tv2 = (TextView) findViewById(R.id.link_tv2);
        tv3 = (TextView) findViewById(R.id.link_tv3);
        tv4 = (TextView) findViewById(R.id.link_tv4);
    }

    @Override
    public void setData() {

        tv2.setMovementMethod(LinkMovementMethod.getInstance());

        tv3.setText(
                Html.fromHtml(
                        "<b>text3: Constructed from HTML programmatically.</b>  Text with a " +
                                "<a href=\"http://www.google.com\">link</a> " +
                                "created in the Java source code using HTML."));
        tv3.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString ss = new SpannableString(
                "text4: Manually created spans. Click here to dial the phone.");

        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, 30,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new URLSpan("tel:4155551212"), 31 + 6, 31 + 10,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv4.setText(ss);
        tv4.setMovementMethod(LinkMovementMethod.getInstance());
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
