package io.github.franxescajimeneez.trackly.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class JobApplicationServiceTest {

    @Test
    void shouldSaveApplicationThroughRepository() {
        JobApplicationRepository repository = mock(JobApplicationRepository.class);
        JobApplicationService service = new JobApplicationService(repository);
        JobApplication application =
                new JobApplication(null, "Empresa Test", "Java Junior", "APPLIED");
        JobApplication savedApplication =
                new JobApplication(1L, "Empresa Test", "Java Junior", "APPLIED");
        when(repository.save(application)).thenReturn(savedApplication);

        JobApplication createdApplication = service.create(application);

        assertSame(savedApplication, createdApplication);
        verify(repository).save(application);
    }

    @Test
    void shouldReturnApplicationsFromRepository() {
        JobApplicationRepository repository = mock(JobApplicationRepository.class);
        JobApplicationService service = new JobApplicationService(repository);
        JobApplication application =
                new JobApplication(1L, "Empresa Test", "Java Junior", "APPLIED");
        when(repository.findAll()).thenReturn(List.of(application));

        List<JobApplication> applications = service.findAll();

        assertEquals(List.of(application), applications);
        verify(repository).findAll();
    }

    @Test
    void shouldFindApplicationByIdThroughRepository() {
        JobApplicationRepository repository = mock(JobApplicationRepository.class);
        JobApplicationService service = new JobApplicationService(repository);
        JobApplication application =
                new JobApplication(1L, "Empresa Test", "Java Junior", "APPLIED");
        when(repository.findById(1L)).thenReturn(Optional.of(application));

        JobApplication foundApplication = service.findById(1L).orElseThrow();

        assertSame(application, foundApplication);
        assertEquals("Empresa Test", foundApplication.getCompany());
        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenApplicationDoesNotExist() {
        JobApplicationRepository repository = mock(JobApplicationRepository.class);
        JobApplicationService service = new JobApplicationService(repository);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertTrue(service.findById(99L).isEmpty());
        verify(repository).findById(99L);
    }

    @Test
    void shouldUpdateExistingApplicationUsingUrlId() {
        JobApplicationRepository repository = mock(JobApplicationRepository.class);
        JobApplicationService service = new JobApplicationService(repository);
        JobApplication existingApplication =
                new JobApplication(1L, "Empresa Antigua", "Java Junior", "APPLIED");
        JobApplication updateData =
                new JobApplication(99L, "Empresa Nueva", "Java Senior", "INTERVIEW");
        when(repository.findById(1L)).thenReturn(Optional.of(existingApplication));
        when(repository.save(any(JobApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication updatedApplication = service.update(1L, updateData).orElseThrow();

        ArgumentCaptor<JobApplication> captor = ArgumentCaptor.forClass(JobApplication.class);
        verify(repository).save(captor.capture());
        assertEquals(1L, captor.getValue().getId());
        assertEquals("Empresa Nueva", updatedApplication.getCompany());
        assertEquals("Java Senior", updatedApplication.getPosition());
        assertEquals("INTERVIEW", updatedApplication.getStatus());
    }

    @Test
    void shouldNotSaveWhenUpdatingMissingApplication() {
        JobApplicationRepository repository = mock(JobApplicationRepository.class);
        JobApplicationService service = new JobApplicationService(repository);
        JobApplication updateData =
                new JobApplication(null, "Empresa Nueva", "Java Senior", "INTERVIEW");
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertTrue(service.update(99L, updateData).isEmpty());
        verify(repository, never()).save(any(JobApplication.class));
    }

    @Test
    void shouldDeleteExistingApplication() {
        JobApplicationRepository repository = mock(JobApplicationRepository.class);
        JobApplicationService service = new JobApplicationService(repository);
        when(repository.existsById(1L)).thenReturn(true);

        boolean deleted = service.deleteById(1L);

        assertTrue(deleted);
        verify(repository).deleteById(1L);
    }

    @Test
    void shouldNotDeleteMissingApplication() {
        JobApplicationRepository repository = mock(JobApplicationRepository.class);
        JobApplicationService service = new JobApplicationService(repository);
        when(repository.existsById(99L)).thenReturn(false);

        boolean deleted = service.deleteById(99L);

        assertFalse(deleted);
        verify(repository, never()).deleteById(99L);
    }
}
