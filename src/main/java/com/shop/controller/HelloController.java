package com.shop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
//@Controller
public class HelloController {

    //@RequestMapping(value = "/", method = RequestMethod.GET)
    @GetMapping("/")
//    public Point hello() {
//        Point p = new Point(10,20);
//        return p;

    public String hello () {
        return "hello";

    }
}
