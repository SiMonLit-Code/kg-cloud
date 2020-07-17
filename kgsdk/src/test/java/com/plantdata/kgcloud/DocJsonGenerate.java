package com.plantdata.kgcloud;

import ai.plantdata.cloud.util.JacksonUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;

/**
 * @author cjw
 */
@Slf4j
public class DocJsonGenerate {
    private static final List<String> PACKAGE_NAMES = Lists.newArrayList(
            "semantic",
            "app.controller",
            "data.obtain.controller",
            "data.statistics.controller",
            "dataset.obtain",
            "dataset.statistic",
            "nlp",
            "structure.analysis"
    );

    public static void main(String[] args) {


        WriteUtils writeUtils1 = new WriteUtils();
        writeUtils1.appendWriteFile("class_dir.json",
                JacksonUtils.writeValueAsString(classDirMap()), false);
        writeUtils1.close();

        WriteUtils writeUtils2 = new WriteUtils();
        writeUtils2.appendWriteFile("class_dir.json",
                JacksonUtils.writeValueAsString(classDirMap()), false);
        writeUtils2.close();
    }

    private static Map<String, String> classDirMap() {
        Map<String, String> classDirMap = Maps.newHashMap();
        for (String name : PACKAGE_NAMES) {
            List<Class<?>> classFromPackage = ClassUtils.getClassFromPackage("com.plantdata.kgcloud.domain." + name);
            String dirName = name;
            String tag = null;

            for (Class<?> clazz : classFromPackage) {
                if (clazz.getInterfaces().length == 0) {
                    continue;
                }
                Class<?> parent = clazz.getInterfaces()[0];
                Api tagAnnotation = parent.getAnnotation(Api.class);
                tag = tagAnnotation.tags()[0];
                if (tag == null) {
                    continue;
                }
                if (name.endsWith(".controller")) {
                    dirName = name.replace(".controller", "");
                }
                dirName = dirName.replace(".", "_");
            }
            classDirMap.put(tag, dirName);
        }
        return classDirMap;
    }

//    private static Map<String, String> reqMap() {
//
//        List<Method> methods = PACKAGE_NAMES.stream()
//                .map(a -> ClassUtils.getClassFromPackage("com.plantdata.kgcloud.domain." + a))
//                .flatMap(Collection::stream)
//                .map(b -> Arrays.asList(b.getDeclaredMethods()))
//                .flatMap(Collection::stream)
//                .collect(Collectors.toList());
//
//        Map<String, Object> queryParamMap = Maps.newHashMap();
//        Map<String, Object> pathParamMap = Maps.newHashMap();
//        Map<String, Object> bodyMap = Maps.newHashMap();
//        for (Method method : methods) {
//            Parameter[] parameters = method.getParameters();
//            for (Parameter param : parameters) {
//                RequestParam query = param.getAnnotation(RequestParam.class);
//                if (query != null) {
//                    queryParamMap.put(query.name(), query.defaultValue());
//                }
//                RequestBody body = param.getAnnotation(RequestBody.class);
//                if (body != null) {
//                    getStr(param.getType());
//                }
//                PathVariable path = param.getAnnotation(PathVariable.class);
//                if (path != null) {
//                    pathParamMap.put(path.name(), 1);
//                }
//            }
//            Class<?> returnType = method.getReturnType();
//            getStr(returnType);
//        }
//        return null;
//    }
//
//    private static void getStr(Class<?> type) {
//        try {
//            log.error(JacksonUtils.writeValueAsString(type.newInstance()));
//        } catch (InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }

}

