package com.plantdata.kgcloud.domain.app.util;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NamedEntityRsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HanLPUtil {

    public static List<NamedEntityRsp> ner(String input) {
        Map<String, String> bMap = new HashMap<>();
        bMap.put("nr", "人名");
        bMap.put("nrf", "人名");
        bMap.put("nt", "机构名");
        bMap.put("ns", "地名");
        bMap.put("nsf", "地名");
        Segment segment = HanLP.newSegment().enableAllNamedEntityRecognize(true).enableOffset(true);
        List<Term> termList = segment.seg(input);
        List<NamedEntityRsp> resultList = new ArrayList<>();
        for(Term term : termList){
            if(bMap.containsKey(term.nature.toString())) {
                NamedEntityRsp entity = new NamedEntityRsp();
                entity.setPos(term.offset);
                entity.setName(term.word);
                entity.setTag(bMap.get(term.nature.toString()));
                resultList.add(entity);
            }
        }
        return resultList;
    }
}
