package com.cretin.collegehelper.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要用于对整个App中的一些判断坐下封装
 * 截止目前 主要功能有：
 * 手机号是否合法 邮箱是否合法 判断密码长度是否合法
 * Created by cretin on 15/12/31.
 */
public class CheckUtils {
    /**
     * 判断手机号码手机否合法
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断密码位数够不够 默认最低长度为6位
     */
    public static boolean isPasswordLengthOk(String password){
        return password.length()<6?false:true;
    }
}
