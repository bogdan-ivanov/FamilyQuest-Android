package ro.slingshots.gamifyhome.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class StringUtils {
    private final static String TAG = "StringUtils";
    private StringUtils() {}
    
    public static String convertStreamToString(InputStream is, String enc) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is,enc));
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (Exception e) {
            if(Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, ""+e.getMessage());
                if(Log.isLoggable(TAG, Log.DEBUG))
                    e.printStackTrace();
            }
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                if(Log.isLoggable(TAG, Log.ERROR)) {
                    Log.e(TAG, ""+e.getMessage());
                    if(Log.isLoggable(TAG, Log.ERROR))
                        e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
