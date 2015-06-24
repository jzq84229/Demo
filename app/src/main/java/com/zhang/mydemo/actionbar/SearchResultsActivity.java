package com.zhang.mydemo.actionbar;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.zhang.mydemo.R;

public class SearchResultsActivity extends DebugActivity {
    private static String tag = "Search Results Activity";

    public SearchResultsActivity() {
        super(R.menu.basemenu, R.layout.main, R.id.textViewId, tag);
    }

    @Override
    protected boolean onMenuItemSelected(MenuItem item) {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent queryIntent = getIntent();
        doSearcherQuery(queryIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final Intent queryIntent = getIntent();
        doSearcherQuery(queryIntent);
    }

    private void doSearcherQuery(final Intent queryIntent) {
        final String queryAction = queryIntent.getAction();
        if (!(Intent.ACTION_SEARCH.equals(queryAction))) {
            Log.d(tag, "intent Not for search");
            return;
        }
        final String queryString = queryIntent.getStringExtra(SearchManager.QUERY);
        this.reportBack(tag, queryString);
    }
}
