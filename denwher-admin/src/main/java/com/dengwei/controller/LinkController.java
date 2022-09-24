package com.dengwei.controller;

import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.LinkDto;
import com.dengwei.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Denwher
 * @version 1.0
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult linkList(Integer pageNum, Integer pageSize, String name, String status){
        return linkService.getLinkPageList(pageNum,pageSize,name,status);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody LinkDto linkDto){
        return linkService.addLink(linkDto);
    }

    @GetMapping("{id}")
    public ResponseResult getLinkById(@PathVariable("id") Long id){
        return linkService.getLinkById(id);
    }

    @DeleteMapping("{id}")
    public ResponseResult deleteLink(@PathVariable("id") String id){
        return linkService.deleteLink(id);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkDto linkDto){
        return linkService.updateLink(linkDto);
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody LinkDto linkDto){
        return linkService.changeLinkStatus(linkDto);
    }
}
