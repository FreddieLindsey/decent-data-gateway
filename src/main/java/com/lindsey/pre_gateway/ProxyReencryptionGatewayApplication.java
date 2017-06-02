package com.lindsey.pre_gateway;

import com.lindsey.pre_gateway.health.ProxyReEncryptionHealthCheck;
import com.lindsey.pre_gateway.resources.encryption.FirstLevelDecryptResource;
import com.lindsey.pre_gateway.resources.encryption.FirstLevelEncryptResource;
import com.lindsey.pre_gateway.resources.key.generate.GeneratePublicResource;
import com.lindsey.pre_gateway.resources.key.generate.GenerateReencryptionResource;
import com.lindsey.pre_gateway.resources.key.generate.GenerateSecretResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nics.crypto.proxy.afgh.AFGHGlobalParameters;

public class ProxyReencryptionGatewayApplication extends Application<ProxyReencryptionGatewayConfiguration> {

  // Setup
  static public final AFGHGlobalParameters globalParameters = new
    AFGHGlobalParameters
    (256, 1536);

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
    // Resources
    final Object[] resources = new Object[]{
      new GenerateSecretResource(),
      new GeneratePublicResource(),
      new GenerateReencryptionResource(),
      new FirstLevelEncryptResource(),
      new FirstLevelDecryptResource()
    };
    for (Object r : resources) environment.jersey().register(r);

    // Health Checks
    final ProxyReEncryptionHealthCheck healthCheck = new
      ProxyReEncryptionHealthCheck();
    environment.healthChecks().register("Dummy", healthCheck);
  }

}
