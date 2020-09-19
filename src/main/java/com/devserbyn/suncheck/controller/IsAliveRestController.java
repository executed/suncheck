package com.devserbyn.suncheck.controller;

import com.devserbyn.suncheck.model.IsAliveResponseEntity;
import com.devserbyn.suncheck.service.IsAliveService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/isalive")
@RequiredArgsConstructor
public class IsAliveRestController {

    private final IsAliveService isAliveService;

    @PostMapping
    public IsAliveResponseEntity isAlive() {
        return isAliveService.processIsAlive();
    }
}
