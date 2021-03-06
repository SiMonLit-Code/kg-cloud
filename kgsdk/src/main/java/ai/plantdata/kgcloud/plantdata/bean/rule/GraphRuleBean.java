package ai.plantdata.kgcloud.plantdata.bean.rule;

import ai.plantdata.kgcloud.plantdata.rsp.MarkObject;
import ai.plantdata.kgcloud.plantdata.req.explore.common.PathRule;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class GraphRuleBean implements MarkObject {
	private String name;
	private Long domain;
	private List<PathRule> nextNode;

}
