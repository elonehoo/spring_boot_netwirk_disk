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
 *      网站说明
 * </p>
 *
 * @author HCY
 * @since 2020-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_web")
public class Web implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 网站说明序号
     * @author HCY
     * @date 2020-09-18
     */
    @TableId("web_id")
    private String webId;

    /**
     * 网站说明
     * @author HCY
     * @date 2020-09-18
     */
    private String webValue;

    /**
     * 网站说明创建时间
     * @author HCY
     * @date 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date webCreation;

    /**
     * 网站说明修改时间
     * @author HCY
     * @date 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date webModification;


}
