package com.example.kfgclient.ui.main;
/**
 * https://thispointer.com/java-how-to-update-the-value-of-an-existing-key-in-hashmap-put-vs-replace/
 */

//TODO https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.widget.Toast;

import com.example.kfgclient.ConnectionManager;
import com.example.kfgclient.Const;

import java.io.IOException;
import java.util.HashMap;
public class MonitoringViewModel extends ViewModel {


    private MutableLiveData<HashMap<String,String>> objectsMap = new MutableLiveData<>();
    private HashMap<String,Integer> objectsWithTimeStepMap = new HashMap<>();

    public HashMap<String, Integer> getObjectsWithTimeStepMap() {
        return objectsWithTimeStepMap;
    }

    private ConnectionManager connectionManagerRef;


    //TODO constructor


    public MonitoringViewModel() {

        connectionManagerRef = ConnectionManager.getInstance();
        this.connectionManagerRef = connectionManagerRef;
    }

    public MutableLiveData<HashMap<String, String>> getObjectsMap() {
        return objectsMap;
    }


   public void createOrUpdateObject(String url,String value){

            HashMap<String,String> newHashMap = new HashMap<>();
            newHashMap.put(url,value);
            objectsMap.postValue(newHashMap);

    }

    public void startSubscription(final String url, final int val){

        if(connectionManagerRef!=null && connectionManagerRef.getKFGClient()!=null && connectionManagerRef.getKFGClient().isConnected()){

            try{
                Log.d(Const.MYTAG,"Start sub called");
                //TODO new Runnable, a może podać tutaj instancje ConnectionManager? Zbadaj obie opcje.

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            connectionManagerRef.getKFGClient().subscribeObject(url,val);
                            objectsWithTimeStepMap.put(url,val);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }catch (Exception e){
                e.printStackTrace();
            }



        }else{
            Log.d(Const.MYTAG,"You are not connected");
        }

    }


    public void stopSubscription(final String url){

        if(connectionManagerRef!=null && connectionManagerRef.getKFGClient()!=null && connectionManagerRef.getKFGClient().isConnected()){

            try{
                Log.d(Const.MYTAG,"Stop sub called");
                //TODO new Runnable, a może podać tutaj instancje ConnectionManager? Zbadaj obie opcje.

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            connectionManagerRef.getKFGClient().unsubscribeObject(url);
                            objectsWithTimeStepMap.remove(url);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }catch (Exception e){
                e.printStackTrace();
            }



        }else{
            Log.d(Const.MYTAG,"You are not connected");
        }

    }


}