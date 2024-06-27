package com.auth.security.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    public DemoController() {
        logger.info("DemoController initialized");
    }

    @GetMapping
    public ResponseEntity<String> sayHello(){
        logger.info("sayHello endpoint was called");
        return ok("Hello World");
    }
}
