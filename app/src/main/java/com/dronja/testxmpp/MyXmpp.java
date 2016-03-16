package com.dronja.testxmpp;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by ron on 27.02.2016.
 */
public class MyXmpp {
    String TAG = "myXMPP";

    private static final String Domain = "xmpp.jp";
    private static final String Host = "xmpp.jp";
    private static final int Port = 5222;
    private String userName = "";
    private String password = "";

    AbstractXMPPConnection connection;
    ChatManager manager;
    Chat newChat;
    XMPPConnectionListener connectionListener = new XMPPConnectionListener();

    private boolean connected;
    private boolean isToasted;
    private boolean chat_created;
    private boolean loggedin;

    public void init(String userId, String pwd){
        Log.i(TAG, "Start to init");
        this.userName = userId;
        this.password = pwd;
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setUsernameAndPassword(userName, password);
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
        configBuilder.setResource("Android");
        configBuilder.setHost(Host);
        configBuilder.setServiceName(Domain);
        configBuilder.setPort(Port);
        configBuilder.setDebuggerEnabled(true);
        connection = new XMPPTCPConnection(configBuilder.build());
        connection.addConnectionListener(connectionListener);
    }

    public void disconnectConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"Disconnect");
                connection.disconnect();
            }
        }).start();
    }
    public void connectConnection(){
        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... arg0) {

                // Create a connection
                try {
                    Log.i(TAG,"Try connect");
                    connection.connect();
                    login();
                    connected = true;

                } catch (IOException e) {
                    Log.e(TAG,e.getLocalizedMessage());
                } catch (SmackException e) {
                    Log.e(TAG,e.getLocalizedMessage());
                } catch (XMPPException e) {
                    Log.e(TAG,e.getLocalizedMessage());
                }

                return null;
            }
        };
        connectionThread.execute();
    }

    public void login() {

        try {
            connection.login(userName, password);
           Log.i(TAG,"Login ok!");
            sendMsg();
        } catch (XMPPException | SmackException | IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(TAG,e.getLocalizedMessage());
        }

    }
    public void sendMsg() {
        if (connection.isConnected()== true) {

            manager = ChatManager.getInstanceFor(connection);
            newChat = manager.createChat("login1@xmpp.jp");

            try {
                newChat.sendMessage("Hello!!!");
            } catch (SmackException.NotConnectedException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
        }
    }

    //Connection Listener to check connection state
    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(final XMPPConnection connection) {

            Log.d(TAG, "Connected!");
            connected = true;
            if (!connection.isAuthenticated()) {
                login();
            }
        }

        @Override
        public void connectionClosed() {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub


                    }
                });
            Log.d(TAG, "Connection closed!");
            connected = false;
            chat_created = false;
            loggedin = false;
        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                    }
                });
            Log.d(TAG, "ConnectionClosedOn Error!");
            connected = false;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectingIn(int arg0) {

            Log.d(TAG, "Reconnectingin " + arg0);

            loggedin = false;
        }

        @Override
        public void reconnectionFailed(Exception arg0) {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {



                    }
                });
            Log.d(TAG, "ReconnectionFailed!");
            connected = false;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectionSuccessful() {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub



                    }
                });
            Log.d(TAG, "ReconnectionSuccessful");
            connected = true;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.d(TAG, "Authenticated!");
            loggedin = true;

            chat_created = false;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }).start();
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub



                    }
                });
        }
    }


}
