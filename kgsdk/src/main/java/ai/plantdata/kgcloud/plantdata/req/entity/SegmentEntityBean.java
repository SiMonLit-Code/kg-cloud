package ai.plantdata.kgcloud.plantdata.req.entity;

import lombok.Data;

import java.util.List;

@Data
public class SegmentEntityBean extends EntityBean {


    private String word;
    private List<String> synonym;

}
