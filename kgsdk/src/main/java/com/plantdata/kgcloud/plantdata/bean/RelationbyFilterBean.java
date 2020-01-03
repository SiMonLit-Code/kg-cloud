package com.plantdata.kgcloud.plantdata.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Administrator
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RelationbyFilterBean<T> {
    private Integer layer;
    private List<T> ids;

}
