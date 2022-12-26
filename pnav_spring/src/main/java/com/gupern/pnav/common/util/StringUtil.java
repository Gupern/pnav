package com.gupern.pnav.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: Gupern
 * description: 用于字符串操作，如校验手机号
 */
public class StringUtil {
    public static boolean isPhoneFormat(String phone) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$+");
        Matcher matcher = pattern.matcher(phone);
        return matcher.find();
    }
}
