package project215.project215;

import android.app.Application;

public class SuperController extends Application
{
    public int userID; //how to get? set from UserModel on checkUser()?

    /**************************************PIN CONTROLLER****************************************/

    public void createPin(double latitude, double longitude, String category, String description)
    {
        PinModel.createPinRecord(latitude, longitude, category, description, userID);
    }

    public void setVote(int pinID, boolean isHere)
    {
        VoteModel.createVoteRecord(userID, pinID, isHere);
    }

    public void setReport(int pinID)
    {
        ReportModel.createReportRecord(userID, pinID);
    }

    /**************************************USER CONTROLLER****************************************/

    public boolean checkUser(String userEmail, String userPassword)
    {
        return UserModel.checkUser(userEmail, userPassword);
    }

    public boolean createUser(String userEmail, String userPassword)
    {
        return UserModel.createUser(userEmail, userPassword);
    }

    /****************************************MAP CONTROLLER***************************************/

    public Pin[] getPins(double latitude1, double longitude1, double latitude2, double longitude2)
    {
        return PinModel.getPinByBounds(latitude1, longitude1, latitude2, longitude2);
    }
}
