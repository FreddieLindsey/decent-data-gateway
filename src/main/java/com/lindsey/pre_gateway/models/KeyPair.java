package com.lindsey.pre_gateway.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lindsey.pre_gateway.ProxyReencryptionGatewayApplication;
import com.lindsey.pre_gateway.deserializers.KeyPairDeserializer;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import nics.crypto.proxy.afgh.AFGHProxyReEncryption;

import java.math.BigInteger;
import java.util.Optional;

@JsonDeserialize(using = KeyPairDeserializer.class)
public class KeyPair {

  // Mandatory properties
  final public Element secretKey;

  // Optional properties
  public Element publicKey;

  public KeyPair(Element secretKey, Optional<Element> publicKey) {
    this.secretKey = secretKey;

    publicKey.ifPresent(key -> this.publicKey = key);
    initialize();
  }

  public KeyPair(byte[] secretKeyBytes, Optional<byte[]> publicKeyBytes) {
    final Field Zq =
      ProxyReencryptionGatewayApplication.globalParameters.getZq();

    this.secretKey = Zq.newElement(new BigInteger(secretKeyBytes));

    publicKeyBytes.ifPresent(key ->
      this.publicKey = Zq.newElement(new BigInteger(key))
    );

    initialize();
  }

  private void initialize() {
    if (this.publicKey == null)
      this.publicKey =
        AFGHProxyReEncryption.generatePublicKey(
          this.secretKey,
          ProxyReencryptionGatewayApplication.globalParameters
        );
  }

  public Element getSecretKey() {
    return secretKey;
  }

  public Element getPublicKey() {
    return publicKey;
  }

}
