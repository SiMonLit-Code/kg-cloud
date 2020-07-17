package ai.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 19:23
 * @Description:
 */
@Getter
public enum SchemaType {
    KG("kg"),
    ;

    private String type;

    SchemaType(String type) {
        this.type = type;
    }

    public static boolean isKG(String type) {
        return KG.getType().equals(type);
    }

}
