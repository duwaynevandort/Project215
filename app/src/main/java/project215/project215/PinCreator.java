package project215.project215;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/*
 * Author: Kenny
 * Manipulates the pin creation layout
 *      and takes in the category and description
 *      from the user throught the Pin Creation Screen
 *      and determines their current location
 *
 *      all input is then sent through the SuperController,
 *      to the PinModel, and then to the server
 */

//TODO test if GoogleApiClient implement statements are needed

public class PinCreator extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    private Spinner categorySpinner;
    private EditText descriptionField;

    private String categorySelected;
    private double myLatitude;
    private double myLongitude;
    private final String TAG = "PinCreator Log";

    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationManager;
    private String provider;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_create_screen);

        descriptionField = (EditText) findViewById(R.id.pin_description_text);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        //My attempt at getting location
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = mLocationManager.getBestProvider(criteria, false);
        Location myLocation = mLocationManager.getLastKnownLocation(provider);

        if (myLocation != null)
        {
            Log.d(TAG, "Provider " + provider + " has been selected.");
            onLocationChanged(myLocation);
        }
        //End get location

        fillCategorySpinner();
        addListenerToCategorySpinner();
    }

    @Override
    public void onLocationChanged(Location location) {
        myLatitude = (location.getLatitude());
        myLongitude = (location.getLongitude());
    }

    //Populate drop down box via an ArrayAdapter filled
    //      with categories listed in categories.xml
    public void fillCategorySpinner()
    {
        categorySpinner = (Spinner)
                findViewById(R.id.category_spinner);

        ArrayAdapter<CharSequence> categorySpinnerAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.categories,
                        android.R.layout.simple_spinner_item);

        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        categorySpinner.setAdapter(categorySpinnerAdapter);
    }

    //Listens for a category selection from the user
    public void addListenerToCategorySpinner()
    {
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(TAG, "Location connected");
        if (location == null)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else
        {
            Log.d(TAG, "Attempting to handle new Location");
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if (connectionResult.hasResolution())
        {
            try
            {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Log.d(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.d(TAG, "Location services suspended. Please reconnect.");
    }

    public void submitPinCreation(View view)
    {
        String descriptionText = descriptionField.getText().toString();
        Toast.makeText(PinCreator.this, "Submitting pin!", Toast.LENGTH_SHORT).show();

        //TODO test toasts for success and failure
        if ( SuperController.createPin(myLatitude, myLongitude, categorySelected, descriptionText) )
        {
            Toast.makeText(PinCreator.this, "Pin created successfully!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(PinCreator.this, "Failed to submit pin!", Toast.LENGTH_SHORT).show();
        }
        this.finish();
    }

    public void cancelPinCreation(View view)
    {
        Toast.makeText(PinCreator.this, "Pin creation cancelled!", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
