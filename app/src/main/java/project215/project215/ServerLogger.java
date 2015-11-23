package project215.project215;

import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Adam on 11/22/2015.
 * Class for logging events in the client application, uploading them to the server after the
 * number of events reaches a set threshold.
 */

public class ServerLogger
{
    //TODO  add user ID to server logs

    final static int SUCCESS_CODE = 200;

    final static String FILENAME = "215.log";

    final static int UPLOAD_THRESHOLD = 10;

    //logging error and success codes
    final static int LOCAL_LOG_SUCCESS = 10;
    final static int SERVER_LOG_SUCCESS = 20;
    final static int LOCAL_LOG_FAILURE = -10;
    final static int SERVER_LOG_FAILURE = -20;

    //log levels
    final static int DEBUG = 10;
    final static int INFO = 20;
    final static int ERROR = 30;

    static int eventCount = 0;

    /**
     * Takes a short logging message and a level for that message (Model.DEBUG, Model.INFO, or
     * Model.ERROR) and stores it in a local file. Increments eventCount, then checks to see if
     * eventCount >= UPLOAD_THRESHOLD. If so, calls transferLogs(), which sends the contents of the
     * log file to the server. If this is successful, clears the client log file and resets
     * eventCount.
     *
     * @param level logging level for organization of log events; Model.DEBUG, Model.INFO, or
     *              Model.ERROR
     * @param message message to be logged
     * @return a success or error code defined in class Model
     */
    public static int log(int level, String message)
    {
        BufferedWriter output = null;

        try
        {
            File logfile = new File(FILENAME);
            output = new BufferedWriter(new FileWriter(logfile, true));
            String text = "";
            Date currentTime = new Date();

            switch(level)
            {
                case DEBUG: text = "[DEBUG]\t" + currentTime + "\t" + message;
                    break;
                case ERROR: text = "[ERROR]\t" + currentTime + "\t" + message;
                    break;
                default: text = "[INFO]\t" + currentTime + "\t" + message;
            }

            output.write(text + "\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return LOCAL_LOG_FAILURE;

        }
        finally
        {
            try
            {
                if (output != null)
                    output.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return LOCAL_LOG_FAILURE;
            }
        }

        eventCount++;

        if(eventCount >= UPLOAD_THRESHOLD)
        {
            boolean success = uploadLogs();

            if(success)
            {
                eventCount = 0;

                try
                {
                    new BufferedWriter(new FileWriter(new File(FILENAME), false));
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    return SERVER_LOG_SUCCESS;
                }
            }

            return SERVER_LOG_FAILURE;
        }

        return LOCAL_LOG_SUCCESS;
    }

    /**
     * POSTs the contents of the log file to the server.
     *
     * @return whether the upload was successful or not
     */
    public static boolean uploadLogs()
    {
        boolean success = false;

        JSONObject json = new JSONObject();
        URL url;
        HttpURLConnection urlConnection;
        DataOutputStream printout;

        try{
            //url for login
            String http = Model.SERVER_URL + "/log/";

            List<String> entries = new ArrayList<String>();

            try
            {
                Scanner scanner = new Scanner(new File("215.log"));

                while(scanner.hasNextLine())
                {
                    entries.add(scanner.nextLine());
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
                return false;
            }

            //make url object
            url = new URL (http);
            //open the connection, set methods and stuff
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");

            //Creating the JSON object, it's basically a hash map
            //json.put(KEY, VALUE)
            //json.put("UserID", userID);
            //json.put("PinID", pinID);

            for(int i = 0; i < entries.size(); i++)
            {
                System.out.println(entries.get(i));
            }

            for(int i = 0; i < entries.size(); i++)
            {
                json.put("" + i, entries.get(i));
            }

            printout = new DataOutputStream(urlConnection.getOutputStream ());

            String str = json.toString();
            byte[] data=str.getBytes("UTF-8");
            printout.write(data);
            printout.flush ();
            printout.close ();

            if(urlConnection.getResponseCode() == SUCCESS_CODE)
                success = true;

            System.out.println("Json String:   " + json.toString());
            System.out.println("Response code: " + urlConnection.getResponseCode());

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
