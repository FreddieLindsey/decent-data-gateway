package com.lindsey.pre_gateway.resources.encryption;

import com.codahale.metrics.annotation.Timed;
import com.lindsey.pre_gateway.api.encryption.FirstLevelDecrypt;
import com.lindsey.pre_gateway.api.encryption.FirstLevelEncrypt;
import com.lindsey.pre_gateway.models.EncryptionPacket;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/encryption/decrypt")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FirstLevelDecryptResource {

  @POST
  @Timed
  public FirstLevelDecrypt firstLevelDecrypt(EncryptionPacket packet) {
    return new FirstLevelDecrypt(packet);
  }

}
