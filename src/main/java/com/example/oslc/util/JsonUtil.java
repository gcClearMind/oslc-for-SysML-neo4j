package com.example.oslc.util;

import com.example.oslc.resource.BlockResource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;



import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class JsonUtil {
    public static List<BlockResource> JsonToResource(HttpClient client, HttpRequest request) {
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
}
