package com.inet.codebase.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
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
 *      用户的具体信息
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
@TableName("tbl_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户序号
     * @author HCY
     * @date 2020-09-18
     */
    @TableId("user_id")
    private String userId;

    /**
     * 用户昵称
     * @author HCY
     * @date 2020-09-18
     */
    private String userName;

    /**
     * 用户头像
     * @author HCY
     * @date 2020-09-18
     */
    private String userIcon;

    /**
     * 用户Token
     * @author HCY
     * @date 2020-09-18
     */
    @TableField("user_KID")
    private String userKid;

    /**
     * 用户创建时间
     * @author HCY
     * @date 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date userCreation;

    /**
     * 用户修改时间
     * @author HCY
     * @date 2020-09-18
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date userModification;


}
