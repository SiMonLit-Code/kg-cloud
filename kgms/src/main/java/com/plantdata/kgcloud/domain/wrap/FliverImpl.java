//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.plantdata.kgcloud.domain.wrap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.hiekn.wrapper.service.Fliver;
import com.hiekn.wrapper.service.FliverContext;
import com.hiekn.wrapper.util.StringUtils;
import com.hiekn.wrapper.util.TimeUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.util.JSON;
import com.plantdata.kgcloud.domain.app.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class FliverImpl implements Fliver {
    static final String FILTER_TYPE = "sfe4j.filter";
    static final String FINDER_TYPE = "sfe4j.finder";
    static final String REPLACER_TYPE = "sfe4j.replacer";
    static final String SUBSTRING_TYPE = "sfe4j.substring";
    static final String TIME_TYPE = "sfe4j.time";
    static final String COMPLETE_TYPE = "sfe4j.complete";
    @Autowired
    private FliverContext context;

    public FliverImpl() {
    }

    @Override
    public final List<Map<String, Object>> clean(List<Map<String, Object>> odata, String cleanTaskJson) {
        LOGGER.info("fliver clean ... init. size ... " + odata.size());
        Optional<JsonNode> jsonNodeOpt = JsonUtils.parseJsonNode(cleanTaskJson);

        ArrayNode cleanTask = (ArrayNode) jsonNodeOpt.get();
        Iterator var7 = odata.iterator();

        while (var7.hasNext()) {
            Map<String, Object> data;
            data = (Map) var7.next();
            int i = 0;

            for (int len = cleanTask.size(); i < len; ++i) {
                JsonNode task = cleanTask.get(i);
                String field = task.get("field").asText();
                String formatter = task.get("formatter").asText();
                String fv = (String) ((String) (null == data.get(field) ? "" : data.get(field)));
                fv = this.doFormat(fv, formatter);
                data.put(field, fv);
            }
        }

        LOGGER.info("fliver clean ... done. size ... " + odata.size());
        return odata;
    }

    @Override
    public final String doFormat(String src, String mixs) {
        if (StringUtils.isNullOrEmpty(mixs)) {
            return src;
        } else {
            JsonNode mixin = JsonUtils.parseJsonNode(mixs).orElse(JsonNodeFactory.instance.objectNode());
            ArrayNode defaults = (ArrayNode) mixin.get("defaults");
            ArrayNode customs = (ArrayNode) mixin.get("customs");
            int i = 0;

            for (int len = defaults.size(); i < len; ++i) {
                String fno = defaults.get(i).asText();
                JsonNode json = JsonUtils.parseJsonNode(this.context.findFormatter(fno)).orElse(JsonNodeFactory.instance.objectNode());
                if (fno.startsWith("1")) {
                    src = doFilter(src, json.get("regex").asText(), json.get("useMulti").asBoolean());
                } else if (fno.startsWith("2")) {
                    src = doFind(src, json.get("regex").asText(), json.get("group").asInt(), json.get("useMulti").asBoolean(), json.get("multiSeparator").asText());
                } else if (fno.startsWith("3")) {
                    src = doReplace(src, json.get("regex").asText(), json.get("replacement").asText(), json.get("useMulti").asBoolean());
                } else if (fno.startsWith("4")) {
                    src = doSubstring(src, json.get("beginIndex").asInt(), json.get("endIndex").asInt(), json.get("beginStr").asText(), json.get("includeBeginStr").asBoolean(), json.get("useLastBeginStr").asBoolean(), json.get("endStr").asText(), json.get("includeEndStr").asBoolean(), json.get("useLastEndStr").asBoolean());
                } else if (fno.startsWith("5")) {
                    src = this.doTimeFormat(src, json.get("timeType").asText());
                }
            }

            i = 0;

            for (int len = customs.size(); i < len; ++i) {
                JsonNode json = customs.get(i);
                String type = json.get("type").asText();
                String regex = json.get("regex").asText();
                boolean useMulti = json.get("useMulti").asBoolean();
                byte var13 = -1;
                switch (type.hashCode()) {
                    case -1321599349:
                        if (type.equals("sfe4j.substring")) {
                            var13 = 3;
                        }
                        break;
                    case 1115489875:
                        if (type.equals("sfe4j.time")) {
                            var13 = 4;
                        }
                        break;
                    case 1834660287:
                        if (type.equals("sfe4j.complete")) {
                            var13 = 5;
                        }
                        break;
                    case 2003772612:
                        if (type.equals("sfe4j.replacer")) {
                            var13 = 2;
                        }
                        break;
                    case 2138092926:
                        if (type.equals("sfe4j.filter")) {
                            var13 = 0;
                        }
                        break;
                    case 2138137132:
                        if (type.equals("sfe4j.finder")) {
                            var13 = 1;
                        }
                }

                switch (var13) {
                    case 0:
                        src = doFilter(src, regex, useMulti);
                        break;
                    case 1:
                        String multiSeparator = null == getStringVal(json, "multiSeparator") ? ";" : getStringVal(json, "multiSeparator");
                        int group = getIntVal(json, "group");
                        src = doFind(src, regex, group, useMulti, multiSeparator);
                        break;
                    case 2:
                        String replacement = null == getStringVal(json, "replacement") ? "" : getStringVal(json, "replacement");
                        src = doReplace(src, regex, replacement, useMulti);
                        break;
                    case 3:
                        src = doSubstring(src, getIntVal(json, "beginIndex"), getIntVal(json, "endIndex"), getStringVal(json, "beginStr"), getBoolVal(json, "includeBeginStr"), getBoolVal(json, "useLastBeginStr"), getStringVal(json, "endStr"), getBoolVal(json, "includeEndStr"), getBoolVal(json, "useLastEndStr"));
                        break;
                    case 4:
                        src = this.doTimeFormat(src, getStringVal(json, "timeType"));
                        break;
                    case 5:
                        src = doComplete(src, getStringVal(json, "prefix"), getStringVal(json, "prefixStd"), getStringVal(json, "suffix"), getStringVal(json, "suffixStd"));
                }
            }

            return src;
        }
    }

    private String getStringVal(JsonNode jsonNode, String field) {
        return jsonNode.get(field).asText();
    }

    private Integer getIntVal(JsonNode jsonNode, String field) {
        return jsonNode.get(field).asInt();
    }

    private Boolean getBoolVal(JsonNode jsonNode, String field) {
        return jsonNode.get(field).asBoolean();
    }

    public static final String doFilter(String src, String regex, boolean useMulti) {
        return useMulti ? src.replaceAll(regex, "") : src.replaceFirst(regex, "");
    }

    public static final String doFind(String src, String regex, int group, boolean useMulti, String multiSeparator) {
        if (group < 0) {
            group = 0;
        }

        Matcher matcher = Pattern.compile(regex).matcher(src);
        if (!useMulti) {
            return matcher.find() ? matcher.group(group) : "";
        } else {
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isNullOrEmpty(multiSeparator)) {
                multiSeparator = ";";
            }

            while (matcher.find()) {
                sb.append(matcher.group(group)).append(multiSeparator);
            }

            return sb.length() > 0 ? sb.substring(0, sb.lastIndexOf(multiSeparator)) : "";
        }
    }

    public static final String doReplace(String src, String regex, String replacement, boolean useMulti) {
        return useMulti ? src.replaceAll(regex, replacement) : src.replaceFirst(regex, replacement);
    }

    public static final String doSubstring(String src, int beginIndex, int endIndex, String beginStr, boolean includeBeginStr, boolean useLastBeginStr, String endStr, boolean includeEndStr, boolean useLastEndStr) {
        if (!StringUtils.isNullOrEmpty(beginStr)) {
            if (useLastBeginStr) {
                beginIndex = includeBeginStr ? src.lastIndexOf(beginStr) : src.lastIndexOf(beginStr) + beginStr.length();
            } else {
                beginIndex = includeBeginStr ? src.indexOf(beginStr) : src.indexOf(beginStr) + beginStr.length();
            }
        }

        if (!StringUtils.isNullOrEmpty(endStr)) {
            if (useLastEndStr) {
                endIndex = includeEndStr ? src.lastIndexOf(endStr) + endStr.length() : src.lastIndexOf(endStr);
            } else {
                endIndex = includeEndStr ? src.indexOf(endStr) + endStr.length() : src.indexOf(endStr);
            }
        }

        if (beginIndex < 0) {
            beginIndex = 0;
        }

        if (endIndex < 0) {
            endIndex += src.length();
        }

        if (endIndex < 1) {
            endIndex = src.length();
        }

        if (beginIndex > endIndex) {
            throw new RuntimeException("!!!字符串截取，起始位置不能大于结束位置!!!");
        } else {
            return src.substring(beginIndex, endIndex);
        }
    }

    public static final String doComplete(String src, String prefix, String prefixStd, String suffix, String suffixStd) {
        if (!StringUtils.isNullOrEmpty(prefix)) {
            if (!StringUtils.isNullOrEmpty(prefixStd)) {
                if (!src.startsWith(prefixStd)) {
                    src = prefix + src;
                }
            } else {
                src = prefix + src;
            }
        }

        if (!StringUtils.isNullOrEmpty(suffix)) {
            if (!StringUtils.isNullOrEmpty(suffixStd)) {
                if (!src.endsWith(suffixStd)) {
                    src = src + suffix;
                }
            } else {
                src = src + suffix;
            }
        }

        return src;
    }

    @Override
    public final String doTimeFormat(String src, String timeType) {
        String resultTime = "";
        TimeUtils timeUtils = new TimeUtils();
        if ("longTime".equalsIgnoreCase(timeType)) {
            resultTime = timeUtils.formatLongTime(src);
        } else {
            resultTime = timeUtils.formatStringTime(src);
        }

        return resultTime;
    }
}
