package com.ems.business.model.response;


import lombok.Data;

@Data
public class AssetCategoryDetail {
    /**
     * 资产分类Id
     */
    private Integer categoryId;

    /**
     * 品类编码
     */
    private String categoryCode;

    /**
     * 品类名称
     */

    private String categoryName;

    /**
     * 类别描述
     */

    private String categoryDesc;

    /**
     * 单位
     */

    private String unit;

    /**
     * 备注
     */
    private String categoryRemark;

    /**
     * 父级分类Id
     */
    private Integer parentId;

    /**
     * 分类级别
     */

    private Integer categoryLevel;
}
