package com.example.kfgclient.ui.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.kfgclient.ConnectionManager;
import com.example.kfgclient.Const;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class MonitoringViewModel extends ViewModel {
	private MutableLiveData<HashMap<String,String>> objectsMap = new MutableLiveData<>();
	private HashMap<String,Integer> objectsWithTimeStepMap = new HashMap<>();
	public HashMap<String, Integer> getObjectsWithTimeStepMap() {
		return objectsWithTimeStepMap;
	}
	private ConnectionManager connectionManagerRef;

   public MonitoringViewModel() {
		this.connectionManagerRef = ConnectionManager.getInstance();
	}

   public MutableLiveData<HashMap<String, String>> getObjectsMap() {
		return objectsMap;
	}

   public void createOrUpdateObject(String url,String value){
			HashMap<String,String> newHashMap = new HashMap<>();
			newHashMap.put(url,value);
			objectsMap.postValue(newHashMap);
	}

	public void startSubscription(final String url, final int val) {
		if (connectionManagerRef!=null && connectionManagerRef.getKFGClient()!=null && connectionManagerRef.getKFGClient().isConnected()) {
			try {
				Log.d(Const.MYTAG,"Start sub called");
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
		} else {
			Log.d(Const.MYTAG,"You are not connected");
		}
	}

	public void stopSubscription(final String url) {
		if (connectionManagerRef!=null && connectionManagerRef.getKFGClient()!=null && connectionManagerRef.getKFGClient().isConnected()) {
			try {
				Log.d(Const.MYTAG,"Stop sub called");
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Log.d(Const.MYTAG,"You are not connected");
		}
	}

	public void callFunction(String methodName, int value, boolean shouldReset) {
				if(connectionManagerRef!=null && connectionManagerRef.getKFGClient()!=null && connectionManagerRef.getKFGClient().isConnected()){
				CallValueAsync callValueAsync = new CallValueAsync(methodName, value, shouldReset);
				Thread thread = new Thread(callValueAsync);
				thread.start();
			} else {
				Log.d(Const.MYTAG,"You are not connected");
			}
	}

	private class CallValueAsync implements Runnable  {
		String methodName;
		int val;
		boolean shouldReset;

		public CallValueAsync(String methodName, Integer value, boolean shouldReset) {
			this.methodName = methodName;
			this.val = value;
			this.shouldReset=shouldReset;
		}

		@Override
		public void run() {
			try {
				if (!shouldReset) {
					Method method;
					method = connectionManagerRef.getKFGClient().getClass().getMethod(methodName, Integer.class);
					method.invoke(connectionManagerRef.getKFGClient(), val);
				} else {
					Method method;
					method = connectionManagerRef.getKFGClient().getClass().getMethod(methodName, Integer.class);
					method.invoke(connectionManagerRef.getKFGClient(), val);
					Thread.sleep(1000);
					method.invoke(connectionManagerRef.getKFGClient(), 0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}