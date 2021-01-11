package com.group6.service;

import com.fehead.lang.error.BusinessException;
import com.group6.controller.view.UserVO;
import com.group6.entity.User;

import java.util.List;

/**
 * @ClassName: UserService
 * @Description:
 * @Author: 西瓜
 * @Date: 2021/1/8 19:13
 */
public interface UserService {
   UserVO getUser(String phone);
   UserVO getUserByPassword(String jobId,String password) throws BusinessException;
   Integer updatePassword(String password,String jobId,String telephone) throws BusinessException;
   UserVO getUserInfo(String telephone);
   List<UserVO> getAllUserInfo(Integer page);
   void insertUser(User user) throws BusinessException;
}
