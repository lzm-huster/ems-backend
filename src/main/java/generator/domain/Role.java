package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 角色表
 * @TableName Role
 */
@TableName(value ="Role")
@Data
public class Role implements Serializable {
    /**
     * 角色编号
     */
    @TableId(value = "RoleID", type = IdType.AUTO)
    private Integer roleID;

    /**
     * 角色名称
     */
    @TableField(value = "RoleName")
    private String roleName;

    /**
     * 角色描述
     */
    @TableField(value = "RoleDescription")
    private String roleDescription;

    /**
     * 是否删除
     */
    @TableField(value = "IsDeleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CreateTime")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "UpdateTime")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}