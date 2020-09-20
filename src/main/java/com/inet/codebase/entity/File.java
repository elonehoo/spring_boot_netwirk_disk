package com.inet.codebase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_file")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件序号
     */
    private String fileId;

    /**
     * 文件原本的名称
     */
    private String fileName;

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
    private LocalDateTime fileCreation;

    /**
     * 文件的修改时间
     */
    private LocalDateTime fileModification;

    /**
     * 文件的所属人
     */
    private String fileAffiliation;


}
