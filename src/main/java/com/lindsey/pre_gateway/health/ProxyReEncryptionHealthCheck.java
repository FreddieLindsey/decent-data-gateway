package com.lindsey.pre_gateway.health;

import com.codahale.metrics.health.HealthCheck;

public class ProxyReEncryptionHealthCheck extends HealthCheck{

  @Override
  protected Result check() throws Exception {
    return Result.healthy();
  }
}
