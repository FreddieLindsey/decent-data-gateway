package com.lindsey.pre_gateway.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lindsey.pre_gateway.ProxyReencryptionGatewayApplication;
import com.lindsey.pre_gateway.deserializers.EncryptionPacketDeserializer;
import nics.crypto.proxy.afgh.AFGHProxyReEncryption;

@JsonDeserialize(using = EncryptionPacketDeserializer.class)
public class EncryptionPacket {

  final private KeyPair keyPair;
  final private byte[] data;

  private byte[] encrypted;
  private byte[] decrypted;

  public EncryptionPacket(KeyPair keyPair, byte[] data) {
    this.keyPair = keyPair;
    this.data = data;
  }

  public byte[] getEncryption() {
    if (encrypted == null)
      encrypted = AFGHProxyReEncryption.firstLevelEncryption(
        this.data,
        this.keyPair.getPublicKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return encrypted;
  }

  public byte[] getDecryption() {
    if (decrypted == null)
      decrypted = AFGHProxyReEncryption.firstLevelDecryption(
        this.data,
        this.keyPair.getSecretKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return decrypted;
  }

}
