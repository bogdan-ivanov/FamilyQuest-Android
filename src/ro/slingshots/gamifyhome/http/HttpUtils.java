package ro.slingshots.gamifyhome.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.util.Log;

/**
 * Provides utility methods for communicating with the server.
 */
public final class HttpUtils {
    private static final String TAG = HttpUtils.class.getSimpleName();

    private HttpUtils() {
    }

    public static boolean checkInternetConnection(final Context context) {
        
        final ConnectivityManager conMgr =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Internet Connection Not Present");
            }
            return false;
        }
    }
    
    public static String getPostRequestBody(final HttpPost request) {

        String responseBody = null;
        HttpEntity entity = null;

        try {
            entity = request.getEntity();
            responseBody = _getEntityBody(entity);
        } catch (Exception e) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "" + e.getMessage());
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    e.printStackTrace();
                }
            }
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (Exception e1) {
                    if (Log.isLoggable(TAG, Log.ERROR)) {
                        Log.e(TAG, e1.getMessage());
                        if (Log.isLoggable(TAG, Log.DEBUG)) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
        return responseBody;
    }

    public static String getResponseBody(final HttpResponse response) {

        String responseBody = null;
        HttpEntity entity = null;

        try {
            entity = response.getEntity();
            responseBody = _getEntityBody(entity);

        } catch (Exception e) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "" + e.getMessage());
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    e.printStackTrace();
                }
            }
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (Exception e1) {
                    if (Log.isLoggable(TAG, Log.ERROR)) {
                        Log.e(TAG, e1.getMessage());
                        if (Log.isLoggable(TAG, Log.DEBUG)) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Response content: " + responseBody);
        }
        return responseBody;
    }

    public static String _getEntityBody(final HttpEntity entity)
                    throws IOException, ParseException {

        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }

        InputStream instream = entity.getContent();

        if (instream == null) {
            return "";
        }

        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                            "HTTP entity too large to be buffered in memory");
        }

        String charset = getContentCharSet(entity);

        if (charset == null) {
            charset = HTTP.UTF_8;
        }

        return StringUtils.convertStreamToString(instream, charset);
    }

    public static String getContentCharSet(final HttpEntity entity)
                    throws ParseException {

        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }

        String charset = null;

        if (entity.getContentType() != null) {

            HeaderElement[] values = entity.getContentType().getElements();

            if (values.length > 0) {

                NameValuePair param = values[0].getParameterByName("charset");

                if (param != null) {

                    charset = param.getValue();
                }
            }
        }

        return charset;

    }

    public static void logPostRequest(final String tag, final HttpPost request) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            if (request != null) {
                Log.d(tag, "Request line: " + request.getRequestLine());
                Header[] h = request.getAllHeaders();
                for (int i = 0; i < h.length; i++) {
                    Log.d(tag, "Request header: " + h[i].getName() + " : "
                                    + h[i].getValue());
                }
                Log.d(tag, "Request content: " + getPostRequestBody(request));
            } else {
                Log.d(tag, "Request: null");
            }
        }
    }
    
    public static void logGetRequest(final String tag, final HttpGet request) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            if (request != null) {
                Log.d(tag, "Request line: " + request.getRequestLine());
                Header[] h = request.getAllHeaders();
                for (int i = 0; i < h.length; i++) {
                    Log.d(tag, "Request header: " + h[i].getName() + " : "
                                    + h[i].getValue());
                }                
            } else {
                Log.d(tag, "Request: null");
            }
        }
    }

    public static void logResponse(final String tag, final HttpResponse response) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            if (response != null) {
                Header[] resph = response.getAllHeaders();
                for (int i = 0; i < resph.length; i++) {
                    Log.d(tag, "Response header: " + resph[i].getName() + " : "
                                    + resph[i].getValue());
                }
                Log.d(tag, "Response status code: "
                                + response.getStatusLine().getStatusCode());
                Log.d(tag, "Response reason: "
                                + response.getStatusLine().getReasonPhrase());
            } else {
                Log.d(tag, "Response: null");
            }
        }
    }
}
