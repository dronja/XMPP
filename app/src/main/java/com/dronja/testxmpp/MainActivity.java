package com.dronja.testxmpp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    private static final String TAG = "myXMPP";
    private ConnectXmppService mService;
    private boolean mBounded;
    private final ServiceConnection mConnection = new ServiceConnection() {

        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            mService = ((LocalBinder<ConnectXmppService>) service).getService();
            mBounded = true;
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            mBounded = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onConnect(View view)
    {
        try {

            String userName = "dronja";
            String passWord = "testtest12345678";

            Intent intent = new Intent(getBaseContext(),ConnectXmppService.class );
            intent.putExtra("user",userName);
            intent.putExtra("pwd",passWord);
            startService(intent);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
