package ch.bytecrowd.service.impl;

import ch.bytecrowd.domain.Job;
import ch.bytecrowd.repository.JobRepository;
import ch.bytecrowd.service.JobService;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Inject
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Uni<List<Job>> findAll() {
        return jobRepository.findAll().list();
    }

    @Override
    public Uni<Job> findById(Long id) {
        return jobRepository.findById(id);
    }

    @Override
    public Uni<Job> create(Job job) {
        return Panache.withTransaction(() -> jobRepository.persist(job.id(null)));
    }

    @Override
    public Uni<Job> update(Job job) {
        return Panache.withTransaction(() ->
                findById(job.getId())
                        .invoke(j -> j
                                .label(job.getLabel())
                                .payload(job.getPayload())
                                .schedule(job.getSchedule())
                        ).flatMap(jobRepository::persist)
        );
    }

    @Override
    public Uni<Boolean> delete(Long id) {
        return Panache.withTransaction(() -> jobRepository.deleteById(id));
    }
}
