package com.plantdata.kgcloud.domain.common.util;

import org.apache.poi.xwpf.converter.core.IURIResolver;

import java.util.Map;

public class BasicImageURIResolver implements IURIResolver {


    private final Map<String, String> pathMap;

    public BasicImageURIResolver(Map<String, String> pathMap){
        this.pathMap = pathMap;
    }

    public String resolve( String uri ){

        if(pathMap.containsKey(uri)){
            uri = pathMap.get(uri);
        }

        return uri;
    }
}
