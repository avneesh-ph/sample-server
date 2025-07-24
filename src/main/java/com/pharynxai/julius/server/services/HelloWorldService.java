package com.pharynxai.julius.server.services;

import org.springframework.stereotype.Service;

import com.pharynxai.julius.dto.SamplePayload;

@Service
public class HelloWorldService {
   
   public String HelloWorld() {
    return "Hello World";
   }

   public SamplePayload InnerHelloWorldService(SamplePayload entity) {
      return new SamplePayload("data", entity.value());
   }

}
