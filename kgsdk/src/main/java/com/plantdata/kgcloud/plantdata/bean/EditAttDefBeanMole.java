package com.plantdata.kgcloud.plantdata.bean;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Getter
@Setter
public class EditAttDefBeanMole extends AttributeDefinitionItem {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String conceptName;

    private List<Object> children = new ArrayList<>();

}
