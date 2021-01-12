package com.group6.controller.view;

import com.sun.tracing.dtrace.ArgsAttributes;
import lombok.Data;

/**
 * @ClassName: UserVO
 * @Description:
 * @Author: 西瓜
 * @Date: 2021/1/8 22:27
 */
@Data
public class UserVO {
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
     * 部门id
     */
    private String apartment;

    /**
     * 是否首次登录
     * 1. 首次登陆
     * 0. 非首次登录
     */
    private int first;
}
