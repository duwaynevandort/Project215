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
//import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Adam on 11/22/2015.
 * Class for logging events in the client application, uploading them to the server after the
 * number of events reaches a set threshold.
 */

public class Log
{
    //TODO  add user ID to server logs

    final static int SUCCESS_CODE = 200;

    final static String FILENAME = "215.log";

    final static int UPLOAD_THRESHOLD = 10;

    //logging error and success codes
    //deprecated
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
    public static void log(int level, String tag, String message)
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
                case DEBUG: text = "[DEBUG]\t" + currentTime + "\t" + tag + "\t" + message;
                    break;
                case ERROR: text = "[ERROR]\t" + currentTime + "\t" + tag + "\t" + message;
                    break;
                case INFO:
                default: text = "[INFO]\t" + currentTime + "\t" + tag + "\t" + message;
            }

            output.write(text + "\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
            }
        }

        eventCount++;

        if(eventCount >= UPLOAD_THRESHOLD)
        {
            eventCount = 0;

            //read in log entries into memory so we can delete the log file contents
            final List<String> entries = new ArrayList<String>();
            try
            {
                Scanner scanner = new Scanner(new File(FILENAME));

                while(scanner.hasNextLine())
                {
                    entries.add(scanner.nextLine());
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            //delete log file contents
            try
            {
                new BufferedWriter(new FileWriter(new File(FILENAME), false));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            eventCount = 0;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try
                    {
                        uploadLogs(entries);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }
    }

    //Aliases for log()
    public static void d(String tag, String msg) { log(DEBUG, tag, msg); }
    public static void e(String tag, String msg) { log(ERROR, tag, msg); }
    public static void i(String tag, String msg) { log(INFO, tag, msg); }
    public static void wtf(String tag, String msg){ log(ERROR, tag, msg); }

    /**
     * POSTs the given log entries to the server.
     *
     * @param entries the logs to be uploaded
     * @return whether the upload was successful or not
     */
    public static boolean uploadLogs(List<String> entries)
    {
        boolean success = false;

        JSONObject json = new JSONObject();
        URL url;
        HttpURLConnection urlConnection;
        DataOutputStream printout;

        try{
            //String http = "http://162.243.52.70:7000/log/";
            String http = Model.SERVER_URL + "/log/";

            //List<String> entries = new ArrayList<String>();

            /*try
            {
                Scanner scanner = new Scanner(new File(FILENAME + ".buffer"));
                while(scanner.hasNextLine())
                {
                    entries.add(scanner.nextLine());
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
                return false;
            }*/


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