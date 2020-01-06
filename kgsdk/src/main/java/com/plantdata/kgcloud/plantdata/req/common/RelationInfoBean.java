package com.plantdata.kgcloud.plantdata.req.common;

import java.util.ArrayList;
import java.util.List;

public class RelationInfoBean {
	private static final String RELATION_START_TIME_SSE_KEY = "开始时间";
	private static final String RELATION_END_TIME_SSE_KEY = "结束时间";
	private String id;
	private List<KVBean<String,String>> kvs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setKvs(List<KVBean<String, String>> kvs) {
		this.kvs = kvs;
	}

	public List<KVBean<String,String>> getKvs() {
		return kvs;
	}

	public void addKv(String k, String v) {
		if(this.kvs==null){
			this.kvs = new ArrayList<>();
		}
		this.kvs.add(new KVBean<>(k, v));
	}
	public void addKv(String k, String v,Integer attrDefId) {
		if(this.kvs==null){
			this.kvs = new ArrayList<>();
		}
		this.kvs.add(new KVBean<>(k, v, attrDefId));
	}


}
