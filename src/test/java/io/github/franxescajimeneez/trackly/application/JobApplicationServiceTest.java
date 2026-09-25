package io.github.franxescajimeneez.trackly.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JobApplicationServiceTest {

    @Test
    void shouldAssignIdToNewApplication() {
        JobApplicationService service = new JobApplicationService();
        JobApplication application =
                new JobApplication(null, "Empresa Test", "Java Junior", "APPLIED");

        JobApplication createdApplication = service.create(application);

        assertNotNull(createdApplication.getId());
    }

    @Test
    void shouldAssignDifferentIdsToSuccessiveApplications() {
        JobApplicationService service = new JobApplicationService();

        JobApplication firstApplication = service.create(
                new JobApplication(null, "Empresa Uno", "Java Junior", "APPLIED"));
        JobApplication secondApplication = service.create(
                new JobApplication(null, "Empresa Dos", "Java Senior", "INTERVIEW"));

        assertNotEquals(firstApplication.getId(), secondApplication.getId());
    }

    @Test
    void shouldFindNewlyCreatedApplicationByItsId() {
        JobApplicationService service = new JobApplicationService();
        JobApplication createdApplication = service.create(
                new JobApplication(null, "Empresa Test", "Java Junior", "APPLIED"));

        JobApplication foundApplication = service.findById(createdApplication.getId()).orElseThrow();

        assertSame(createdApplication, foundApplication);
        assertEquals("Empresa Test", foundApplication.getCompany());
    }

    @Test
    void shouldReturnEmptyWhenApplicationDoesNotExist() {
        JobApplicationService service = new JobApplicationService();

        assertTrue(service.findById(99L).isEmpty());
    }
}
