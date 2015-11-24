package project215.project215;

import android.app.Application;
import android.util.Log;

import java.util.List;

public class SuperController extends Application
{
    public static int userID = 47;

    //private static ServerLogger logger;
    private static final String TAG = "Controller Log";

    /**************************************PIN CONTROLLER****************************************/

    public static void createPin(double latitude, double longitude, String category, String description) throws InterruptedException {
        final double lat = latitude;
        final double lng = longitude;
        final String cat = category;
        final String desc = description;

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    if ( PinModel.createPinRecord(lat, lng, cat, desc, userID) )
                    {
                        Log.d(TAG, "Pin created successfully");
                    }
                    else
                    {
                        Log.d(TAG, "Pin creation failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setVote(final int pinID, final boolean isHere)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    if (VoteModel.createVoteRecord(userID, pinID, isHere)) {
                        Log.d(TAG, "Vote logged successfully");
                    } else {
                        Log.d(TAG, "Failed to log vote");
                    }
                } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setReport(int pinID)
    {
        if ( ReportModel.createReportRecord(userID, pinID) )
        {
            Log.d(TAG, "Report logged successfully");
        }
        else
        {
            Log.d(TAG, "Failed to log report");
        }
    }

    /**************************************USER CONTROLLER****************************************/

    //userID = return values for both?

    public boolean checkUser(String userEmail, String userPassword)
    {
        String hashedPassword = "butts";
        //Bcrypt stuff

        return UserModel.checkUser(userEmail, hashedPassword);
    }

    public boolean createUser(String userEmail, String userPassword)
    {
        String hashedPassword = "butts";
        //Bcrypt stuff

        return UserModel.createUser(userEmail, hashedPassword);
    }

    /****************************************MAP CONTROLLER***************************************/

    public List<Pin> getPins(final double latitude1, final double longitude1, final double latitude2, final double longitude2)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    return PinModel.getPinByBounds(latitude1, longitude1, latitude2, longitude2);
                } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
