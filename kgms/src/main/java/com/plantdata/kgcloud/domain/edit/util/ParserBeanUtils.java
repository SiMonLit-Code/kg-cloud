package com.plantdata.kgcloud.domain.edit.util;

import ai.plantdata.kg.api.edit.resp.EntityAttributeValueVO;
import ai.plantdata.kg.api.edit.resp.EntityVO;
import ai.plantdata.kg.api.edit.resp.RelationAttrValueVO;
import ai.plantdata.kg.api.pub.resp.RelationVO;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.constant.MongoOperation;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.rsp.RelationRsp;
import com.plantdata.kgcloud.domain.edit.vo.EntityAttrValueVO;
import com.plantdata.kgcloud.domain.edit.vo.EntityTagVO;
import com.plantdata.kgcloud.domain.edit.vo.GisVO;
import com.plantdata.kgcloud.domain.edit.vo.ObjectAttrValueVO;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import com.plantdata.kgcloud.sdk.rsp.UserDetailRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: LinHo
 * @Date: 2019/11/21 19:01
 * @Description:
 */
@Slf4j
public class ParserBeanUtils {

    /**
     * 解析根据metadata排序
     *
     * @param sort
     * @return
     */
    public static Map<String, Object> parserSortMetadata(List<String> sort) {

        Map<String, Object> sortMap = new HashMap<>();
        if (Objects.nonNull(sort) && sort.size() > 0) {
            sort.forEach(s -> {
                String[] split = s.split(":");
                if (split.length == 2) {
                    sortMap.put(MetaDataInfo.getCodeByName(split[0]), parserSort(split[1]));
                }
            });
        }
        return sortMap;
    }

    /**
     * 解析排序
     *
     * @param sortWay
     * @return
     */
    private static int parserSort(String sortWay) {
        if (!(MongoOperation.DESC.getType()).equals(sortWay.toUpperCase())) {
            return 1;
        }
        return -1;
    }

