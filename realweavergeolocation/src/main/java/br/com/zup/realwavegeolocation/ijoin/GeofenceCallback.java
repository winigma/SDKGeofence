package br.com.zup.realwavegeolocation.ijoin;

import com.google.android.gms.location.Geofence;

import br.com.zup.realwavegeolocation.model.Tag;

/**
 * Created by wisle on 27/07/2017.
 */

public interface GeofenceCallback {
    /**
     * This method is call in Error of processing geofence
     *
     * @param args0
     */
    void onError(final String args0);

    /**
     * this method
     * is called upon entering the point of interest
     *
     * @param args
     */
    void onEnterGeofence(final String args);

    /**
     * Is called when leaving at the point of interest
     *
     * @param args
     */
    void onExitGeofence(final String args);


    /**
     * Is called by staying at the point of interest
     *
     * @param args
     */
    void onDWellGeofence(final String args);


}
