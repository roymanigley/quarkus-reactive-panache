package ch.bytecrowd.web.rest.error;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
class CustomExceptionTest {

    public static final String MESSAGE = "QWERTZUI";
    CustomException ex = new CustomException(MESSAGE) {
        @Override
        public Response.Status getStatus() {
            return super.getStatus();
        }
    };

    @Test
    void testCustomExceptionDefaultValues() {
        assertThat(ex.getKey(), is("error.unknown"));
        assertThat(ex.getStatus(), is(Response.Status.INTERNAL_SERVER_ERROR));
        assertThat(ex.getMessage(), is(MESSAGE));
    }
}