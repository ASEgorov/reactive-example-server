package com.asegorov.reactive.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Afh on 18.08.2016.
 */
@RestController
public class EntryPointController {
    @RequestMapping(value = "/")
    public String starter() {
        return "forward:/index.html";
    }

}
