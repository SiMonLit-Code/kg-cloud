package com.plantdata.kgcloud.domain.graph.config.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.alibaba.fastjson.JSON;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfAlgorithmService;
import com.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReq;
import com.plantdata.kgcloud.sdk.req.GraphConfAlgorithmReqList;
import com.plantdata.kgcloud.sdk.rsp.GraphConfAlgorithmRsp;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.AlgorithmStatisticeRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 图谱算法配置
 *
 * @author jiangdeming
 * @date 2019/11/29
 */
@Api(tags = "图谱配置")
@RestController
@RequestMapping("/config")
public class GraphConfAlgorithmController {
    @Autowired
    private GraphConfAlgorithmService graphConfAlgorithmService;

    @ApiOperation("图谱配置-算法-新建")
    @PostMapping("/algorithm/{kgName}")
    public ApiReturn<GraphConfAlgorithmRsp> save(@PathVariable("kgName") String kgName, @RequestBody @Valid GraphConfAlgorithmReq req) {
        return ApiReturn.success(graphConfAlgorithmService.createAlgorithm(kgName, req));
    }

    @ApiOperation("图谱配置-算法-更新")
    @PutMapping("/algorithm/{id}")
    public ApiReturn<GraphConfAlgorithmRsp> update(@PathVariable("id") Long id, @RequestBody @Valid GraphConfAlgorithmReq req) {
        return ApiReturn.success(graphConfAlgorithmService.updateAlgorithm(id, req));
    }

    @ApiOperation("图谱配置-算法-删除")
    @DeleteMapping("/algorithm/{id}")
    public ApiReturn delete(@PathVariable("id") Long id) {
        graphConfAlgorithmService.deleteAlgorithm(id);
        return ApiReturn.success();
    }

    @ApiOperation("图谱配置-算法-获取")
    @GetMapping("/algorithm/{kgName}")
    public ApiReturn<Page<GraphConfAlgorithmRsp>> select(@PathVariable("kgName") String kgName, GraphConfAlgorithmReqList baseReq) {
        return ApiReturn.success(graphConfAlgorithmService.findByKgName(kgName, baseReq));
    }

    @ApiOperation("图谱配置-算法-详情")
    @GetMapping("/algorithm/detail/{id}")
    public ApiReturn<GraphConfAlgorithmRsp> detailAlgorithm(@PathVariable("id") Long id) {
        return ApiReturn.success(graphConfAlgorithmService.findById(id));
    }

    @ApiOperation("test")
    @PostMapping("/test")
    public RestResp<AlgorithmStatisticeRsp> test() {

        String str = "{\"series\":[{\"name\":\"分类一\",\"data\":[2,3,6,0],\"entityIds\":[[15],[11,27],[29,33,32,12,48],[]],\"relationIds\":[[\"5e1552e3873cef639f1c637e\"],[\"5e1552e3873cef639f1c6381\",\"5e1552e3873cef639f1c6391\"],[\"5e1552e3873cef639f1c637f\",\"5e1552e3873cef639f1c6395\",\"5e1552e3873cef639f1c63b0\"],[\"5e1552e3873cef639f1c639f\"]]},{\"name\":\"分类二\",\"data\":[9,3,5,2],\"entityIds\":[[93],[11,27],[29,33,32],[7]],\"relationIds\":[[\"5e1552e3873cef639f1c637e\"],[\"5e12e043e6df6c6acdee653b\",\"5e12e043e6df6c6acdee6548\"],[\"5e12e043e6df6c6acdee6538\",\"5e12e043e6df6c6acdee655c\"],[\"5e12e043e6df6c6acdee654c\"]]},{\"name\":\"分类三\",\"data\":[2,3,5,5],\"entityIds\":[[15],[11,27],[29,33,32,12,48],[93]],\"relationIds\":[[\"5e12e043e6df6c6acdee6538\"],[],[],[]]},{\"name\":\"分类四\",\"data\":[7,3,5,8],\"entityIds\":[[15],[11,27],[29,33,32,12,48],[93]],\"relationIds\":[[\"5e12e043e6df6c6acdee654c\"],[],[],[]]}],\"xaxis\":[\"投资机构\",\"产品\",\"学校学校学校学校学校学校学校学校\",\"人物\"]}";
        AlgorithmStatisticeRsp rsp = JSON.parseObject(str,AlgorithmStatisticeRsp.class);
        return new RestResp(rsp);
    }

    @ApiOperation("test1")
    @PostMapping("/test1")
    public RestResp<AlgorithmStatisticeRsp> test1() {


        int i = 0;
        int j = 1 / i;
        String str = "{\"xaxis\":[\"x1\",\"x2\",\"x3\"],\"chartTypes\":[\"line\",\"chart\"],\"series\":[{\"name\":\"名称\",\"data\":[3,5,1],\"ids\":[[\"id1\",\"id2\",\"id3\"],[\"id4\",\"id5\",\"id6\",\"id7\",\"id8\"],[\"id9\"]]}]}";
        AlgorithmStatisticeRsp rsp = JSON.parseObject(str,AlgorithmStatisticeRsp.class);
        return new RestResp(rsp);
    }
}
