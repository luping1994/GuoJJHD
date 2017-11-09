package net.suntrans.guojjhd;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Looney on 2017/8/31.
 */

public class App extends Application {

    public static SharedPreferences sharedPreferences;
    private static Application application;

    public static Application getApplication() {
        return application;
    }

    public static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = getApplication().getSharedPreferences("suntransconfig", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        startService(new Intent(this,MyService.class));

    }
}
