package com.lindsey.pre_gateway.models;

import org.junit.Test;

public class EncryptionPacketTest {

  @Test
  public void getEncryption() throws Exception {
    KeyPair keyPair = new KeyPair();
    keyPair.generatePublicKey();
    EncryptionPacket packet = new EncryptionPacket(
      keyPair,
      "Hello World\n".getBytes()
    );
    packet.getEncryption();
  }

  @Test
  public void getDecryption() throws Exception {
    KeyPair keyPair = new KeyPair();
    keyPair.generateSecretKey();
    keyPair.generatePublicKey();
    EncryptionPacket packet = new EncryptionPacket(
      keyPair,
      "Hello World\n".getBytes()
    );
    byte[] encrypted = packet.getEncryption();

  }

}
