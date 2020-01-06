package com.plantdata.kgcloud.domain.edit.util;

import ai.plantdata.kg.api.edit.resp.EntityVO;
import ai.plantdata.kg.api.pub.resp.RelationVO;
import com.fasterxml.jackson.core.type.TypeReference;
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
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

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
                    basicInfoRsp.setScore(Double.valueOf(entityMetaData.get(MetaDataInfo.SCORE.getFieldName()).toString()));
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
                    basicInfoRsp.setReliability(Double.valueOf(entityMetaData.get(MetaDataInfo.RELIABILITY.getFieldName()).toString()));
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
                    List<Double> gis = (List<Double>) entityMetaData.get(MetaDataInfo.GIS_COORDINATE.getFieldName());
                    gisVO.setLng(gis.get(0));
                    gisVO.setLat(gis.get(1));
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
            }
        } catch (Exception e) {
            log.error("解析实体metadata数据异常: ", e);
            throw BizException.of(KgmsErrorCodeEnum.METADATA_TYPE_ERROR);
        }
        List<EntityAttrValueVO> attrValue = basicInfoRsp.getAttrValue();
        if (!CollectionUtils.isEmpty(attrValue)) {
            //填充关系的metadata
            List<EntityAttrValueVO> entityAttrValues = attrValue.stream().peek(entityAttrValueVO -> {
                List<ObjectAttrValueVO> objectValues = entityAttrValueVO.getObjectValues();
                if (Objects.nonNull(objectValues) && !objectValues.isEmpty()) {
                    List<ObjectAttrValueVO> relationAttrValues = objectValues.stream()
                            .map(ParserBeanUtils::parserRelationValue).collect(Collectors.toList());
                    entityAttrValueVO.setObjectValues(relationAttrValues);
                }
            }).collect(Collectors.toList());
            basicInfoRsp.setAttrValue(entityAttrValues);

        }

        return basicInfoRsp;
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
                    relationAttrValueVO.setScore((Double) relationMetaData.get(MetaDataInfo.SCORE.getFieldName()));
                }
                if (relationMetaData.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
                    relationAttrValueVO.setSource((String) relationMetaData.get(MetaDataInfo.SOURCE.getFieldName()));
                }
                if (relationMetaData.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
                    relationAttrValueVO.setReliability((Double) relationMetaData.get(MetaDataInfo.RELIABILITY.getFieldName()));
                }
                if (relationMetaData.containsKey(MetaDataInfo.SOURCE_REASON.getFieldName())) {
                    relationAttrValueVO.setSourceReason((String) relationMetaData.get(MetaDataInfo.SOURCE_REASON.getFieldName()));
                }
                if (relationMetaData.containsKey(MetaDataInfo.BATCH_NO.getFieldName())) {
                    relationAttrValueVO.setBatch(relationMetaData.get(MetaDataInfo.BATCH_NO.getFieldName()).toString());
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
                    relationRsp.setScore((Double) relationMetaData.get(MetaDataInfo.SCORE.getFieldName()));
                }
                if (relationMetaData.containsKey(MetaDataInfo.SOURCE.getFieldName())) {
                    relationRsp.setSource((String) relationMetaData.get(MetaDataInfo.SOURCE.getFieldName()));
                }
                if (relationMetaData.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
                    relationRsp.setReliability((Double) relationMetaData.get(MetaDataInfo.RELIABILITY.getFieldName()));
                }
                if (relationMetaData.containsKey(MetaDataInfo.BATCH_NO.getFieldName())) {
                    relationRsp.setBatch(relationMetaData.get(MetaDataInfo.BATCH_NO.getFieldName()).toString());
                }
            }
        } catch (Exception e) {
            log.error("解析关系metadata数据异常: ", e);
            throw BizException.of(KgmsErrorCodeEnum.METADATA_TYPE_ERROR);
        }
        return relationRsp;
    }
}
