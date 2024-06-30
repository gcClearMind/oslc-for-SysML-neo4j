package com.example.oslc.util;

import com.alibaba.fastjson2.JSON;

import com.example.oslc.resource.BlockResource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.example.oslc.service.BlockService;
import com.example.oslc.util.JsonUtil;
import org.apache.jena.base.Sys;


public class neo4jUtil {

    private static final String dataPath = "http://localhost:8080/neo4j/";

    public static List<BlockResource> getBlockResourceById(String id) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri
                        (URI.create(dataPath + "block/getBlockById/" + id))
                .build();
        List<BlockResource> data = (List<BlockResource>) JsonUtil.JsonToResource(client, request);
        return  data;
    }

    public static List<BlockResource> getBlockResourceByName(String name) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri
                        (URI.create(dataPath + "block/getBlockByName/" + name))
                .build();
        List<BlockResource> data = (List<BlockResource>) JsonUtil.JsonToResource(client, request);
        return  data;
    }

    public static List<BlockResource> getAllBlockResource() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri
                        (URI.create(dataPath + "block/getAllBlocks"))
                .build();
        List<BlockResource> data = (List<BlockResource>) JsonUtil.JsonToResource(client, request);
        return  data;
    }


    public static void main(String[] args)   {
        System.out.println();
    }
}
