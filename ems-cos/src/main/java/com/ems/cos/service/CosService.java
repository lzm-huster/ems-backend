package com.ems.cos.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CosService {

    /**
     * 上传文件
     * @param file
     * @return
     */
    String uploadFile(MultipartFile file);

    /**
     * 上传文件-带文件夹
     * @param file
     * @param folder
     * @return
     */
    String uploadFile(MultipartFile file,String folder);

    /**
     * 文件批量上传
     * @param files
     * @return
     */
    List<String> batchUpload(MultipartFile[] files);


    /**
     * 文件批量上传 -带文件夹
     * @param files
     * @param folder
     * @return
     */

    List<String> batchUpload(MultipartFile[] files,String folder);
}
