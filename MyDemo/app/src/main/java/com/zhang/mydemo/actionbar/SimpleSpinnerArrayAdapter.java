package com.zhang.mydemo.actionbar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

/**
 * Created by zjun on 2015/6/24.
 */
public class SimpleSpinnerArrayAdapter extends ArrayAdapter<String> implements SpinnerAdapter {

    public SimpleSpinnerArrayAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item, new String[]{"one", "two"});

        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }
}
