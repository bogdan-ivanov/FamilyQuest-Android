package ro.slingshots.gamifyhome.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import ro.slingshots.gamifyhome.http.exception.HttpApiException;



import android.text.format.DateUtils;

public class HttpApi {
    private static final String TAG = HttpApi.class.getSimpleName();

    private static final String CLIENT_VERSION_HEADER = "User-Agent";

    private static final int SECOND_IN_MILLIS = (int) DateUtils.SECOND_IN_MILLIS;

    public static final int CONNECTION_TIMEOUT = 60 * SECOND_IN_MILLIS;

    protected final DefaultHttpClient mHttpClient;
    private final String mClientVersion;

    public HttpApi(String clientVersion) {
        mHttpClient = createHttpClient();
        
        if (clientVersion != null) {
            mClientVersion = clientVersion;
        } else {
            mClientVersion = "";
        }
    }

    /**
     * execute() an httpRequest catching exceptions and returning null instead.
     * 
     * @param httpRequest
     * @return
     * @throws IOException
     */
    public String executeHttpRequest(HttpRequestBase httpRequest)
                    throws HttpApiException, IOException,IllegalStateException {
    	
        try {
            mHttpClient.getConnectionManager().closeExpiredConnections();
            HttpResponse response = mHttpClient.execute(httpRequest);
            HttpUtils.logResponse(TAG, response);

            int statusCode = response.getStatusLine().getStatusCode();
            String content;
            switch (statusCode) {
            
            case HttpStatus.SC_OK:
                content = EntityUtils.toString(response.getEntity());
                response.getEntity().consumeContent();
                return content;
                
            case HttpStatus.SC_CREATED:
                content = EntityUtils.toString(response.getEntity());
                response.getEntity().consumeContent();
                return content;

            case HttpStatus.SC_ACCEPTED:
                content = EntityUtils.toString(response.getEntity());
                response.getEntity().consumeContent();
                return content;

            case HttpStatus.SC_BAD_REQUEST:
                content = EntityUtils.toString(response.getEntity());
                response.getEntity().consumeContent();
                return content;
                
            case HttpStatus.SC_UNAUTHORIZED:
                content = EntityUtils.toString(response.getEntity());
                response.getEntity().consumeContent();
                return content;

            case HttpStatus.SC_NOT_FOUND:
                content = EntityUtils.toString(response.getEntity());
                response.getEntity().consumeContent();
                return content;

            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                content = EntityUtils.toString(response.getEntity());
                response.getEntity().consumeContent();
                return content;

            default:
                content = EntityUtils.toString(response.getEntity());
                response.getEntity().consumeContent();
                return content;
            }
        } catch (IOException e) {
            httpRequest.abort();
            throw e;
        }
        catch (IllegalStateException ise){
        	httpRequest.abort();
        	throw ise;
        }
    }

    public HttpGet createHttpGet(String url, List<NameValuePair> nameValuePairs) {
        String query = URLEncodedUtils.format(stripNulls(nameValuePairs),
                        HTTP.UTF_8);
        HttpGet httpGet = new HttpGet(url + "?" + query);
        httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        return httpGet;
    }

    public HttpPost createHttpPost(String url, List<NameValuePair> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);        
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        HttpUtils.logPostRequest(TAG, httpPost);
        return httpPost;
    }

    private List<NameValuePair> stripNulls(List<NameValuePair> nameValuePairs) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (nameValuePairs != null) {
            for (int i = 0; i < nameValuePairs.size(); i++) {
                NameValuePair param = nameValuePairs.get(i);
                if (param.getValue() != null) {
                    params.add(param);
                }
            }
        }
        return params;
    }
    public static final DefaultHttpClient createHttpClient() {

        // Set some client http client parameter defaults.
        final HttpParams httpParams = createHttpParams();
        
//        DefaultHttpClient client = new DefaultHttpClient();
//        ClientConnectionManager mgr = client.getConnectionManager();
//        HttpParams params = client.getParams();
//        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, 
//
//                mgr.getSchemeRegistry()), params);

        DefaultHttpClient client = new DefaultHttpClient(httpParams);
        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(
                        3, true);
        client.setHttpRequestRetryHandler(retryHandler);

        return client;
    }
    /**
     * Create a thread-safe client. This client does not do redirecting, to
     * allow us to capture correct "error" codes.
     * 
     * @return HttpClient
     */
    public static final DefaultHttpClient createHttpsClient() {

        // Set some client http client parameter defaults.
        final HttpParams httpParams = createHttpParams();
        
//        DefaultHttpClient client = new DefaultHttpClient();
//        ClientConnectionManager mgr = client.getConnectionManager();
//        HttpParams params = client.getParams();
//        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, 
//
//                mgr.getSchemeRegistry()), params);

       
        DefaultHttpClient client = new DefaultHttpClient(httpParams);
        
        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        
        DefaultHttpClient httpClient  = new DefaultHttpClient(mgr, client.getParams());
        
        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(
                        3, true);
        httpClient.setHttpRequestRetryHandler(retryHandler);

        return sslClient(httpClient);
    }
    private static DefaultHttpClient sslClient(DefaultHttpClient client) {
        try {
            X509TrustManager tm = new X509TrustManager() { 
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                } 
            };
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }
    /**
     * Create the default HTTP protocol parameters.
     */
    public static final HttpParams createHttpParams() {
        final HttpParams params = new BasicHttpParams();

        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        // HttpConnectionParams.setStaleCheckingEnabled(params, false);

        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpConnectionParams.setSoTimeout(params, CONNECTION_TIMEOUT);
        ConnManagerParams.setTimeout(params, CONNECTION_TIMEOUT);
        ConnManagerParams.setMaxTotalConnections(params, 20);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setHttpElementCharset(params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, false);
        
        return params;
    }
    
    
    public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        public MySSLSocketFactory(SSLContext context) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
           super(null);
           sslContext = context;
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
   }
    
    
}
