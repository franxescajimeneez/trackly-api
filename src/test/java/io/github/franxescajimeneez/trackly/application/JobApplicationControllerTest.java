package io.github.franxescajimeneez.trackly.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

class JobApplicationControllerTest {

    @Test
    void shouldReturnApplicationsFromService() {

        JobApplicationService service = mock(JobApplicationService.class);

        JobApplication application =
                new JobApplication(1L, "Empresa Test", "Java Junior", "APPLIED");

        when(service.findAll()).thenReturn(List.of(application));

        JobApplicationController controller =
                new JobApplicationController(service);

        List<JobApplication> result = controller.findAll();

        assertEquals(1, result.size());
        assertEquals("Empresa Test", result.get(0).getCompany());
    }
}