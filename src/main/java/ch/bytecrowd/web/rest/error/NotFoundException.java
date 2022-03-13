package ch.bytecrowd.web.rest.error;

import javax.ws.rs.core.Response;

public class NotFoundException extends CustomException {

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public Response.Status getStatus() {
        return Response.Status.NOT_FOUND;
    }

    @Override
    public String getKey() {
        return "error.not-found";
    }
}