package ch.bytecrowd.web.rest;

import ch.bytecrowd.domain.Job;
import ch.bytecrowd.service.JobService;
import ch.bytecrowd.web.rest.error.BadRequestException;
import ch.bytecrowd.web.rest.error.NotFoundException;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path(JobResource.API_JOBS)
@Tag(name = "JobResource", description = "REST Api for Jobs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobResource {

    private static final Logger LOG = LoggerFactory.getLogger(JobResource.class);
    public static final String API_JOBS = "/api/jobs";

    private final JobService jobService;

    @Inject
    public JobResource(JobService jobService) {
        this.jobService = jobService;
    }

    @GET
    @Operation(description = "returns all jobs")
    public Uni<Response> listAll() {
        LOG.info("GET request to {}", API_JOBS);
        return jobService.findAll()
                .map(jobs -> Response.ok(jobs).build());
    }

    @GET
    @Path("/{id}")
    public Uni<Response> findOne(@PathParam("id") Long id) {
        LOG.info("GET request to {}/{}", API_JOBS, id);
        return jobService.findById(id)
                .onItem().ifNull().failWith(new NotFoundException("job not found with id " + id))
                .map(job -> Response.ok(job).build());
    }

    @POST
    public Uni<Response> create(Job job) {
        LOG.info("POST request to {} with ", API_JOBS, job);
        if (job.getId() != null)
            throw new BadRequestException("id has to be null when creating");

        return jobService.create(job)
                .map(j -> Response.created(URI.create("/api/jobs/")).entity(j).build());
    }

    @PUT
    public Uni<Response> update(Job job) {
        LOG.info("PUT request to {} with ", API_JOBS, job);
        if (job.getId() == null)
            throw new BadRequestException("id can not be null when updating");

        return jobService.update(job)
                .onItem().ifNull().failWith(new NotFoundException("job not found with id " + job.getId()))
            .map(j -> Response.ok(j).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") Long id) {
        LOG.info("DELETE request to {}/{}", API_JOBS, id);
        return jobService.delete(id)
                .map(b -> Response.accepted().build());
    }
}