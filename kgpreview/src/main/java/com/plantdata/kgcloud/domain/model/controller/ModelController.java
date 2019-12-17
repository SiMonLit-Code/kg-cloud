package com.plantdata.kgcloud.domain.model.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.constant.CommonErrorCode;
import com.plantdata.kgcloud.domain.model.entity.*;
import com.plantdata.kgcloud.domain.model.service.ModelService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.JacksonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author xiezhenxiang 2019/12/9
 */
@RestController
@RequestMapping("/struct")
@Api(tags = "半自动化建模")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @PostMapping("/db/test")
    @ApiOperation("测试数据库连接")
    public ApiReturn test(@RequestBody DbTestReq req) {

        modelService.dbTest(req.getPath(), req.getType(), req.getIp(), req.getPort(), req.getDatabase(), req.getUserName(), req.getPwd());
        return ApiReturn.success();
    }

    @PostMapping("/tables/name")
    @ApiOperation("获取所有数据表名称")
    public ApiReturn<BasePage<String>> getTableName(@RequestBody TableNameReq req) {

        BasePage<String> pageList = modelService.getTables(req.getPath(), req.getType(), req.getIp(), req.getPort(), req.getDatabase(),
                req.getUserName(), req.getPwd(), req.getKw(), req.getPage(), req.getSize());
        return ApiReturn.success(pageList);
    }

    @PostMapping("/tables/schema")
    @ApiOperation("获取所有数据表结构schema")
    public ApiReturn<List<TableSchemaRsp>> getTableSchema(@RequestBody TableSchemaReq req) {

        List<TableSchemaRsp> tableSchemaLs = modelService.getTableSchema(req.getPath(), req.getType(), req.getIp(), req.getPort(),
                req.getDatabase(), req.getUserName(), req.getPwd(), req.getTables());
        return ApiReturn.success(tableSchemaLs);
    }

    @GetMapping("/test/api")
    @ApiOperation("测试接口路径")
    public ApiReturn<List<TableSchemaRsp>> testApi() {

        String str = "[{\"tableName\":\"t_my_data\",\"tableComment\":\"\",\"fieldInfoLs\":[{\"name\":\"id\",\"comment\":\"自增id\",\"type\":\"1\",\"attrType\":0,\"values\":[]},{\"name\":\"user_id\",\"comment\":\"用户id\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"data_name\",\"comment\":\"数据集标识\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"title\",\"comment\":\"标题\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"data_num\",\"comment\":\"数据数量\",\"type\":\"1\",\"attrType\":0,\"values\":[]},{\"name\":\"is_private\",\"comment\":\"数据集是否是私人的\",\"type\":\"1\",\"attrType\":0,\"values\":[]},{\"name\":\"allow_edit\",\"comment\":\"数据集是否允许编辑\",\"type\":\"1\",\"attrType\":0,\"values\":[]},{\"name\":\"fields\",\"comment\":\"列\",\"type\":\"10\",\"attrType\":0,\"values\":[]},{\"name\":\"data_type\",\"comment\":\"数据集存储介质 1-mongo 2-elasticsearch\",\"type\":\"1\",\"attrType\":0,\"values\":[]},{\"name\":\"create_type\",\"comment\":\"数据集创建方式 0-linking 1-selfCreate 2-File 3-Stroe\",\"type\":\"1\",\"attrType\":0,\"values\":[]},{\"name\":\"ip\",\"comment\":\"地址\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"port\",\"comment\":\"端口\",\"type\":\"1\",\"attrType\":0,\"values\":[]},{\"name\":\"username\",\"comment\":\"用户名\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"password\",\"comment\":\"密码\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"db_name\",\"comment\":\"数据库名\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"tb_name\",\"comment\":\"表名\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"schema_config\",\"comment\":\"表结构配置\",\"type\":\"10\",\"attrType\":0,\"values\":[]},{\"name\":\"create_time\",\"comment\":\"创建时间\",\"type\":\"4\",\"attrType\":0,\"values\":[]},{\"name\":\"update_time\",\"comment\":\"修改时间\",\"type\":\"4\",\"attrType\":0,\"values\":[]},{\"name\":\"mapper\",\"comment\":\"表关系映射\",\"type\":\"10\",\"attrType\":0,\"values\":[]},{\"name\":\"icon\",\"comment\":\"图标\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"skill_config\",\"comment\":\"\",\"type\":\"10\",\"attrType\":0,\"values\":[]},{\"name\":\"product_id\",\"comment\":\"\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"create_way\",\"comment\":\"创建方式\",\"type\":\"5\",\"attrType\":0,\"values\":[]}],\"foreignKeyLs\":[]},{\"tableName\":\"t_user\",\"tableComment\":\"用户信息，包括申请试用信息\",\"fieldInfoLs\":[{\"name\":\"user_id\",\"comment\":\"用户id\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"username\",\"comment\":\"用户名称\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"pwd\",\"comment\":\"\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"wx_openid\",\"comment\":\"微信用户唯一标识\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"head_img\",\"comment\":\"头像\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"status\",\"comment\":\"用户状态 0：申请试用 1：试用审核通过 2：试用审核失败 3：正常 4：禁止访问 5：删除\",\"type\":\"1\",\"attrType\":0,\"values\":[]},{\"name\":\"name\",\"comment\":\"用户姓名\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"phone\",\"comment\":\"电话\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"position\",\"comment\":\"职位\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"company\",\"comment\":\"公司\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"create_time\",\"comment\":\"创建时间\",\"type\":\"4\",\"attrType\":0,\"values\":[]},{\"name\":\"update_time\",\"comment\":\"更新时间\",\"type\":\"4\",\"attrType\":0,\"values\":[]},{\"name\":\"email\",\"comment\":\"邮箱\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"info\",\"comment\":\"团队介绍\",\"type\":\"5\",\"attrType\":0,\"values\":[]},{\"name\":\"data_count\",\"comment\":\"数据集数量\",\"type\":\"1\",\"attrType\":0,\"values\":[]},{\"name\":\"graph_count\",\"comment\":\"图谱数量\",\"type\":\"1\",\"attrType\":0,\"values\":[]}],\"foreignKeyLs\":[]}]";

        List<TableSchemaRsp> ls;
        try {
            ls = JacksonUtils.getInstance().readValue(str, new TypeReference<TableSchemaRsp>(){});
        } catch (IOException e) {
            throw BizException.of(CommonErrorCode.BAD_REQUEST);
        }
        return ApiReturn.success(ls);
    }

    @PostMapping("/config/test")
    @ApiOperation("配置参数测试")
    public ApiReturn testConfig(@RequestBody ModelSetting modelSetting) {

        modelService.testConfig(modelSetting);
        return ApiReturn.success();
    }
}
