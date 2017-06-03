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

  public KeyPair() {}

  public KeyPair(
    Optional<Element> secretKey, Optional<Element> publicKey) {
    secretKey.ifPresent(key -> this.secretKey = key);
    publicKey.ifPresent(key -> this.publicKey = key);
  }

  public static KeyPair fromBytes(
    Optional<byte[]> secretKeyBytes, Optional<byte[]> publicKeyBytes) {

    final Optional<Element>[] keys =
      new Optional[]{ Optional.empty(), Optional.empty() };

    secretKeyBytes.ifPresent(bs ->
      keys[0] = Optional.of(secretKeyFromBytes(bs)));
    publicKeyBytes.ifPresent(bs ->
      keys[1] = Optional.of(publicKeyFromBytes(bs)));

    return new KeyPair(keys[0], keys[1]);
  }

  public Element getSecretKey() {
    return secretKey;
  }

  public void generateSecretKey() {
    secretKey = AFGHProxyReEncryption.generateSecretKey(
      ProxyReencryptionGatewayApplication.globalParameters
    );
  }

  public Element getPublicKey() {
    return publicKey;
  }

  public void generatePublicKey() {
    if (secretKey != null)
      publicKey = AFGHProxyReEncryption.generatePublicKey(
        secretKey,
        ProxyReencryptionGatewayApplication.globalParameters
      );
  }

  private static Element secretKeyFromBytes(byte[] key) {
    final Field Zq =
      ProxyReencryptionGatewayApplication.globalParameters.getZq();
    return AFGHProxyReEncryption.bytesToElement(key, Zq);
  }

  private static Element publicKeyFromBytes(byte[] key) {
    final Field G1 =
      ProxyReencryptionGatewayApplication.globalParameters.getG1();
    return AFGHProxyReEncryption.bytesToElement(key, G1);
  }

}
