package com.plantdata.kgcloud.domain.dataset.provider;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.config.MongoProperties;
import com.plantdata.kgcloud.domain.dataset.entity.DataSet;
import com.plantdata.kgcloud.domain.dw.entity.DWDatabase;
import com.plantdata.kgcloud.domain.dw.entity.DWTable;
import com.plantdata.kgcloud.domain.dw.rsp.DWDatabaseRsp;
import com.plantdata.kgcloud.sdk.constant.DataType;
import jodd.util.ArraysUtil;
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

    public static DataOptConnect of(DWDatabaseRsp database, DWTable table, MongoProperties mongoProperties) {
        DataOptConnect connect = new DataOptConnect();


        //远程表
        if(table.getCreateWay().equals(1)){
            if(table.getIsWriteDW() != null && table.getIsWriteDW().equals(1)){

                //远程落地表
                connect.setAddresses(Lists.newArrayList(mongoProperties.getAddrs()));
                connect.setUsername(mongoProperties.getUsername());
                connect.setPassword(mongoProperties.getPassword());
                connect.setTable(table.getTableName());
                connect.setDatabase(database.getDataName());
                connect.setDataType(DataType.MONGO);

            }else{
                //远程不落地表
                connect.setAddresses(database.getAddr());
                connect.setUsername(database.getUsername());
                connect.setPassword(database.getPassword());
                connect.setTable(table.getTbName());
                connect.setDatabase(database.getDbName());
                connect.setDataType(DataType.findType(database.getDataType()));
            }


        }else{
            //本地表
            connect.setAddresses(Lists.newArrayList(mongoProperties.getAddrs()));
            connect.setUsername(mongoProperties.getUsername());
            connect.setPassword(mongoProperties.getPassword());
            connect.setTable(table.getTableName());
            connect.setDatabase(database.getDataName());
            connect.setDataType(DataType.MONGO);

        }

        return connect;
    }
}
