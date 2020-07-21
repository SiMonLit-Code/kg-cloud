package ai.plantdata.kgcloud.domain.edit.req.merge;

import ai.plantdata.kg.api.edit.merge.EntityLink;
import ai.plantdata.kg.api.edit.merge.TagItem;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author Bovin
 * @create: 2019-11-29 14:50
 **/
@Setter
@Getter
public class MergeFinalEntityReq {

    @NotEmpty
    @NotBlank
    @Length(max = 50, message = "长度不能超过50")
    private String name;

    @Length(max = 100, message = "长度不能超过100")
    private String meaningTag;
    private String abs;
    private String imgUrl;
    private String source;
    private String gis;
    private String fromTime;
    private String toTime;
    @DecimalMax(value = "1.0")
    @DecimalMin(value = "0.0")
    @Digits(integer = 1,fraction = 2,message = "只保留两位小数")
    private Double reliability;
    private List<TagItem> tag;
    private List<EntityLink> link;

    private Map<Integer, Object> attributes;
    private Map<String, String> privateAttributes;

}
