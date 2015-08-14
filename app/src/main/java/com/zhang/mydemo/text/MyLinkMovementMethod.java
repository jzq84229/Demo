package com.zhang.mydemo.text;

import android.graphics.Color;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhang.mydemo.R;

/**
 * Created by zjun on 2015/8/14 0014.
 */
public class MyLinkMovementMethod extends LinkMovementMethod {

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {int action = event.getAction();

        if (action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off,
                    ClickableSpan.class);

            if (link.length != 0) {
                // 如果点击了span区域,改变textview的背景.
                // R.drawable.comment_item_selector 这个资源是点击前后 color都是white
//                widget.setBackgroundResource(R.drawable.comment_item_selector);
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
//                    buffer.setSpan(new BackgroundColorSpan(Color.parseColor("#000000")),
//                			buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                return true;
            } else {
                // 如果点击了非span区域,改变textview的背景.
                // R.drawable.layout_item_selector 这个资源是点击前color
                // 是white,点击后color是grey
//                widget.setBackgroundResource(R.drawable.layout_item_selector);
                Selection.removeSelection(buffer);
                //获取textview的父layout,手动调用layout的onTouchEvent()方法.否则外层layout不会执行onClick方法.
                ViewGroup parent = (ViewGroup) widget.getParent();
                if (parent != null) {
                    parent.onTouchEvent(event);
                }
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }
}
