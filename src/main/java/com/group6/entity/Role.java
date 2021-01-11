package com.group6.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author xigua
 * @since 2021-01-08
 */
@Data
public class Role {

    private static final long serialVersionUID=1L;

    /**
     * 角色id
(10001, leader)
(10002, manager)
(10003, member)
     */
      private String roleId;

    /**
     * 权限字符串
     */
    private String privilege;


}
