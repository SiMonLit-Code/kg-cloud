package com.plantdata.kgcloud.domain.app.util;

import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.exception.BizException;
import lombok.NonNull;

import java.util.Map;
import java.util.regex.Pattern;

public class DateUtils {
    public static Pattern datePattern;
    public static Pattern dateTimePattern;

    static {

        String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        datePattern = Pattern.compile(eL);
        dateTimePattern = Pattern.compile( "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) "
                + "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]");
    }


    public static void checkDataMap(@NonNull Map<String, Object> timeFilter) {
        timeFilter.forEach((k, v) -> {
            if (!checkDateTime(v.toString()) && !checkDate(v.toString())) {
                throw BizException.of(AppErrorCodeEnum.DATA_FORMAT_ERROR);
            }
        });
    }


    private static boolean checkDateTime(@NonNull String date) {
        return datePattern.matcher(date).matches();
    }

    private static boolean checkDate(@NonNull String date) {
        return datePattern.matcher(date).matches();
    }


}
