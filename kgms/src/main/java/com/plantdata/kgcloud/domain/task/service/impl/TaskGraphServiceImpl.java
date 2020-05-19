package com.plantdata.kgcloud.domain.task.service.impl;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.plantdata.kgcloud.domain.task.entity.TaskGraphSnapshot;
import com.plantdata.kgcloud.domain.task.repository.TaskGraphSnapshotRepository;
import com.plantdata.kgcloud.domain.task.req.TaskGraphSnapshotReq;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphSnapshotRsp;
import com.plantdata.kgcloud.domain.task.service.TaskGraphService;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-12-10 10:43
 **/
@Service
public class TaskGraphServiceImpl implements TaskGraphService {

    @Autowired
    private TaskGraphSnapshotRepository taskGraphSnapshotRepository;

    @Override
    public Page<TaskGraphSnapshotRsp> snapshotList(TaskGraphSnapshotReq req) {

        Sort sort = new Sort(Sort.Direction.DESC, "updateAt");
        PageRequest of = PageRequest.of(req.getPage() - 1, req.getSize(), sort);
        TaskGraphSnapshot query = new TaskGraphSnapshot();
        if (StringUtils.hasText(req.getKgName())) {
            query.setKgName(req.getKgName());
        } else if (StringUtils.hasText(req.getUserId())) {
            query.setUserId(req.getUserId());
        }
        Page<TaskGraphSnapshot> page = taskGraphSnapshotRepository.findAll(Example.of(query), of);
        return page.map(ConvertUtils.convert(TaskGraphSnapshotRsp.class));
    }

    @Override
    public void snapshotDelete(Long id) {
        taskGraphSnapshotRepository.deleteById(id);
    }


    @Override
    public List<Map<String, Object>> nameConversion(List<Map<String, Object>> maps) {
        try {
            for (Map<String, Object> map : maps) {
                String mapperName = map.get("mapperName").toString();
                String id = map.get("id").toString();
                String after = PinyinHelper.convertToPinyinString(mapperName, "", PinyinFormat.WITHOUT_TONE) + "_" + id;
                map.put("mapperName", after);
            }
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return maps;
    }

}
