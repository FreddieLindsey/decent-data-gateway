package com.lindsey.pre_gateway.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lindsey.pre_gateway.ProxyReencryptionGatewayApplication;
import com.lindsey.pre_gateway.deserializers.EncryptionPacketDeserializer;
import nics.crypto.proxy.afgh.AFGHProxyReEncryption;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

@JsonDeserialize(using = EncryptionPacketDeserializer.class)
public class EncryptionPacket {

  final private KeyPair keyPair;
  final private byte[] data;

  private byte[] symmetricKey;
  private byte[] symmetricEncryption;
  private byte[] symmetricDecryption;

  private byte[] firstLevelEncrypted;
  private byte[] secondLevelEncrypted;
  private byte[] reencrypted;

  public EncryptionPacket(KeyPair keyPair, byte[] data) {
    this.keyPair = keyPair;
    this.data = data;
  }

  public byte[] getFirstLevelEncryption() {
    if (symmetricEncryption == null)
      encrypt();

    if (firstLevelEncrypted == null)
      firstLevelEncrypted = AFGHProxyReEncryption.firstLevelEncryption(
        symmetricKey,
        this.keyPair.getPublicKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return mergeByteArrays(firstLevelEncrypted, symmetricEncryption);
  }

  public byte[] getFirstLevelDecryption() {
    if (symmetricDecryption == null) {
      ArrayList<byte[]> parts = splitByteArrays(data);

      symmetricKey = strip(AFGHProxyReEncryption.firstLevelDecryption(
        parts.get(0),
        this.keyPair.getSecretKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      ));
      decrypt();
    }

    return symmetricDecryption;
  }

  public byte[] getSecondLevelEncryption() {
    if (symmetricEncryption == null)
      encrypt();

    if (secondLevelEncrypted == null)
      secondLevelEncrypted = AFGHProxyReEncryption.secondLevelEncryption(
        symmetricKey,
        this.keyPair.getPublicKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return mergeByteArrays(secondLevelEncrypted, symmetricEncryption);
  }

  public byte[] getSecondLevelDecryption() {
    if (symmetricDecryption == null) {
      ArrayList<byte[]> parts = splitByteArrays(data);

      symmetricKey = strip(AFGHProxyReEncryption.secondLevelDecryption(
        parts.get(0),
        this.keyPair.getSecretKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      ));
      decrypt();
    }

    return symmetricDecryption;
  }

  public byte[] getReencryption() {
    ArrayList<byte[]> parts = splitByteArrays(data);

    reencrypted = AFGHProxyReEncryption.reEncryption(
      parts.get(0),
      this.keyPair.getPublicKey().toBytes(),
      ProxyReencryptionGatewayApplication.globalParameters
    );

    return mergeByteArrays(reencrypted, parts.get(1));
  }

  private void encrypt() {

    KeyGenerator KeyGen = null;
    try {
      KeyGen = KeyGenerator.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    //KeyGen.init(128);

    SecretKey symmetricKey = KeyGen.generateKey();

    Cipher aesCipher = null;
    try {
      aesCipher = Cipher.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    }

    try {
      aesCipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    }

    try {
      symmetricEncryption = aesCipher.doFinal(data);
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    this.symmetricKey = symmetricKey.getEncoded();
  }

  private void decrypt() {
    ArrayList<byte[]> parts = splitByteArrays(data);
    SecretKey symmetricKey = new SecretKeySpec(this.symmetricKey, "AES");

    Cipher aesCipher = null;
    try {
      aesCipher = Cipher.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    }

    try {
      aesCipher.init(Cipher.DECRYPT_MODE, symmetricKey);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    }

    try {
      symmetricDecryption = aesCipher.doFinal(parts.get(1));
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
  }

  private byte[] mergeByteArrays(byte[] in1, byte[] in2) {
    byte[] le = ByteBuffer.allocate(8).putInt(in1.length).array();

    byte[] out = new byte[le.length + in1.length + in2.length];

    int counter = 0;
    for (byte b : le) out[counter++] = b;
    for (byte b : in1) out[counter++] = b;
    for (byte b : in2) out[counter++] = b;

    return out;
  }

  private ArrayList<byte[]> splitByteArrays(byte[] in) {
    byte[] inl = new byte[8];
    for (int i = 0; i < inl.length; i++) inl[i] = in[i];
    int split = ByteBuffer.wrap(inl).getInt();

    byte[] keyData = new byte[split];
    byte[] encData = new byte[in.length - split - inl.length];

    int counter = inl.length;
    for (int i = 0; i < keyData.length; i++) keyData[i] = in[counter++];
    for (int i = 0; i < encData.length; i++) encData[i] = in[counter++];

    ArrayList<byte[]> out = new ArrayList<>();
    out.add(keyData);
    out.add(encData);
    return out;
  }

  private byte[] strip(byte[] in) {
    // Find last index
    int last = in.length - 1;
    while (last > 0 && in[last] == 0) last--;

    // Find first index
    int index = 0;
    while (index < in.length && index <= last && in[index] == 0) index++;

    byte[] out = new byte[last - index + 1];
    while (index <= last) {
      out[out.length - (last - index) - 1] = in[index];
      index++;
    }

    return out;
  }

}
