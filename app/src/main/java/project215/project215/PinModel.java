package project215.project215;

/**
 * Created by Adam on 11/15/2015.
 *
 * As specified in the diagram, except the get methods are "getPin" rather than "getPinRecord"
 */
public class PinModel
{
    public static Pin getPinByID(int id)
    {
        // TODO: 11/15/2015
        return new Pin(0, 0, 0, 0, 0, "foo", "bar");
    }

    public static Pin[] getPinByBounds(double lat1, double lng1, double lat2, double lng2)
    {
        // TODO: 11/15/2015
        Pin[] pins = new Pin[1];
        return pins;
    }

    public static boolean createPinRecord(double lat, double lng, String cat, String desc, int uID)
    {
        // TODO: 11/15/2015
        return false;
    }
}