    /**
     * 解析entity的metadata
     *
     * @param vo
     * @return
     */
    public static BasicInfoRsp parserEntityVO(EntityVO vo) {
        BasicInfoRsp basicInfoRsp = MapperUtils.map(vo, BasicInfoRsp.class);

        Map<String, Object> entityMetaData = vo.getMetaData();
        try {
            if (Objects.nonNull(entityMetaData)) {
                if (entityMetaData.containsKey(MetaDataInfo.SCORE.getFieldName())) {
                    String score = entityMetaData.get(MetaDataInfo.SCORE.getFieldName()).toString();
                    if (StringUtils.hasText(score)) {
                        basicInfoRsp.setScore(Double.valueOf(score));
                    }
                }

                if (entityMetaData.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
                    basicInfoRsp.setSource(entityMetaData.get(MetaDataInfo.SOURCE.getFieldName()).toString());
                }

                if (entityMetaData.containsKey(MetaDataInfo.BATCH_NO.getFieldName())) {
                    basicInfoRsp.setBatch(entityMetaData.get(MetaDataInfo.BATCH_NO.getFieldName()).toString());
                }

                if (entityMetaData.containsKey(MetaDataInfo.FROM_TIME.getFieldName())) {
                    basicInfoRsp.setFromTime(entityMetaData.get(MetaDataInfo.FROM_TIME.getFieldName()).toString());
                }

                if (entityMetaData.containsKey(MetaDataInfo.TO_TIME.getFieldName())) {
                    basicInfoRsp.setToTime(entityMetaData.get(MetaDataInfo.TO_TIME.getFieldName()).toString());
                }

                if (entityMetaData.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
                    String reliability = entityMetaData.get(MetaDataInfo.RELIABILITY.getFieldName()).toString();
                    if (StringUtils.hasText(reliability)) {
                        basicInfoRsp.setReliability(Double.valueOf(reliability));
                    }
                }

                if (entityMetaData.containsKey(MetaDataInfo.UPDATE_TIME.getFieldName())) {
                    basicInfoRsp.setUpdateTime(VeDateUtils.dateToStrLong(VeDateUtils.strToDateLong(
                            entityMetaData.get(MetaDataInfo.UPDATE_TIME.getFieldName()).toString())));
                }

                if (entityMetaData.containsKey(MetaDataInfo.TAG.getFieldName())) {
                    basicInfoRsp.setTags(JacksonUtils.readValue(JacksonUtils.writeValueAsString(
                            entityMetaData.get(MetaDataInfo.TAG.getFieldName())),
                            new TypeReference<List<EntityTagVO>>() {
                            }));
                }
                //设置gis
                GisVO gisVO = new GisVO();
                if (entityMetaData.containsKey(MetaDataInfo.GIS_COORDINATE.getFieldName())) {
                    try {
                        List<Double> gis = (List<Double>) entityMetaData.get(MetaDataInfo.GIS_COORDINATE.getFieldName());
                        gisVO.setLng(gis.get(0));
                        gisVO.setLat(gis.get(1));
                    }catch (Exception e){
                        System.out.println("GIS格式不正确："+JSON.toJSONString(entityMetaData.get(MetaDataInfo.GIS_COORDINATE.getFieldName())));
                    }
                }
                if (entityMetaData.containsKey(MetaDataInfo.OPEN_GIS.getFieldName())) {
                    gisVO.setIsOpenGis((Boolean) entityMetaData.get(MetaDataInfo.OPEN_GIS.getFieldName()));
                }
                if (entityMetaData.containsKey(MetaDataInfo.GIS_ADDRESS.getFieldName())) {
                    gisVO.setAddress(entityMetaData.get(MetaDataInfo.GIS_ADDRESS.getFieldName()).toString());
                }
                basicInfoRsp.setGis(gisVO);
                if (entityMetaData.containsKey(MetaDataInfo.ADDITIONAL.getFieldName())) {
                    basicInfoRsp.setAdditional((Map<String, Object>) entityMetaData.get(MetaDataInfo.ADDITIONAL.getFieldName()));
                }
                if (entityMetaData.containsKey(MetaDataInfo.ENTITY_LINK.getFieldName())) {
                    basicInfoRsp.setEntityLinks(JacksonUtils.readValue(JacksonUtils.writeValueAsString(
                            entityMetaData.get(MetaDataInfo.ENTITY_LINK.getFieldName())),
                            new TypeReference<Set<EntityLinkVO>>() {
                            }));
                }

                if (entityMetaData.containsKey(MetaDataInfo.TRUE_SOURCE.getFieldName())) {
                    Object value = entityMetaData.get(MetaDataInfo.TRUE_SOURCE.getFieldName());
                    entityMetaData.remove(MetaDataInfo.TRUE_SOURCE.getFieldName());
                    if(value != null){
                        try {
                            basicInfoRsp.setTrueSource(JSON.parseObject(JSON.toJSONString(value)));
                        }catch (Exception e){
                            basicInfoRsp.setTrueSource(Maps.newHashMap());
                        }
                    }
                }
                if (entityMetaData.containsKey(MetaDataInfo.SOURCE_USER.getFieldName())) {
                    String userId = entityMetaData.get(MetaDataInfo.SOURCE_USER.getFieldName()).toString();
                    basicInfoRsp.setSourceUser(userId);
                }

                if (entityMetaData.containsKey(MetaDataInfo.SOURCE_ACTION.getFieldName())) {
                    basicInfoRsp.setSourceAction(entityMetaData.get(MetaDataInfo.SOURCE_ACTION.getFieldName()).toString());
                }
            }
        } catch (Exception e) {
            log.error("解析实体metadata数据异常: ", e);
            throw BizException.of(KgmsErrorCodeEnum.METADATA_TYPE_ERROR);
        }
        List<EntityAttrValueVO> attrValue = basicInfoRsp.getAttrValue();
        if (!CollectionUtils.isEmpty(attrValue)) {
            List<EntityAttrValueVO> entityAttrValues = attrValue.stream().peek(entityAttrValueVO -> {

                //填充关系的metadata
                List<ObjectAttrValueVO> objectValues = entityAttrValueVO.getObjectValues();
                if (Objects.nonNull(objectValues) && !objectValues.isEmpty()) {
                    List<ObjectAttrValueVO> relationAttrValues = objectValues.stream()
                            .map(ParserBeanUtils::parserRelationValue).collect(Collectors.toList());
                    entityAttrValueVO.setObjectValues(relationAttrValues);
                }

                //填充数值属性来源
                Map<String, Object> attrMetaData = entityAttrValueVO.getMetaData();
                if (Objects.nonNull(attrMetaData)) {
                    attrMetaData.remove(MetaDataInfo.GIS_COORDINATE.getFieldName());

                    if (attrMetaData.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
                        entityAttrValueVO.setSource(attrMetaData.get(MetaDataInfo.SOURCE.getFieldName()).toString());
                    }

                    if (attrMetaData.containsKey(MetaDataInfo.TRUE_SOURCE.getFieldName())) {
                        Object value = attrMetaData.get(MetaDataInfo.TRUE_SOURCE.getFieldName());
                        attrMetaData.remove(MetaDataInfo.TRUE_SOURCE.getFieldName());
                        if(value != null){
                            try {
                                entityAttrValueVO.setTrueSource(JSON.parseObject(JSON.toJSONString(value)));
                            }catch (Exception e){
                                entityAttrValueVO.setTrueSource(Maps.newHashMap());
                            }
                        }
                    }
                    if (attrMetaData.containsKey(MetaDataInfo.SOURCE_USER.getFieldName())) {
                        String userId = attrMetaData.get(MetaDataInfo.SOURCE_USER.getFieldName()).toString();
                        entityAttrValueVO.setSourceUser(userId);
                    }

                    if (attrMetaData.containsKey(MetaDataInfo.SOURCE_ACTION.getFieldName())) {
                        entityAttrValueVO.setSourceAction(attrMetaData.get(MetaDataInfo.SOURCE_ACTION.getFieldName()).toString());
                    }
                }

            }).collect(Collectors.toList());

            basicInfoRsp.setAttrValue(entityAttrValues);

        }

        return basicInfoRsp;
    }

