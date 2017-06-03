package com.lindsey.pre_gateway.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.lindsey.pre_gateway.models.EncryptionPacket;
import com.lindsey.pre_gateway.models.KeyPair;

import java.io.IOException;
import java.util.Optional;

public class EncryptionPacketDeserializer extends JsonDeserializer<EncryptionPacket> {

  @Override
  public EncryptionPacket deserialize(JsonParser p, DeserializationContext ctxt)
    throws IOException, JsonProcessingException {
    ObjectCodec oc = p.getCodec();
    JsonNode node = oc.readTree(p);

    final byte[] data = node.get("data").binaryValue();

    Optional<byte[]> secretKey = Optional.empty();
    Optional<byte[]> publicKey = Optional.empty();

    if (node.get("secretKey") != null)
      secretKey = Optional.of(node.get("secretKey").binaryValue());

    if (node.get("publicKey") != null)
      publicKey = Optional.of(node.get("publicKey").binaryValue());

    if (!(secretKey.isPresent() || publicKey.isPresent()))
      throw new IOException("Must have either secret or public key");

    return new EncryptionPacket(KeyPair.fromBytes(secretKey, publicKey), data);
  }

}
