package com.lindsey.pre_gateway.api.key.generate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lindsey.pre_gateway.models.KeyPair;
import it.unisa.dia.gas.jpbc.Element;

public class GeneratePublic {
  private final Element publicKey;

  public GeneratePublic(KeyPair keyPair) {
    this.publicKey = keyPair.getPublicKey();
  }

  @JsonProperty
  public byte[] getPublicKey() {
    return publicKey.toBytes();
  }

}
