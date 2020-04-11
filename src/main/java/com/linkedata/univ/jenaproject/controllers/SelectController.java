package com.linkedata.univ.jenaproject.controllers;

import com.linkedata.univ.jenaproject.services.QueryObject;
import com.linkedata.univ.jenaproject.services.QueryService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/select")
public class SelectController {

    @GetMapping(value = "/test")
    public String test() {
        return "I'm alive";
    }
}
