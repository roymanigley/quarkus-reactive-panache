package ch.bytecrowd.service;

import ch.bytecrowd.domain.Job;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface JobService {

    Uni<List<Job>> findAll();

    Uni<Job> findById(Long id);

    Uni<Job> create(Job job);

    Uni<Job> update(Job job);

    Uni<Boolean> delete(Long id);
}
