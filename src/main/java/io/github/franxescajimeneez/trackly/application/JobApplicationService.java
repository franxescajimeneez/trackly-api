package io.github.franxescajimeneez.trackly.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public JobApplicationService(JobApplicationRepository repository) {
        this.repository = repository;
    }

    public List<JobApplication> findAll() {
        return repository.findAll();
    }

    public Optional<JobApplication> findById(Long id) {
        return repository.findById(id);
    }

    public JobApplication create(JobApplication application) {
        return repository.save(application);
    }

    public Optional<JobApplication> update(Long id, JobApplication application) {
        return repository.findById(id)
                .map(existingApplication -> repository.save(new JobApplication(
                        id,
                        application.getCompany(),
                        application.getPosition(),
                        application.getStatus())));
    }

    public boolean deleteById(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}
