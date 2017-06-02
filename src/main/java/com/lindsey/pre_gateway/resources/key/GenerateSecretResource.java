package com.lindsey.pre_gateway.resources.key;

import com.codahale.metrics.annotation.Timed;
import com.lindsey.pre_gateway.api.key.GenerateSecret;
import nics.crypto.proxy.afgh.AFGHGlobalParameters;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/key/generate")
@Produces(MediaType.APPLICATION_JSON)
public class GenerateSecretResource {

  private final AFGHGlobalParameters global;

  public GenerateSecretResource(AFGHGlobalParameters global) {
    this.global = global;
  }

  @GET
  @Timed
  public GenerateSecret generateSecretKey() {
    return new GenerateSecret(global);
  }

}
