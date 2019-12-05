package com.plantdata.kgcloud.constant;

import com.plantdata.kgcloud.domain.edit.req.upload.ImportConceptHeader;
import com.plantdata.kgcloud.domain.edit.req.upload.ImportFieldHeader;
import com.plantdata.kgcloud.domain.edit.req.upload.ImportNumberAttrHeader;
import com.plantdata.kgcloud.domain.edit.req.upload.ImportObjectAttrHeader;
import com.plantdata.kgcloud.domain.edit.req.upload.ImportRelationHeader;
import com.plantdata.kgcloud.domain.edit.req.upload.ImportSynonymHeader;
import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/12/2 10:37
 * @Description:
 */
@Getter
public enum ImportType {
    /**
     * 概念
     */
    CONCEPT("concept", 1, ImportConceptHeader.class),
    /**
     * 实体
     */
    ENTITY("entity", 2, Object.class),
    /**
     * 关系
     */
    RELATION("relation", 3, ImportRelationHeader.class),
    /**
     * 同义
     */
    SYNONYMY("synonymy", 4, ImportSynonymHeader.class),
    /**
     * 数值属性
     */
    NUMERICAL_ATTR("number", 5, ImportNumberAttrHeader.class),
    /**
     * 对象属性
     */
    OBJECT_ATTR("object", 6, ImportObjectAttrHeader.class),
    /**
     * 特定关系
     */
    SPECIFIC_RELATION("specific", 7, Object.class),
    /**
     * 领域词典
     */
    FIELD("field", 8, ImportFieldHeader.class),

    ;

    private final String type;

    private final Integer code;

    private final Class classType;

    ImportType(String type, Integer code, Class classType) {
        this.type = type;
        this.code = code;
        this.classType = classType;
    }

    public static boolean isInclude(String type) {
        for (ImportType importType : values()) {
            if (importType.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static ImportType getByType(String type) {
        for (ImportType importType : values()) {
            if (importType.getType().equals(type)) {
                return importType;
            }
        }
        return null;
    }

    public static Class getClassType(String type) {
        for (ImportType importType : values()) {
            if (importType.getType().equals(type)) {
                return importType.getClassType();
            }
        }
        return Object.class;
    }
}
