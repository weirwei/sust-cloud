package com.group6.cookie;

import javax.servlet.http.Cookie;

/**
 * @ClassName: CookieUtil
 * @Description:
 * @Author: 西瓜
 * @Date: 2021/1/8 17:58
 */
public class CookieUtil {
    /**
     * @Description:从一个cookie数组中找出具体我们想要的cookie
     * @Author: 
     * @Date: 2021/1/8
     */
    public static Cookie findCookie(Cookie[] cookies, String name){
        if(cookies!=null){
            for (Cookie cookie:cookies) {
                if (name.equals(cookie.getName())){
                    return cookie;
                }
            }
        }
        return null;
    }
}
