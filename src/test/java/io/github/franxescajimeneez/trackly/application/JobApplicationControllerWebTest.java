package io.github.franxescajimeneez.trackly.application;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        MockMvc mockMvcWithRealService = MockMvcBuilders
                .standaloneSetup(new JobApplicationController(new JobApplicationService()))
                .build();

        mockMvcWithRealService.perform(post("/api/applications")
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

        mockMvcWithRealService.perform(get("/api/applications/1"))
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

    private void assertInvalidApplication(String requestBody) throws Exception {
        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
