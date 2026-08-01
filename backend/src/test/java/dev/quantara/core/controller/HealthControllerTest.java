package dev.quantara.core.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HealthControllerTest {

    @Test
    void healthReturnsUpStatus() {
        var response = new HealthController().health();

        assertThat(response.status()).isEqualTo("UP");
    }
}
