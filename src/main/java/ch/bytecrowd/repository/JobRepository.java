package ch.bytecrowd.repository;

import ch.bytecrowd.domain.Job;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JobRepository implements PanacheRepositoryBase<Job, Long> {

}
