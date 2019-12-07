package com.plantdata.kgcloud.domain.parse.util;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hiekn.pddocument.bean.PdDocument;
import com.hiekn.pddocument.bean.element.PdEntity;
import com.hiekn.pddocument.bean.element.PdEntityBase;
import com.hiekn.pddocument.bean.element.PdRelation;
import com.hiekn.pddocument.bean.element.PdValue;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseCountryUtils {

    public static PdDocument getReplaceElementsInWord(String fileName, InputStream in) {

        return getReplaceElementsInWord(fileName, in,"\\\\[([\\u4e00-\\u9fa5]+)\\\\]");
    }

    // 返回Docx中需要替换的特殊字符，没有重复项
    // 推荐传入正则表达式参数"\\$\\{[^{}]+\\}"
    public static PdDocument getReplaceElementsInWord(String fileName,InputStream in,String regex) {

        //查询可能是（最近更新时间：2019年1月）的情况
        boolean finddate=false;
        //判断文本是否追加到上一属性内容下
        boolean findtext=false;
        //text信息采取完的情况
        Map<String, String> stringStringMap = new HashMap<>();
        //text信息采取完的情况
        Map<String, String> stringStringMap2;
        //text信息采取完的情况保存为JSONobject
        JSONObject stringStringOb=new JSONObject();
        List<Map<String,String>> poj  = new ArrayList<>();
        List<PdEntity> alls  = new ArrayList<>();
        //实体名
        String titles="";
        //最后一个实体名
        String endtitles="";
        //最后一个实体
        PdEntity endob = new PdEntity();
        //最后一个实体的属性
        List<PdValue> endarr = new ArrayList<>();
        int ins=0;
        int asw=0;
        int ints=0;
        int ones=1;
        //text信息采取完的情况
        Map<String, String> stringStringMap3 = new HashMap<>();

        String[] p = fileName.split("\\.");
        if (p.length > 0) {// 判断文件有无扩展名

            // 比较文件扩展名
            if (p[p.length - 1].equalsIgnoreCase("doc")) {

                return null;
            } else if (p[p.length - 1].equalsIgnoreCase("docx")) {
                alls = new ArrayList<>();
                poj = new ArrayList<>();//保存获取到的属性和属性值（呈现的是一个Map集合）
                ArrayList<String> al = new ArrayList<>();
                XWPFDocument document = null;
                try {
                    document = new XWPFDocument(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PdEntity js=new PdEntity();
                // 遍历段落
                Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();

                List<PdValue> jsonArray = new ArrayList<>();
                while (itPara.hasNext()) {
                    XWPFParagraph paragraph = itPara.next();
                    String paragraphString = paragraph.getText();
                    paragraphString=paragraphString.replaceAll("\\\\s+", "");

                    while (paragraphString.startsWith("　")) {//这里判断是不是全角空格
                        paragraphString = paragraphString.substring(1).trim();
                    }
                    while (paragraphString.endsWith("　")) {
                        paragraphString = paragraphString.substring(0, paragraphString.length() - 1).trim();
                    }

                    boolean findtitle=false;
                    //获取标题
                    List<XWPFRun> runsLists = paragraph.getRuns();//获取段楼中的句列表
                    for (XWPFRun runsList : runsLists ){

                        int f = runsList.getFontSize();//获取句中字的大小

                        if (f==12){
                            asw++;

                            if (jsonArray.size()>0){
                                ins=0;
                                int aa = titles.lastIndexOf("国");
                                if (aa>0){
                                    titles=titles.substring(0,aa);
                                }
                                int cc = titles.lastIndexOf("概");
                                if (cc>0){
                                    titles=titles.substring(0,cc);
                                }
                                System.out.println(titles);
                                js.setName(titles);
                                js.setElements(jsonArray);
                                alls.add(js);
                                js=new PdEntity();
                                jsonArray = new ArrayList<>();
                            }
                            endtitles=titles;
                            String c = runsList.getColor();//获取句的字体颜色
                            titles = runsList.getText(0);//获取文本内容
                            findtitle=true;//代表标题以找到
                            finddate=true;//时间还需查找

                            endob.setName(titles);//保存最后一个实体
                            endarr=jsonArray;//保存最后一个实体属性
                        }
                    }

                    if (findtitle){
                        continue;
                    }

                    //获取更新时间
                    if (finddate){
                        Pattern p2 = Pattern.compile("(\\（[^\\）]*\\）)");
                        Matcher m = p2.matcher(paragraphString);
                        if (m.find()){
                                finddate=false;
                                continue;
                        }
                    }

                    boolean pojBoolean = new ExtractMessageUtils().findPoj(paragraphString);
                    if (pojBoolean){
                        //此时节点是属性
                        findtext=false;
                        if (stringStringMap!=null){
                            PdValue jsonObject = new PdValue();
                            stringStringMap2=stringStringMap;
                            Set<Entry<String, String>> entries = stringStringMap.entrySet();
                            for (Entry en:entries) {
                                String key = (String) en.getKey();
                                String value = (String) en.getValue();
                                jsonObject.setAttName(key);
                                jsonObject.setName(value);
                                jsonObject.setDataType("10");
                            }

                            if (ins==0){
                                ins=ins+1;
                                int a=ins;
                                if (alls!=null&&alls.size()>0){
                                    asw=alls.size()+1;

                                    PdEntity o = alls.get(asw-2);
                                    List<PdValue> elements = o.getElements();
                                    elements.add(jsonObject);
                                }

                                jsonArray.add(jsonObject);//把属性添加进去
                            }else{
                                ins=ins+1;
                                jsonArray.add(jsonObject);//把属性添加进去
                            }

                        }
                        //下一个属性缓存池清空
                        stringStringOb=new JSONObject();
                    }else{
                        //此时节点是纯文本
                        findtext=true;
                    }

                    if (paragraph.getText().trim().length()>0){
                    //获取段落中的【】包裹的文字
                    if (findtext==false){
                        if (ints==0){
                            //第一次要获取【】包裹文字且获取后面的文本
                            stringStringMap = ExtractMessageUtils.extractMessageByRegular(paragraphString, al);
                            findtext=true;

                            stringStringMap3=stringStringMap;
                            ints=1;
                        }
                            //第一次要获取【】包裹文字且获取后面的文本
                            stringStringMap = ExtractMessageUtils.extractMessageByRegular(paragraphString, al);
                            findtext=true;

//
                    }else{
                            //ExtractMessage.extractMessageByRegular2(paragraphString,stringStringMap);
                            Set<Entry<String, String>> entries = stringStringMap.entrySet();
                            for (Entry en:entries) {
                                String key = (String) en.getKey();
                                String value = (String) en.getValue();


                                stringStringMap.put(key,value+paragraphString.trim());
                                stringStringOb.put(key,value+paragraphString);
                            }
                        }

                    }

                }
                js=new PdEntity();
                String endtitle=endob.getName();

                int aa = endtitle.lastIndexOf("国");
                if (aa>0){
                    endtitle=endtitle.substring(0,aa);
                }
                int cc = endtitle.lastIndexOf("概");
                if (cc>0){
                    endtitle=endtitle.substring(0,cc);
                }

                js.setName(endtitle);
                js.setElements(endarr);
                alls.add(js);
                stringStringMap2=stringStringMap;
                Set<Entry<String, String>> entries = stringStringMap2.entrySet();
                PdValue jsonObjects = new PdValue();
                String keys="",values="";
                for (Entry en:entries) {
                    keys = (String) en.getKey();
                    values = (String) en.getValue();
                    jsonObjects.setAttName(keys);
                    jsonObjects.setName(values);
                    jsonObjects.setDataType("10");
                }


                for (int i = 0 ; i<alls.size();i++){
                        PdEntity jsonObject = alls.get(i);
                        List<PdValue> elements = jsonObject.getElements();
                    elements.remove(0);

                    if (i==alls.size()-1){
                        elements.add(jsonObjects);
                    }

                }

                PdDocument jsonObject = new PdDocument();

                jsonObject.setPdEntity(alls);
                List<PdEntity> pdEntity = jsonObject.getPdEntity();
                PdEntity o = pdEntity.get(0);
                List<PdValue> elements = o.getElements();
                PdValue jsonObjects2 = new PdValue();
                String keys2="",values2="";
                Set<Entry<String, String>> entries1 = stringStringMap3.entrySet();
                for (Entry en:entries1) {
                    keys2 = (String) en.getKey();
                    values2 = (String) en.getValue();
                    jsonObjects2.setAttName(keys2);
                    jsonObjects2.setName(values2);
                    jsonObjects2.setDataType("10");
                }
                elements.add(jsonObjects2);
                if (ones!=1){
                    PdEntity pdEntity1 = jsonObject.getPdEntity().get(0);
                    pdEntity1.getElements().remove(0);
                }else{
                    ones=2;
                }

                if(jsonObject != null && jsonObject.getPdEntity() != null){
                    jsonObject.getPdEntity().forEach(entity -> entity.setConceptName("国家"));
                }

                List<PdRelation>relationList = Lists.newArrayList();
                PdRelation relation = new PdRelation();
                PdEntityBase subject = new PdEntityBase();
                subject.setName("美国");
                subject.setConceptName("国家");
                PdEntityBase object = new PdEntityBase();
                object.setConceptName("国家");
                object.setName("日本");
                relation.setSubject(subject);
                relation.setObject(object);
                relation.setAttName("人民生活2");
                relationList.add(relation);
                jsonObject.setPdRelation(relationList);
                return jsonObject;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }



}