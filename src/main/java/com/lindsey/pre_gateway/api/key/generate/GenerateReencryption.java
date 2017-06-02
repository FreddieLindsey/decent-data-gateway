package com.lindsey.pre_gateway.api.key.generate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lindsey.pre_gateway.models.KeyPair;
import it.unisa.dia.gas.jpbc.Element;
import nics.crypto.proxy.afgh.AFGHProxyReEncryption;

public class GenerateReencryption {

  private final Element secretKey;
  private final Element publicKey;

  public GenerateReencryption(KeyPair keyPair) {
    this.secretKey = keyPair.getSecretKey();
    this.publicKey = keyPair.getPublicKey();
  }

  @JsonProperty
  public byte[] getReencryptionKey() {
    return AFGHProxyReEncryption.generateReEncryptionKey(
      this.publicKey, this.secretKey
    ).toBytes();
  }

}
