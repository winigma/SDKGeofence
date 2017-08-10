package br.com.zup.realwavegeolocation.ijoin;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.util.List;

import br.com.zup.realwavegeolocation.connectorcallback.TagTaskConnectorCallback;
import br.com.zup.realwavegeolocation.ijoin.interfaces.ITagServiceIntegration;
import br.com.zup.realwavegeolocation.model.TagBO;
import br.com.zup.realwavegeolocation.model.enums.ERWGeoResponses;


/**
 * Created by wisle on 24/07/2017.
 */

public class RWGeofenceController implements IControllerGeoLocation, LocationListener, ITagServiceIntegration {


    //private final LocationManager mLocationManager;
    private final Context mContext;
    private Location mLocation;
    private static RWGeofenceController mInstance;

    private GeofenceCallback mGeofenceCallbackListener;

    boolean isFirst= false;

    private ConnectorSample mConnectorSample;

    TagTaskConnectorCallback mTagTaskConnectorCallback;

    private GeofencingClient mAPIClient;



    private RWGeofenceController(final Context context, final GeofenceCallback mGeofenceCallbackListener,
                                 final Location mLocation, GeofencingClient googleApiClient) {
        mContext = context;
        //this.mLocationManager = new LocationManager(mContext, this, LocationRequest.PRIORITY_HIGH_ACCURACY,mGeofenceCallbackListener);
        //connectLocationService();
        setmGeofenceCallbackListener(mGeofenceCallbackListener);
        this.mLocation = mLocation;
        this.mAPIClient = googleApiClient;
        initiService();


    }

    @Override
    public void onLocationChanged(Location location) {

        if(!isFirst){
            //this.mLocationManager.getService(location);
           // this.mLocationManager.setmLastLocation(location);
            isFirst= true;

        }

    }


    @Override
    public boolean isRequiredLocalizationSettingsEnabled() {
       /* if (mLocationManager.isGpsEnabled()
                || mLocationManager.isNetworkEnabled()) {
            return true;
        }*/
        return false;
    }

    @Override
    public void connectLocationService() {
       /* if (mLocationManager != null) {
            if (mLocationManager.isConnected()) {
                mLocationManager.updateLocation();
            } else {
                mLocationManager.connect();
                mLocation = mLocationManager.getLocation();
            }
        }*/
    }

    /**
     * Create instance of
     * @param mGeofenceCallbackListener
     */
    public static void create(final GeofenceCallback mGeofenceCallbackListener, final Context context, final Location mLocation,  GeofencingClient googleApiClient) {
        mInstance = new RWGeofenceController(context,mGeofenceCallbackListener,mLocation, googleApiClient);
        mInstance.connectLocationService();

    }

    public void setmGeofenceCallbackListener(GeofenceCallback mGeofenceCallbackListener) {
        this.mGeofenceCallbackListener = mGeofenceCallbackListener;
    }

    @Override
    public void notifyStartListener() {

    }

    @Override
    public void notifySuccessListener(List<TagBO> list) {

        mConnectorSample = new ConnectorSample(mGeofenceCallbackListener,mContext,mAPIClient);
        mConnectorSample.notifySuccessListener(list);

    }

    @Override
    public void notifyFailureListener(ERWGeoResponses response) {

    }

    private void initiService(){
        mTagTaskConnectorCallback = new TagTaskConnectorCallback(this);
        mTagTaskConnectorCallback.notifyStartPresenter(mLocation);
    }
}
