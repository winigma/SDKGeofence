package geofence.sdk.sample.zup.sdkrwgeofencesample;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import br.com.zup.realwavegeolocation.ijoin.GeofenceCallback;
import br.com.zup.realwavegeolocation.ijoin.RWGeofenceController;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener, GeofenceCallback {

    private TextView textLat, textLong;
    private MapFragment mapFragment;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RWGeofenceController.create(this,this);
        initGMaps();
    }

    // Initialize GoogleMaps
    private void initGMaps(){
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("TAG", "onMapClick("+latLng +")");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
    }

    @Override
    public void onError(String args0) {
        Log.d("TAG", args0);
    }

    @Override
    public void onEnterGeofence(String args) {
        Log.d("TAG", args);
    }

    @Override
    public void onExitGeofence(String args) {
        Log.d("TAG", args);
    }

    @Override
    public void onDWellGeofence(String args) {
        Log.d("TAG", args);
    }
}
