package com.plantdata.kgcloud.plantdata.req.explore;

import lombok.Data;

import java.util.List;
@Data
public class PathAGBean {
	
	private Long start;
	private Long end;
	private List<String> edges;
	private List<Long> nodes;
}
