package br.com.zup.realwavegeolocation.ijoin;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import br.com.zup.realwavegeolocation.connectorcallback.TagTaskConnectorCallback;
import br.com.zup.realwavegeolocation.connectorcallback.interfaces.ITagConnectorCallback;
import br.com.zup.realwavegeolocation.ijoin.broadcast.MyResultReceive;
import br.com.zup.realwavegeolocation.ijoin.interfaces.ITagServiceIntegration;
import br.com.zup.realwavegeolocation.model.Tag;
import br.com.zup.realwavegeolocation.model.TagBO;
import br.com.zup.realwavegeolocation.model.enums.ERWGeoResponses;
import br.com.zup.realwavegeolocation.service.RealWaveTagTransitionService;

/**
 * Created by wisle on 24/07/2017.
 */

public class LocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, IProviderLocationSetting,
        ICallBackServiceTag, ITagServiceIntegration {

    private final String TAG = "LocationManager";
    public final static  String RECEIVER= "receiver";


    private GoogleApiClient mLocationClient;
    private final LocationListener mLocationListener;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private final Context mContext;

    private int mPriorityAccurrancy;


    /**
     * GeoFence Params
     */

    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters

    private GeofencingClient mGeofencingClient;


    TagTaskConnectorCallback mTagTaskConnectorCallback;

    private GeofenceCallback mGeofenceCallbackListener;



    private MyResultReceive myResultReceive;


    public LocationManager(final Context context, final LocationListener listener, final int accurancy,final GeofenceCallback mGeofenceCallbackListener) {
        this.mContext = context;
        this.mLocationListener = listener;
        this.mPriorityAccurrancy = accurancy;
        this.mGeofenceCallbackListener =  mGeofenceCallbackListener;

        initialize();
    }

    private void initialize() {
        this.mLocationClient = new GoogleApiClient.Builder(this.mContext)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .build();
        this.mGeofencingClient = LocationServices.getGeofencingClient(mContext);
        this.myResultReceive =  new MyResultReceive(null, this.mGeofenceCallbackListener);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(this.mPriorityAccurrancy);
        mLocationRequest.setNumUpdates(1);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mLocationClient, mLocationRequest, mLocationListener);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        if (mLocationClient != null && mLocationClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            } else {
                mLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mLocationClient);
            }

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Check whether gps provider is enable for location services.
     *
     * @return true if the gps provider is available or false if not.
     */
    @Override
    public boolean isGpsEnabled() {
        final android.location.LocationManager locationManager = (android.location.LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }


    /**
     * Check whether network provider is enable for location services.
     *
     * @return true if the network provider is available or false if not.
     */
    @Override
    public boolean isNetworkEnabled() {
        final android.location.LocationManager locationManager = (android.location.LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager
                .isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
    }

    public void stopSelf() {
        if (mLocationClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mLocationClient, mLocationListener);
            mLocationClient.disconnect();
        }
    }

    /**
     * Retrieves the last registered location.
     *
     * @return the Location or null if location configuration is disabled.
     */
    public Location getLocation() {
        return mLocation;
    }

    /**
     * Connect on location client
     */
    public void connect() {
        if (mLocationClient != null) {
            mLocationClient.connect();
        }
    }

    public boolean isConnected() {
        return mLocationClient.isConnected();
    }

    /**
     * Force update location
     */
    public void updateLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mLocationClient, mLocationRequest, mLocationListener);
    }


    /**
     * Create Geofence object
     * @param poi
     * @param radius
     * @return Geofence Object
     */
    private final Geofence createGeofence(final TagBO poi, final float radius) {
        Log.d(TAG, "creategeofence");
        return new Geofence.Builder().setRequestId(poi.getName()).
                setCircularRegion(poi.getLatitude(), poi.getLongitude(), radius).
                setExpirationDuration(Geofence.NEVER_EXPIRE).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                | Geofence.GEOFENCE_TRANSITION_EXIT).setLoiteringDelay(20000).
                build();

    }


    private GeofencingRequest createGeofenceRequest(final List<Geofence> geofences) {
        Log.d(TAG, "createGeofenceRequest");

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofences);


        return builder.build();

    }

    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
       /* mGeofencingClient.addGeofences(request, createGeofencePendingIntent()).addOnSuccessListener((Activity) mContext, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });*/

        geoFencePendingIntent =  createGeofencePendingIntent();
        LocationServices.GeofencingApi.addGeofences(
                mLocationClient,
                request,
                createGeofencePendingIntent()
        );/*.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });*/

    }

    private List<Geofence> buildGeofenceList(final List<TagBO> list){
        List<Geofence> mGeofences = new ArrayList<>();
        for (TagBO bo:list) {
           mGeofences.add(createGeofence(bo, GEOFENCE_RADIUS));
        }

        return mGeofences;
    }

    private PendingIntent geoFencePendingIntent;

    private PendingIntent createGeofencePendingIntent(){
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( mContext, RealWaveTagTransitionService.class);
        intent.putExtra(RECEIVER, myResultReceive);
        return PendingIntent.getService(
                mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }


    /**
     *
     * @param mLocation
     */
    @Override
    public void getService(final Location mLocation) {
        mTagTaskConnectorCallback = new TagTaskConnectorCallback(this);
        mTagTaskConnectorCallback.notifyStartPresenter(mLocation);
    }


    @Override
    public void notifyStartListener() {
        Log.i("Start Conection","Iniciou***********************************************************");
    }

    @Override
    public void notifySuccessListener(List<TagBO> list) {
        Log.i(TAG, "startGeofence()");

        if(list != null && !list.isEmpty()){
            List<Geofence> mGeofences = new ArrayList<>();
            mGeofences = buildGeofenceList(list);
            GeofencingRequest geofencingRequest = createGeofenceRequest(mGeofences);
            addGeofence(geofencingRequest);

        }else {
            Log.i(TAG, "TAGs Point is null)");
        }


    }

    @Override
    public void notifyFailureListener(ERWGeoResponses response) {
        Log.e("Error Conection",response.toString());

    }
}
