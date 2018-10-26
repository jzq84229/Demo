package com.zhang.mydemo.text;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;
import com.zhang.mydemo.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ListViewClickableTextActivity extends BaseActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_list_view_clickable_text);
    }

    @Override
    public void findViews() {
        mListView = (ListView) findViewById(R.id.lv_list_view);
    }

    @Override
    public void setData() {
        MyAdapter adapter = new MyAdapter(initData());
        mListView.setAdapter(adapter);
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

    private List<String> initData(){
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            strList.add("用户名" + i);
        }
        return strList;
    }












    private class MyAdapter extends BaseAdapter{
        private List<String> list = new ArrayList<>();
        private LayoutInflater inflater;

        public MyAdapter(List<String> list) {
            this.list = list;
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.activity_list_view_clickable_text_item, parent, false);
                holder.mTextView = (TextViewFixTouchConsume) convertView.findViewById(R.id.tv_text_view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final String name = getItem(position);
//            String str = "<a>" + name + "</a>" + "：回复内容，回复内容";
//            SpannableStringBuilder ssb = new SpannableStringBuilder();
            SpannableString sp = new SpannableString(name + "：回复内容，回复内容");
            sp.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    //设置文字颜色
                    ds.setColor(getResources().getColor(R.color.comment_name_color));
                    //设置下划线.
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View widget) {
                    ToastUtil.showMessage("点击了" + name);
//                    ToastUtil.showToast(ListViewClickableTextActivity.this, "点击了" + name, Toast.LENGTH_SHORT);
                }
            }, 0, name.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

//            ssb.append(sp);
//            ssb.append("：");
//            ssb.append("回复内容，回复内容");
            holder.mTextView.setText(sp);
//            holder.mTextView.setTextViewHTML(str);
            holder.mTextView.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());

            return convertView;
        }
    }

    private static class ViewHolder{
        TextViewFixTouchConsume mTextView;
    }
}
