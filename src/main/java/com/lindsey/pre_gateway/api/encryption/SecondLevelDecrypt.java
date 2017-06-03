package com.lindsey.pre_gateway.api.encryption;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lindsey.pre_gateway.models.EncryptionPacket;

public class SecondLevelDecrypt {

  private final EncryptionPacket encryptionPacket;

  public SecondLevelDecrypt(EncryptionPacket encryptionPacket) {
    this.encryptionPacket = encryptionPacket;
  }

  @JsonProperty
  public byte[] getDecrypted() {
    return encryptionPacket.getSecondLevelDecryption();
  }

}
