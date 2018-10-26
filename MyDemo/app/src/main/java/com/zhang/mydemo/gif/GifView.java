package com.zhang.mydemo.gif;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import com.zhang.mydemo.BaseApplication;
import com.zhang.mydemo.R;

/**
 * Created by zjun on 2016/1/25 0025.
 */
public class GifView extends View {

    private Movie mMovie;
    private long mMovieStart;



    private int mWidth, mHeight;
    private int mViewWidht, mViewHeight;

    private OnPlayListener onPlayListener;

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public GifView(Context context) {
        super(context);

        mMovie = Movie.decodeStream(getResources().openRawResource(
                R.raw.luyin));
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //使用Movie解析gif图片
        mMovie = Movie.decodeStream(getResources().openRawResource( R.raw.luyin));
        //获得屏幕宽度，高度
        mWidth = BaseApplication.getInstance().screenWidth;
        mHeight = BaseApplication.getInstance().screenHeight;
        //gif图片宽度，高度
        mViewHeight = mMovie.height();
        mViewWidht = mMovie.width();
    }

    public OnPlayListener getOnPlayListener() {
        return onPlayListener;
    }

    public void setOnPlayListener(OnPlayListener onPlayListener) {
        this.onPlayListener = onPlayListener;
    }

    boolean isDraw = true;

    public void onDraw(Canvas canvas) {
        long now = android.os.SystemClock.uptimeMillis();

        if (isDraw) {

            if (mMovieStart == 0) { // first time
                mMovieStart = now;
            }
            if (mMovie != null) {

                int dur = mMovie.duration();
                if (dur == 0) {
                    dur = 5000;
                }
                //计算gif播放时间，gif播放完成，关闭界面
                if (now - mMovieStart >= dur) {
                    isDraw = false;
                    if (onPlayListener != null) {
                        onPlayListener.onFinished();
                    }
                }

                int relTime = (int) ((now - mMovieStart) % dur);

                mMovie.setTime(relTime);
                //根据屏幕大小计算缩放比例
                float saclex = (float) mWidth / (float) mViewWidht;
                float sacley = (float) mHeight / (float) mViewHeight;
                float sameRate = saclex > sacley ? saclex : sacley;
                canvas.scale(sameRate, sameRate);
                mMovie.draw(canvas, 0, 0);

                invalidate();
            }
        }
    }
    //gif关闭接口
    public static interface OnPlayListener {
        public void onFinished();
    }
}
