package project215.project215;

import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PinModel extends Model
{
    static final int SUCCESS_CODE = 200;

    //deprecated
    public static Pin getPinByID(int id)
    {
        // TODO: 11/15/2015
        return new Pin(0, 0, 0, 0, "foo", "bar");
    }

    public static List<Pin> getPinByBounds(double lat1, double lng1, double lat2, double lng2)
    {

        JSONObject json = new JSONObject();
        URL url;
        HttpURLConnection urlConnection;
        DataOutputStream printout;
        JSONParser parser = new JSONParser();

        List<Pin> pins = new ArrayList<Pin>();

        try {
            String http = SERVER_URL + "/pins/getpins" +
                    "/" + lat1 +
                    "/" + lng1 +
                    "/" + lat2 +
                    "/" + lng2;

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
                Object obj = parser.parse(s);

                //put in in an array
                JSONArray jPinArray = (JSONArray) obj;
                //System.out.print(array.get(0));

                //put 0 element into json object, can now search by key
                //JSONObject test = (JSONObject)array.get(0);
                //System.out.print("Test" + test.get("UserID"));

                for (Object p : jPinArray)
                {
                    JSONObject jPin = (JSONObject) p;
                    Log.i("PinModel", jPin.toString());
                    pins.add(new Pin(jPin));
                }

            }catch (ParseException e){
                e.printStackTrace();
                return null;
            }

            in.close();

            //print result
            System.out.print("\nResponse code: " + urlConnection.getResponseCode());
            urlConnection.disconnect();

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }



        // TODO: 11/16/2015 test this
        return pins;

    }

    public static boolean createPinRecord(double lat, double lng, String cat, String desc, int uID)
    {
        // TODO: 11/15/2015 todo

        boolean success = false;

        JSONObject json = new JSONObject();
        URL url;
        HttpURLConnection urlConnection;
        DataOutputStream printout;

        try{
            //url for register
            String http = SERVER_URL + "/pins/addpin/";

            //make url object
            url = new URL (http);
            //open the connection, set methods and stuff
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");

            //Creating the JSON object, it's basically a hash map
            //json.put(KEY, VALUE)
            json.put("Longitude", lng);
            json.put("Latitude", lat);
            json.put("Category", cat);
            json.put("Description", desc);
            json.put("UserID", uID);

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