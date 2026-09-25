package io.github.franxescajimeneez.trackly.application;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String company;

    @NotBlank
    private String position;

    @NotBlank
    private String status;

    protected JobApplication() {
    }

    public JobApplication(Long id, String company, String position, String status) {
        this.id = id;
        this.company = company;
        this.position = position;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public String getStatus() {
        return status;
    }
}
