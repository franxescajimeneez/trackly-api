package io.github.franxescajimeneez.trackly.application;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<JobApplication> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<JobApplication> create(@Valid @RequestBody JobApplication application)  {
        JobApplication createdApplication = service.create(application);
        URI location = URI.create("/api/applications/" + createdApplication.getId());
        return ResponseEntity.created(location).body(createdApplication);

    }
}
