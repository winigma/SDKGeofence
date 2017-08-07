package br.com.zup.realwavegeolocation.ijoin.broadcast;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;

import java.util.ArrayList;
import java.util.List;

import br.com.zup.realwavegeolocation.ijoin.GeofenceCallback;

/**
 * Created by wisle on 03/08/2017.
 */

public class MyResultReceive extends ResultReceiver {


    private GeofenceCallback geofenceCallback;
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public MyResultReceive(Handler handler) {
        super(handler);
    }

    public MyResultReceive(Handler handler,GeofenceCallback geofenceCallback ) {
        super(handler);
       this.geofenceCallback = geofenceCallback;
    }

    public void setGeofenceCallback(GeofenceCallback geofenceCallback) {
        this.geofenceCallback = geofenceCallback;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                this.geofenceCallback.onEnterGeofence(resultData.getString("enter"));
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                this.geofenceCallback.onExitGeofence(resultData.getString("exit"));
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                this.geofenceCallback.onDWellGeofence(resultData.getString("dwell"));
                break;
            case 666:
                this.geofenceCallback.onError(resultData.getString("error"));
                break;
        }
    }


    private String getGeofenceTransitionDetails(final int geofenceTrasition, List<Geofence> triggeringGeofences) {

        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesList.add(geofence.getRequestId());
        }

        String status = null;
        switch (geofenceTrasition) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                status = "Entering";

                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                status = "Exiting";
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                status = "DWell";
                break;
        }
        return status + TextUtils.join(", ", triggeringGeofencesList);
    }


    /**
     * Error in Geofence
     *
     * @param errorCode
     * @return String
     */
    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }
}
