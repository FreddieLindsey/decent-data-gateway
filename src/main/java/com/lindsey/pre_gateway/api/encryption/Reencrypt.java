package com.lindsey.pre_gateway.api.encryption;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lindsey.pre_gateway.models.EncryptionPacket;

public class Reencrypt {

  private final EncryptionPacket encryptionPacket;

  public Reencrypt(EncryptionPacket encryptionPacket) {
    this.encryptionPacket = encryptionPacket;
  }

  @JsonProperty
  public byte[] getReencrypted() {
    return encryptionPacket.getReencryption();
  }

}