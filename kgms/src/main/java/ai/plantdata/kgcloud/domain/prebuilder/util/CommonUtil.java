package ai.plantdata.kgcloud.domain.prebuilder.util;

import com.google.common.collect.Maps;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 将属性 meta 过滤转成Mongo查询语法
 *
 * @author DingHao
 * @since 2019/9/24 10:10
 */
public abstract class CommonUtil {

    public static final String EDGE_ATTR_PREFIX = "attr_ext_";

    private static final ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static final ThreadLocal<SimpleDateFormat> sdfDate = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    private static final ThreadLocal<SimpleDateFormat> sdfTime = ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss"));

    public static String formatNowDate() {
        return sdf.get().format(new Date());
    }

    public static String formatDate(Date date) {
        return sdfDate.get().format(date);
    }

    public static String formatDateTime(Date date) {
        return sdf.get().format(date);
    }

    public static String formatTime(Date date) {
        return sdfTime.get().format(date);
    }

    public static Date parseDate(String date) {
        try {
            return sdfDate.get().parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }
    public static Date parseDateTime(String date) {
        try {
            return sdf.get().parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }


    public static Map<Integer, Object> parseEdgeNumericAttr(Map<String, Object> result) {
        Map<Integer, Object> adf = null;
        for (Map.Entry<String, Object> entry : result.entrySet()) {//处理边数值属性值
            String filed = entry.getKey();
            if (filed.startsWith(EDGE_ATTR_PREFIX)) {
                if (adf == null) {
                    adf = Maps.newHashMapWithExpectedSize(4);
                }
                String[] s = filed.split("_");
                adf.put(Integer.parseInt(s[3]), entry.getValue());
            }
        }
        return adf;
    }

    public static Map<String, Object> parseEdgeCondition(Map<Integer, Map<Integer, Object>> edgeAttr) {
        Map<String, Object> con = Maps.newHashMap();
        for (Map.Entry<Integer, Map<Integer, Object>> entry : edgeAttr.entrySet()) {
            Integer attrId = entry.getKey();
            entry.getValue().forEach((k,v) -> con.put(EDGE_ATTR_PREFIX+attrId+"_"+k,v));
        }
        return con;
    }

    public static <T> List<T> removeDuplicate(List<T> ids) {
        if(ids == null){
            return null;
        }
        return new ArrayList<>(new HashSet<>(ids));
    }

    /**
     * 比较两个字符串的 equals,空格 tab 都视为相等
     * @param s1
     * @param s2
     * @return
     */
    public static boolean equals(String s1,String s2){
        s1 = StringUtils.hasText(s1) ? s1 : null;
        s2 = StringUtils.hasText(s2) ? s2 : null;
        return Objects.equals(s1,s2);
    }

    /**
     * 检查值是否存在更新
     * @param newValue 要更新的值
     * @param oldValue 原值
     * @return if newValue is null false
     */
    public static boolean hasUpdate(Object newValue,Object oldValue){
        if(newValue == null){
            return false;
        }
        return !newValue.equals(oldValue);
    }

    public static Set<String> buildNameAlias(String name , String alias){
        Set<String> needCheckList = new LinkedHashSet<>(4);
        if(StringUtils.hasText(alias)){
            String[] s = alias.split("\n");
            needCheckList.addAll(Arrays.asList(s));
        }
        if(StringUtils.hasText(name)) {
            needCheckList.add(name);
        }
        return needCheckList;
    }

}
