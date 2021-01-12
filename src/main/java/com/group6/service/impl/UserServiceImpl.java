package com.group6.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.group6.controller.view.UserPageVO;
import com.group6.controller.view.UserVO;
import com.group6.mapper.UserMapper;
import com.group6.entity.User;
import com.group6.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UserServiceImpl
 * @Description:
 * @Author: 西瓜
 * @Date: 2021/1/8 19:14
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserVO getUser(String phone, String firstLogin) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("telephone",phone);
        //int count=userMapper.selectCount(queryWrapper);
        User user=userMapper.selectOne(queryWrapper);
        UserVO userVO = transformToVo(user);
        if (firstLogin.equals(user.getPassword())) {
            userVO.setFirst(1);
        } else {
            userVO.setFirst(0);
        }
        return userVO;
    }

    @Override
    public UserVO getUserByPassword(String jobId, String password) throws BusinessException {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("uid",jobId).or().eq("telephone",jobId);
        queryWrapper.eq("password",password);

        User user=userMapper.selectOne(queryWrapper);
        if(user==null || !user.getPassword().equals(password)){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST,"用户id或密码错误");
        }
        return transformToVo(user);
    }

    @Override
    public Integer updatePassword(String password, String jobId,String telephone) throws BusinessException {
        LambdaUpdateWrapper<User> updateWrapper=new UpdateWrapper().lambda();
        updateWrapper.eq(User::getUid,jobId);
        User user=userMapper.selectOne(updateWrapper);
        user.setPassword(password);
        int count=0;
        try {
            count=userMapper.update(user,updateWrapper);
        }catch (Exception e){
             throw new BusinessException(EmBusinessError.JSON_SEQUENCE_WRONG,"修改密码失败");
        }

        return count;
    }

    @Override
    public UserVO getUserInfo(String uid) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        User user=userMapper.selectOne(queryWrapper);
        return transformToVo(user);
    }

    @Override
    public UserPageVO getAllUserInfo(Integer page) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.select("*");
        Page<User> page1=new Page<>(page,6);
        IPage<User> iPage=userMapper.selectPage(page1,queryWrapper);
        List<User> users=iPage.getRecords();
        List<UserVO> userVOS=new ArrayList<>();
        for(int i=0;i<users.size();i++){
            UserVO userVO=transformToVo(users.get(i));
            userVOS.add(userVO);
        }
        UserPageVO userPageVO = new UserPageVO(userVOS, iPage.getTotal());
        return userPageVO;
    }

    @Override
    public void insertUser(User user) throws BusinessException {
        try {
            userMapper.insert(user);
        }catch (Exception e){
             throw new BusinessException(EmBusinessError.OPERATION_ILLEGAL,"用户插入失败");
        }
    }

    @Override
    public void deleteUser(String jobId) throws BusinessException {
        LambdaUpdateWrapper<User> updateWrapper=new UpdateWrapper().lambda();
        updateWrapper.eq(User::getUid,jobId).or().eq(User::getTelephone,jobId);
        updateWrapper.set(User::getStatus,2);
        try {
             userMapper.update(userMapper.selectOne(updateWrapper),updateWrapper);
        }catch (Exception e){
             throw new BusinessException(EmBusinessError.OPERATION_ILLEGAL,"用户禁用失败");
        }
    }

    public UserVO transformToVo(User user){
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }
}
