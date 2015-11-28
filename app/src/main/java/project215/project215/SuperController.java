package project215.project215;

import android.app.Application;
import android.util.Log;

import java.util.List;

public class SuperController extends Application
{
    public static int userID = 47;

    //private static ServerLogger logger;
    private static final String TAG = "Controller Log";
    private List<Pin> pinList;

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

    public void setReport(final int pinID)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
        if ( ReportModel.createReportRecord(userID, pinID) )
        {
            Log.d(TAG, "Report logged successfully");
        }
        else
        {
            Log.d(TAG, "Failed to log report");
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

    public List<Pin> getPinList(final double latitude1, final double longitude1, final double latitude2, final double longitude2){
        // Replace the choords with around the user? Otherwise, who cares.
        // Fuck it, lets just get them all. No reason not to.
        this.getPins(-1000, -1000, 1000, 1000);
        return pinList;
    }

    private void getPins(final double latitude1, final double longitude1, final double latitude2, final double longitude2)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    pinList = PinModel.getPinByBounds(latitude1, longitude1, latitude2, longitude2);

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


