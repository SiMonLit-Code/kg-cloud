package com.plantdata.kgcloud.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/25 11:46
 */
public class HttpUtils {

    public static Boolean isHttp(String url) {
        //使用正则表达式过滤，
        String re = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}
