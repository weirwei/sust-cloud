package com.group6.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author xigua
 * @since 2021-01-08
 */
@Data
@AllArgsConstructor
public class User{

    public static final int DELETE = 0;
    public static final int NORMAL = 1;
    public static final int DISABLE = 2;

    private static final long serialVersionUID=1L;

    /**
     * 工号
     */
      private String uid;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像链接
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 部门id
     */
    private String apartment;

    /**
     * 用户状态
(0, 删除)
(1, 正常)
(2, 禁用)
     */
    private Integer status;

    /**
     * 云盘使用量
     */
    private Double diskUsage;


}
