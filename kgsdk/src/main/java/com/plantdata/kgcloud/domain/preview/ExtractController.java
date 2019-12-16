package com.plantdata.kgcloud.domain.preview;

import com.hiekn.parser.bean.ie.ExtractConfigVO;
import com.hiekn.parser.ie.IE;
import com.hiekn.parser.ie.impl.IEImpl;
import com.hiekn.pddocument.bean.PdDocument;
import com.hiekn.pddocument.bean.element.PdEntity;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.ExtractTypeEnum;
import com.plantdata.kgcloud.domain.common.module.GraphApplicationInterface;
import com.plantdata.kgcloud.sdk.KgDataClient;
import com.plantdata.kgcloud.util.JsonUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 15:38
 */
@RestController
@RequestMapping("v3/kgData/extract")
public class ExtractController implements GraphApplicationInterface {

    private IE ie = new IEImpl();
    @Autowired
    private KgDataClient kgDataClient;

    @PostMapping("entity")
    @ApiOperation("实体抽取")
    public ApiReturn<List<PdDocument>> entity(@ApiParam(required = true) @RequestParam("input") String input,
                                              @ApiParam(required = true) @RequestParam("config") String config) {
        return extractByType(ExtractTypeEnum.ENTITY, input, null, config);
    }

    @PostMapping("attribute")
    @ApiOperation("属性抽取")
    public ApiReturn<List<PdDocument>> attribute(@ApiParam(required = true) @RequestParam("input") String input,
                                                 @ApiParam(required = true) @RequestParam("entities") String entities,
                                                 @ApiParam(required = true) @RequestParam("config") String config) {
        return extractByType(ExtractTypeEnum.ATTRIBUTE, input, entities, config);
    }

    @PostMapping("synonym")
    @ApiOperation("同义抽取")
    public ApiReturn<List<PdDocument>> synonym(@ApiParam(required = true) @RequestParam("input") String input,
                                               @ApiParam(required = true, value = "[电池,电解质]") @RequestParam("entities") String entities,
                                               @ApiParam(required = true) @RequestParam("config") String config) {
        return extractByType(ExtractTypeEnum.SYNONYM, input, entities, config);
    }

    @PostMapping("relation")
    @ApiOperation("关系抽取")
    public ApiReturn relation(@ApiParam(required = true) @RequestParam("input") String input,
                              @ApiParam(required = true) @RequestParam("entities") String entities,
                              @ApiParam(required = true) @RequestParam("config") String config) {
        return extractByType(ExtractTypeEnum.RELATION, input, entities, config);
    }

    @ApiOperation("第三方模型抽取")
    @PostMapping("extract/thirdModel/{modelId}")
    public ApiReturn<Object> extractThirdModel(@PathVariable("modelId") Long modelId,
                                               String input, @RequestBody List<Map<String, String>> configList) {
        return kgDataClient.extractThirdModel(modelId, input, configList);
    }

    private ApiReturn<List<PdDocument>> extractByType(ExtractTypeEnum extractType, String input, String entities, String config) {
        List<PdEntity> entityList = JsonUtils.isEmpty(entities) ? Collections.emptyList() : JsonUtils.jsonToList(entities, PdEntity.class);
        List<ExtractConfigVO> configList = JsonUtils.isEmpty(entities) ? Collections.emptyList() : JsonUtils.jsonToList(config, ExtractConfigVO.class);
        List<PdDocument> result;
        switch (extractType) {
            case ENTITY:
                result = ie.ner(input, configList);
                break;
            case ATTRIBUTE:
                result = ie.attr(input, entityList, configList);
                break;
            case RELATION:
                result = ie.relation(input, entityList, configList);
                break;
            case SYNONYM:
                result = ie.synonym(input, entityList, configList);
                break;
            default:
                result = Collections.emptyList();
        }
        return ApiReturn.success(result);

    }
}
