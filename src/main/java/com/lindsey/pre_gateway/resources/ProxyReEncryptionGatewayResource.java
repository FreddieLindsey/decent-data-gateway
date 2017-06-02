package com.lindsey.pre_gateway.resources;


import com.codahale.metrics.annotation.Timed;
import com.lindsey.pre_gateway.api.Saying;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class ProxyReEncryptionGatewayResource {

    private final AtomicLong counter;

    public ProxyReEncryptionGatewayResource() {
      this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
      String value = name.orElse("Your name here");
      return new Saying(counter.incrementAndGet(), value);
    }
}
