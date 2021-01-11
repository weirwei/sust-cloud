package com.group6.mapper;

import com.group6.entity.RoleUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色用户关联表 Mapper 接口
 * </p>
 *
 * @author xigua
 * @since 2021-01-08
 */
@Mapper
public interface RoleUserMapper extends BaseMapper<RoleUser> {

}
