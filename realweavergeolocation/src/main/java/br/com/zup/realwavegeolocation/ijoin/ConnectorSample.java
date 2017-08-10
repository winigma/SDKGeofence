package br.com.zup.realwavegeolocation.ijoin;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.zup.realwavegeolocation.ijoin.broadcast.MyResultReceive;
import br.com.zup.realwavegeolocation.ijoin.interfaces.ITagServiceIntegration;
import br.com.zup.realwavegeolocation.model.TagBO;
import br.com.zup.realwavegeolocation.model.enums.ERWGeoResponses;
import br.com.zup.realwavegeolocation.service.RealWaveTagTransitionService;

/**
 * Created by wisle on 10/08/2017.
 */

public class ConnectorSample implements  OnCompleteListener<Void> {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    /**
     * GeoFence Params
     */

    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters
    public final static  String RECEIVER= "receiver";

    private MyResultReceive myResultReceive;
    GoogleApiClient googleApiClient;


    public void notifySuccessListener(List<TagBO> list) {
        if (list != null && !list.isEmpty()) {
           mGeofenceList = buildGeofenceList(list);
           addGeofences();

        } else {
            Log.i("ConnectorSampleErro", "TAGs Point is nul++++++++++++++++++++++++l)");
        }
    }



    @Override
    public void onComplete(@NonNull Task<Void> task) {

    }

    /**
     * Tracks whether the user requested to add or remove geofences, or to do neither.
     */
    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }


    /**
     * Provides access to the Geofencing API.
     */

    private GeofencingClient mGeofencingClient;

    /**
     * The list of geofences used in this sample.
     */
    private ArrayList<Geofence> mGeofenceList;

    private Context mContext;

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;
    private GeofenceCallback mGeofenceCallbackListener;

    public ConnectorSample(GeofenceCallback mGeofenceCallbackListener, final Context context, GeofencingClient googleApiClient) {
        this.mGeofenceCallbackListener = mGeofenceCallbackListener;
        this.mContext = context;
        this.mGeofencingClient = googleApiClient;
        initialize();
    }

    public void initialize() {

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<>();

        // Initially set the PendingIntent used in addGeofences() and removeGeofences() to null.
        mGeofencePendingIntent = null;
        this.myResultReceive =  new MyResultReceive(null, this.mGeofenceCallbackListener);
       // mGeofencingClient = LocationServices.getGeofencingClient(mContext);

    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT | GeofencingRequest.INITIAL_TRIGGER_DWELL);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    /**
     * Adds geofences. This method should be called after the user has granted the location
     * permission.
     */
    @SuppressWarnings("MissingPermission")
    private void addGeofences() {
        /*if (!checkPermissions()) {
            showSnackbar(getString(R.string.insufficient_permissions));
            return;
        }*/
        /*LocationServices.GeofencingApi.addGeofences(
                googleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent());*/
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(mContext, RealWaveTagTransitionService.class);
        intent.putExtra(RECEIVER, myResultReceive);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private ArrayList<Geofence> buildGeofenceList(final List<TagBO> list){
        ArrayList<Geofence> mGeofences = new ArrayList<>();
        for (TagBO bo:list) {
            mGeofences.add(createGeofence(bo, GEOFENCE_RADIUS));
        }

        return mGeofences;
    }

    /**
     * Create Geofence object
     * @param poi
     * @param radius
     * @return Geofence Object
     */
    private final Geofence createGeofence(final TagBO poi, final float radius) {
        Log.d("creategeofence", "creategeofence");
        return new Geofence.Builder().setRequestId(poi.getName()).
                setCircularRegion(poi.getLatitude(), poi.getLongitude(), radius).
                setExpirationDuration(60 * 60 * 1000).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL).setLoiteringDelay(2000).
                build();

    }


}
