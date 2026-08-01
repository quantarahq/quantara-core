package dev.quantara.core.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class HealthControllerTest {

  @Test
  void healthReturnsUpStatus() {
    var response = new HealthController().health();

    assertThat(response.status()).isEqualTo("UP");
  }
}
