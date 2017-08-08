package br.com.zup.realwavegeolocation.ijoin;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


/**
 * Created by wisle on 24/07/2017.
 */

public class RWGeofenceController implements IControllerGeoLocation, LocationListener {


    private final LocationManager mLocationManager;
    private final Context mContext;
    private Location mLocation;
    private static RWGeofenceController mInstance;

    private GeofenceCallback mGeofenceCallbackListener;




    private RWGeofenceController(final Context context, final GeofenceCallback mGeofenceCallbackListener) {
        mContext = context;
        this.mLocationManager = new LocationManager(mContext, this, LocationRequest.PRIORITY_HIGH_ACCURACY,mGeofenceCallbackListener);
    }

    @Override
    public void onLocationChanged(Location location) {

        this.mLocationManager.getService(location);

    }


    @Override
    public boolean isRequiredLocalizationSettingsEnabled() {
        if (mLocationManager.isGpsEnabled()
                || mLocationManager.isNetworkEnabled()) {
            return true;
        }
        return false;
    }

    @Override
    public void connectLocationService() {
        if (mLocationManager != null) {
            if (mLocationManager.isConnected()) {
                mLocationManager.updateLocation();
            } else {
                mLocationManager.connect();
                mLocation = mLocationManager.getLocation();
            }
        }
    }

    /**
     * Create instance of
     * @param mGeofenceCallbackListener
     */
    public static void create(final GeofenceCallback mGeofenceCallbackListener, final Context context) {
        mInstance = new RWGeofenceController(context,mGeofenceCallbackListener);
        mInstance.connectLocationService();
        mInstance.setmGeofenceCallbackListener(mGeofenceCallbackListener);

    }

    public void setmGeofenceCallbackListener(GeofenceCallback mGeofenceCallbackListener) {
        this.mGeofenceCallbackListener = mGeofenceCallbackListener;
    }
}
