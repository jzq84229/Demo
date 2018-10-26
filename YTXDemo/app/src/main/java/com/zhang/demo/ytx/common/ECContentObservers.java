package com.zhang.demo.ytx.common;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;

import com.zhang.demo.ytx.ECApplication;
import com.zhang.demo.ytx.core.ContactsCache;

/**
 *
 * Created by Administrator on 2016/7/21.
 */
public class ECContentObservers {

    private static final int CONTACTS_CHANGED = 300;
    private Context mContext;
    private static ECContentObservers ourInstance = new ECContentObservers();

    public static ECContentObservers getInstance() {
        return ourInstance;
    }

    private ECContentObservers() {
        mContext = ECApplication.getInstance().getApplicationContext();
    }

    public void initContentObserver() {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.registerContentObserver(ContactsContract.Data.CONTENT_URI, true, new MyContactObserver(null));
    }


    private class MyContactObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContactObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            observerHandler.removeMessages(CONTACTS_CHANGED);
            observerHandler.sendEmptyMessageDelayed(CONTACTS_CHANGED, 1000);
        }
    }

    private Handler observerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONTACTS_CHANGED:
                    ContactsCache.getInstance().reload();
                    break;
            }
        }
    };
}
