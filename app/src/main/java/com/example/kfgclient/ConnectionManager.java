package com.example.kfgclient;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import de.exlap.Capabilities;
import de.exlap.ConnectionListener;
import de.exlap.DataListener;
import de.exlap.DataObject;
import de.vwn.kfg.client.KFGClient;
import de.vwn.kfg.client.KFGListenerAdapter;
//TODO synchoronized...?
/**
 * Manager class which handles connection establishment to KFG. This manager provides WLAN connection to KFG server.
 * This is the point where the KFG library informs the app about connection status (e.g. connection loss or connection
 * failure). As a result the app can react to connection changes. If the connection to a server is established, this
 * manager will monitor this connection by using a background task.
 *
 * https://stackoverflow.com/questions/1050991/singleton-with-arguments-in-java
 * https://stackoverflow.com/questions/12492533/android-progress-bar-is-not-dismissed-properly
 * https://stackoverflow.com/questions/11407943/this-handler-class-should-be-static-or-leaks-might-occur-incominghandler
 *
 * Korzystam z Singletona, który posiada metodę init. Pozwala ona na przekazanie contextu do utworzenia ProgressBarów.
 */

public class ConnectionManager implements Runnable, ConnectionListener, DataListener {


    private static ConnectionManager instance =null;
    private final MainActivity activity;
    private MyHandler handler;

    private KFGClient kfgClient;
    private ProgressDialog callbackDialog;


    private ConnectionManager(MainActivity activity){
        this.activity = activity;

       Log.d(Const.MYTAG,"ConnectionManager created");


    }

    public static ConnectionManager getInstance(){
        Log.d(Const.MYTAG,"getInstance called");

        if(instance == null){
            throw new AssertionError("You have to call init first");
        }
        return instance;

    }

    public synchronized static void init(MainActivity activity) {

        if (instance != null)
        {
            // in my opinion this is optional, but for the purists it ensures
            // that you only ever get the same instance when you call getInstance
            throw new AssertionError("You already initialized me");
        }
        Log.d(Const.MYTAG,"Init called");

        instance = new ConnectionManager(activity);

    }

    @Override
    public void run() {
        Log.d(Const.MYTAG,"run in ConnectionManager executed");

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callbackDialog = new ProgressDialog(activity);
                handler = new MyHandler(callbackDialog);

                callbackDialog.setCancelable(false);
                callbackDialog.setMessage("Connecting...");
                callbackDialog.show();
            }
        });

        Looper.prepare();
        connect();
        Looper.loop();
    }

    private void connect() {
        //KFGConfiguration kfgConfiguration = new KFGConfiguration(true,true);
        Log.d(Const.MYTAG,"connect in ConnectionManager executed");

        kfgClient = new KFGClient();
        kfgClient.addDataListener(this);
        kfgClient.addConnectionListener(this);
        kfgClient.addKFGListener(new KFGListenerAdapter());
        kfgClient.connect();


    }

    public void disconnect() {
        kfgClient.disconnect();
    }

    static class MyHandler extends Handler {
        private final WeakReference<ProgressDialog> callbackDialogRef;

        MyHandler(ProgressDialog progressDialog) {
            callbackDialogRef = new WeakReference<>(progressDialog);
        }
        @Override
        public void handleMessage(Message msg)
        {
            ProgressDialog dialog = callbackDialogRef.get();
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    /**
     Memory leaks!!
         private Handler handler = new Handler(){

            @Override
             public void handleMessage ( Message message )
            {
                callbackDialog.dismiss();
            }
        };
     */



    @Override
    public void onConnectionSuccessful(final Capabilities capabilities) {
        Log.d(Const.MYTAG, "Connection successfully created");

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (callbackDialog != null && callbackDialog.isShowing()) {

                    callbackDialog.setMessage(Const.ON_CONNECTION_SUCCESFUL);
                    callbackDialog.setCancelable(true);
                    handler.sendMessageDelayed(Message.obtain(),3000);
                }
                Toast.makeText(activity, "Connection established.", Toast.LENGTH_LONG).show();
            }
        });
/**
        //TODO Authentication
        //check KFG Identifier
       activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                getKFGIdentifier();
           }
        });
 */

    }

    @Override
    public void onConnectionClosed(boolean closedByServer, String message) {

        if (callbackDialog != null && callbackDialog.isShowing())
        {
            callbackDialog.setMessage(Const.ON_CONNECTION_CLOSED);
            callbackDialog.setCancelable(true);
            handler.sendMessageDelayed(Message.obtain(),3000);
        }
        Log.d(Const.MYTAG, " connection closed because: " + message + " closed by server? " + (closedByServer ? "yes" : "no"));
    }

    @Override
    public void onConnectionClosedOnError(Exception e) {
        if (callbackDialog != null && callbackDialog.isShowing()) {
            callbackDialog.setMessage(Const.ON_CONNECTION_CLOSED_ON_ERROR);
            callbackDialog.setCancelable(true);
            handler.sendMessageDelayed(Message.obtain(),3000);
        }

        Log.d(Const.MYTAG, Const.ON_CONNECTION_CLOSED_ON_ERROR + e);
    }

    @Override
    public void onConnectionFailed(Exception e) {
        if (callbackDialog != null && callbackDialog.isShowing()) {

            callbackDialog.setMessage(Const.ON_CONNECTION_FAILED);
            callbackDialog.setCancelable(true);
            handler.sendMessageDelayed(Message.obtain(),3000);
        }

        Log.d(Const.MYTAG, Const.ON_CONNECTION_FAILED + e);

    }

    @Override
    public void onReconnect() {
        Log.d(Const.MYTAG,"onReconnect");

    }

    @Override
    public void onReconnectSuccessful() {
        Log.d(Const.MYTAG, "reconnection is successful!");
        if (callbackDialog != null && callbackDialog.isShowing())
        {
            callbackDialog.setMessage(Const.ON_RECONNECTION_SUCCESFUL);
            callbackDialog.setCancelable(true);
            handler.sendMessageDelayed(Message.obtain(),3000);
        }
    }

    @Override
    public void onDataloss() {
        Log.d(Const.MYTAG,"onDataLoss");

    }

    @Override
    public void onData(final DataObject dataObject) {


        activity.runOnUiThread(new Runnable() {

            public void run() {
                Log.d(Const.MYTAG, "ConnectionManager.onData(...) called");
                activity.updateObjectsView(dataObject);

            }
        });
    }

    @Override
    public void onDataError(Exception e) {
        Log.d(Const.MYTAG,"onDataError: " + e.toString());

    }
    public KFGClient getKFGClient() {
        return kfgClient;
    }
}
