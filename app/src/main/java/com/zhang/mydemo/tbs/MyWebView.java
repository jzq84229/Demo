package com.zhang.mydemo.tbs;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.tencent.smtt.sdk.WebView;

import java.util.Map;

/**
 * Created by zjun on 2015/9/6 0006.
 */
public class MyWebView extends WebView {

    public MyWebView(Context context) {
        super(context);
        this.context = context;
        this.getSettings().setJavaScriptEnabled(true);
        this.loadUrl("http://jzq84229.github.io");
    }

    public MyWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.getSettings().setJavaScriptEnabled(true);
        this.loadUrl("http://jzq84229.github.io");

    }

    public MyWebView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        this.context = context;
        this.getSettings().setJavaScriptEnabled(true);
        this.loadUrl("http://jzq84229.github.io");
    }


    public MyWebView(Context context, AttributeSet attributeSet, int defStyle, boolean privateBrowsing) {
        super(context, attributeSet, defStyle, privateBrowsing);
        this.context = context;
        this.getSettings().setJavaScriptEnabled(true);
        this.loadUrl("http://jzq84229.github.io");
    }

    public MyWebView(Context context, AttributeSet attributeSet, int defStyle, Map<String, Object> map, boolean privateBrowsing) {
        super(context, attributeSet, defStyle, map, privateBrowsing);
        this.context = context;
        this.getSettings().setJavaScriptEnabled(true);
        this.loadUrl("http://jzq84229.github.io");
    }

    private static final String TAG = "MicroMsg.BounceScrollView";

    private static final int UNKNOWN_ORIENTATION = 0;
    private static final int ORIENTATION_VERTICAL = 1;
    private static final int ORIENTATION_HORIZONTAL = 2;

    private static final int MAX_OFFSET = 300;  //tmp value
    private static final int INIT_OFFSET = -15; //tmp value

    private Context context;
    private Handler handler = new Handler();
    private Scroller scroller;
    private int saveRange = 0;

    /**
     * in case of horizontal sroll in web page
     */
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret = super.drawChild(canvas, child, drawingTime);
        canvas.save();
        Paint paint = new Paint();
        paint.setColor(0x7fff0000);
        paint.setTextSize(24.f);
        paint.setAntiAlias(true);

        canvas.drawText(getContext().getPackageName() + "-pid:" + Process.myPid(), 10, 50, paint);
        canvas.drawText("TBS sdk version:" + WebView.getTbsSDKVersion(getContext()), 10, 100, paint);
        if (getX5WebViewExtension() != null) {
            canvas.drawText("X5 core:" + WebView.getTbsCoreVersion(getContext()), 10, 150, paint);
        } else {
            canvas.drawText("Sys Core", 10, 150, paint);
        }
        canvas.restore();
        return ret;
    }

    /**
     * delta decelerates as long as offset increase
     */
    private int calNewOffset(int offset, int delta) {
        if (Math.abs(offset) > MAX_OFFSET) {
            return MAX_OFFSET;
        }
        double ratio = (MAX_OFFSET - Math.abs(offset)) / (1.0 * MAX_OFFSET);
        android.util.Log.d("Bran", "ratio = " + ratio);
        double newRatio = Math.sqrt(ratio); // not linear
        int newOff = offset + (int) (newRatio * delta);
        android.util.Log.d("Bran", "ratio newOff = " + newOff);
        if (Math.abs(newOff) > Math.abs(offset + delta)) {
            newOff = offset + delta;
        }
        return newOff;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    // TBS: Do not use @Override to avoid false calls
    public boolean tbs_overScrollBy(int deltaX, int deltaY, int scrollX,
                                    int scrollY, int scrollRangeX, int scrollRangeY,
                                    int maxOverScrollX, int maxOverScrollY,
                                    boolean isTouchEvent, View view) {
        android.util.Log.d("Bran", "overScrollBy deltaY = " + deltaY
                + " scrollY =" + scrollY + " scrollRangeY = " + scrollRangeY
                + " maxOverScrollY = " + maxOverScrollY);

        // TBS: call View method
        view.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        /**
         * This condition can't use scrollY <= 0 representing top edge, avoiding
         * jitter in bottom edge The test case is web page that do not scroll at
         * all such as www.baidu.com
         *
         *
         * deltaY < INIT_OFFSET instead of deltaY < 0, avoiding jitter in top
         * edge
         */
        if ((scrollY < 0) || (scrollY == 0 && deltaY < INIT_OFFSET)) {
            int originalOffset = scrollY + deltaY; // + Math.abs(deltaY)
            int newOffset = calNewOffset(scrollY, deltaY);
            android.util.Log.d("Bran", "originalOffset = " + originalOffset
                    + " newOffset = " + newOffset);
            maxOverScrollY = Math.abs(newOffset);
        }
        boolean r = super.super_overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
                isTouchEvent);
        saveRange = scrollRangeY;

        // setOverScrollMode(View.OVER_SCROLL_NEVER);

        return r;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    // TBS: Do not use @Override to avoid false calls
    public void tbs_onOverScrolled(int scrollX, int scrollY, boolean clampedX,
                                   boolean clampedY, View view) {
        super.super_onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        android.util.Log.d("Bran", "onOverScrolled scrollY = " + scrollY
                + " clampedY = " + clampedY);

        // TODO scrollTo
    }

    // TBS: Do not use @Override to avoid false calls
    public boolean tbs_dispatchTouchEvent(MotionEvent ev, View view) {
        boolean r = super.super_dispatchTouchEvent(ev);
        android.util.Log.d("Bran", "dispatchTouchEvent " + ev.getAction() + " "
                + r);
        // MotionEvent.ACTION_DOWN 0
        // up 1
        // move 2

        return r;
    }

    // TBS: Do not use @Override to avoid false calls
    public boolean tbs_onInterceptTouchEvent(MotionEvent ev, View view) {
        boolean r = super.super_onInterceptTouchEvent(ev);
        android.util.Log.d("Bran", "onInterceptTouchEvent " + ev.getAction()
                + " " + r);

        final int action = ev.getAction();

        /**
         * get gesture orientation
         */
        // switch (action & MotionEvent.ACTION_MASK) {
        // case MotionEvent.ACTION_DOWN:
        // case MotionEvent.ACTION_MOVE:
        // android.util.Log.d("Bran", "onInterceptTouchEvent lastX = " + lastX +
        // " lastY = " + lastY);
        // if(lastX < 0 || lastY < 0) {
        // lastX = (int) ev.getX();
        // lastY = (int) ev.getY();
        // //First Time
        // r = false;
        // }
        // else if(orientation == UNKNOWN_ORIENTATION) {
        // final int x = (int) ev.getX();
        // final int y = (int) ev.getY();
        // int dx = Math.abs(x - lastX);
        // int dy = Math.abs(y - lastY);
        // if(dy > dx) {
        // orientation = ORIENTATION_VERTICAL;
        // r = false;
        // }
        // else {
        // orientation = ORIENTATION_HORIZONTAL;
        // r = false;
        // }
        // android.util.Log.d("Bran", "onInterceptTouchEvent orientation = " +
        // orientation);
        // }
        // break;
        // case MotionEvent.ACTION_UP:
        // case MotionEvent.ACTION_CANCEL:
        // lastX = -1;
        // lastY = -1;
        // orientation = UNKNOWN_ORIENTATION;
        // break;
        // default:
        // break;
        // }

        return r;
    }

    // TBS: Do not use @Override to avoid false calls
    public boolean tbs_onTouchEvent(MotionEvent ev, final View view) {

        final int action = ev.getAction();

        /**
         * For abort running animation
         */
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (scroller != null) {
                    android.util.Log.d("Bran", "onTouchEvent down scroller");
                    if (!scroller.isFinished()) {
                        android.util.Log.d("Bran",
                                "onTouchEvent down  !scroller.isFinished() ");
                        scroller.abortAnimation();
                    }
                    scroller = null;
                }
                break;
            default:
                break;
        }

        boolean r = super.super_onTouchEvent(ev);
        android.util.Log.d("Bran", "onTouchEvent " + ev.getAction() + " " + r);

        /**
         * restore page scrollY to be zero
         */
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // TODO
                android.util.Log.d("Bran", "!!!onTouchEvent getScrollY() = "
                        + view.getScrollY());
                // TBS - call view method
                if (view.getScrollY() < 0) {
                    handler.post(new Runnable() {

                        // Do not use @Override to avoid false calls
                        public void run() {
                            // it's very quick, not smooth at all.
                            // smoothScrollTo(0, 0);
                            smoothScrollToZero(view);
                        }

                    });
                }
                break;
            default:
                break;
        }
        return r;
    }

    private void smoothScrollToZero(View view) {
        // TBS - call ViewGroup method
        if (((ViewGroup) view.getParent()).getChildCount() == 0) {
            // Nothing to do.
            return;
        }

        // TBS - call view method
        int scrollY = view.getScrollY();
        android.util.Log.d("Bran", "smoothScrollToZero scrollY = " + scrollY);
        if (scrollY > 0) {
            // ignore normal scrolling page
            return;
        }

        /**
         * new scroller to restore page scrollY to be zero, as I do not want to
         * reflect private mScroller in super class.
         */
        scroller = new Scroller(context);
        int dy = 0 - scrollY;
        scroller.startScroll(view.getScrollX(), scrollY, 0, dy, 1000);

        // TBS: Call view method
        view.invalidate();
    }

    // TBS: Do not use @Override to avoid false calls
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void tbs_computeScroll(View view) {
        android.util.Log.d("Bran", "computeScroll ");
        if (scroller != null) {
            if (scroller.computeScrollOffset()) {
                android.util.Log.d("Bran", "getCurrX = " + scroller.getCurrX()
                        + " getCurrY = " + scroller.getCurrY());
                /**
                 * scrollTo do not support overScroll. clamp force negative
                 * scrollY to be zero.
                 */
                // scrollTo(scroller.getCurrX(), scroller.getCurrY());

                int oldX = view.getScrollX();
                int oldY = view.getScrollY();
                int x = scroller.getCurrX();
                int y = scroller.getCurrY();

                if (oldX != x || oldY != y) {
                    final int range = saveRange;

                    // TODO: TBS or super?
                    tbs_overScrollBy(x - oldX, y - oldY, oldX, oldY, 0, range, 0,
                            0, false, view);
                    tbs_onScrollChanged(view.getScrollX(), view.getScrollY(), oldX, oldY, view);

                }

                /**
                 * Maybe draw scroll bar is not required by test, but no idea
                 * why.
                 */
                // if (!awakenScrollBars()) {
                // // Keep on drawing until the animation has finished.
                // postInvalidateOnAnimation();
                // }
            } else {
                android.util.Log.d("Bran", "computeScrollOffset return false ");
                if (!scroller.isFinished()) {
                    android.util.Log.d("Bran", "!scroller.isFinished() ");
                    scroller.abortAnimation();
                }
                scroller = null;
            }
        } else {
            super.super_computeScroll();
        }

    }

    // TBS: Do not use @Override to avoid false calls
    public void tbs_onScrollChanged(int l, int t, int oldl, int oldt, View view) {
        android.util.Log.d("Bran", "onScrollChanged oldY = " + oldt
                + " mScrollY = " + t);
        super.super_onScrollChanged(l, t, oldl, oldt);
    }

}