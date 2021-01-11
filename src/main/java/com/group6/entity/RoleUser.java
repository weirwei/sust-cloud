package com.group6.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色用户关联表
 * </p>
 *
 * @author xigua
 * @since 2021-01-08
 */
@Data
public class RoleUser{

    private static final long serialVersionUID=1L;

    /**
     * 角色id
     */
      private String roleId;

    /**
     * 用户id
     */
    private String uid;


}
