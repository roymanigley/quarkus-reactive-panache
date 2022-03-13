package ch.bytecrowd.web.rest;

import ch.bytecrowd.domain.Job;
import ch.bytecrowd.repository.JobRepository;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
public class JobResourceTest {

    public static final String DEFAULT_LABEL = "AAAAAAAAAAAA";
    public static final String DEFAULT_PAYLOAD = "BBBBBBBBBBB";
    public static final String UPDATED_LABEL = "XXXXXXXXXXXX";
    public static final String UPDATED_PAYLOAD = "YYYYYYYYYY";
    public static final ZoneId ZONE_ID = ZoneId.of("UTC");
    public static final OffsetDateTime DEFAULT_SCHEDULE = OffsetDateTime.of(
            2022, 5, 4, 04, 05, 0, 0,
            ZoneOffset.from(OffsetDateTime.now(ZONE_ID))
    );
    public static final OffsetDateTime UPDATED_SCHEDULE = OffsetDateTime.of(
            2022, 5, 11, 04, 05, 0, 0,
            ZoneOffset.from(OffsetDateTime.now(ZONE_ID))
    );
    public static final String DEFAULT_SCHEDULE_EXPECTED_STRING = DEFAULT_SCHEDULE.withOffsetSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    public static final String UPDATED_SCHEDULE_EXPECTED_STRING = UPDATED_SCHEDULE.withOffsetSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    @Inject
    JobRepository jobRepository;

    public static Job createJob() {
        return new Job()
                .label(DEFAULT_LABEL)
                .payload(DEFAULT_PAYLOAD)
                .schedule(DEFAULT_SCHEDULE);
    }

    @Test
    public void testFindAllEndpoint() {
        Panache.withTransaction(() ->
                jobRepository.persist(createJob())
        ).await().indefinitely();
        var expectedCount = jobRepository.count().await().indefinitely().intValue();

        System.out.println(DEFAULT_SCHEDULE);
        given()
                .when().get(JobResource.API_JOBS)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("$.size()", is(expectedCount))
                .body("id", notNullValue())
                .body("label", hasItem(DEFAULT_LABEL))
                .body("payload", hasItem(DEFAULT_PAYLOAD))
                .body("schedule", hasItem(DEFAULT_SCHEDULE_EXPECTED_STRING));
    }

    @Test
    public void testFindByIdEndpointSuccess() {
        var job = Panache.withTransaction(() ->
                jobRepository.persist(createJob())
        ).await().indefinitely();
        given()
                .when().get(JobResource.API_JOBS + "/" + job.getId().intValue())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("id", notNullValue())
                .body("label", is(DEFAULT_LABEL))
                .body("payload", is(DEFAULT_PAYLOAD))
                .body("schedule", is(DEFAULT_SCHEDULE_EXPECTED_STRING));
    }

    @Test
    public void testFindByIdEndpointNotFound() {
        given()
                .when().get(JobResource.API_JOBS + "/-1")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("key", is("error.not-found"))
                .body("message", is("job not found with id -1"));
    }

    @Test
    public void testCreateEndpointSuccess() {
        var job = createJob();
        given()
                .body(job)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post(JobResource.API_JOBS)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("id", notNullValue())
                .body("label", is(DEFAULT_LABEL))
                .body("payload", is(DEFAULT_PAYLOAD))
                .body("schedule", is(DEFAULT_SCHEDULE_EXPECTED_STRING));
    }

    @Test
    public void testCreateEndpointFail() {
        var job = createJob().id(999L);
        given()
                .body(job)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post(JobResource.API_JOBS)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("key", is("error.bad-request"))
                .body("message", is("id has to be null when creating"));
    }


    @Test
    public void testUpdateEndpointSuccess() {
        var job = Panache.withTransaction(() ->
                        jobRepository.persistAndFlush(createJob())
                )
                .await().indefinitely();

        job
                .label(UPDATED_LABEL)
                .payload(UPDATED_PAYLOAD)
                .schedule(UPDATED_SCHEDULE);

        given()
                .body(job)
                .contentType(MediaType.APPLICATION_JSON)
                .when().put(JobResource.API_JOBS)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("id", is(job.getId().intValue()))
                .body("label", is(UPDATED_LABEL))
                .body("payload", is(UPDATED_PAYLOAD))
                .body("schedule", is(UPDATED_SCHEDULE_EXPECTED_STRING));
    }

    @Test
    public void testUpdateEndpointFail() {
        var job = Panache.withTransaction(() ->
                jobRepository.persistAndFlush(createJob())
        ).await().indefinitely();

        job.id(null);

        given()
                .body(job)
                .contentType(MediaType.APPLICATION_JSON)
                .when().put(JobResource.API_JOBS)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("key", is("error.bad-request"))
                .body("message", is("id can not be null when updating"));
    }

    @Test
    public void testDeleteByIdEndpointSuccess() {
        var job = Panache.withTransaction(() ->
                jobRepository.persist(createJob())
        ).await().indefinitely();
        Long countBeforeDelete = jobRepository.count().await().indefinitely();

        given()
                .when().delete(JobResource.API_JOBS + "/" + job.getId().intValue())
                .then()
                .statusCode(Response.Status.ACCEPTED.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON);

        Long countAfterDelete = jobRepository.count().await().indefinitely();
        assertThat(countAfterDelete, is(countBeforeDelete -1));
    }

    @Test
    public void testDeleteByIdEndpointNotExist() {
        Long countBeforeDelete = jobRepository.count().await().indefinitely();
        given()
                .when().delete(JobResource.API_JOBS + "/-1")
                .then()
                .statusCode(Response.Status.ACCEPTED.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON);

        Long countAfterDelete = jobRepository.count().await().indefinitely();
        assertThat(countAfterDelete, is(countBeforeDelete));
    }
}