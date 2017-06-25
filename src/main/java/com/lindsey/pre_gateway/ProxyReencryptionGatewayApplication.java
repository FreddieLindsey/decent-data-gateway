package com.lindsey.pre_gateway;

import com.lindsey.pre_gateway.health.ProxyReEncryptionHealthCheck;
import com.lindsey.pre_gateway.resources.encryption.*;
import com.lindsey.pre_gateway.resources.key.generate.GeneratePublicResource;
import com.lindsey.pre_gateway.resources.key.generate.GenerateReencryptionResource;
import com.lindsey.pre_gateway.resources.key.generate.GenerateSecretResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nics.crypto.proxy.afgh.AFGHGlobalParameters;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

public class ProxyReencryptionGatewayApplication extends Application<ProxyReencryptionGatewayConfiguration> {

  // Setup
  public static AFGHGlobalParameters globalParameters = new
    AFGHGlobalParameters(256, 1536);

  public static void main(final String[] args) throws Exception {
    File globals = new File("/data/globals");
    if (globals.exists()) {
      globalParameters =
        new AFGHGlobalParameters(Files.readAllBytes(globals.toPath()));
    } else {
      globalParameters = new AFGHGlobalParameters(256, 1536);
      try {
        Files.write(globals.toPath(), globalParameters.toBytes());
      } catch (Exception e) {
        System.out.println(
          "Could not save global parameters. May change on next restart");
      }
    }

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
    // Enable CORS headers
    final FilterRegistration.Dynamic cors =
      environment.servlets().addFilter("CORS", CrossOriginFilter.class);

    // Configure CORS parameters
    cors.setInitParameter("allowedOrigins", "*");
    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    // Resources
    final Object[] resources = new Object[]{
      new GenerateSecretResource(),
      new GeneratePublicResource(),
      new GenerateReencryptionResource(),
      new FirstLevelEncryptResource(),
      new FirstLevelDecryptResource(),
      new SecondLevelEncryptResource(),
      new SecondLevelDecryptResource(),
      new ReencryptResource()
    };
    for (Object r : resources) environment.jersey().register(r);

    // Health Checks
    final ProxyReEncryptionHealthCheck healthCheck = new
      ProxyReEncryptionHealthCheck();
    environment.healthChecks().register("Dummy", healthCheck);
  }

}
