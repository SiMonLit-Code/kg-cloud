package com.plantdata.kgcloud.domain.parse.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配中括号中的内容
 */
public class ExtractMessageUtils {

    /**
     * 使用正则表达式提取中括号中的内容
     * @param msg
     * @return
     */
    public static Map<String, String> extractMessageByRegular(String msg, ArrayList<String> al){
        //List<String> list=new ArrayList<String>();
        Pattern p = Pattern.compile("(\\【[^\\】]*\\】)");
        Map<String,String> maps = new HashMap<>();
        Matcher m = p.matcher(msg);
        String key ="",value="";
        while(m.find()){
            if (!al.contains(m.group())) {
                key=m.group().substring(1, m.group().length()-1);
                al.add(m.group().substring(1, m.group().length()-1));
            }
        }
        int i = msg.lastIndexOf("】");
        value=msg.substring(i+1,msg.length());

        key=key.replaceAll("\\s*", "");
        value=value.replaceAll("\\s*", "");
        //System.out.println("属性名："+key+"  值："+value);
        maps.put(key.trim(),value.trim());


        return maps;
    }

    /**
     * 判断当前是否是一个带属性的文本
     * @param msg
     */
    public boolean findPoj(String msg){
        Pattern p = Pattern.compile("(\\【[^\\】]*\\】)");
        Matcher m = p.matcher(msg);
        return m.find();

    }

}
