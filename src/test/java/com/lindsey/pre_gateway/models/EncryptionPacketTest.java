package com.lindsey.pre_gateway.models;

import org.junit.Test;

public class EncryptionPacketTest {

  @Test
  public void getFirstLevelEncryption() throws Exception {
    KeyPair keyPair = new KeyPair();
    keyPair.generateSecretKey();
    keyPair.generatePublicKey();
    EncryptionPacket packet = new EncryptionPacket(
      keyPair,
      "Hello World\n".getBytes()
    );
    packet.getFirstLevelEncryption();
  }

  @Test
  public void getFirstLevelDecryption() throws Exception {
    KeyPair keyPair = new KeyPair();
    keyPair.generateSecretKey();
    keyPair.generatePublicKey();
    String input = "Hello World\n";
    EncryptionPacket packet = new EncryptionPacket(
      keyPair,
      input.getBytes()
    );
    byte[] encrypted = packet.getFirstLevelEncryption();
    packet = new EncryptionPacket(
      keyPair,
      encrypted
    );
    byte[] decrypted = packet.getFirstLevelDecryption();

    StringBuilder s = new StringBuilder();
    for (byte b : decrypted)
      s.append((char) b);
    assert input.equals(s.toString());
  }

}
