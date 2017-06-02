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

  // Optional properties
  public Element secretKey;
  public Element publicKey;

  public KeyPair(
    Optional<Element> secretKey, Optional<Element> publicKey) {
    secretKey.ifPresent(key -> this.secretKey = key);
    publicKey.ifPresent(key -> this.publicKey = key);
    initialize();
  }

  public static KeyPair fromBytes(
    Optional<byte[]> secretKeyBytes, Optional<byte[]> publicKeyBytes) {

    final Optional<Element>[] keys = new Optional[]{ Optional.empty() };

    secretKeyBytes.ifPresent(bs ->
      keys[0] = Optional.of(keyFromBytes(bs)));
    publicKeyBytes.ifPresent(bs ->
      keys[1] = Optional.of(keyFromBytes(bs)));

    return new KeyPair(keys[0], keys[1]);
  }

  private void initialize() {
    if (this.secretKey != null && this.publicKey == null)
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

  private static Element keyFromBytes(byte[] key) {
    final Field Zq =
      ProxyReencryptionGatewayApplication.globalParameters.getZq();
    return Zq.newElement(new BigInteger(key));
  }

}
