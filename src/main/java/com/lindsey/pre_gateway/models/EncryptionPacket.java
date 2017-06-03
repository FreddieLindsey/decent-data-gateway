package com.lindsey.pre_gateway.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lindsey.pre_gateway.ProxyReencryptionGatewayApplication;
import com.lindsey.pre_gateway.deserializers.EncryptionPacketDeserializer;
import nics.crypto.proxy.afgh.AFGHProxyReEncryption;

@JsonDeserialize(using = EncryptionPacketDeserializer.class)
public class EncryptionPacket {

  final private KeyPair keyPair;
  final private byte[] data;

  private byte[] firstLevelEncrypted;
  private byte[] firstLevelDecrypted;
  private byte[] secondLevelEncrypted;
  private byte[] secondLevelDecrypted;
  private byte[] reencrypted;

  public EncryptionPacket(KeyPair keyPair, byte[] data) {
    this.keyPair = keyPair;
    this.data = data;
  }

  public byte[] getFirstLevelEncryption() {
    if (firstLevelEncrypted == null)
      firstLevelEncrypted = AFGHProxyReEncryption.firstLevelEncryption(
        this.data,
        this.keyPair.getPublicKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return firstLevelEncrypted;
  }

  public byte[] getFirstLevelDecryption() {
    if (firstLevelDecrypted == null)
      firstLevelDecrypted = strip(AFGHProxyReEncryption.firstLevelDecryption(
        this.data,
        this.keyPair.getSecretKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      ));

    return firstLevelDecrypted;
  }

  public byte[] getSecondLevelEncryption() {
    if (secondLevelEncrypted == null)
      secondLevelEncrypted = AFGHProxyReEncryption.secondLevelEncryption(
        this.data,
        this.keyPair.getPublicKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return secondLevelEncrypted;
  }

  public byte[] getSecondLevelDecryption() {
    if (secondLevelDecrypted == null)
      secondLevelDecrypted = AFGHProxyReEncryption.secondLevelDecryption(
        this.data,
        this.keyPair.getSecretKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return secondLevelDecrypted;
  }

  public byte[] getReencryption() {
    if (reencrypted == null)
      reencrypted = AFGHProxyReEncryption.reEncryption(
        this.data,
        this.keyPair.getPublicKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return reencrypted;
  }

  private byte[] strip(byte[] in) {
    // Find last index
    int last = in.length - 1;
    while (in[last] == 0 && last >= 0) last--;

    // Find first index
    int index = 0;
    while (in[index] == 0 && index < in.length) index++;

    byte[] out = new byte[last - index + 1];
    while (index <= last) {
      out[out.length - (last - index) - 1] = in[index];
      index++;
    }

    return out;
  }

}
