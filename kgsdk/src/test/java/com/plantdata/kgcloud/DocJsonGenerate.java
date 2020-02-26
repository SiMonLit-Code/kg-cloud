package com.plantdata.kgcloud;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.util.JacksonUtils;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
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
        WriteUtils writeUtils = new WriteUtils();
        writeUtils.appendWriteFile("class_dir.json",
                JacksonUtils.writeValueAsString(classDirMap), false);
        writeUtils.close();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class JsonClass {
        private List<String> prefix;
        private String dir;

        public JsonClass(List<String> prefix, String dir) {
            this.prefix = prefix;
            this.dir = dir;
        }
    }

}

