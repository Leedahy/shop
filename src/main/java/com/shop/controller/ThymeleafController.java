package com.shop.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafController {

    @GetMapping("/thymeleaf/ex1")
    public String ex1() {
        return "/thymeleaf/ex1";
    }
}
