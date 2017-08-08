package br.com.zup.realwavegeolocation.model.task;

import android.os.AsyncTask;

import br.com.zup.realwavegeolocation.RWGApplication;


/**
 * Created by wisle on 02/08/2017.
 */

public abstract class AbstractAsyncTask<T> extends AsyncTask<T, Void, Boolean> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        RWGApplication.addTask(this);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        try {
            RWGApplication.removeTask(this);
            this.finalize();
        } catch (final Throwable exce) {
            exce.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        super.onPostExecute(result);
        RWGApplication.removeTask(this);
    }

}
