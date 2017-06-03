package com.lindsey.pre_gateway.api.key.generate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lindsey.pre_gateway.models.KeyPair;
import it.unisa.dia.gas.jpbc.Element;

public class GenerateSecret {

  private final Element secretKey;
  private final Element publicKey;

  public GenerateSecret() {
    KeyPair keyPair = new KeyPair();
    keyPair.generateSecretKey();
    keyPair.generatePublicKey();

    secretKey = keyPair.getSecretKey();
    publicKey = keyPair.getPublicKey();
  }

  @JsonProperty
  public byte[] getSecretKey() {
    return secretKey.toBytes();
  }

  @JsonProperty
  public byte[] getPublicKey() {
    return publicKey.toBytes();
  }

}
