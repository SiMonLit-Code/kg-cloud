package com.plantdata.kgcloud.constant;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 16:26
 **/
public class KgmsConstants {

    public interface FileType {
        String XLS = ".xls";
        String XLSX = ".xlsx";
        String JSON = ".json";
    }

    public static final String LOG_DB_PREFIX = "log_";
    public static final String LOG_SERVICE_TB = "service_log";
    public static final String LOG_DATA_TB = "data_log";
    //多模态数据表名称
    public static final String MULTI_MODAL = "multi_modal";
}
