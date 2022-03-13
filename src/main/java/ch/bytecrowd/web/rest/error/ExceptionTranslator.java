package ch.bytecrowd.web.rest.error;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionTranslator implements ExceptionMapper<CustomException> {

    @Override
    public Response toResponse(CustomException e) {
        return Response
                .status(e.getStatus())
                .entity(ExceptionBody.from(e))
                .build();
    }
}