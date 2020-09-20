package com.inet.codebase.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *      类别
 * </p>
 *
 * @author HCY
 * @since 2020-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_type")
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类别序号
     * @author HCY
     * @since 2020-09-18
     */
    @TableId("type_id")
    private String typeId;

    /**
     * 类别名称
     * @author HCY
     * @since 2020-09-18
     */
    private String typeName;

    /**
     * 类别创建时间
     * @author HCY
     * @since 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date typeCreation;

    /**
     * 类别修改时间
     * @author HCY
     * @since 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date typeModification;


}
