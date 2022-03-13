package ch.bytecrowd.web.rest.error;

import javax.ws.rs.core.Response;

public class BadRequestException extends CustomException {
    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public Response.Status getStatus() {
        return Response.Status.BAD_REQUEST;
    }

    @Override
    public String getKey() {
        return "error.bad-request";
    }
}