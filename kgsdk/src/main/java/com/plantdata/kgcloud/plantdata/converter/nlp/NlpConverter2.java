package com.plantdata.kgcloud.plantdata.converter.nlp;

import com.hiekn.pddocument.bean.PdDocument;
import com.hiekn.pddocument.bean.element.PdAnnotation;
import com.hiekn.pddocument.bean.element.PdKeyword;
import com.hiekn.pddocument.bean.element.PdSegment;
import com.hiekn.pddocument.bean.element.PdEntity;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.SegmentEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
import lombok.NonNull;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * @author cx
 */
public class NlpConverter2 {

    public static PdDocument stringToPdDocument(@NonNull String string) {
        PdDocument document = new PdDocument();
        document.setContent(string);
        return document; 
    }

    public static PdDocument phoneticToPdDocument(@NonNull List<String> list) {
        PdDocument document = new PdDocument();
        document.setPdSegment(new ArrayList<PdSegment>());
        List<PdSegment> segmentList = document.getPdSegment();
        int counter = 0;
        for(String string : list){
            segmentList.add(new PdSegment(string,null,counter++));
        }
        return document;
    }

    public static PdDocument segmentToPdDocument(@NonNull List<String> list) {
        PdDocument document = new PdDocument();
        document.setPdSegment(new ArrayList<PdSegment>());
        List<PdSegment> segmentList = document.getPdSegment();
        int counter = 0;
        for(String string : list){
            segmentList.add(new PdSegment(string,null,counter));
            counter += string.length();
        }
        return document;
    }

    public static PdDocument segmentToPdDocument(@NonNull List<String> list,@NonNull String input) {
        PdDocument document = new PdDocument();
        document.setPdSegment(new ArrayList<PdSegment>());
        List<PdSegment> segmentList = document.getPdSegment();
        Map<String,Integer> lastIndexMap = new HashMap<>();
        for(String string : list){
            int lastIndex;
            if(!lastIndexMap.containsKey(string)){
                lastIndex = 0;
            }else{
                lastIndex = lastIndexMap.get(string)+1;
            }
            int index = input.indexOf(string,lastIndex);
            segmentList.add(new PdSegment(string,null, index));
            lastIndexMap.put(string,index);
        }
        return document;
    }

    public static PdDocument graphSegmentToPdDocument(@NonNull List<GraphSegmentRsp> list, @NonNull String input) {
        PdDocument document = new PdDocument();
        document.setPdSegment(new ArrayList<PdSegment>());
        List<PdSegment> segmentList = document.getPdSegment();
        Map<String,Integer> lastIndexMap = new HashMap<>();
        for(GraphSegmentRsp graphSegment : list){
            String name = graphSegment.getName();
            Double source = graphSegment.getSource();
            int lastIndex;
            if(!lastIndexMap.containsKey(name)){
                lastIndex = 0;
            }else{
                lastIndex = lastIndexMap.get(name)+1;
            }
            int index = input.indexOf(name,lastIndex);
            segmentList.add(new PdSegment(name,null, index));
            lastIndexMap.put(name,index);
        }
        return document;
    }

    public static PdDocument keywordToPdDocument(@NonNull List<String> list,@NonNull String input) {
        PdDocument document = new PdDocument();
        document.setPdKeyword(new ArrayList<PdKeyword>());
        List<PdKeyword> keywordList = document.getPdKeyword();
        Map<String,Integer> lastIndexMap = new HashMap<>();
        for(String string : list){
            int lastIndex;
            if(!lastIndexMap.containsKey(string)){
                lastIndex = 0;
            }else{
                lastIndex = lastIndexMap.get(string)+1;
            }
            int index = input.indexOf(string,lastIndex);
            keywordList.add(new PdKeyword(string,index));
            lastIndexMap.put(string,index);
        }
        return document;
    }

    public static PdDocument posToPdDocument(@NonNull List<String> list) {
        PdDocument document = new PdDocument();
        document.setPdSegment(new ArrayList<PdSegment>());
        List<PdSegment> segmentList = document.getPdSegment();
        int counter = 0;
        for(String string : list){
            int position = string.lastIndexOf("/");
            String leftPart = string.substring(0,position);
            String rightPart = string.substring(position+1);
            segmentList.add(new PdSegment(leftPart,rightPart,counter));
            counter += leftPart.length();
        }
        return document;
    }

    public static PdDocument nerToPdDocument(@NonNull Map<String, List<String>> map,@NonNull String input) {
        PdDocument document = new PdDocument();
        document.setPdSegment(new ArrayList<PdSegment>());
        List<PdSegment> segmentList = document.getPdSegment();
        Map<String,Integer> lastIndexMap = new HashMap<>();
        for(String key : map.keySet()){
            List<String> list = map.get(key);
            for(String string: list){
                int lastIndex;
                if(!lastIndexMap.containsKey(key)){
                    lastIndex = 0;
                }else{
                    lastIndex = lastIndexMap.get(key)+1;
                }
                int index = input.indexOf(string,lastIndex);
                segmentList.add(new PdSegment(string,key, index));
                lastIndexMap.put(key,index);
            }
        }
        return document;
    }

    public static PdDocument annotationToPdDocument(@NonNull List<TaggingItemRsp> list) {
        PdDocument document = new PdDocument();
        document.setPdAnnotation(new ArrayList<PdAnnotation>());
        List<PdAnnotation> annotationList = document.getPdAnnotation();
        for(TaggingItemRsp rsp : list){
            PdAnnotation annotation = new PdAnnotation();
            annotation.setId(rsp.getId());
            annotation.setName(rsp.getName());
            annotation.setScore(rsp.getScore());
            annotation.setClassId(rsp.getClassId());
            annotationList.add(annotation);
        }
        return document;
    }
}
