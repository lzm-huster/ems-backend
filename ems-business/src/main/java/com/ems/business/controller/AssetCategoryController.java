package com.ems.business.controller;

import com.ems.annotation.ResponseResult;
import com.ems.business.model.entity.AssetCategory;
import com.ems.business.model.response.AssetCategoryDetail;
import com.ems.business.service.AssetCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@ResponseResult
@RestController
@RequestMapping("/assetCategory")
public class AssetCategoryController {
    @Autowired
    private AssetCategoryService assetCategoryService;

    @GetMapping("/list")
    public List<AssetCategoryDetail> getAssetCategoryList(){
        List<AssetCategory> assetCategories = assetCategoryService.list();
        List<AssetCategoryDetail> collect = assetCategories.stream().map(assetCategory -> {
            AssetCategoryDetail assetCategoryDetail = new AssetCategoryDetail();
            BeanUtils.copyProperties(assetCategory, assetCategoryDetail);
            return assetCategoryDetail;
        }).collect(Collectors.toList());
        return collect;
    }
}
