package com.group6.controller;

import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.group6.controller.view.UserVO;
import com.group6.cookie.CookieUtil;
import com.group6.entity.User;
import com.group6.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.group6.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/user")
@Api("用户管理相关接口")
@Slf4j
public class UserController extends BaseController {

    @Resource
    HttpServletRequest httpServletRequest;
    @Resource
    HttpServletResponse httpServletResponse;
    @Resource
    UserService userService;

    @Resource
    ExcelUtil excelUtil;

    /**
     * @Description: 手机验证码登录
     * @Author:
     * @Date: 2021/1/8
     */
    @PostMapping("/phone_login")
    @ApiOperation("手机登录的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "telephone", value = "电话号"),
            @ApiImplicitParam(name = "otpCode", value = "验证码")
    })
    public CommonReturnType login(@RequestParam("telephone") String telephone,
                                  @RequestParam("otpCode") String otpCode) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String firstLogin = EncodeByMD5("666666");
        UserVO userVO = userService.getUser(telephone, firstLogin);
        if (userVO == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        //验证手机号对应的otpCode相符合
        String inSessionotpCode = (String) httpServletRequest.getSession().getAttribute(telephone);
        log.info("验证码：" + inSessionotpCode);
        if (!inSessionotpCode.equals(otpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "验证码不合法");
        }
        return CommonReturnType.create(userVO);
    }

    /**
     * @Description: 密码登录
     * @Author:
     * @Date: 2021/1/8
     */
    @PostMapping("/pwd_login")
    @ApiOperation("密码登录的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobId", value = "工号/电话号"),
            @ApiImplicitParam(name = "password", value = "密码")

    })
    public CommonReturnType passLogin(@RequestParam("jobId") String jobId,
                                      @RequestParam("password") String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException {
        String newPass = EncodeByMD5(password);
        String firstLogin = EncodeByMD5("666666");
        UserVO userVO = userService.getUserByPassword(jobId, newPass);
        if (userVO == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        if (firstLogin.equals(newPass)) {
            userVO.setFirst(1);
        } else {
            userVO.setFirst(0);
        }

        return CommonReturnType.create(userVO);
    }

    /**
     * @Description: 用户获取otp短信接口
     * @Author:
     * @Date: 2021/1/8
     */
    @ApiOperation("获取验证码的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "telephone", value = "电话号")
    })
    @GetMapping(value = "/getotp")
    public CommonReturnType getOtp(String telephone) throws BusinessException {

        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);
        //将OTP验证码同对应用户手机号相关联,用httpsession的方式进行相关联
        httpServletRequest.getSession().setAttribute(telephone, otpCode);
        //将OPT验证码通过短信通道发送给用户
        log.info("telphone=" + telephone + "&otpCode=" + otpCode);
        System.out.println("telphone=" + telephone + "&otpCode=" + otpCode);
        //返回验证码
        return CommonReturnType.create(otpCode);
    }

    /**
     * @Description: 修改密码
     * @Author:
     * @Date: 2021/1/8
     */
    @ApiOperation("忘记密码后重置，在获取手机验证后修改新密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "密码"),
            @ApiImplicitParam(name = "jobId0", value = "工号"),
            @ApiImplicitParam(name = "telephone", value = "手机号")
    })
    @GetMapping(value = "/update")
    public CommonReturnType updatePassword(@RequestParam("password") String password,
                                           @RequestParam("jobId") String jobId,
                                           @RequestParam("telephone") String telephone) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String newPass = EncodeByMD5(password);
        int count = userService.updatePassword(newPass, jobId, telephone);
        if (count == 0) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL, "修改密码失败");
        }
        return CommonReturnType.create("修改密码成功");
    }

    /**
     * @Description: 加密
     * @Author:
     * @Date: 2021/1/8
     */
    public String EncodeByMD5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    /**
     * @Description: 查看用户详细信息
     * @Author:
     * @Date: 2021/1/8
     */
    @GetMapping("/getUserInfo")
    @ApiOperation("管理员获取用户详情信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "telephone", value = "手机号")
    })
    public CommonReturnType getUserInfo(String uid) throws BusinessException {
        UserVO userVO = userService.getUserInfo(uid);
        if (userVO == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL, "用户未找到");
        }
        return CommonReturnType.create(userVO);
    }

    /**
     * @Description:
     * @Author:
     * @Date: 2021/1/9
     */
    @PostMapping("/insert/numbers")
    @ApiOperation("管理员批量导入用户信息")
    public CommonReturnType insertNumbers(@RequestParam(value = "file", required = true) MultipartFile file) throws BusinessException {
        List<String[]> list = new ArrayList<>();
        try {
            list = excelUtil.readExcel(file);
            //去掉表头
            list.remove(0);
            //插入表格中所有用户，暂未开通
            insertAllUser(list);
        } catch (IOException e) {
            throw new BusinessException(EmBusinessError.OPERATION_ILLEGAL, "解析excel文件失败");
        }
        return CommonReturnType.create("用户全部导入成功");
    }

    /**
     * @Description: 管理员注册用户信息
     * @Author:
     * @Date: 2021/1/9
     */
    @PostMapping("/insert/one")
    @ApiOperation("管理员单个导入用户信息")
    @ApiImplicitParam(name = "user", value = "用户信息", required = true, dataType = "User", paramType = "body")
    public CommonReturnType getAllUser(@RequestBody User user) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException {
        user.setPassword(EncodeByMD5(user.getPassword()));
        userService.insertUser(user);
        return CommonReturnType.create(user);
    }

    /**
     * @Description: 查看所有用户信息
     * @Author:
     * @Date: 2021/1/9
     */
    @GetMapping("/getAllUserInfo")
    @ApiOperation("管理员获取所有用户详情信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数，一页查看15人")
    })
    public CommonReturnType getAllUser(Integer page) {
        List<UserVO> list = userService.getAllUserInfo(page);
        return CommonReturnType.create(list);
    }

    /**
     * @Description: 验证cookie
     * @Author:
     * @Date: 2021/1/8
     */
    public CommonReturnType getCookie(UserVO userVO) {
        Cookie[] cookies = httpServletRequest.getCookies();
        String uid = userVO.getUid();
        Cookie cookie = CookieUtil.findCookie(cookies, "uid");
        //第一次登陆，没有cookie
        if (cookie == null) {
            Cookie c = new Cookie("uid", uid);
            c.setMaxAge(60 * 60);
            c.setPath("/");
            httpServletResponse.addCookie(c);
        } else {
            //第二次登陆有cookie,获取以前的cookie
            long lastVisitTime = Long.parseLong(cookie.getValue());
            //重置上面的登陆时间
            cookie.setValue(System.currentTimeMillis() + "");
            httpServletResponse.addCookie(cookie);
            //输出到界面
            //httpServletResponse.getWriter().write("欢迎您，"+userId+",上次来访的时间是："+new Date(lastVisitTime));
            // return CommonReturnType.create(new Date(lastVisitTime));
            return CommonReturnType.create("isFirstLogin:" + 0);
        }
        //return CommonReturnType.create(System.currentTimeMillis()+"");
        return CommonReturnType.create("isFirstLogin:" + 1);
    }


    /**
     * @Description: 解析成用户列表
     * @Author:
     * @Date: 2021/1/9
     */
    public void insertAllUser(List<String[]> list) {
        list.stream().forEach(s -> {
            if (s[0] != "") {
                User user = new User(s[0], s[1], s[2], s[3], s[4], s[5], s[6], Integer.parseInt(s[7].trim()), Long.parseLong(s[8]));
                try {
                    user.setPassword(EncodeByMD5(s[5]));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                try {
                    if (s[0] != "") {
                        userService.insertUser(user);
                    }
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * @Description: 删除用户
     * @Author:
     * @Date: 2021/1/11
     */
    @PostMapping("/delete/one")
    @ApiOperation("禁用用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobId", value = "手机号/工号")
    })
    public CommonReturnType deleteUser(@RequestBody String jobId) throws BusinessException {
        log.info(PARAM + "jobId: " + jobId);
        userService.deleteUser(jobId);
        return CommonReturnType.create("此用户已经被禁用");
    }
}
