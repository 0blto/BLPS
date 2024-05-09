package com.drainshawty.lab1.serializers;

import com.drainshawty.lab1.model.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {
    public UserSerializer() { this(null); }

    public UserSerializer(Class<User> t) { super(t); }

    @Override
    public void serialize(User user, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("userId", user.getUserId());
        jgen.writeFieldName("adverts");
        jgen.writeStartArray();
        jgen.writeEndArray();
        jgen.writeEndObject();
    }
}
