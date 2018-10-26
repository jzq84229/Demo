package com.zhang.demo.ytx.core;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.ui.contact.ContactLogic;
import com.zhang.demo.ytx.ui.contact.ECContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机联系人缓存
 * Created by Administrator on 2016/7/14.
 */
public class ContactsCache {
    //初始化联系人
    public static final String ACTION_ACCOUNT_INIT_CONTACTS = "com.yuntongxun.ecdemo.intent.ACCOUT_INIT_CONTACTS";
    private static ContactsCache instance;
    private ECArrayLists<ECContacts> contacts;
    private LoadingTask asyncTask;

    private ContactsCache() {}

    public static ContactsCache getInstance() {
        if (instance == null) {
            instance = new ContactsCache();
        }
        return instance;
    }

    /**
     * 查询手机联系人
     */
    public synchronized void load() {
        try {
            if (asyncTask == null) {
                asyncTask = new LoadingTask();
            }
            asyncTask.execute();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新加载手机联系人
     */
    public void reload() {
        try {
            stop();
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止加载手机联系人
     */
    public void stop() {
        try {
            if (asyncTask != null && !asyncTask.isCancelled()) {
                asyncTask.cancel(true);
                asyncTask = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取手机联系人
     * @return      ECArrayLists<ECContacts>
     */
    public synchronized ECArrayLists<ECContacts> getContacts() {
        return contacts;
    }











    /**
     * 异步加载手机联系人
     */
    private class LoadingTask extends AsyncTask<Integer, Void, Long> {
        ECArrayLists<ECContacts> contactList = null;

        public LoadingTask() {}

        @Override
        protected Long doInBackground(Integer... params) {
            try {
                LogUtil.d("contatsCache:开始加载联系人");
                //contactList = ContactLogic.getPhoneContac
                //获取手机联系人
                contactList = ContactLogic.getContractList(true);
                //获取联系人头像保存到本地，并更新联系人数据库
                ContactLogic.getMobileContactPhoto(contactList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (contactList != null) {
                ECArrayLists<ECContacts> oldContacts = contacts;
                contacts = contactList;
                ArrayList<String> phones = new ArrayList<>();
                for (ECContacts o : contacts) {
                    List<Phone> phoneList = o.getPhoneList();
                    if (phoneList == null) {
                        continue;
                    }
                    for (Phone phone : phoneList) {
                        if (!TextUtils.isEmpty(phone.getPhoneNum())) {
                            phones.add(phone.getPhoneNum());
                        }
                    }
                }
                String[] array = phones.toArray(new String[]{});
                Intent intent = new Intent(ACTION_ACCOUNT_INIT_CONTACTS);
                intent.putExtra("array", array);

                CCPAppManager.getContext().sendBroadcast(intent);
            }
        }
    }



}
