package ai.plantdata.kgcloud.plantdata.rsp.app;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Getter
@Setter
public class TreeItemVo extends TreeItem {

    private List<Object> children = new ArrayList<>();
}
