package com.plantdata.kgcloud.domain.dataset.provider;

import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.sdk.constant.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataOptConnect {

    private List<String> addresses;
    private String username;
    private String password;
    private String database;
    private String table;
    private DataType dataType;

    public static DataOptConnect of(DataSet dataSet) {
        DataOptConnect connect = new DataOptConnect();
        connect.setAddresses(dataSet.getAddr());
        connect.setUsername(dataSet.getUsername());
        connect.setPassword(dataSet.getPassword());
        connect.setTable(dataSet.getTbName());
        connect.setDatabase(dataSet.getDbName());
        return connect;
    }

}
