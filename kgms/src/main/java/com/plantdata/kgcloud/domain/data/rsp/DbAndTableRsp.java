package com.plantdata.kgcloud.domain.data.rsp;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2020/3/25 15:37
 * @Description:
 */
@Setter
@Getter
public class DbAndTableRsp {

    private String dbName;

    private String dbTitle;

    private List<String> dbTable;
}
