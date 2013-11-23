package ro.slingshots.gamifyhome.http.exception;

public class HttpApiException extends Exception {
    
    private static final long serialVersionUID = 1L;

    private String mExtra;
    private int mStatusCode;

    public HttpApiException(String message) {
        super(message);
    }

    public HttpApiException(String message, String extra) {
        super(message);
        mExtra = extra;
    }
    
    public HttpApiException(int statusCode, String message) {
        super(message);
        mStatusCode = statusCode;
    }
    
    public HttpApiException(int statusCode, String message, String extra) {
        super(message);
        mExtra = extra;
        mStatusCode = statusCode;
    }
    
    public String getExtra() {
        return mExtra;
    }
    
    public void setExtra(String extra) {
        this.mExtra = extra;
    }
    
    public int getmStatusCode() {
        return mStatusCode;
    }

    public void setmStatusCode(int mStatusCode) {
        this.mStatusCode = mStatusCode;
    }
}
