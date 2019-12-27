package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.plantdata.bean.rule.RuleBean;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.semantic.ReasonConverter;
import com.plantdata.kgcloud.plantdata.req.rule.FormatParameter;
import com.plantdata.kgcloud.plantdata.req.rule.RuleAdd;
import com.plantdata.kgcloud.plantdata.req.rule.RuleUpdate;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.ReasoningClient;
import com.plantdata.kgcloud.sdk.req.GraphConfReasonReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.JsonUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Administrator
 */
@RestController("reasonRuleController-v2")
@RequestMapping("sdk/rule")
public class ReasonRuleController implements SdkOldApiInterface {

    @Autowired
    private KgmsClient kgmsClient;
    @Autowired
    private ReasoningClient reasoningClient;

    @GetMapping("get/list")
    @ApiOperation("推理规则-分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "pageNo", defaultValue = "1", dataType = "int", paramType = "query", value = "分页页码最小值为1"),
            @ApiImplicitParam(name = "pageSize", defaultValue = "10", dataType = "int", paramType = "query", value = "分页每页最小为1"),
    })
    public RestResp<RestData<RuleBean>> listByPage(@RequestParam("kgName") String kgName,
                                                   @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        ApiReturn<BasePage<GraphConfReasonRsp>> apiReturn = kgmsClient.selectReasoningPage(kgName, pageNo, pageSize);
        RestData<RuleBean> restData = BasicConverter.convert(apiReturn,
                a -> BasicConverter.basePageToRestData(a, ReasonConverter::graphConfReasonRspToRuleBean));
        return new RestResp<>(restData);
    }


    @GetMapping("get")
    @ApiOperation("推理规则-详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", defaultValue = "1", dataType = "int", paramType = "query"),
    })
    public RestResp<RuleBean> get(@RequestParam(required = false) String kgName, @RequestParam Integer id) {
        ApiReturn<GraphConfReasonRsp> apiReturn = kgmsClient.detailReasoning(id.longValue());
        RuleBean ruleBean = BasicConverter.convert(apiReturn, ReasonConverter::graphConfReasonRspToRuleBean);
        return new RestResp<>(ruleBean);
    }


    @PostMapping("add")
    @ApiOperation("推理规则-新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "bean", required = true, dataType = "string", paramType = "form", value = "保存的数据"),
    })
    public RestResp<RuleBean> add(@Valid @ApiIgnore RuleAdd ruleAdd) {
        Function<GraphConfReasonReq, ApiReturn<GraphConfReasonRsp>> returnFunction = a -> kgmsClient.saveReasoning(ruleAdd.getKgName(), a);
        RuleBean ruleBean = returnFunction
                .compose(ReasonConverter::ruleBeanToGraphConfReasonReq)
                .andThen(a -> BasicConverter.convert(a, ReasonConverter::graphConfReasonRspToRuleBean))
                .apply(ruleAdd.getBean());
        return new RestResp<>(ruleBean);
    }

    @PostMapping("delete")
    @ApiOperation("推理规则-删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
    })
    public RestResp add(@RequestParam("id") Integer id, @RequestParam(value = "kgName", required = false) String kgName) {
        kgmsClient.deleteReasoning(id.longValue());
        return new RestResp();
    }


    @PostMapping("update")
    @ApiOperation("推理规则-修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
            @ApiImplicitParam(name = "data", required = true, dataType = "string", paramType = "form", value = "需要修改的数据"),
    })
    public RestResp<RuleBean> update(@Valid @ApiIgnore RuleUpdate ruleUpdate) {
        Function<GraphConfReasonReq, ApiReturn<GraphConfReasonRsp>> returnFunction = a -> kgmsClient.updateReasoning(ruleUpdate.getId().longValue(), a);
        RuleBean ruleBean = returnFunction
                .compose(ReasonConverter::ruleBeanToGraphConfReasonReq)
                .andThen(a -> BasicConverter.convert(a, ReasonConverter::graphConfReasonRspToRuleBean))
                .apply(ruleUpdate.getBean());
        return new RestResp<>(ruleBean);
    }


    @PostMapping("format")
    @ApiOperation("推理规则-生成")
    public RestResp<String> format(@Valid @ApiIgnore FormatParameter formatParameter) {
        String configStr = JacksonUtils.writeValueAsString(formatParameter.getRuleSetting());
        if (StringUtils.isNoneBlank(configStr)) {
            return new RestResp<>();
        }
        Map<Integer, Object> objectMap = JsonUtils.jsonToObj(configStr, new TypeReference<Map<Integer, Object>>() {
        });
        Map<Long, Object> ruleMap = BasicConverter.keyIntToLong(objectMap);
        return new RestResp<>(BasicConverter.convert(reasoningClient.reasoningRuleGenerate(ruleMap), JacksonUtils::writeValueAsString));
    }
}