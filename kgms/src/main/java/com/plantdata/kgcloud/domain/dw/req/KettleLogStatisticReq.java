package com.plantdata.kgcloud.domain.dw.req;

import com.plantdata.kgcloud.constant.KettleLogStatisticTypeEnum;
import com.plantdata.kgcloud.constant.KettleLogTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @Description
 * @data 2020-03-29 9:56
 **/
@Getter
@Setter
@ApiModel("Kettle日志统计请求参数")
public class KettleLogStatisticReq {

    @NotEmpty
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @ApiModelProperty(value = "开始日期", required = true)
    private Date startDate;
    @NotNull
    @ApiModelProperty(value = "统计类型 默认 DAY 按统计，HOUR按小时 MONTH 按月份")
    private KettleLogStatisticTypeEnum statisticType=KettleLogStatisticTypeEnum.DAY;
    @NotEmpty
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @ApiModelProperty(value = "结束日期", required = true)
    private Date endDate;
    @ApiModelProperty(value = "表名", required = true)
    @NotNull
    private List<String> tableName;
    @NotNull
    @ApiModelProperty(value = "是否只显示正确数据 默认ALL显示全部 SUCCESS正确数据 ERROR 错误数据 ")
    private KettleLogTypeEnum dataType = KettleLogTypeEnum.ALL;
}
