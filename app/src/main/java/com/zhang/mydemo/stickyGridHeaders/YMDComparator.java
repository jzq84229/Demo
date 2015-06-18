package com.zhang.mydemo.stickyGridHeaders;

import java.util.Comparator;

/**
 * Created by zjun on 2015/6/18.
 */
public class YMDComparator implements Comparator<GridItem> {
    @Override
    public int compare(GridItem lhs, GridItem rhs) {
        return lhs.getTime().compareTo(rhs.getTime());
    }
}
