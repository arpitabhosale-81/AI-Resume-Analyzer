package com.resumeai.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResumeController {

    @GetMapping("/api/resume/test")
    public String test() {
        return "Resume AI Backend is working!";
    }
}