    /**
     * 解析实体关系
     *
     * @param vo
     * @return
     */
    public static EntityAttrValueVO parserEntityAttrValue(EntityAttributeValueVO vo, int size) {
        EntityAttrValueVO entityAttrValueVO = MapperUtils.map(vo,EntityAttrValueVO.class);
        List<ObjectAttrValueVO> objectValues = entityAttrValueVO.getObjectValues();
        if (Objects.nonNull(objectValues) && !objectValues.isEmpty()) {
            if (objectValues.size() > size){
                entityAttrValueVO.setHasNext(true);
                objectValues.remove(size);
            }
            List<ObjectAttrValueVO> relationAttrValues = objectValues.stream()
                    .map(ParserBeanUtils::parserRelationValue).collect(Collectors.toList());
            entityAttrValueVO.setObjectValues(relationAttrValues);
        }
        return entityAttrValueVO;
    }

    /**
     * 解析关系的metadata
     *
     * @param relationAttrValueVO
     * @return
     */
    public static ObjectAttrValueVO parserRelationValue(ObjectAttrValueVO relationAttrValueVO) {
        Map<String, Object> relationMetaData = relationAttrValueVO.getMetaData();
        try {
            if (Objects.nonNull(relationMetaData)) {
                if (relationMetaData.containsKey(MetaDataInfo.SCORE.getFieldName())) {
                    String score = relationMetaData.get(MetaDataInfo.SCORE.getFieldName()).toString();
                    if (StringUtils.hasText(score)) {
                        relationAttrValueVO.setScore(Double.valueOf(score));
                    }
                }
                if (relationMetaData.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
                    relationAttrValueVO.setSource((String) relationMetaData.get(MetaDataInfo.SOURCE.getFieldName()));
                }
                if (relationMetaData.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
                    String reliability = relationMetaData.get(MetaDataInfo.RELIABILITY.getFieldName()).toString();
                    if (StringUtils.hasText(reliability)) {
                        relationAttrValueVO.setReliability(Double.valueOf(reliability));
                    }
                }
                if (relationMetaData.containsKey(MetaDataInfo.SOURCE_REASON.getFieldName())) {
                    relationAttrValueVO.setSourceReason((String) relationMetaData.get(MetaDataInfo.SOURCE_REASON.getFieldName()));
                }
                if (relationMetaData.containsKey(MetaDataInfo.BATCH_NO.getFieldName())) {
                    relationAttrValueVO.setBatch(relationMetaData.get(MetaDataInfo.BATCH_NO.getFieldName()).toString());
                }


                if (relationMetaData.containsKey(MetaDataInfo.TRUE_SOURCE.getFieldName())) {

                    Object value = relationMetaData.get(MetaDataInfo.TRUE_SOURCE.getFieldName());
                    relationMetaData.remove(MetaDataInfo.TRUE_SOURCE.getFieldName());
                    if(value != null ){
                        try {
                            relationAttrValueVO.setTrueSource(JSON.parseObject(JSON.toJSONString(value)));
                        }catch (Exception e){
                            relationAttrValueVO.setTrueSource(Maps.newHashMap());
                        }
                    }
                }

                if (relationMetaData.containsKey(MetaDataInfo.SOURCE_USER.getFieldName())) {
                    relationAttrValueVO.setSourceUser(relationMetaData.get(MetaDataInfo.SOURCE_USER.getFieldName()).toString());
                }

                if (relationMetaData.containsKey(MetaDataInfo.SOURCE_ACTION.getFieldName())) {
                    relationAttrValueVO.setSourceAction(relationMetaData.get(MetaDataInfo.SOURCE_ACTION.getFieldName()).toString());
                }
            }
        } catch (Exception e) {
            log.error("解析关系metadata数据异常: ", e);
            throw BizException.of(KgmsErrorCodeEnum.METADATA_TYPE_ERROR);
        }
        return relationAttrValueVO;
    }

