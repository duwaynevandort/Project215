package project215.project215;

import android.app.Application;
import android.util.Log;

import java.util.List;

/*
*   Author: Kenny Cook
*
*   This class acts as a mediator between the views (activities)
*       and the models. It is accessible by any activity in the
*       application
*/

public class SuperController extends Application
{
    private static int userID = -1;

    private static boolean pinSubmitted;
    private static boolean voteSubmitted;
    private static boolean reportSubmitted;
    private static boolean userCreated;
    private static boolean validUser;
    private static List<Pin> pinList;

    private static final String TAG = "Controller Log";

    /**************************************PIN CONTROLLER****************************************/

    public static boolean createPin(final double latitude, final double longitude, final String category, final String description)
    {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    if ( PinModel.createPinRecord(latitude, longitude, category, description, userID) )
                    {
                        Log.d(TAG, "Pin created successfully");
                        pinSubmitted = true;
                    }
                    else
                    {
                        Log.d(TAG, "Pin creation failed");
                        pinSubmitted = false;
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

        return pinSubmitted;
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

    public static boolean checkUser(final String userEmail, String userPassword)
    {
        final String hashedPassword = userPassword;

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    if ( UserModel.checkUser(userEmail, hashedPassword) )
                    {
                        validUser = true;
                        Log.d(TAG, "User verification success!" + validUser);
                    }
                    else
                    {
                        validUser = false;
                        Log.d(TAG, "User verification failed!" + validUser);
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

        Log.d(TAG, "validUser = " + validUser);
        return validUser;
    }

    public static boolean createUser(final String userEmail, String userPassword)
    {
        final String hashedPassword = userPassword;

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    if (UserModel.createUser(userEmail, hashedPassword) )
                    {
                        userCreated = true;
                        Log.d(TAG, "User created successfully" + userCreated);
                    }
                    else
                    {
                        userCreated = false;
                        Log.d(TAG, "User creation failed" + userCreated);
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

        Log.d(TAG, "userCreated = " + userCreated);
        return userCreated;
    }

    /****************************************MAP CONTROLLER***************************************/

    public List<Pin> getPinList(final double latitude1, final double longitude1, final double latitude2, final double longitude2)
    {
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

    public int getUserID() { return userID; }

    public void setUserID(int UID) { userID = UID; }
}
