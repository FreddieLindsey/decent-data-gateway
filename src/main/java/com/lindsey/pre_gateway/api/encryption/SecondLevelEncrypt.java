package com.lindsey.pre_gateway.api.encryption;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lindsey.pre_gateway.models.EncryptionPacket;

public class SecondLevelEncrypt {

  private final EncryptionPacket encryptionPacket;

  public SecondLevelEncrypt(EncryptionPacket encryptionPacket) {
    this.encryptionPacket = encryptionPacket;
  }

  @JsonProperty
  public byte[] getEncrypted() {
    return encryptionPacket.getSecondLevelEncryption();
  }

}
