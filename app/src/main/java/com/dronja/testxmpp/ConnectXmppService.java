package com.dronja.testxmpp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by ron on 27.02.2016.
 */
public class ConnectXmppService extends Service {
    private String userName;
    private String passWord;
    private MyXmpp xmpp = new MyXmpp();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder<ConnectXmppService>(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        xmpp.disconnectConnection();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            userName = intent.getStringExtra("user");
            passWord = intent.getStringExtra("pwd");
            xmpp.init(userName, passWord);
            xmpp.connectConnection();
        }
        return START_NOT_STICKY;
    }
}
