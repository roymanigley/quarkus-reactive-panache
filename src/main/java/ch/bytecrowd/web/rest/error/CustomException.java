package ch.bytecrowd.web.rest.error;

import javax.ws.rs.core.Response;

public abstract class CustomException extends RuntimeException {

    protected CustomException(String message) {
        super(message);
    }

    public Response.Status getStatus() {
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

    public String getKey() {
        return "error.unknown";
    }
}