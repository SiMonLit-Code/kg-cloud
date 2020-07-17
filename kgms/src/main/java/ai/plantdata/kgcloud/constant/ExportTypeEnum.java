package ai.plantdata.kgcloud.constant;

import ai.plantdata.kgcloud.sdk.constant.BaseEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/14 11:55
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExportTypeEnum implements BaseEnum {
    /**
     * excel 类型
     */
    XLS(".xls", 2),
    XLSX(".xlsx", 1),
    TXT(".txt", 0);

    @Getter
    private String value;
    @Getter
    private Integer id;

   @Override
   public Integer fetchId(){
       return id;
   }
}
