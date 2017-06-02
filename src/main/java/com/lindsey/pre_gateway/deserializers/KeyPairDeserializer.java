package com.lindsey.pre_gateway.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.lindsey.pre_gateway.models.KeyPair;

import java.io.IOException;
import java.util.Optional;

public class KeyPairDeserializer extends JsonDeserializer<KeyPair> {

  @Override
  public KeyPair deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectCodec oc = p.getCodec();
    JsonNode node = oc.readTree(p);

    // Optional properties
    Optional<byte[]> secretKey = Optional.empty();
    if (node.get("secretKey") != null)
      secretKey = Optional.of(node.get("secretKey").binaryValue());
    Optional<byte[]> publicKey = Optional.empty();
    if (node.get("publicKey") != null)
      publicKey = Optional.of(node.get("publicKey").binaryValue());

    return KeyPair.fromBytes(secretKey, publicKey);
  }

}
