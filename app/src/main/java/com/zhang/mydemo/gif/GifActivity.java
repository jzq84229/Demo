package com.zhang.mydemo.gif;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zhang.mydemo.BaseActivity;
import com.zhang.mydemo.R;

public class GifActivity extends BaseActivity {
    private RelativeLayout rlContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContent() {
        setContentView(R.layout.activity_gif);
    }

    @Override
    public void findViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        rlContent = (RelativeLayout) findViewById(R.id.rl_content);
    }

    @Override
    public void setData() {
        GameView mGameView = new GameView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mGameView.setLayoutParams(params);
        rlContent.addView(mGameView);
    }

    @Override
    public void showContent() {

    }

}
