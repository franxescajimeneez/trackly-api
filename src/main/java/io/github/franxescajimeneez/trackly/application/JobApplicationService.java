package io.github.franxescajimeneez.trackly.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class JobApplicationService {

    private final List<JobApplication> applications = new ArrayList<>();

    public List<JobApplication> findAll() {
        return applications;
    }
    public JobApplication create(JobApplication application) {
        applications.add(application);
        return application;
    }
}