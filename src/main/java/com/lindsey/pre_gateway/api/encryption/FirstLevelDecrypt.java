package com.lindsey.pre_gateway.api.encryption;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lindsey.pre_gateway.models.EncryptionPacket;

public class FirstLevelDecrypt {

  private final EncryptionPacket encryptionPacket;

  public FirstLevelDecrypt(EncryptionPacket encryptionPacket) {
    this.encryptionPacket = encryptionPacket;
  }

  @JsonProperty
  public byte[] getDecrypted() {
    return encryptionPacket.getDecryption();
  }

}
