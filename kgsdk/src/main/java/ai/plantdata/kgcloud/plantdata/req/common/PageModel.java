package ai.plantdata.kgcloud.plantdata.req.common;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class PageModel {

    @Min(1)
    private Integer pageNo = 1;

    @Min(1)
    private Integer pageSize = 10;

}
