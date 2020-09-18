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
 *      文件
 * </p>
 *
 * @author HCY
 * @since 2020-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_file")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件序号
     * @author HCY
     * @date 2020-09-18
     */
    @TableId("file_id")
    private String fileId;

    /**
     * 文件原本的名称
     * @author HCY
     * @date 2020-09-18
     */
    private String fileName;

    /**
     * 文件的类别
     * @author HCY
     * @date 2020-09-18
     */
    private String fileType;

    /**
     * 文件的大小
     * @author HCY
     * @date 2020-09-18
     */
    private Double fileSize;

    /**
     * 文件的地址
     * @author HCY
     * @date 2020-09-18
     */
    private String fileSite;

    /**
     * 文件的创建时间
     * @author HCY
     * @date 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fileCreation;

    /**
     * 文件的修改时间
     * @author HCY
     * @date 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fileModification;


}
