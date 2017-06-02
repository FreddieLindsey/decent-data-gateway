package com.lindsey.pre_gateway.resources.encryption;

import com.codahale.metrics.annotation.Timed;
import com.lindsey.pre_gateway.api.encryption.FirstLevelEncrypt;
import com.lindsey.pre_gateway.models.EncryptionPacket;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/encryption/encrypt")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FirstLevelEncryptResource {

  @POST
  @Timed
  public FirstLevelEncrypt firstLevelEncrypt(EncryptionPacket packet) {
    return new FirstLevelEncrypt(packet);
  }

}
