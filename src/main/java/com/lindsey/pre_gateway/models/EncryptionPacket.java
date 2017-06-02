package com.lindsey.pre_gateway.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lindsey.pre_gateway.ProxyReencryptionGatewayApplication;
import com.lindsey.pre_gateway.deserializers.EncryptionPacketDeserializer;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import nics.crypto.proxy.afgh.AFGHProxyReEncryption;

import java.math.BigInteger;

@JsonDeserialize(using = EncryptionPacketDeserializer.class)
public class EncryptionPacket {

  final private Element key;
  final private byte[] data;

  private byte[] encrypted;
  private byte[] decrypted;

  public EncryptionPacket(Element key, byte[] data) {
    this.key = key;
    this.data = data;
  }

  public EncryptionPacket(byte[] key, byte[] data) {
    final Field Zq =
      ProxyReencryptionGatewayApplication.globalParameters.getZq();

    this.key = Zq.newElement(new BigInteger(key));
    this.data = data;
  }

  public byte[] getEncryption() {
    if (encrypted == null)
      encrypted = AFGHProxyReEncryption.firstLevelEncryption(
        this.data,
        this.key.toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return encrypted;
  }

  public byte[] getDecryption() {
    if (decrypted == null)
      decrypted = AFGHProxyReEncryption.firstLevelDecryption(
        this.data,
        this.key.toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return decrypted;
  }

}
