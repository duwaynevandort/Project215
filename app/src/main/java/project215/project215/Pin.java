package project215.project215;

/**
 * Created by Adam on 11/15/2015.
 * Just holds Pin data
 */
public class Pin
{
    private int PinID;
    private int UserID; //of the user that created the Pin
    private double latitude; //where the Pin was posted from
    private double longitude; //where the Pin was posted from
    private int timeCreated; //in Unix time
    private String category;
    private String description;
    private String state;

    public Pin(int pinID, int userID, double latitude, double longitude, int timeCreated, String category, String description) {
        PinID = pinID;
        UserID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeCreated = timeCreated;
        this.category = category;
        this.description = description;
    }

    public int getPinID() {
        return PinID;
    }

    public void setPinID(int pinID) {
        PinID = pinID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(int timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserID() {
        return UserID;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String PinState)
    {
        state = PinState;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
}
