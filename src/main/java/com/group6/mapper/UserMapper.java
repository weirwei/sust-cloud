package com.group6.mapper;

import com.group6.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author xigua
 * @since 2021-01-08
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
