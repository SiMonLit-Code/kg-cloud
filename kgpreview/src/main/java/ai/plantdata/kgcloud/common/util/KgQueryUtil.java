package ai.plantdata.kgcloud.common.util;

import ai.plantdata.kgcloud.config.Constants;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Kg查询工具类
 * @author xiezhenxiang 2019/6/18
 */
public class KgQueryUtil {

    public static String getKgDbName(String kgName) {

        String sql = "select db_name from graph where kg_name = ?";
        JSONObject data = Constants.KG_MYSQL.findOne(sql, kgName);
        String kgDbName = null;
        if (!data.isEmpty()) {
            kgDbName = data.getString("db_name");
        }
        return kgDbName;
    }

    /**
     * 获取所有子概念Id
     * @param includeSelf 是否包括自身
     */
    public static HashSet<Long> getSonConceptIds(String kgDbName, boolean includeSelf, Long... conceptIds) {

        HashSet<Long> all = includeSelf ? new HashSet<>(Arrays.asList(conceptIds)) : new HashSet<>();

        for (Long id : conceptIds) {
            HashSet<Long> sons = new HashSet<>();
            getSonConceptIds(kgDbName, id, sons);
            all.addAll(sons);
        }
        return all;
    }

    private static void getSonConceptIds(String kgDbName, Long conceptId, HashSet<Long> sons) {

        MongoCursor<Document> cursor = Constants.KG_MONGO.find(kgDbName, "parent_son", Filters.eq("parent", conceptId));
        cursor.forEachRemaining(doc -> {
            Long son = doc.getLong("son");
            sons.add(son);
            getSonConceptIds(kgDbName, son, sons);
        });
    }
}