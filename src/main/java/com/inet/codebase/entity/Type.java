package com.inet.codebase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

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
    private String typeId;

    /**
     * 类别名称
     */
    private String typeName;

    /**
     * 类别创建时间
     */
    private Date typeCreation;

    /**
     * 类别修改时间
     */
    private Date typeModification;

    /**
     * 类别的所属人员
     */
    private String typeAffiliation;


}
