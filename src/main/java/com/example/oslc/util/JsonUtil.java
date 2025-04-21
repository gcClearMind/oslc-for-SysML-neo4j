package com.example.oslc.util;

import com.example.oslc.resource.BlockResource;
import com.example.oslc.resource.NodeResource;
import com.example.oslc.resource.Property;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Node;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonUtil {
    public static List<BlockResource> JsonToBlockResource(HttpClient client, HttpRequest request) {
        List<BlockResource> data = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            data = objectMapper.readValue(response.body(), new TypeReference<List<BlockResource>>() {
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static List<NodeResource> JsonToNodeResource(HttpClient client, HttpRequest request) {
        List<NodeResource> data = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            data = objectMapper.readValue(response.body(), new TypeReference<List<NodeResource>>() {
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static List<Property> convert(Map<String, Object> map) {
        List<Property> properties = new ArrayList<>();
        if (map == null || map.isEmpty()) {
            return properties;
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Property property = new Property();
            property.setKey(entry.getKey());
            property.setValue(entry.getValue());
            properties.add(property);
        }

        return properties;
    }


    public static Map<String, Object> reverseConvert(List<Property> properties) {
        return properties.stream()
                .collect(Collectors.toMap(Property::getKey, Property::getValue));
    }
}
