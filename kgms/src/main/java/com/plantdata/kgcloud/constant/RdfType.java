package com.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/12/17 20:50
 * @Description:
 */
@Getter
public enum  RdfType {
    RDF_XML("RDF/XML","rdf"),
    N_TRIPLES("N-TRIPLE","nt"),
    TURTLE("TURTLE","ttl"),
    N3("N3","n3"),
    ;

    private String type;
    private String format;

    RdfType(String type, String format) {
        this.type = type;
        this.format = format;
    }

    public static RdfType findByFormat(String format){
        for (RdfType rdfType : values()){
            if (rdfType.getFormat().equals(format)){
                return rdfType;
            }
        }
        return RdfType.RDF_XML;
    }
}
