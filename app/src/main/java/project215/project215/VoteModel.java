package project215.project215;

import org.json.simple.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Adam on 11/15/2015.
 *
 */
public class VoteModel extends Model
{
    final static int SUCCESS_CODE = 200;

    public static boolean createVoteRecord(int userID, int pinID, boolean isHere)
    {
        boolean success = false;

        JSONObject json = new JSONObject();
        URL url;
        HttpURLConnection urlConnection;
        DataOutputStream printout;

        try{
            //url for login
            String http = SERVER_URL + "/pins/vote/";

            //make url object
            url = new URL (http);
            //open the connection, set methods and stuff
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");

            //Creating the JSON object, it's basically a hash map
            //json.put(KEY, VALUE)
            json.put("UserID", userID);
            json.put("PinID", pinID);
            json.put("isHere", isHere);

            printout = new DataOutputStream(urlConnection.getOutputStream ());

            String str = json.toString();
            byte[] data=str.getBytes("UTF-8");
            printout.write(data);
            printout.flush ();
            printout.close ();

            if(urlConnection.getResponseCode() == SUCCESS_CODE)
                success = true;

            System.out.println("Json String:   " + json.toString());
            System.out.println("\nResponse code: " + urlConnection.getResponseCode());

            urlConnection.disconnect();

        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: 11/16/2015 test this
        return success;
    }
}
