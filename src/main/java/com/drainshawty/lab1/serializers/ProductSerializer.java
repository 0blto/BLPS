package com.drainshawty.lab1.serializers;

import com.drainshawty.lab1.model.shoppingdb.Product;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ProductSerializer extends JsonSerializer<Product> {

    @Override
    public void serialize(Product product, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", product.getName());
        jsonGenerator.writeStringField("description", product.getDescription());
        jsonGenerator.writeNumberField("number", product.getNumber());
        jsonGenerator.writeNumberField("price", product.getPrice());
        jsonGenerator.writeEndObject();
    }
}
