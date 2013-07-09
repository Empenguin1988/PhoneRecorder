package com.empenguin.phonerecorder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
/*
 * recorder out: 1. outgoing; 2.off-hook and start recorder; 3.hang off and stop recorder; 
 * 				 4. wait 1.5s and get time > 0 save or delete
 * recorder in : 1.ringing; 2.off-hook and start recorder; 3.hang off and stop recorder; 4. save
 */
public class PhoneReceiver extends BroadcastReceiver {
	private final static String TAG = PhoneReceiver.class.getSimpleName();
	private static TelephonyManager mTm = null;
	private static CallRecorder mCallRecorder = new CallRecorder();
	private static String mTelNumber = "";
	private static boolean mRecord = false;
    @Override
    public void onReceive(Context context, Intent intent){
        Log.d(TAG,"action " + intent.getAction());
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            Log.d(TAG,"dial:" + this.getResultData() + " out.......");
            if ("13862074264".contains(getResultData())) {
            	mTelNumber = "13862074264";
            	mRecord = true;
            }
        } else {
        	if (mTm == null) {
        		Log.d(TAG, "add listener when state change");
        		mTm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
        		mTm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);    	
            }
        }
    }
    private PhoneStateListener listener=new PhoneStateListener(){
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d(TAG,"hang off..........");
                if (mRecord) {
                	Log.d(TAG, "stop recorder......");
                	mCallRecorder.stop();
                	mRecord = false;
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
            //At least one call exists that is dialing, active, or on hold, and no calls are ringing or waiting
                Log.d(TAG,"hang up.........");
                if (mRecord) {
                	DateFormat df = new SimpleDateFormat("HH-mm-ss");
                	Log.d(TAG, "start recorder......" + mTelNumber + "-" + df.format(new Date()));
                	try {
						mCallRecorder.start("/sdcard/" + mTelNumber + "-" + df.format(new Date()) + ".amr");
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
                break;
            case TelephonyManager.CALL_STATE_RINGING:
            //A new call arrived and is ringing or waiting. In the latter case, another call is already active.
                Log.d(TAG,"telephone number:" + incomingNumber + " coming....");
                if ("13862074264".contains(getResultData())) {
                	mTelNumber = "13862074264";
                	mRecord = true;
                }
                break;
            }
        } 
    };
}


/*
 * getActiveFgCallState() do not work! always return IDLE
 *
new Thread() {
private int counter = 0;
public void run() {
	Looper.prepare();
	while(true) {
		Log.d(TAG, "phone state:" + CallManager.getInstance().getActiveFgCallState());
		if (CallManager.getInstance().getActiveFgCallState() == Call.State.ACTIVE) {
        	break;
        }
		try {
			if (counter >= 20) {
				break;
			}
			counter++;
			sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
}.start();
*/