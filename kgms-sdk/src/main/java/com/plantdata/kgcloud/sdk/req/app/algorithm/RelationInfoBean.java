package com.plantdata.kgcloud.sdk.req.app.algorithm;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class RelationInfoBean {

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
		this.kvs.add(new KVBean<String,String>(k,v));
	}
	public void addKv(String k, String v,Integer attrDefId) {
		if(this.kvs==null){
			this.kvs = new ArrayList<>();
		}
		this.kvs.add(new KVBean<String,String>(k,v,attrDefId));
	}


}
