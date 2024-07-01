package com.example.oslc.contoller;


import com.alibaba.fastjson2.JSON;
import com.example.oslc.resource.BlockResource;
import com.example.oslc.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/block")
@RestController
public class BlockContoller {

    @Autowired
    private BlockService blockService;


    @GetMapping("/getAllBlockResourceURI")
    public Object getAllBlockResourceURI()  {
        return JSON.toJSON(blockService.getAllResourceURIs());
    }


}
