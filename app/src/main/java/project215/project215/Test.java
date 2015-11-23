package project215.project215;

import project215.project215.MapWrapperLayout;
import project215.project215.OnInfoWindowElemTouchListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Test extends Activity {
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button HereButton, GoneButton, ReportButton;
    private OnInfoWindowElemTouchListener HereButtonListener, GoneButtonListener, ReportButtonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
        final GoogleMap map = mapFragment.getMap();

        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));

        // We DO NOT want to reuse the info window for all the markers,
        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.infowindow, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
        this.HereButton = (Button)infoWindow.findViewById(R.id.Here);
        this.GoneButton = (Button)infoWindow.findViewById(R.id.Gone);
        this.ReportButton = (Button)infoWindow.findViewById(R.id.Report);


        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        this.HereButtonListener = new OnInfoWindowElemTouchListener(HereButton,
                ContextCompat.getDrawable(this, R.drawable.basic_here_button),
                ContextCompat.getDrawable(this, R.drawable.red_button))
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                Toast.makeText(Test.this, marker.getTitle() + "'s Here button clicked!", Toast.LENGTH_SHORT).show();
            }
        };
        this.HereButton.setOnTouchListener(HereButtonListener);

        this.GoneButtonListener = new OnInfoWindowElemTouchListener(GoneButton,
                ContextCompat.getDrawable(this, R.drawable.basic_gone_button),
                ContextCompat.getDrawable(this, R.drawable.red_button))
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                Toast.makeText(Test.this, marker.getTitle() + "'s Gone button clicked!", Toast.LENGTH_SHORT).show();
            }
        };
        this.GoneButton.setOnTouchListener(GoneButtonListener);

        this.ReportButtonListener = new OnInfoWindowElemTouchListener(ReportButton,
                ContextCompat.getDrawable(this, R.drawable.basic_report_button),
                ContextCompat.getDrawable(this, R.drawable.red_button))
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                Toast.makeText(Test.this, marker.getTitle() + "'s Report button clicked!", Toast.LENGTH_SHORT).show();
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
                HereButtonListener.setMarker(marker);
                GoneButtonListener.setMarker(marker);
                ReportButtonListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        // Let's add a couple of markers

        map.addMarker(new MarkerOptions()
                .title("Party in Prague")
                .snippet("Bitches be trippin'")
                .position(new LatLng(50.08, 14.43)));

        map.addMarker(new MarkerOptions()
                .title("Paris")
                .snippet("France")
                .position(new LatLng(48.86,2.33)));

        map.addMarker(new MarkerOptions()
                .title("London")
                .snippet("United Kingdom")
                .position(new LatLng(51.51,-0.1)));
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

}