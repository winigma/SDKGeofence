package br.com.zup.realwavegeolocation;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wisle on 25/07/2017.
 */

public class RWGApplication extends Application {


    public static Context sContext = null;

    public static RWGApplication sInstance;

    private static volatile List<AsyncTask<?, ?, ?>> tasks = new ArrayList<AsyncTask<?, ?, ?>>();


    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (RWGApplication.class) {
            if (sContext == null) {
                sContext = getApplicationContext();
            }
            if (sInstance == null) {
                sInstance = this;
            }

        }
    }


    @Override
    public void onTerminate() {
        for (final AsyncTask<?, ?, ?> task : tasks) {
            task.cancel(true);
        }

        super.onTerminate();
    }


    /**
     * add task in pool
     *
     * @param task
     */
    public static void addTask(final AsyncTask<?, ?, ?> task) {
        tasks.add(task);
    }

    /**
     * remove task of the pool
     *
     * @param task
     */
    public static void removeTask(final AsyncTask<?, ?, ?> task) {
        tasks.remove(task);
    }

}
