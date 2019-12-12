package com.plantdata.kgcloud.domain.common.util;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

/**
 * @author xiezhenxiang 2019/12/12
 */
public class KgQueryUtil {

    public static String getKgDbName(MongoClient client, String kgName) {

        String kgDbName = null;
        MongoCursor<Document> cursor = client.getDatabase("kg_attribute_definition").getCollection("kg_db_name")
                .find(new Document("kg_name", kgName)).iterator();
        if (cursor.hasNext()) {
            kgDbName = cursor.next().getString("db_name");
        }
        return kgDbName;
    }

    public static String getEntityNameById(MongoClient client, String kgDbName, Long id) {

        String name = "";
        MongoCursor<Document> cursor = client.getDatabase(kgDbName).getCollection("basic_info")
                .find(new Document("_id", id)).iterator();
        if (cursor.hasNext()) {
            name = cursor.next().getString("name");
        }
        return name;
    }
}
