package br.com.zup.realwavegeolocation.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import br.com.zup.realwavegeolocation.ijoin.GeofenceCallback;
import br.com.zup.realwavegeolocation.ijoin.LocationManager;

/**
 * Created by wisle on 27/07/2017.
 */

public class RealWaveTagTransitionService extends IntentService {

    private static final String TAG = RealWaveTagTransitionService.class.getSimpleName();

    ResultReceiver resultReceiver;



    private GeofenceCallback geofenceCallback;

    public static final int GEOFENCE_NOTIFICATION_ID = 0;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RealWaveTagTransitionService() {
        super(TAG);
        // this.geofenceCallback = geofenceCallback;
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        resultReceiver = intent.getParcelableExtra(LocationManager.RECEIVER);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // Retrieve the Geofencing intent
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        Bundle bundle = new Bundle();

        // Handling errors
        if (geofencingEvent.hasError()) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMsg);

            //this.geofenceCallback.onError(errorMsg);
            bundle.putString("error", errorMsg);

            resultReceiver.send(666,bundle);
            return;
        }

        // Retrieve type of GeofenceTrasition
        int geofenceTrasiontion = geofencingEvent.getGeofenceTransition();



            switch (geofenceTrasiontion) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    bundle.putString("enter", getGeofenceTransitionDetails(geofenceTrasiontion, geofencingEvent.getTriggeringGeofences()));
                    resultReceiver.send(Geofence.GEOFENCE_TRANSITION_ENTER,bundle);

                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    bundle.putString("exit", getGeofenceTransitionDetails(geofenceTrasiontion, geofencingEvent.getTriggeringGeofences()));
                    resultReceiver.send(Geofence.GEOFENCE_TRANSITION_ENTER,bundle);

                    break;
                case Geofence.GEOFENCE_TRANSITION_DWELL:
                    bundle.putString("dwell", getGeofenceTransitionDetails(geofenceTrasiontion, geofencingEvent.getTriggeringGeofences()));
                    resultReceiver.send(Geofence.GEOFENCE_TRANSITION_ENTER,bundle);

                    break;

            }


    }


    /**
     * @param geofenceTrasition
     * @param triggeringGeofences
     * @return Detaisl geofence
     */

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
