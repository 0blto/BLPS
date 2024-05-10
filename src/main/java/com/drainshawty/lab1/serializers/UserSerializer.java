package com.drainshawty.lab1.serializers;

import com.drainshawty.lab1.model.Product;
import com.drainshawty.lab1.model.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class UserSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("userId", user.getUserId());
        jsonGenerator.writeStringField("name", user.getName());
        jsonGenerator.writeStringField("email", user.getEmail());
        jsonGenerator.writeEndObject();
    }
}
