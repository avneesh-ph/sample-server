package com.pharynxai.julius.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharynxai.julius.server.dto.SamplePayload;
import com.pharynxai.julius.server.services.HelloWorldService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/")
public class HelloWorldController {
    
    @Autowired
    private HelloWorldService helloWorldService;

    @GetMapping
    public String RootController() {
        return helloWorldService.HelloWorld();
    }

    @PostMapping()
    public SamplePayload PostRoot(@RequestBody SamplePayload entity) {
        return helloWorldService.InnerHelloWorldService(entity);
    }
    
}
