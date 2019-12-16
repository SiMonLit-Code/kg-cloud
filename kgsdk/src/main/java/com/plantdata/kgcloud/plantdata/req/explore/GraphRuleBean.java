package com.plantdata.kgcloud.plantdata.req.explore;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class GraphRuleBean {
	private String name;
	private Long domain;
	private List<PathRule> nextNode;

}
