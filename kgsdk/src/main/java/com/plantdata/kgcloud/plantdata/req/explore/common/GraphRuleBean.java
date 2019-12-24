package com.plantdata.kgcloud.plantdata.req.explore.common;

import com.plantdata.kgcloud.config.MarkObject;
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
