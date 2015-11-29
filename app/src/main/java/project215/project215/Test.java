package project215.project215;

import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.List;

public class Test extends FragmentActivity implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener
{
    private ViewGroup infoWindow;
    private TextView infoTitle, infoSnippet, infoScore;
    private Button HereButton, GoneButton, ReportButton;
    private OnInfoWindowElemTouchListener HereButtonListener, GoneButtonListener, ReportButtonListener;
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    public static final String TAG = Test.class.getSimpleName();
    private GoogleMap map;
    private HashMap<Marker, Integer> markerMap = new HashMap<Marker, Integer>();

    //Super Controller initialization
    final SuperController sControl = new SuperController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
        final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(20 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(10 * 1000); // 1 second, in milliseconds


        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));

        // We DO NOT want to reuse the info window for all the markers,
        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.infowindow, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
        this.infoScore = (TextView)infoWindow.findViewById(R.id.score);
        this.HereButton = (Button)infoWindow.findViewById(R.id.Here);
        this.GoneButton = (Button)infoWindow.findViewById(R.id.Gone);
        this.ReportButton = (Button)infoWindow.findViewById(R.id.Report);


        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        this.HereButtonListener = new OnInfoWindowElemTouchListener(HereButton,
                ContextCompat.getDrawable(this, R.drawable.new_here),
                ContextCompat.getDrawable(this, R.drawable.basic_gone_button))
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                if(this.getPressed()) {
                    sControl.setVote(markerMap.get(marker),true);
                    Toast.makeText(Test.this, "Vote Registered!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean onTouch(View vv, MotionEvent event) {
                if(GoneButtonListener.getPressed())
                    GoneButtonListener.onTouch(vv, event);
                return super.onTouch(vv, event);
            }
        };
        this.HereButton.setOnTouchListener(HereButtonListener);

        this.GoneButtonListener = new OnInfoWindowElemTouchListener(GoneButton,
                ContextCompat.getDrawable(this, R.drawable.new_gone),
                ContextCompat.getDrawable(this, R.drawable.basic_gone_button))
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                if(this.getPressed()) {
                  sControl.setVote(markerMap.get(marker),false);
                  Toast.makeText(Test.this, "Vote Registered!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean onTouch(View vv, MotionEvent event) {
                if(HereButtonListener.getPressed())
                    HereButtonListener.onTouch(vv, event);
                return super.onTouch(vv, event);
            }
        };
        this.GoneButton.setOnTouchListener(GoneButtonListener);

        this.ReportButtonListener = new OnInfoWindowElemTouchListener(ReportButton,
                ContextCompat.getDrawable(this, R.drawable.new_report),
                ContextCompat.getDrawable(this, R.drawable.basic_report_button))
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                //Report the marker and remove it
                sControl.setReport(markerMap.get(marker));
                Toast.makeText(Test.this, "Report Registered!", Toast.LENGTH_SHORT).show();
                marker.remove();
            }
        };
        this.ReportButton.setOnTouchListener(ReportButtonListener);


        map.setInfoWindowAdapter(new InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                infoTitle.setText(marker.getTitle());
                infoSnippet.setText(marker.getSnippet());
                // Place holder for the Pin's Score
                infoScore.setText("points");
                HereButtonListener.setMarker(marker);
                GoneButtonListener.setMarker(marker);
                ReportButtonListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Get User Location
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.i(TAG, "location connected");
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            Log.i(TAG, "Attempting to handle new Location");
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            // Disconnect the API client and stop location updates when paused
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    // Get Pins and Render
    private void handleNewLocation(Location location) {
        Log.i(TAG, location.toString());
        Log.i(TAG, "handling new location");

        //turn location into a latlng
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        Log.i("current latitude",""+currentLatitude);
        Log.i("current longitude",""+currentLongitude);

        LatLng userLatLng = new LatLng(currentLatitude, currentLongitude);

        try {
            //Get Pins within Bounds
            List<Pin> sPins = sControl.getPinList(currentLatitude-0.1, currentLongitude-0.1, currentLatitude+0.1, currentLongitude+0.1);

            //Display the Pins
            for (Pin sPin : sPins) {
                MarkerOptions m = new MarkerOptions();
                BitmapDescriptor pinCategory;
                switch(sPin.getCategory()) {
                    case "Events": pinCategory = BitmapDescriptorFactory.defaultMarker(); break;
                    case "Wait Time": pinCategory = BitmapDescriptorFactory.fromResource(R.drawable.wait_time); break;
                    case "Parking": pinCategory = BitmapDescriptorFactory.fromResource(R.drawable.parking); break;
                    case "Free Stuff": pinCategory = BitmapDescriptorFactory.fromResource(R.drawable.free_stuff); break;
                    case "Study": pinCategory = BitmapDescriptorFactory.defaultMarker(); break;
                    case "Construction": pinCategory = BitmapDescriptorFactory.fromResource(R.drawable.new_construction); break;
                    case "Class": pinCategory = BitmapDescriptorFactory.fromResource(R.drawable.class_pin); break;
                    default: pinCategory = BitmapDescriptorFactory.defaultMarker(); break;
                }
                markerMap.put(map.addMarker(new MarkerOptions()
                        .title(sPin.getCategory())
                        .icon(pinCategory)
                        .snippet(sPin.getDescription())
                        .position(new LatLng(sPin.getLatitude(), sPin.getLongitude()))), sPin.getPinID());
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,(float)17.0));
        } catch(Exception e) {
            Log.i("pins", e.toString());
        }
    }

        @Override
        public void onLocationChanged(Location location) {
            handleNewLocation(location);
        }
    }