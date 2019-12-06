package com.plantdata.kgcloud.domain.common.util;

import com.plantdata.kgcloud.constant.ConvertConstent;
import com.plantdata.kgcloud.domain.common.service.CommonService;
import org.apache.poi.xwpf.converter.core.IImageExtractor;

import java.util.Map;


public class KgDocumentImageExtractor implements IImageExtractor {

    private CommonService commonService;

    private Map<String,String> pathMap;

    private String documentPath;

    public KgDocumentImageExtractor( CommonService commonService,Map<String,String> pathMap,String documentPath) {
        this.commonService = commonService;
        this.pathMap = pathMap;
        this.documentPath = documentPath;
    }

    @Override
    public void extract(String name, byte[] bytes){

        String imageName = name.replaceAll("/","-");
        if(imageName.endsWith(ConvertConstent.EMF)){
            bytes = ConvertUtil.emfToPng(bytes);
            imageName += ConvertConstent.PNG;
        }else if(imageName.endsWith(ConvertConstent.WMF)){
            bytes = ConvertUtil.wmfToPng(bytes,documentPath);
            imageName += ConvertConstent.PNG;
        }

        pathMap.put(name,this.commonService.uploadImage(imageName,bytes).getData());
    }

}
