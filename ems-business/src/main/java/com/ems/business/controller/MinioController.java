package com.ems.business.controller;


import com.ems.cos.config.MinioConfigProperties;
import com.ems.cos.MinioUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/minio")
public class MinioController {

    @Autowired
    private MinioConfigProperties minioProperties;
    @Autowired
    private MinioUtil minioUtil;

    @SneakyThrows
    @PostMapping(value = "/upload")
    public String upload(@RequestParam(name = "file") MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        if(!minioUtil.bucketExists()){
            minioUtil.createBucket(minioProperties.getBucketName());
        }
        minioUtil.upload( multipartFile, fileName);
        String filePath = minioProperties.getEndpoint()+"/"+minioProperties.getBucketName()+"/"+fileName;
        return filePath;
    }
}
