package com.lindsey.pre_gateway.resources.key.generate;

import com.codahale.metrics.annotation.Timed;
import com.lindsey.pre_gateway.api.key.generate.GenerateSecret;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/key/generate/secret")
@Produces(MediaType.APPLICATION_JSON)
public class GenerateSecretResource {

  @POST
  @Timed
  public GenerateSecret generateSecretKey() {
    return new GenerateSecret();
  }

}
