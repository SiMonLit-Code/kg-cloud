package com.plantdata.kgcloud.plantdata.converter.common;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.plantdata.bean.EditAttDefBeanMole;
import com.plantdata.kgcloud.plantdata.req.data.InsertConceptParameter;
import com.plantdata.kgcloud.plantdata.req.data.UpdataConceptParameter;
import com.plantdata.kgcloud.plantdata.rsp.app.TreeItemVo;
import com.plantdata.kgcloud.plantdata.rsp.data.TreeBean;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import com.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/24 17:59
 */
public class ConceptConverter extends BasicConverter {

    public static TreeItemVo basicConceptTreeRspToTreeItemVo(BasicConceptTreeRsp treeRsp) {
        TreeItemVo treeItemVo = new TreeItemVo();
        treeItemVo.setAction(StringUtils.EMPTY);
        treeItemVo.setPath(StringUtils.EMPTY);
        treeItemVo.setId(treeRsp.getId());
        treeItemVo.setImgUrl(treeRsp.getImgUrl());
        treeItemVo.setKey(treeRsp.getKey());
        treeItemVo.setMeaningTag(treeRsp.getMeaningTag());
        treeItemVo.setName(treeRsp.getName());
        treeItemVo.setParentId(treeRsp.getParentId());
        treeItemVo.setType(treeRsp.getType());
        treeItemVo.setChildren(listToRsp(treeRsp.getChildren(), ConceptConverter::basicConceptTreeRspToTreeItemVo));
        List<EditAttDefBeanMole> editAttDefBeanMoles = listToRsp(treeRsp.getObjAttrs(), a -> objAttrToEditAttDefBeanMole(a, treeRsp.getName(), treeRsp.getId()));
        consumerIfNoNull(editAttDefBeanMoles, a -> {
            if (CollectionUtils.isEmpty(treeItemVo.getChildren())) {
                treeItemVo.setChildren(Lists.newArrayList(editAttDefBeanMoles));
            } else {
                treeItemVo.getChildren().add(editAttDefBeanMoles);
            }
        });
        return treeItemVo;
    }

    public static BasicInfoModifyReq updataConceptParameterToBasicInfoModifyReq(@NonNull UpdataConceptParameter param) {
        BasicInfoModifyReq modifyReq = new BasicInfoModifyReq();
        modifyReq.setId(param.getConceptId());
        modifyReq.setKey(param.getKey());
        modifyReq.setMeaningTag(param.getMeaningTag());
        modifyReq.setName(param.getName());
        modifyReq.setType(EntityTypeEnum.CONCEPT.getValue());
        return modifyReq;
    }

    public static TreeBean basicInfoVoToTreeBean(@NonNull BasicInfoVO basicInfo) {
        TreeBean treeBean = new TreeBean();
        treeBean.setId(basicInfo.getId());
        treeBean.setKey(basicInfo.getKey());
        treeBean.setMeaningTag(basicInfo.getMeaningTag());
        treeBean.setName(basicInfo.getName());
        treeBean.setParentId(basicInfo.getConceptId());
        return treeBean;
    }

    public static ConceptAddReq insertConceptParameterToConceptAddReq(@NonNull InsertConceptParameter param) {
        ConceptAddReq conceptAddReq = new ConceptAddReq();
        conceptAddReq.setParentId(param.getParentId());
        conceptAddReq.setMeaningTag(param.getMeaningTag());
        conceptAddReq.setName(param.getName());
        conceptAddReq.setKey(param.getKey());
        return conceptAddReq;
    }


    private static EditAttDefBeanMole objAttrToEditAttDefBeanMole(BasicConceptTreeRsp.ObjectAttr objAttr, String conceptName, Long conceptId) {
        EditAttDefBeanMole defBeanMole = new EditAttDefBeanMole();
        consumerIfNoNull(objAttr.getDataType(), defBeanMole::setDataType);
        defBeanMole.setId(objAttr.getId());
        defBeanMole.setName(objAttr.getName());
        defBeanMole.setDomain(String.valueOf(conceptId));
        consumerIfNoNull(objAttr.getRangeValue(), a -> defBeanMole.setRange(JacksonUtils.writeValueAsString(a)));
        defBeanMole.setConceptName(conceptName);
        defBeanMole.setDirection(objAttr.getDirection());
        defBeanMole.setType(String.valueOf(NumberUtils.INTEGER_ONE));
        consumerIfNoNull(objAttr.getRangeConcept(), a -> defBeanMole.setChildren(listToRsp(a, ConceptConverter::basicConceptTreeRspToTreeItemVo)));
        return defBeanMole;
    }

}

