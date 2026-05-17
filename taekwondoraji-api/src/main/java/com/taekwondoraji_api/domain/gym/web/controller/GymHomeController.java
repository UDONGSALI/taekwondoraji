package com.taekwondoraji_api.domain.gym.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gym/{gymId}")
public class GymHomeController {

    @GetMapping
    public String home(@PathVariable Integer gymId) {
        return "redirect:/gym/" + gymId + "/member/info";
    }
}
