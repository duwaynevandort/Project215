package project215.project215;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;

/**
 * Created by Adam on 11/15/2015.
 * As specified in the diagram
 */

public class UserModel
{
    final static int SUCCESS_CODE = 200;

    public static boolean createUser(String username, String password)
    {
        boolean success = false;

        JSONObject json = new JSONObject();
        URL url;
        HttpURLConnection urlConnection;
        DataOutputStream printout;

        try{
            //url for register
            String http = "http://162.243.52.70:7000/user/register/";

            //make url object
            url = new URL (http);
            //open the connection, set methods and stuff
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");

            //Creating the JSON object, it's basically a hash map
            //json.put(KEY, VALUE)
            json.put("Username", username);
            json.put("Password", password);

            printout = new DataOutputStream(urlConnection.getOutputStream ());

            String str = json.toString();
            byte[] data=str.getBytes("UTF-8");
            printout.write(data);
            printout.flush ();
            printout.close ();

            if(urlConnection.getResponseCode() == SUCCESS_CODE)
                success = true;

            System.out.print("Json String:   " + json.toString());
            System.out.print("\nResponse code: " + urlConnection.getResponseCode());

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

    public static boolean checkUser(String username, String passwordHash)
    {
        boolean success = false;

        JSONObject json = new JSONObject();
        URL url;
        HttpURLConnection urlConnection;
        DataOutputStream printout;

        try{
            //url for login
            String http = "http://162.243.52.70:7000/user/login/";

            //make url object
            url = new URL (http);
            //open the connection, set methods and stuff
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");

            //Creating the JSON object, it's basically a hash map
            //json.put(KEY, VALUE)
            json.put("Username", username);
            json.put("Password", passwordHash);

            printout = new DataOutputStream(urlConnection.getOutputStream ());

            String str = json.toString();
            byte[] data=str.getBytes("UTF-8");
            printout.write(data);
            printout.flush ();
            printout.close ();

            if(urlConnection.getResponseCode() == SUCCESS_CODE)
                success = true;

            System.out.print("Json String:   " + json.toString());
            System.out.print("\nResponse code: " + urlConnection.getResponseCode());

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
