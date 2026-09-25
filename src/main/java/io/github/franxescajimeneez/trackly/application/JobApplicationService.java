package io.github.franxescajimeneez.trackly.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class JobApplicationService {

    private final List<JobApplication> applications = new ArrayList<>();
    private long nextId = 1L;

    public List<JobApplication> findAll() {
        return applications;
    }

    public Optional<JobApplication> findById(Long id) {
        return applications.stream()
                .filter(application -> id.equals(application.getId()))
                .findFirst();
    }

    public JobApplication create(JobApplication application) {
        JobApplication createdApplication = new JobApplication(
                nextId++,
                application.getCompany(),
                application.getPosition(),
                application.getStatus());
        applications.add(createdApplication);
        return createdApplication;
    }
}
