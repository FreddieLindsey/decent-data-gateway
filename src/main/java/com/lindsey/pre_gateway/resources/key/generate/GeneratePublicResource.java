package com.lindsey.pre_gateway.resources.key.generate;

import com.codahale.metrics.annotation.Timed;
import com.lindsey.pre_gateway.api.key.generate.GeneratePublic;
import com.lindsey.pre_gateway.models.KeyPair;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/key/generate/public")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GeneratePublicResource {

  @POST
  @Timed
  public GeneratePublic generatePublicKey(KeyPair keyPair) {
    return new GeneratePublic(keyPair);
  }

}
