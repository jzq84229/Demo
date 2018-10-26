package com.zhang.mydemo.actionbar;

import android.content.Context;
import android.support.v7.app.ActionBar;

/**
 * Created by zjun on 2015/6/24.
 */
public class ListListener extends BaseListener implements ActionBar.OnNavigationListener {
    public ListListener(Context ctx, IReportBack target) {
        super(ctx, target);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        this.mReportTo.reportBack("list listener", "ItemPosition:" + itemPosition);
        return true;
    }
}
