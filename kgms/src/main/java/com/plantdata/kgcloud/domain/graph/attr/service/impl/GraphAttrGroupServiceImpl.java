package com.plantdata.kgcloud.domain.graph.attr.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.req.attr.AttrDefinitionSearchReq;
import com.plantdata.kgcloud.domain.edit.service.AttributeService;
import com.plantdata.kgcloud.domain.graph.attr.dto.AttrDefGroupDTO;
import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroup;
import com.plantdata.kgcloud.domain.graph.attr.entity.GraphAttrGroupDetails;
import com.plantdata.kgcloud.domain.graph.attr.repository.GraphAttrGroupDetailsRepository;
import com.plantdata.kgcloud.domain.graph.attr.repository.GraphAttrGroupRepository;
import com.plantdata.kgcloud.domain.graph.attr.req.AttrGroupReq;
import com.plantdata.kgcloud.domain.graph.attr.req.AttrGroupSearchReq;
import com.plantdata.kgcloud.domain.graph.attr.rsp.GraphAttrGroupRsp;
import com.plantdata.kgcloud.domain.graph.attr.service.GraphAttrGroupService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.rsp.edit.AttrDefinitionRsp;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 16:19
 */
@Service
public class GraphAttrGroupServiceImpl implements GraphAttrGroupService {

    @Autowired
    private GraphAttrGroupDetailsRepository graphAttrGroupDetailsRepository;
    @Autowired
    private GraphAttrGroupRepository graphAttrGroupRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Autowired
    private AttributeService attributeService;

    @Override
    public List<AttrDefGroupDTO> queryAllByKgName(String kgName) {
        List<GraphAttrGroup> attrDefGroupList = graphAttrGroupRepository.findAllByKgName(kgName);
        if (CollectionUtils.isEmpty(attrDefGroupList)) {
            return Collections.emptyList();
        }
        List<Long> groupIdList = attrDefGroupList.stream().map(GraphAttrGroup::getId).collect(Collectors.toList());
        List<GraphAttrGroupDetails> detailList = graphAttrGroupDetailsRepository.findAllByGroupIdIn(groupIdList);
        return AttrDefGroupDTO.listOfAttrGroupAndDetail(attrDefGroupList, detailList);
    }

    @Override
    public Long createAttrGroup(String kgName, AttrGroupReq attrGroupReq) {
        this.checkGroupName(attrGroupReq.getGroupName());
        GraphAttrGroup graphAttrGroup = GraphAttrGroup.builder()
                .id(kgKeyGenerator.getNextId())
                .kgName(kgName)
                .groupName(attrGroupReq.getGroupName())
                .build();
        GraphAttrGroup group = graphAttrGroupRepository.save(graphAttrGroup);
        return group.getId();
    }

    /**
     * 校验属性分组名称不能相同
     *
     * @param groupName
     */
    private void checkGroupName(String groupName) {
        List<GraphAttrGroup> attrGroups =
                graphAttrGroupRepository.findAll(Example.of(GraphAttrGroup.builder().groupName(groupName).build()));
        if (!CollectionUtils.isEmpty(attrGroups)) {
            throw BizException.of(KgmsErrorCodeEnum.ATTR_GROUP_NOT_EXISTS_SAME_NAME);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttrGroup(String kgName, Long id) {
        GraphAttrGroup graphAttrGroup = GraphAttrGroup.builder()
                .id(id)
                .kgName(kgName)
                .build();
        graphAttrGroupRepository.delete(graphAttrGroup);
    }

    @Override
    public Long updateAttrGroup(String kgName, Long id, AttrGroupReq attrGroupReq) {
        this.checkGroupName(attrGroupReq.getGroupName());
        Optional<GraphAttrGroup> optional = graphAttrGroupRepository.findById(id);
        GraphAttrGroup graphAttrGroup =
                optional.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.ATTR_GROUP_NOT_EXISTS));
        graphAttrGroup.setGroupName(attrGroupReq.getGroupName());
        graphAttrGroup = graphAttrGroupRepository.save(graphAttrGroup);
        return graphAttrGroup.getId();
    }

