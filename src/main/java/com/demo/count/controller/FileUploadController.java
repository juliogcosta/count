package com.demo.count.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.count.Observables;
import com.demo.count.service.FileStorageService;

@RestController
public class FileUploadController 
{
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestHeader(name = "X-Secret", required = true) String secret, 
    		@RequestParam("file") MultipartFile file) throws Exception 
    {
		if (secret == null || !secret.equals(Observables.getSecret())) 
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

        fileStorageService.storeFile(file);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/compute")
    public ResponseEntity<String> count(@RequestHeader(name = "X-Secret", required = true) String secret) throws Exception 
    {
		if (secret == null || !secret.equals(Observables.getSecret())) 
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

        

        return ResponseEntity.ok().build();
    }
}
