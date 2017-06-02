package com.lindsey.pre_gateway.api.key.generate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lindsey.pre_gateway.ProxyReencryptionGatewayApplication;
import com.lindsey.pre_gateway.models.KeyPair;
import it.unisa.dia.gas.jpbc.Element;
import nics.crypto.proxy.afgh.AFGHProxyReEncryption;

import java.util.Optional;

public class GenerateSecret {

  private final Element secretKey;
  private final Element publicKey;

  public GenerateSecret() {
    this.secretKey = AFGHProxyReEncryption.generateSecretKey(
      ProxyReencryptionGatewayApplication.globalParameters
    );
    this.publicKey = new KeyPair(
      Optional.of(secretKey), Optional.empty()).getPublicKey();
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