    @Override
    public List<GraphAttrGroupRsp> listAttrGroups(String kgName, AttrGroupSearchReq attrGroupSearchReq) {
        GraphAttrGroup.GraphAttrGroupBuilder builder = GraphAttrGroup.builder().kgName(kgName);
        Long id = attrGroupSearchReq.getId();
        if (Objects.nonNull(id)) {
            builder.id(id);
        }
        GraphAttrGroup graphAttrGroup = builder.build();
        List<GraphAttrGroup> graphAttrGroups = graphAttrGroupRepository.findAll(Example.of(graphAttrGroup));
        if (CollectionUtils.isEmpty(graphAttrGroups)) {
            return Collections.emptyList();
        }
        List<Long> ids = graphAttrGroups.stream().map(GraphAttrGroup::getId).collect(Collectors.toList());
        List<GraphAttrGroupDetails> detailsList = graphAttrGroupDetailsRepository.findAllByGroupIdIn(ids);
        Map<Long, List<GraphAttrGroupDetails>> detailMap = CollectionUtils.isEmpty(detailsList) ?
                Collections.emptyMap() :
                detailsList.stream().collect(Collectors.groupingBy(GraphAttrGroupDetails::getGroupId));
        List<GraphAttrGroupRsp> groupRsps = new ArrayList<>(graphAttrGroups.size());
        return graphAttrGroups.stream().map(group -> {
            GraphAttrGroupRsp groupRsp = new GraphAttrGroupRsp();
            groupRsp.setId(group.getId());
            groupRsp.setGroupName(group.getGroupName());

            List<GraphAttrGroupDetails> attrGroupDetails = detailMap.get(group.getId());
            if (!CollectionUtils.isEmpty(attrGroupDetails)) {
                List<Integer> attrIds =
                        attrGroupDetails.stream().map(GraphAttrGroupDetails::getAttrId).collect(Collectors.toList());
                groupRsp.setAttrIds(attrIds);
                if (attrGroupSearchReq.getReadDetail()) {
                    AttrDefinitionSearchReq searchReq =
                            AttrDefinitionSearchReq.builder().conceptId(0L).inherit(true).type(0).build();
                    List<AttrDefinitionRsp> attrDefinitionRsps =
                            attributeService.getAttrDefinitionByConceptId(kgName, searchReq);
                    List<AttrDefinitionRsp> definitionRsps =
                            attrDefinitionRsps.stream().filter(rsp -> attrIds.contains(rsp.getId())).collect(Collectors.toList());
                    groupRsp.setAttrRsp(definitionRsps);
                }
            }
            return groupRsp;
        }).collect(Collectors.toList());
    }

    @Override
    public void addAttrToAttrGroup(String kgName, Long id, List<Integer> attrIds) {
        List<GraphAttrGroupRsp> groupRsps = this.listAttrGroups(kgName,
                AttrGroupSearchReq.builder().readDetail(false).build());
        if (CollectionUtils.isEmpty(groupRsps)) {
            return;
        }
        List<Integer> allIds = new ArrayList<>();
        groupRsps.stream().filter(graphAttrGroupRsp -> !CollectionUtils.isEmpty(graphAttrGroupRsp.getAttrIds()))
                .forEach(graphAttrGroupRsp -> allIds.addAll(graphAttrGroupRsp.getAttrIds()));
        attrIds.forEach(attrId -> {
            if (allIds.contains(attrId)) {
                throw BizException.of(KgmsErrorCodeEnum.SAME_ATTRIBUTE_ONLY_EXIST_ONE);
            }
        });

        attrIds.forEach(attrId -> {
            GraphAttrGroupDetails attrGroupDetails =
                    GraphAttrGroupDetails.builder().groupId(id).attrId(attrId).build();
            graphAttrGroupDetailsRepository.save(attrGroupDetails);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttrFromAttrGroup(String kgName, Long id, List<Integer> attrIds) {
        List<GraphAttrGroupDetails> groupDetails = attrIds.stream()
                .map(attrId -> GraphAttrGroupDetails.builder().groupId(id).attrId(attrId).build())
                .collect(Collectors.toList());
        graphAttrGroupDetailsRepository.deleteAll(groupDetails);
    }
}
