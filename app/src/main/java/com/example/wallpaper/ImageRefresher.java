package com.example.wallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.os.Build;
import android.renderscript.ScriptGroup;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jason Mattiace on 9/11/16.
 */
public class ImageRefresher {

    private Context context;

    public ImageRefresher(Context context)
    {
        this.context = context;
    }

    /**
     * Gets image from local file storage and sets the system wallpaper.
     * If checkElapsed is true, the wallpaper is changed only if the
     * image refresh period has elapsed.
     *
     * @param checkElapsed If true, we must check the image refresh period
     * @throws FileNotFoundException if local file is not found on the file system
     */
    public void refreshWallpaperImage(boolean checkElapsed) throws FileNotFoundException
    {
        File fileStreamPath = context.getFileStreamPath(Constants.CURRENT_IMG_FILE);
        if(fileStreamPath.exists())
        {
            if(checkElapsed)
            {
                long lastMod = fileStreamPath.lastModified();
                if((System.currentTimeMillis() - lastMod) > Constants.IMG_REFRESH_PERIOD)
                {
                    setWallpaperImage();
                }
            }
            else
            {
                setWallpaperImage();
            }
        }
        else
        {
            throw new FileNotFoundException("Couldn't find file!");
        }
    }

    private void setWallpaperImage() throws FileNotFoundException
    {
        //isWallpaperSupported()
        //isWallpaperSetAllowed()

        InputStream fileStream = context.openFileInput(Constants.CURRENT_IMG_FILE);

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        //If api is >= 24 only set the lock screen, else set system wallpaper
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setStream(fileStream, null, true, WallpaperManager.FLAG_LOCK);

            } else {
                Log.i("TAG", "SDK Version is " + Build.VERSION.SDK_INT + ", setting system wallpaper");
                wallpaperManager.setStream(fileStream);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.e("ImageRefresher", "IOException caught setting wallpaper");
        }

        Toast.makeText(context, "Wallpaper Set", Toast.LENGTH_SHORT).show();
    }
}
