package ai.plantdata.kgcloud.plantdata.converter.common;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.plantdata.bean.EditAttDefBeanMole;
import ai.plantdata.kgcloud.plantdata.rsp.app.TreeItemVo;
import ai.plantdata.kgcloud.plantdata.rsp.data.TreeBean;
import com.google.common.collect.Lists;
import ai.plantdata.kgcloud.plantdata.req.data.InsertConceptParameter;
import ai.plantdata.kgcloud.plantdata.req.data.UpdataConceptParameter;
import ai.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import ai.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import ai.plantdata.kgcloud.sdk.req.edit.ConceptAddReq;
import ai.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
        treeItemVo.setChildren(toListNoNull(treeRsp.getChildren(), ConceptConverter::basicConceptTreeRspToTreeItemVo));
        List<EditAttDefBeanMole> editAttDefBeanMoles = toListNoNull(treeRsp.getObjAttrs(), a -> objAttrToEditAttDefBeanMole(a, treeRsp.getName(), treeRsp.getId()));
        consumerIfNoNull(editAttDefBeanMoles, a -> {
            if (CollectionUtils.isEmpty(treeItemVo.getChildren())) {
                treeItemVo.setChildren(Lists.newArrayList(editAttDefBeanMoles));
            } else {
                treeItemVo.getChildren().addAll(editAttDefBeanMoles);
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
        consumerIfNoNull(objAttr.getRangeConcept(), a -> defBeanMole.setChildren(toListNoNull(a, ConceptConverter::basicConceptTreeRspToTreeItemVo)));
        return defBeanMole;
    }

    public static TreeItemVo basicConceptTreeRspToTreeItemVoWithRangeOption(BasicConceptTreeRsp treeRsp,boolean isRangeDisplay) {
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
        if(treeRsp.getChildren()!= null){
            treeItemVo.setChildren(new ArrayList<>());
            for(BasicConceptTreeRsp children : treeRsp.getChildren()){
                treeItemVo.getChildren().add(basicConceptTreeRspToTreeItemVoWithRangeOption(children,isRangeDisplay));
            }
        }
        List<EditAttDefBeanMole> editAttDefBeanMoles = toListNoNull(treeRsp.getObjAttrs(), a -> objAttrToEditAttDefBeanMoleWithRangeOption(a, treeRsp.getName(), treeRsp.getId(),isRangeDisplay));
        consumerIfNoNull(editAttDefBeanMoles, a -> {
            if (CollectionUtils.isEmpty(treeItemVo.getChildren())) {
                treeItemVo.setChildren(Lists.newArrayList(editAttDefBeanMoles));
            } else {
                treeItemVo.getChildren().addAll(editAttDefBeanMoles);
            }
        });
        if(treeRsp.getNumAttrs()!=null) {
            if(treeRsp.getChildren()== null){
                treeItemVo.setChildren(new ArrayList<>());
            }
            for (BasicConceptTreeRsp.NumberAttr attr : treeRsp.getNumAttrs()) {
                EditAttDefBeanMole item = new EditAttDefBeanMole();
                item.setId(attr.getId());
                item.setName(attr.getName());
                item.setType("2");
                item.setDataType(attr.getDataType());
                treeItemVo.getChildren().add(item);
            }
        }
        return treeItemVo;
    }

    private static EditAttDefBeanMole objAttrToEditAttDefBeanMoleWithRangeOption(BasicConceptTreeRsp.ObjectAttr objAttr, String conceptName, Long conceptId,boolean isRangeDisplay) {
        EditAttDefBeanMole defBeanMole = new EditAttDefBeanMole();
        consumerIfNoNull(objAttr.getDataType(), defBeanMole::setDataType);
        defBeanMole.setId(objAttr.getId());
        defBeanMole.setName(objAttr.getName());
        defBeanMole.setDomain(String.valueOf(conceptId));
        consumerIfNoNull(objAttr.getRangeValue(), a -> defBeanMole.setRange(JacksonUtils.writeValueAsString(a)));
        defBeanMole.setConceptName(conceptName);
        defBeanMole.setDirection(objAttr.getDirection());
        defBeanMole.setType(String.valueOf(NumberUtils.INTEGER_ONE));
        if(isRangeDisplay) {
            consumerIfNoNull(objAttr.getRangeConcept(), a -> defBeanMole.setChildren(toListNoNull(a, ConceptConverter::basicConceptTreeRspToTreeItemVo)));
        }
        return defBeanMole;
    }

}

