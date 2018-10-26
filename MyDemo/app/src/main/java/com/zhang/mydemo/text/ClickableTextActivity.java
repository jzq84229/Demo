package com.zhang.mydemo.text;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
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

public class ClickableTextActivity extends BaseActivity {
    private TextView mTextView;

    @Override
    public void setContent() {
        setContentView(R.layout.activity_clickable_text);
    }

    @Override
    public void findViews() {
        mTextView = (TextView) findViewById(R.id.tv_content);
    }

    @Override
    public void setData() {
        /*********** 同一个TextView不同文字的点击事件*******/
        StringBuilder actionText = new StringBuilder();
        actionText
                .append("<a style=\"text-decoration:none;\" href='username'>"
                        + "username:" + " </a>");
        actionText
                .append("隐形人"
                        + "<a style=\"color:blue;text-decoration:none;\" href='singstar'> "
                        + " love" + "</a>");
        actionText.append(" : \"" + "孙燕姿" + "\"");
        mTextView.setText(Html.fromHtml(actionText.toString()));
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = mTextView.getText();
        int ends = text.length();
        Spannable spannable = (Spannable) mTextView.getText();
        URLSpan[] urlspan = spannable.getSpans(0, ends, URLSpan.class);
        SpannableStringBuilder stylesBuilder = new SpannableStringBuilder(text);
        stylesBuilder.clearSpans(); // should clear old spans
        for (URLSpan url : urlspan) {
            TextViewURLSpan myURLSpan = new TextViewURLSpan(url.getURL());
            stylesBuilder.setSpan(myURLSpan, spannable.getSpanStart(url),
                    spannable.getSpanEnd(url), spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mTextView.setText(stylesBuilder);
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








    private class TextViewURLSpan extends ClickableSpan {
        private String clickString;

        public TextViewURLSpan(String clickString) {
            this.clickString = clickString;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ClickableTextActivity.this.getResources().getColor(R.color.text_color));
            ds.setUnderlineText(false); //去掉下划线
        }

        @Override
        public void onClick(View widget) {
            if (clickString.equals("username")) {
                Toast.makeText(getApplication(), clickString, Toast.LENGTH_LONG).show();
            } else if (clickString.equals("singstar")) {
                Toast.makeText(getApplication(), clickString, Toast.LENGTH_LONG).show();
            }
        }
    }
}
