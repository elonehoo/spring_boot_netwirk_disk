package com.inet.codebase.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author HCY
 * @since 2020-09-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_type")
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类别序号
     */
    @TableId("type_id")
    private String typeId;

    /**
     * 类别名称
     */
    private String typeName;

    /**
     * 类别创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date typeCreation;

    /**
     * 类别修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date typeModification;

    /**
     * 类别的所属人员
     */
    private String typeAffiliation;

    /**
     * 属于类别的文件数
     */
    @TableField(exist = false)
    private Integer count;

}
