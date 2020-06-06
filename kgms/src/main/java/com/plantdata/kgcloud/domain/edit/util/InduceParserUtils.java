package com.plantdata.kgcloud.domain.edit.util;

import com.plantdata.kgcloud.domain.edit.rsp.AttrInduceFindRsp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/11/29 14:44
 * @Description:
 */
public class InduceParserUtils {

    public static List<AttrInduceFindRsp> attrFindItemAddMsg(List<AttrInduceFindRsp> attrInduceFindList,
                                                             Integer number) {
        if (attrInduceFindList == null){
            return new ArrayList<>();
        }
        attrInduceFindList = attrInduceFindList.stream().filter(s -> {
            if (number == null) {
                return true;
            }
            return s.getCount() >= number;
        }).map(InduceParserUtils::montageMsg).collect(Collectors.toList());
        return attrInduceFindList;
    }

    private static AttrInduceFindRsp montageMsg(AttrInduceFindRsp rsp) {
        Integer count = rsp.getCount();
        Integer type = rsp.getType();
        Double percent = rsp.getPercent();
        String msg = "";
        String tipsMsg = "";
        switch (type) {
            case 1:
                msg = "该属性值匹配到了" + count + "个同名对象，占比" + percent + "%";
                tipsMsg = "转变对象属性";
                break;
            case 2:
                msg = "该属性数量达到" + count + "个";
                tipsMsg = "提升公有属性";
                break;
            case 3:
                msg = "通过同义与文本关联关系推荐,相关属性共" + count + "个";
                tipsMsg = "合并属性";
                break;
            default:
                break;
        }
        rsp.setMsg(msg);
        rsp.setTipsMsg(tipsMsg);
        return rsp;
    }


    public static String montageMsg(Integer count, Integer type, Integer percent) {
        String msg = "";
        switch (type) {
            case 1:
                msg = "该属性值匹配到了" + count + "个同名对象，占比" + percent + "%";
                break;
            case 2:
                msg = "该属性数量达到" + count + "个";
                break;
            case 3:
                msg = "通过同义与文本关联关系推荐,相关属性共" + count + "个";
                break;
            default:
                break;
        }
        return msg;
    }
}
