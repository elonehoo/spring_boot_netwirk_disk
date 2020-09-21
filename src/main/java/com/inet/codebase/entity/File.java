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
 * 
 * </p>
 *
 * @author HCY
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_file")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件序号
     */
    @TableId("file_id")
    private String fileId;

    /**
     * 文件的uuid+名字
     */
    private String fileUuname;

    /**
     * 文件原本的名称
     */
    private String fileName;

    /**
     * 文件的后缀
     */
    private String filePostfix;

    /**
     * 文件的类别
     */
    private String fileType;

    /**
     * 文件的大小
     */
    private Double fileSize;

    /**
     * 文件的地址
     */
    private String fileSite;

    /**
     * 文件的创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fileCreation;

    /**
     * 文件的修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fileModification;

    /**
     * 文件的所属人
     */
    private String fileAffiliation;


}
