package com.zhang.mydemo.stickyGridHeaders;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by zjun on 2015/7/3.
 */
public class StickyGridHeadsActivityTest extends ActivityInstrumentationTestCase2<StickyGridHeadsActivity> {
    private StickyGridHeadsActivity mStickyGridHeadsActivity;

    public StickyGridHeadsActivityTest(Class<StickyGridHeadsActivity> activityClass) {
        super(activityClass);
        mStickyGridHeadsActivity = getActivity();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testPreconditions(){
        assertNotNull("mStickyGridHeadsActivity is null", mStickyGridHeadsActivity);
    }
}
