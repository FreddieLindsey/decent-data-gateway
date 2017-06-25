package com.lindsey.pre_gateway.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lindsey.pre_gateway.ProxyReencryptionGatewayApplication;
import com.lindsey.pre_gateway.deserializers.EncryptionPacketDeserializer;
import nics.crypto.proxy.afgh.AFGHGlobalParameters;
import nics.crypto.proxy.afgh.AFGHProxyReEncryption;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

@JsonDeserialize(using = EncryptionPacketDeserializer.class)
public class EncryptionPacket {

  private final static int SIZE_LENGTH = 8;

  final private KeyPair keyPair;
  final private byte[] data;
  private ArrayList<byte[]> parts;

  private byte[] symmetricKey;
  private byte[] iv;
  private byte[] symmetricEncryption;
  private byte[] symmetricDecryption;

  private byte[] firstLevelEncryptedKey;
  private byte[] secondLevelEncryptedKey;
  private byte[] reencryptedKey;

  private byte[] firstLevelEncryptedIV;
  private byte[] secondLevelEncryptedIV;
  private byte[] reencryptedIV;

  public EncryptionPacket(KeyPair keyPair, byte[] data) {
    this.keyPair = keyPair;
    this.data = data;
  }

  public byte[] getFirstLevelEncryption() {
    if (symmetricEncryption == null)
      encrypt();

    if (firstLevelEncryptedKey == null)
      firstLevelEncryptedKey = AFGHProxyReEncryption.firstLevelEncryption(
        symmetricKey,
        this.keyPair.getPublicKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

      firstLevelEncryptedIV = AFGHProxyReEncryption.firstLevelEncryption(
        iv,
        this.keyPair.getPublicKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return mergeByteArrays(
      firstLevelEncryptedKey, firstLevelEncryptedIV,
      symmetricEncryption);
  }

  public byte[] getFirstLevelDecryption() {
    if (symmetricDecryption == null) {
      splitByteArrays(data);

      symmetricKey = strip(AFGHProxyReEncryption.firstLevelDecryption(
        parts.get(0),
        this.keyPair.getSecretKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      ));

      iv = strip(AFGHProxyReEncryption.firstLevelDecryption(
        parts.get(1),
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

    if (secondLevelEncryptedKey == null)
      secondLevelEncryptedKey = AFGHProxyReEncryption.secondLevelEncryption(
        symmetricKey,
        this.keyPair.getPublicKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

      secondLevelEncryptedIV = AFGHProxyReEncryption.secondLevelEncryption(
        iv,
        this.keyPair.getPublicKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      );

    return mergeByteArrays(
      secondLevelEncryptedKey, secondLevelEncryptedIV,
      symmetricEncryption);
  }

  public byte[] getSecondLevelDecryption() {
    if (symmetricDecryption == null) {
      splitByteArrays(data);

      symmetricKey = strip(AFGHProxyReEncryption.secondLevelDecryption(
        parts.get(0),
        this.keyPair.getSecretKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      ));

      iv = strip(AFGHProxyReEncryption.secondLevelDecryption(
        parts.get(1),
        this.keyPair.getSecretKey().toBytes(),
        ProxyReencryptionGatewayApplication.globalParameters
      ));

      decrypt();
    }

    return symmetricDecryption;
  }

  public byte[] getReencryption() {
    splitByteArrays(data);

    reencryptedKey = AFGHProxyReEncryption.reEncryption(
      parts.get(0),
      this.keyPair.getPublicKey().toBytes(),
      ProxyReencryptionGatewayApplication.globalParameters
    );

    reencryptedIV = AFGHProxyReEncryption.reEncryption(
      parts.get(1),
      this.keyPair.getPublicKey().toBytes(),
      ProxyReencryptionGatewayApplication.globalParameters
    );

    return mergeByteArrays(reencryptedKey, reencryptedIV, parts.get(2));
  }

  private void encrypt() {
    KeyGenerator KeyGen = null;
    try {
      KeyGen = KeyGenerator.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    Cipher aesCipher = null;
    try {
      aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    }

    SecretKey symmetricKey = KeyGen.generateKey();
    IvParameterSpec ivParameterSpec =
      new IvParameterSpec(SecureRandom.getSeed(16));

    try {
      aesCipher.init(Cipher.ENCRYPT_MODE, symmetricKey, ivParameterSpec);
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
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
    this.iv = ivParameterSpec.getIV();
  }

  private void decrypt() {
    SecretKey symmetricKey = new SecretKeySpec(this.symmetricKey, "AES");
    IvParameterSpec ivParameterSpec = new IvParameterSpec(this.iv);

    Cipher aesCipher = null;
    try {
      aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    }

    try {
      aesCipher.init(Cipher.DECRYPT_MODE, symmetricKey, ivParameterSpec);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }

    try {
      symmetricDecryption = aesCipher.doFinal(parts.get(2));
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
  }

  private byte[] mergeByteArrays(byte[]... inputs) {
    int outSize = 0;
    for (byte[] i : inputs)
      outSize += i.length + SIZE_LENGTH;
    byte[] out = new byte[outSize];

    int counter = 0;
    for (byte[] i : inputs) {
      byte[] size = ByteBuffer.allocate(SIZE_LENGTH).putInt(i.length).array();
      for (byte b : size) out[counter++] = b;
      for (byte b : i)    out[counter++] = b;
    }

    return out;
  }

  private ArrayList<byte[]> splitByteArrays(byte[] in) {
    int counter = 0;
    ArrayList<byte[]> out = new ArrayList<>();
    while (counter < in.length) {
      byte[] size = new byte[SIZE_LENGTH];
      for (int i = 0; i < SIZE_LENGTH; i++) size[i] = in[counter++];
      int split = ByteBuffer.wrap(size).getInt();
      byte[] part = new byte[split];
      for (int i = 0; i < split; i++) part[i] = in[counter++];
      out.add(part);
    }

    parts = out;
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
