package com.lindsey.pre_gateway.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.lindsey.pre_gateway.models.EncryptionPacket;

import java.io.IOException;

public class EncryptionPacketDeserializer extends JsonDeserializer<EncryptionPacket> {

  @Override
  public EncryptionPacket deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectCodec oc = p.getCodec();
    JsonNode node = oc.readTree(p);

    final byte[] key = node.get("key").binaryValue();
    final byte[] data = node.get("data").binaryValue();

    return new EncryptionPacket(key, data);
  }

}
