package com.lindsey.pre_gateway.api.key;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unisa.dia.gas.jpbc.Element;
import nics.crypto.proxy.afgh.AFGHGlobalParameters;
import nics.crypto.proxy.afgh.AFGHProxyReEncryption;

public class GenerateSecret {

  private final AFGHGlobalParameters global;

  public GenerateSecret(AFGHGlobalParameters global) {
    this.global = global;
  }

  @JsonProperty
  public byte[] getContent() {
    final Element el = AFGHProxyReEncryption.generateSecretKey(this.global);
    return el.toBytes();
  }

}
