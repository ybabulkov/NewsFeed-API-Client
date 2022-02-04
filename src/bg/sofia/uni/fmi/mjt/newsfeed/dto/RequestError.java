package bg.sofia.uni.fmi.mjt.newsfeed.dto;

public class RequestError {
    private String status;
    private String code;
    private String message;

    public RequestError(String status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
