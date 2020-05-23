package com.plantdata.kgcloud.domain.dataset.provider;

import com.plantdata.kgcloud.sdk.constant.DataType;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public class DataOptProviderFactory {

    public static DataOptProvider createProvider(DataOptConnect info, DataType dataType) {

        DataOptProvider provider;
        switch (dataType) {
            case MYSQL:
                provider = new NopProvider();
                break;
            case MONGO:
                provider = new MongodbOptProvider(info);
                break;
            case ELASTIC:
                provider = new ElasticSearchOptProvider(info);
                break;
            case PD_DOCUMENT:
                provider = new PdDocumentOptProvider(info);
                break;
            default:
                provider = new NopProvider();
                break;
        }
        return provider;
    }

    public static DataOptProvider createProvider(DataOptConnect info) {

        DataOptProvider provider;
        switch (info.getDataType()) {
            case MYSQL:
                provider = new MysqlOptProvider(info);
                break;
            case MONGO:
                provider = new MongodbOptProvider(info);
                break;
            case ELASTIC:
                provider = new ElasticSearchOptProvider(info);
                break;
            case PD_DOCUMENT:
                provider = new PdDocumentOptProvider(info);
                break;
            case ORACLE:
                provider = new OracleOptProvider(info);
                break;
            default:
                provider = new NopProvider();
                break;
        }
        return provider;
    }
}
