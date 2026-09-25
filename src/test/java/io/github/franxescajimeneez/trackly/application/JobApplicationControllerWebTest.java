package io.github.franxescajimeneez.trackly.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class JobApplicationControllerWebTest {

    private JobApplicationService service;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        service = mock(JobApplicationService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new JobApplicationController(service))
                .build();
    }

    @Test
    void shouldReturnApplicationById() throws Exception {
        JobApplication application =
                new JobApplication(1L, "Empresa Test", "Java Junior", "APPLIED");
        when(service.findById(1L)).thenReturn(Optional.of(application));

        mockMvc.perform(get("/api/applications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.company").value("Empresa Test"))
                .andExpect(jsonPath("$.position").value("Java Junior"))
                .andExpect(jsonPath("$.status").value("APPLIED"));
    }

    @Test
    void shouldReturnNotFoundWhenApplicationDoesNotExist() throws Exception {
        when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/applications/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRetrieveNewlyCreatedApplicationByItsGeneratedId() throws Exception {
        JobApplication createdApplication =
                new JobApplication(1L, "Empresa Test", "Java Junior", "APPLIED");
        when(service.create(any(JobApplication.class))).thenReturn(createdApplication);
        when(service.findById(1L)).thenReturn(Optional.of(createdApplication));

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "company": "Empresa Test",
                                  "position": "Java Junior",
                                  "status": "APPLIED"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/applications/1"))
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/api/applications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.company").value("Empresa Test"));
    }

    @Test
    void shouldReturnBadRequestWhenCompanyIsEmpty() throws Exception {
        assertInvalidApplication("""
                {
                  "company": "",
                  "position": "Java Junior",
                  "status": "APPLIED"
                }
                """);
    }

    @Test
    void shouldReturnBadRequestWhenPositionIsEmpty() throws Exception {
        assertInvalidApplication("""
                {
                  "company": "Empresa Test",
                  "position": "",
                  "status": "APPLIED"
                }
                """);
    }

    @Test
    void shouldReturnBadRequestWhenStatusIsEmpty() throws Exception {
        assertInvalidApplication("""
                {
                  "company": "Empresa Test",
                  "position": "Java Junior",
                  "status": ""
                }
                """);
    }

    @Test
    void shouldReturnBadRequestWhenCompanyContainsOnlySpaces() throws Exception {
        assertInvalidApplication("""
                {
                  "company": "   ",
                  "position": "Java Junior",
                  "status": "APPLIED"
                }
                """);
    }

    @Test
    void shouldUpdateExistingApplication() throws Exception {
        JobApplication updatedApplication =
                new JobApplication(1L, "Empresa Actualizada", "Java Senior", "INTERVIEW");
        when(service.update(eq(1L), any(JobApplication.class)))
                .thenReturn(Optional.of(updatedApplication));

        mockMvc.perform(put("/api/applications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 99,
                                  "company": "Empresa Actualizada",
                                  "position": "Java Senior",
                                  "status": "INTERVIEW"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.company").value("Empresa Actualizada"))
                .andExpect(jsonPath("$.position").value("Java Senior"))
                .andExpect(jsonPath("$.status").value("INTERVIEW"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingMissingApplication() throws Exception {
        when(service.update(eq(99L), any(JobApplication.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/applications/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "company": "Empresa Actualizada",
                                  "position": "Java Senior",
                                  "status": "INTERVIEW"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingWithInvalidBody() throws Exception {
        mockMvc.perform(put("/api/applications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "company": "   ",
                                  "position": "Java Senior",
                                  "status": "INTERVIEW"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteExistingApplication() throws Exception {
        when(service.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/applications/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingMissingApplication() throws Exception {
        when(service.deleteById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/applications/99"))
                .andExpect(status().isNotFound());
    }

    private void assertInvalidApplication(String requestBody) throws Exception {
        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
