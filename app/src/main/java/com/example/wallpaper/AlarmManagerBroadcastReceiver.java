package com.example.wallpaper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    private static int deleteMeCounter = 2;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("Broadcast", "Received broadcast");

        //Get new image from rest call and persist to private file system
        InputStream inStream = retrieveImage(context);

        try
        {
            FileOutputStream fos = context.openFileOutput(Constants.CURRENT_IMG_FILE, Context.MODE_PRIVATE);

            //Copy InputStream to OutputStream
            int byteVal;
            while((byteVal = inStream.read()) != -1)
            {
                fos.write(byteVal);
            }
            fos.close();
        }
        catch (IOException e)
        {
            Log.e("Broadcast", "IOException occurred writing to output file");
        }

        //Refresh wallpaper image
        ImageRefresher imgRef = new ImageRefresher(context);
        try
        {
            imgRef.refreshWallpaperImage(false);
        }
        catch (Exception e)
        {
            //TODO
            e.printStackTrace();
        }

    }

    private InputStream retrieveImage(Context context) {
        if(deleteMeCounter++ % 2 == 0)
            return context.getResources().openRawResource(+R.drawable.img_1); //+ means to look in additional res directories (drawable)
        else
            return context.getResources().openRawResource(+R.drawable.images); //+ means to look in additional res directories (drawable)
    }

    public void setAlarm(Context context)
    {
        Log.i("AlarmMan", "Inside setAlarm()");

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("mypackage.START_ALARM");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 , pi); //After after 60 seconds
    }

    public void cancelAlarm(Context context) {
        Log.i("AlarmMan", "Inside cancelAlarm()");
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}