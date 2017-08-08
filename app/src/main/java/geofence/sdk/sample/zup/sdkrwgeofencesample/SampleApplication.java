package geofence.sdk.sample.zup.sdkrwgeofencesample;

import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by wisle on 08/08/2017.
 */

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
