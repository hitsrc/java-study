package com.sample.tools.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
/**
 * Cookie操作工具类
 * @author swq
 *
 */
public class CookieUtil {
    public static void setCookie(HttpServletRequest request,
            HttpServletResponse response, String name, String value, int seconds) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value))
            return;
        Cookie cookie = new Cookie(name, value);
        //cookie.setDomain(domain);
        cookie.setMaxAge(seconds); 
        cookie.setPath("/");
        //p3p cookie跨域
        response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
        response.addCookie(cookie);
    }
    public static String getCookieValue(HttpServletRequest request,String name){
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (name.equalsIgnoreCase(cookies[i].getName())) {
                    return cookies[i].getValue();
                }
            }
        }
        return "";
    }
}