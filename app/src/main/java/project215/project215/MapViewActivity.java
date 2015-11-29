package project215.project215;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

/*
* Browndon
*
* */

//import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wearable.internal.ChannelSendFileResponse;



public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {

    //Coords for Max Pin Addition Bounds
    private static final LatLng NBound = new LatLng(30.76479, -95.535754);
    private static final LatLng SBound = new LatLng(30.66975, -95.61650);
    private static final LatLngBounds MAXBounds = new LatLngBounds(SBound, NBound);
    private LatLng lastCenter = new LatLng(30.7233, -95.5510);
    private GoogleMap mMap;
    public Handler mHandler;
    LatLng SHSU = new LatLng(30.7139453, -95.550492);

    //Buttons on Map View
    Button NewPinButton;
    ImageButton RefreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        NewPinButton = (Button)findViewById(R.id.NewPinButton);
        NewPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Goto Pin Creation View
                Intent newPin = new Intent(MapViewActivity.this, project215.project215.PinCreator.class);
                startActivity(newPin);
            }
        });

        RefreshButton = (ImageButton) findViewById(R.id.RefreshButton);
        RefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Refresh MapView
                Intent refresh = new Intent(getApplication(), MapViewActivity.class);
                startActivity(refresh);
            }

        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(SHSU).title("Test Event Happening Here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SHSU));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    public void restrictMapBounds() {

        LatLng tempCenter = mMap.getCameraPosition().target;
        LatLngBounds visibleBounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        if (!MAXBounds.contains(visibleBounds.northeast) || !MAXBounds.contains(visibleBounds.southwest)) {

            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastCenter));

        } else
            lastCenter = tempCenter;

    }




    private void setUpHandler(){
        mHandler = new Handler(){
            public void handleMessage(Message msg){
                restrictMapBounds();
                sendEmptyMessageDelayed(0, 5);
            }
        };
    }
}
