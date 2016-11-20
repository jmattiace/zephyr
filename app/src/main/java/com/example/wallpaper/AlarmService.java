package com.example.wallpaper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by 204075127 on 9/1/16.
 */
public class AlarmService extends Service {

    AlarmManagerBroadcastReceiver alarm = new AlarmManagerBroadcastReceiver();

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        alarm.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        //TODO
        return null;
    }

}


