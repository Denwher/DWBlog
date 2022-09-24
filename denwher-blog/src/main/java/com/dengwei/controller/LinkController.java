package com.dengwei.controller;

import com.dengwei.domain.ResponseResult;
import com.dengwei.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;


    @GetMapping("/getAllLink")
    public ResponseResult allLink(){
        return linkService.getAllLink();
    }
}