    /**
     * 解析关系溯源的metadata
     *
     * @param vo
     * @return
     */
    public static RelationRsp parserRelationMeta(RelationVO vo) {
        RelationRsp relationRsp = MapperUtils.map(vo, RelationRsp.class);
        Map<String, Object> relationMetaData = vo.getMetaData();
        try {
            if (Objects.nonNull(relationMetaData)) {
                if (relationMetaData.containsKey(MetaDataInfo.SCORE.getFieldName())) {
                    String score = relationMetaData.get(MetaDataInfo.SCORE.getFieldName()).toString();
                    if (StringUtils.hasText(score)){
                        relationRsp.setScore(Double.valueOf(score));
                    }
                }
                if (relationMetaData.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
                    relationRsp.setSource((String) relationMetaData.get(MetaDataInfo.SOURCE.getFieldName()));
                }
                if (relationMetaData.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
                    String reliability = relationMetaData.get(MetaDataInfo.RELIABILITY.getFieldName()).toString();
                    if (StringUtils.hasText(reliability)){
                        relationRsp.setReliability(Double.valueOf(reliability));
                    }
                }
                if (relationMetaData.containsKey(MetaDataInfo.BATCH_NO.getFieldName())) {
                    relationRsp.setBatch(relationMetaData.get(MetaDataInfo.BATCH_NO.getFieldName()).toString());
                }

                if (relationMetaData.containsKey(MetaDataInfo.TRUE_SOURCE.getFieldName())) {

                    Object value = relationMetaData.get(MetaDataInfo.TRUE_SOURCE.getFieldName());
                    relationMetaData.remove(MetaDataInfo.TRUE_SOURCE.getFieldName());
                    if(value != null){
                        try {
                            relationRsp.setTrueSource(JSON.parseObject(JSON.toJSONString(value)));
                        }catch (Exception e){
                            relationRsp.setTrueSource(Maps.newHashMap());
                        }
                    }
                }

                if (relationMetaData.containsKey(MetaDataInfo.SOURCE_USER.getFieldName())) {
                    String userId = relationMetaData.get(MetaDataInfo.SOURCE_USER.getFieldName()).toString();


                    relationRsp.setSourceUser(userId);
                }

                if (relationMetaData.containsKey(MetaDataInfo.SOURCE_ACTION.getFieldName())) {
                    relationRsp.setSourceAction(relationMetaData.get(MetaDataInfo.SOURCE_ACTION.getFieldName()).toString());
                }

            }
        } catch (Exception e) {
            log.error("解析关系metadata数据异常: ", e);
            throw BizException.of(KgmsErrorCodeEnum.METADATA_TYPE_ERROR);
        }
        return relationRsp;
    }
}
