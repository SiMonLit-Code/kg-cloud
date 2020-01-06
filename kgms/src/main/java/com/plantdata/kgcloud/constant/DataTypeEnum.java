package com.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/11/26 14:25
 * @Description:
 */
@Getter
public enum DataTypeEnum {

    OBJECT(0, "对象型"),
    INTEGER(1, "整数值"),
    FLOAT(2, "浮点值"),
    BOOLEAN(3, "布尔"),
    DATETIME(4, "日期时间"),
    DATE(41, "日期"),
    TIME(42, "时间"),
    STRING(5, "短文本型"),
    LIST(7, "集合"),
    MAP(8, "Map型"),
    URL(9, "超链接"),
    TEXT(10, "文本型"),
    IMAGE(91, "图片"),
    VIDEO(92, "音、视频"),
    ATTACHMENT(93, "文件");

    private Integer type;
    private String displayName;

    DataTypeEnum(Integer type, String displayName) {
        this.type = type;
        this.displayName = displayName;
    }
}
