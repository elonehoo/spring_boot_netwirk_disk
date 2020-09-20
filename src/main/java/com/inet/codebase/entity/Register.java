package com.inet.codebase.entity;

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
 *      用户的登录操作
 * </p>
 *
 * @author HCY
 * @since 2020-09-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_register")
public class Register implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户序号
     * @author HCY
     * @since 2020-09-18
     */
    @TableId("register_id")
    private String registerId;

    /**
     * 用户账号
     * @author HCY
     * @since 2020-09-18
     */
    private String registerAccount;

    /**
     * 用户密码
     * @author HCY
     * @since 2020-09-18
     */
    private String registerPassword;

    /**
     * 用户创建时间
     * @author HCY
     * @since 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerCreation;

    /**
     * 用户修改时间
     * @author HCY
     * @since 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerModification;

}
