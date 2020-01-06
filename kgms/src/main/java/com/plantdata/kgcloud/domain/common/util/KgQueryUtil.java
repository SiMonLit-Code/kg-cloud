package com.plantdata.kgcloud.domain.common.util;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

/**
 * @author xiezhenxiang 2019/12/12
 */
public class KgQueryUtil {

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
