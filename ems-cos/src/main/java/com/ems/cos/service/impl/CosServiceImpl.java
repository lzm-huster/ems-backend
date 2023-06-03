package com.ems.cos.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ems.cos.MinioUtil;
import com.ems.cos.config.MinioConfigProperties;
import com.ems.cos.service.CosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
@Service
public class CosServiceImpl implements CosService {
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private MinioConfigProperties minioConfigProperties;

    /**
     * 上传文件
     *
     * @param file
     * @return 返回文件链接
     */
    @Override
    public String uploadFile(MultipartFile file) {
        return uploadFile(file, null);
    }

    /**
     * 上传文件 - 带文件夹 folder格式  a/b/ 或者 a/
     *
     * @param file
     * @param folder
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file, String folder) {
        if (ObjectUtil.isNull(file) || file.isEmpty()) {
            return null;
        }
        //文件名
        String originalFilename = file.getOriginalFilename();
        //新的文件名 = 时间戳_随机数.后缀名
        if (StringUtils.isBlank(originalFilename)) {
            originalFilename = "temp";
        }
        long now = System.currentTimeMillis() / 1000;
        String fileName = DateUtil.format(DateUtil.date(), "yyyyMMdd") + "_" + now + "_" + new Random().nextInt(1000) +
                originalFilename.substring(originalFilename.lastIndexOf("."));
        // 判断文件夹
        if (StringUtils.isNotBlank(folder)) {
            if (folder.endsWith("/")) {
                fileName = folder + fileName;
            } else {
                fileName = folder + "/" + fileName;
            }

        }
        Boolean upload = minioUtil.upload(file, fileName);
        if (!upload) {
            return null;
        }
        return minioConfigProperties.getEndpoint() + "/" + minioConfigProperties.getBucketName() + "/" + fileName;
    }

    /**
     * 文件批量上传
     * @param files
     * @return
     */
    @Override
    public List<String> batchUpload(MultipartFile[] files) {
        ArrayList<String> paths = new ArrayList<>();
        for (MultipartFile file: files) {
            String res = uploadFile(file);
            if (StringUtils.isNotBlank(res)){
                paths.add(res);
            }
        }
        return paths;
    }

    /**
     * 文件批量上传 -带文件夹
     * @param files
     * @param folder
     * @return
     */
    @Override
    public List<String> batchUpload(MultipartFile[] files, String folder) {
        ArrayList<String> paths = new ArrayList<>();
        for (MultipartFile file: files) {
            String res = uploadFile(file, folder);
            if (StringUtils.isNotBlank(res)){
                paths.add(res);
            }
        }
        return paths;
    }


}
