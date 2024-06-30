package com.example.oslc.contoller;


import com.example.oslc.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/Block")
@RestController
public class BlockContoller {

    @Autowired
    private BlockService blockService;

}
