package com.ems.business.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 资产分类表
 * @TableName AssetCategory
 */
@TableName(value ="AssetCategory")
@Data
public class AssetCategory implements Serializable {
    /**
     * 资产分类Id
     */
    @TableId(value = "CategoryId", type = IdType.AUTO)
    private Integer categoryId;

    /**
     * 品类编码
     */
    @TableField(value = "CategoryCode")
    private String categoryCode;

    /**
     * 品类名称
     */
    @TableField(value = "CategoryName")
    private String categoryName;

    /**
     * 类别描述
     */
    @TableField(value = "CategoryDesc")
    private String categoryDesc;

    /**
     * 单位
     */
    @TableField(value = "Unit")
    private String unit;

    /**
     * 备注
     */
    @TableField(value = "CategoryRemark")
    private String categoryRemark;

    /**
     * 父级分类Id
     */
    @TableField(value = "ParentId")
    private Integer parentId;

    /**
     * 分类级别
     */
    @TableField(value = "CategoryLevel")
    private Integer categoryLevel;

    /**
     * 是否删除
     */
    @TableField(value = "IsDeleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CreateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UpdateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}