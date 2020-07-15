package ai.plantdata.kgcloud.plantdata.rsp.app;

import lombok.*;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TreeItem {
    private Long id;
    private String name;
    private String key;
    private String meaningTag;
    private String path;
    private Long parentId;
    private String action;
    private Integer type;
    private String imgUrl;
    private MetaDataBean metaData;
}
