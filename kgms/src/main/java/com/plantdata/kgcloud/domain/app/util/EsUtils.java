package com.plantdata.kgcloud.domain.app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 16:20
 */
public class EsUtils {

    public static boolean isChinese(String words) {
        Pattern chinesePattern = compile("[\\u4E00-\\u9FA5]+");
        Matcher matcherResult = chinesePattern.matcher(words);
        return matcherResult.find();
    }
}
