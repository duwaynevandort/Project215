package project215.project215;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import java.util.HashMap;


import com.google.android.gms.maps.model.Marker;

public abstract class OnInfoWindowElemTouchListener implements OnTouchListener {
    private final View view;
    private final Drawable bgDrawableNormal;
    private final Drawable bgDrawablePressed;
    private final Handler handler = new Handler();
    private final int sdk = android.os.Build.VERSION.SDK_INT;

    private HashMap<Marker,Drawable> mHash = new HashMap<>();
    private Marker marker;
    private Drawable currentDrawable;
    private boolean pressed = false;

    public OnInfoWindowElemTouchListener(View view, Drawable bgDrawableNormal, Drawable bgDrawablePressed) {
        //Initialize the Drawables and View
        this.view = view;
        this.bgDrawableNormal = bgDrawableNormal;
        this.currentDrawable = bgDrawableNormal;
        this.bgDrawablePressed = bgDrawablePressed;

        //API background handling
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
              view.setBackgroundDrawable(bgDrawableNormal);
        } else {
              view.setBackground(bgDrawableNormal);
        }
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
        //currentDrawable = mHash.get(marker);
        if (mHash.get(marker) != null)
        {
            //get Current State of Marker to redraw button image
            currentDrawable = mHash.get(marker);
        } else
        {
            // Initialize Listener's currentDrawable in Hash
            currentDrawable = bgDrawableNormal;
            mHash.put(marker, currentDrawable);
        }
        //Check SDK version and use correct Background method to set Button Image
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(currentDrawable);
        } else {
            view.setBackground(currentDrawable);
        }
    }

    @Override
    public boolean onTouch(View vv, MotionEvent event) {
        if (0 <= event.getX() && event.getX() <= view.getWidth() &&
                0 <= event.getY() && event.getY() <= view.getHeight())
        {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: toggleButton(); break;

                // We need to delay releasing of the view a little so it shows the pressed state on the screen
                case MotionEvent.ACTION_UP: handler.postDelayed(confirmClickRunnable, 150); break;

                case MotionEvent.ACTION_CANCEL: break;//endPress(); break;
                default: break;
            }
        }
        else {
            // If the touch goes outside of the view's area
            // (like when moving finger out of the pressed button)
            // just release the press
            endPress();
        }
        return false;
    }

    private void toggleButton() {
        if (!pressed) {
            pressed = true;
            handler.removeCallbacks(confirmClickRunnable);
            //Toggle Drawable to other state
            currentDrawable = (currentDrawable == bgDrawableNormal) ? bgDrawablePressed : bgDrawableNormal;

            //Check SDK version and use correct Background method
            int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackgroundDrawable(currentDrawable);
            } else {
                view.setBackground(currentDrawable);
            }

            //Store current state of Drawable
            mHash.put(marker, currentDrawable);
            pressed = false;
            if (marker != null)
                marker.showInfoWindow();
        }
    }

    private boolean endPress() {
        if (pressed) {
            this.pressed = false;
            handler.removeCallbacks(confirmClickRunnable);
            // Check version and change background image to Pressed Button
            currentDrawable = (currentDrawable == bgDrawableNormal) ? bgDrawablePressed : bgDrawableNormal;

            //Check SDK version and use correct Background method
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                  view.setBackgroundDrawable(currentDrawable);
            } else {
                  view.setBackground(currentDrawable);
            }

            //Store Current state of Button
            mHash.put(marker, currentDrawable);
            if (marker != null)
                marker.showInfoWindow();
            return true;
        }
        else
            return false;
    }

    private final Runnable confirmClickRunnable = new Runnable() {
        //Do thing after press
        public void run() {
            if (endPress()) {
                onClickConfirmed(view, marker);
            }
        }
    };

    /**
     * This is called after a successful click
     */
    protected abstract void onClickConfirmed(View v, Marker marker);
}