package ch.bytecrowd.web.rest.error;

public class ExceptionBody {
    private String key;
    private String message;

    static ExceptionBody from(CustomException e) {
        return new ExceptionBody()
                .key(e.getKey())
                .message(e.getMessage());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ExceptionBody key(String key) {
        this.setKey(key);
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ExceptionBody message(String message) {
        this.setMessage(message);
        return this;
    }
}