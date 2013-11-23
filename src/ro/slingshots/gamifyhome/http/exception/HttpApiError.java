package ro.slingshots.gamifyhome.http.exception;

public class HttpApiError extends HttpApiException {
    
    private static final long serialVersionUID = 1L;

    public HttpApiError(String message) {
        super(message);
    }

}
