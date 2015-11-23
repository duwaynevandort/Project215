package project215.project215;

import android.app.Application;
import android.util.Log;

import java.util.List;

public class SuperController extends Application
{
    public static int userID = 1337;

    private static PinModel pModel = new PinModel();
    private static ServerLogger logger;
    private static final String TAG = "Controller Log";

    /**************************************PIN CONTROLLER****************************************/

    public static void createPin(double latitude, double longitude, String category, String description)
    {
        if ( pModel.createPinRecord(latitude, longitude, category, description, userID) )
        {
            Log.d(TAG, "Pin created successfully");
        }
        else
        {
            Log.d(TAG, "Pin creation failed");
        }
    }

    public void setVote(int pinID, boolean isHere)
    {
        if (VoteModel.createVoteRecord(userID, pinID, isHere))
        {
            Log.d(TAG, "Vote logged successfully");
        }
        else
        {
            Log.d(TAG, "Failed to log vote");
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

    public List<Pin> getPins(double latitude1, double longitude1, double latitude2, double longitude2)
    {
        return PinModel.getPinByBounds(latitude1, longitude1, latitude2, longitude2);
    }
}
