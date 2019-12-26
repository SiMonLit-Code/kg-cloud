package com.plantdata.kgcloud.plantdata.req.nlp;

import com.plantdata.kgcloud.config.MarkObject;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class TagConfigSeq implements MarkObject {
    private List<TagConfig> tagConfigList;
}
