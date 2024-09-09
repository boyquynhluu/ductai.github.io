package com.qlsv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DiemController {

    @GetMapping("/diems")
    public ModelAndView index() {
        return new ModelAndView("diem/diem");
    }
}