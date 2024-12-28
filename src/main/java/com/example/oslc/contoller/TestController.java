package com.example.oslc.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {

    @GetMapping("/path")
    public String testPage(HttpServletRequest httpServletRequest,
                           Model model) {
        return "ServiceProviderCatalog";
    }
}