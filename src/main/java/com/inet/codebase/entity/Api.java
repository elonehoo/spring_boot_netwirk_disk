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
 *      api接口文档
 * </p>
 *
 * @author HCY
 * @since 2020-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_api")
public class Api implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * api接口的序号
     * @author HCY
     * @date 2020-09-18
     */
    @TableId("api_id")
    private String apiId;

    /**
     * api接口的说明
     * @author HCY
     * @date 2020-09-18
     */
    private String apiValue;

    /**
     * api接口的代码
     * @author HCY
     * @date 2020-09-18
     */
    private String apiCode;

    /**
     * api接口的创建时间
     * @author HCY
     * @date 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date apiCreation;

    /**
     * api接口的修改时间
     * @author HCY
     * @date 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date apiModification;


}
