package project215.project215;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Adam on 11/15/2015.
 * As specified in the diagram
 */

public class UserModel extends Model
{
    final static int SUCCESS_CODE = 200;

    public static int setUserID(String username)
    {
        JSONObject json = new JSONObject();
        URL url;
        HttpURLConnection urlConnection;
        DataOutputStream printout;
        JSONParser parser = new JSONParser();

        int userID = -1;

        try{
            String http = SERVER_URL + "/" + username;

            url = new URL(http);
            urlConnection = (HttpURLConnection) url.openConnection();

            // GETTING
            urlConnection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            //not sure if necessary, but response is a string buffer, so...?
            String s = response.toString();

            try{
                //parse the response
                JSONObject obj = (JSONObject) parser.parse(s);

                userID = ((Long) obj.get("UserID")).intValue();

            }catch (ParseException e){
                e.printStackTrace();
                return -1;
            }

            in.close();

            //print result
            System.out.println("\nResponse code: " + urlConnection.getResponseCode());
            urlConnection.disconnect();

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        SuperController.setUserID(userID);
        return userID;
    }

    public static boolean createUser(String username, String password)
    {
        boolean success = false;

        JSONObject json = new JSONObject();
        URL url;
        HttpURLConnection urlConnection;
        DataOutputStream printout;

        try{
            //url for register
            String http = SERVER_URL + "/user/register/";

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
            String http = SERVER_URL + "/user/login/";

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
        setUserID(username);
        return success;
    }
}
