package com.zhang.demo.communicat;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class MessengerService extends Service {
    private static final int MSG_SUM = 0x110;

    public MessengerService() {
    }

    //最好换成HandlerThread的形式
    private Messenger mMessenger = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msgfromClient) {
            Message msgToClient = Message.obtain(msgfromClient);//返回给客户端的消息
            switch (msgfromClient.what) {
                case MSG_SUM:
                    msgToClient.what = MSG_SUM;
                    try {
                        //模拟很耗时
                        Thread.sleep(2000);
                        msgToClient.arg2 = msgfromClient.arg1 + msgfromClient.arg2;
                        msgfromClient.replyTo.send(msgToClient);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

            }
            super.handleMessage(msgfromClient);
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
