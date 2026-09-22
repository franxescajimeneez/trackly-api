package io.github.franxescajimeneez.trackly.health;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthControllerTest {

    @Test
    void shouldReturnUpStatus() {
        HealthController controller = new HealthController();

        assertEquals("UP", controller.getHealth().get("status"));
    }
}