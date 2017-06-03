package com.lindsey.pre_gateway.models;

import com.lindsey.pre_gateway.api.key.generate.GenerateReencryption;
import org.junit.Test;

import java.util.Optional;

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

  @Test
  public void e2e() throws Exception {
    // Generate keys for a, b
    KeyPair delegator = new KeyPair();
    delegator.generateSecretKey();
    delegator.generatePublicKey();

    KeyPair delegatee = new KeyPair();
    delegatee.generateSecretKey();
    delegatee.generatePublicKey();

    String input = "Hello World\n";

    EncryptionPacket packet;

    // Encrypt m with pk_a
    packet = new EncryptionPacket(
      delegator,
      input.getBytes()
    );
    byte[] secondLevelEncryption = packet.getSecondLevelEncryption();

    // Generate re_(a -> b)
    KeyPair reencryptionKeyPair = new KeyPair(
      Optional.of(delegator.getSecretKey()),
      Optional.of(delegatee.getPublicKey())
    );

    byte[] reencryptionKey =
      new GenerateReencryption(reencryptionKeyPair).getReencryptionKey();
    reencryptionKeyPair = KeyPair.fromBytes(
      Optional.empty(),
      Optional.of(reencryptionKey)
    );

    // Reencrypt
    packet = new EncryptionPacket(
      reencryptionKeyPair,
      secondLevelEncryption
    );
    byte[] reencrypted = packet.getReencryption();

    // Decrypt m with sk_b
    packet = new EncryptionPacket(
      delegatee,
      reencrypted
    );
    byte[] decrypted = packet.getFirstLevelDecryption();

    assert decrypted.equals(input.getBytes());
  }

}
