package com.lindsey.pre_gateway;

import com.lindsey.pre_gateway.health.ProxyReEncryptionHealthCheck;
import com.lindsey.pre_gateway.resources.key.GenerateSecretResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nics.crypto.proxy.afgh.AFGHGlobalParameters;

public class ProxyReencryptionGatewayApplication extends Application<ProxyReencryptionGatewayConfiguration> {

  public static void main(final String[] args) throws Exception {
    new ProxyReencryptionGatewayApplication().run(args);
  }

  @Override
  public String getName() {
    return "ProxyReencryptionGateway";
  }

  @Override
  public void initialize(final Bootstrap<ProxyReencryptionGatewayConfiguration> bootstrap) {
    // TODO: application initialization
  }

  @Override
  public void run(final ProxyReencryptionGatewayConfiguration configuration,
                  final Environment environment) {
    // Setup
    final AFGHGlobalParameters globalParameters = new AFGHGlobalParameters
      (256, 1536);

    // Resources
    final GenerateSecretResource resource = new
      GenerateSecretResource(globalParameters);
    environment.jersey().register(resource);

    // Health Checks
    final ProxyReEncryptionHealthCheck healthCheck = new
      ProxyReEncryptionHealthCheck();
    environment.healthChecks().register("Dummy", healthCheck);
  }

}
