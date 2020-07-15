package ai.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.cloud.exception.BizException;
import ai.plantdata.kgcloud.constant.AppErrorCodeEnum;
import ai.plantdata.kgcloud.domain.app.service.GraphAlgorithmService;
import ai.plantdata.kgcloud.sdk.req.app.algorithm.BusinessGraphRsp;
import ai.plantdata.kgcloud.sdk.rsp.app.statistic.AlgorithmStatisticeRsp;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.InputStream;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/31 9:44
 */
@RestController
@RequestMapping("app/algorithm")
public class GraphAlgorithmController {

    private static Policy policy;
    private static AntiSamy as = new AntiSamy();
    @Autowired
    private GraphAlgorithmService graphAlgorithmService;

    static {
        try {
            Resource resource = new ClassPathResource("antisamy-anythinggoes.xml");
            InputStream in = resource.getInputStream();
            policy = Policy.getInstance(in);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @PostMapping("run/{kgName}/{id}")
    @ApiOperation("业务算法算法调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "form", value = "id"),
            @ApiImplicitParam(name = "graphBean", required = true, dataType = "string", paramType = "form", value = "graphBean"),
    })
    public ApiReturn<BusinessGraphRsp> executeAlgorithm(@PathVariable("kgName") String kgName,
                                                        @PathVariable("id") long id,
                                                        @Valid @RequestBody BusinessGraphRsp graphBean) {
        BusinessGraphRsp businessGraphRsp = graphAlgorithmService.run(kgName, id, graphBean);
        String message = businessGraphRsp.getMessage();
        if (StringUtils.isEmpty(message)) {
            return ApiReturn.success(businessGraphRsp);
        }
        try {
            message = as.scan(message, policy).getCleanHTML();
            message = message.replaceAll("\\&quot;", "\"");
            businessGraphRsp.setMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw BizException.of(AppErrorCodeEnum.ALGORITHM_EXECUTE_ERROR);
        }
        return ApiReturn.success(businessGraphRsp);
    }

    @PostMapping("statistics/run/{kgName}/{id}")
    @ApiOperation("统计类业务算法算法调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "long", paramType = "form", value = "id"),
            @ApiImplicitParam(name = "graphBean", required = true, dataType = "string", paramType = "form", value = "graphBean"),
    })
    public ApiReturn<AlgorithmStatisticeRsp> executeStatisticsAlgorithm(@PathVariable("kgName") String kgName,
                                                                        @PathVariable("id") long id,
                                                                        @Valid @RequestBody BusinessGraphRsp graphBean) {
        AlgorithmStatisticeRsp algorithmStatisticeRsp = graphAlgorithmService.runStatistics(kgName, id, graphBean);
        return ApiReturn.success(algorithmStatisticeRsp);
    }
}
