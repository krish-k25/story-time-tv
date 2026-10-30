package com.example.storytv;
import android.content.*;
public class StreakRefreshReceiver extends BroadcastReceiver {public void onReceive(Context c,Intent i){final PendingResult result=goAsync();final Context app=c.getApplicationContext();new Thread(new Runnable(){public void run(){try{if(android.os.Build.VERSION.SDK_INT>=26)StreakChannel.refresh(app);}finally{result.finish();}}}).start();}}
