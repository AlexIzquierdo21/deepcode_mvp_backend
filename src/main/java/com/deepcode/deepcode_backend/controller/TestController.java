package com.deepcode.deepcode_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deepcode")
public class TestController {
    @GetMapping("/test")
    public String saludar() {
        return "Hola desde el BackEnd";
    }
}
