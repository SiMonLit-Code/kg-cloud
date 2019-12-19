//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.plantdata.kgcloud.domain.wrap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hiekn.wrapper.bean.FieldConfigBean;
import com.hiekn.wrapper.service.Fliver;
import com.hiekn.wrapper.util.JsoupParserUtils;
import com.hiekn.wrapper.util.StringUtils;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StaticFetchKVActImpl implements StaticFetchKVAct {
    private final DateFormat RT_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final DateFormat RT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private FliverImpl fliver;

    public StaticFetchKVActImpl() {
    }


    @Override
    public Map<String, Object> execute(String html, List<FieldConfigBean> fieldConfig) {
        List<Map<String, Object>> result = this.execute((List) Lists.newArrayList(new String[]{html}), (List) fieldConfig);
        return (Map) (result.isEmpty() ? Maps.newHashMap() : (Map) this.execute((List) Lists.newArrayList(new String[]{html}), (List) fieldConfig).get(0));
    }

    public List<Map<String, Object>> execute(List<String> html, List<FieldConfigBean> fieldConfig) {
        List<Map<String, Object>> result = Lists.newArrayList();
        Iterator var4 = html.iterator();

        while (var4.hasNext()) {
            String s = (String) var4.next();
            Map<String, Object> m = Maps.newHashMap();
            Iterator var7 = fieldConfig.iterator();

            while (var7.hasNext()) {
                FieldConfigBean bean = (FieldConfigBean) var7.next();
                bean.setDependencySource(s);

                try {
                    m.putAll(this.execute(bean, this.fliver));
                } catch (Exception var10) {
                    LOGGER.error(var10);
                }
            }

            result.add(m);
        }

        return result;
    }

    public Map<String, Object> execute(FieldConfigBean bean, Fliver fliver) throws Exception {
        Object val = null;
        String dependencySource = bean.getDependencySource();
        Map<String, Object> resultMap = Maps.newHashMap();
        if (!StringUtils.isNullOrEmpty(dependencySource)) {
            Map<String, Object> formatter = bean.getFormatter();
            String valueType = bean.getByType();
            byte var8 = -1;
            switch (valueType.hashCode()) {
                case 94177836:
                    if (valueType.equals("byCss")) {
                        var8 = 0;
                    }
                    break;
                case 324014640:
                    if (valueType.equals("byRegex")) {
                        var8 = 1;
                    }
                    break;
                case 329878150:
                    if (valueType.equals("byXpath")) {
                        var8 = 2;
                    }
                    break;
                case 1034503458:
                    if (valueType.equals("byCommon")) {
                        var8 = 4;
                    }
                    break;
                case 2054688027:
                    if (valueType.equals("byConstant")) {
                        var8 = 3;
                    }
            }

            switch (var8) {
                case 0:
                    val = this.getValByJsoup(dependencySource, bean.getTypeValue(), bean.getByAttr(), formatter);
                    break;
                case 1:
                    int group = null == bean.getGroup() ? 0 : bean.getGroup();
                    val = this.getValByRegex(dependencySource, bean.getTypeValue(), group);
                    break;
                case 2:
                    val = this.getValByXpath(dependencySource, bean.getTypeValue(), bean.getByAttr(), formatter);
                    break;
                case 3:
                    val = this.getValByRuntime(bean.getTypeValue());
                case 4:
            }

            if (null != val && !StringUtils.isNullOrEmpty(val.toString())) {
                if (null != formatter && formatter.size() > 0) {
                    val = fliver.doFormat(val.toString(), JacksonUtils.writeValueAsString(formatter));
                }

                valueType = bean.getValueType();
                if (!StringUtils.isNullOrEmpty(valueType)) {
                    val = this.convert2ValType(val.toString(), valueType);
                }
            } else {
                val = null;
            }
        }

        resultMap.put(bean.getField(), val);
        return resultMap;
    }

    private Object convert2ValType(String val, String valueType) {
        Object convertedVal = val;
        byte var5 = -1;
        switch (valueType.hashCode()) {
            case -1325958191:
                if (valueType.equals("double")) {
                    var5 = 3;
                }
                break;
            case 3327612:
                if (valueType.equals("long")) {
                    var5 = 1;
                }
                break;
            case 97526364:
                if (valueType.equals("float")) {
                    var5 = 2;
                }
                break;
            case 1958052158:
                if (valueType.equals("integer")) {
                    var5 = 0;
                }
        }

        switch (var5) {
            case 0:
                try {
                    convertedVal = Integer.parseInt(val);
                } catch (NumberFormatException var10) {
                    convertedVal = -1;
                }
                break;
            case 1:
                try {
                    convertedVal = Long.parseLong(val);
                } catch (NumberFormatException var9) {
                    convertedVal = -1L;
                }
                break;
            case 2:
                try {
                    convertedVal = Float.parseFloat(val);
                } catch (NumberFormatException var8) {
                    convertedVal = -1.0F;
                }
                break;
            case 3:
                try {
                    convertedVal = Double.parseDouble(val);
                } catch (NumberFormatException var7) {
                    convertedVal = -1.0D;
                }
        }

        return convertedVal;
    }

    private Object getValByRuntime(String valRef) {

        long ms = System.currentTimeMillis();
        byte var6 = -1;
        switch (valRef.hashCode()) {
            case 38308:
                if (valRef.equals("$tt")) {
                    var6 = 0;
                }
                break;
            case 1187663:
                if (valRef.equals("$tts")) {
                    var6 = 1;
                }
                break;
            case 36322770:
                if (valRef.equals("$date")) {
                    var6 = 3;
                }
                break;
            case 36806897:
                if (valRef.equals("$time")) {
                    var6 = 2;
                }
        }

        Object refVal;
        switch (var6) {
            case 0:
                refVal = ms;
                break;
            case 1:
                refVal = ms / 1000L;
                break;
            case 2:
                refVal = this.RT_TIME_FORMAT.format(ms);
                break;
            case 3:
                refVal = this.RT_DATE_FORMAT.format(ms);
                break;
            default:
                refVal = valRef;
        }

        return refVal;
    }

    private String getValByRegex(String dependencySource, String typeValue, int group) {
        String val = "";
        Matcher matcher = Pattern.compile(typeValue).matcher(dependencySource);
        if (matcher.find()) {
            val = matcher.group(group);
        }

        return val;
    }

    private String getValByJsoup(String dependencySource, String typeValue, String byAttr, Map<String, Object> formatter) throws MalformedURLException {
        Element xe = Jsoup.parse(dependencySource).select(typeValue).first();
        return this.extractData(xe, byAttr);
    }

    private String getValByXpath(String dependencySource, String typeValue, String byAttr, Map<String, Object> formatter) throws MalformedURLException {
        Document doc = Jsoup.parse(dependencySource);
        Element xe = JsoupParserUtils.getJsoupElement(doc, typeValue);
        return this.extractData(xe, byAttr);
    }

    private String extractData(Element xe, String byAttr) {
        String val = "";
        if (null != xe) {
            byte var5 = -1;
            switch (byAttr.hashCode()) {
                case -1549184699:
                    if (byAttr.equals("tagName")) {
                        var5 = 4;
                    }
                    break;
                case -1055246893:
                    if (byAttr.equals("ownText")) {
                        var5 = 3;
                    }
                    break;
                case -528338175:
                    if (byAttr.equals("innerHTML")) {
                        var5 = 0;
                    }
                    break;
                case -527962973:
                    if (byAttr.equals("innerText")) {
                        var5 = 2;
                    }
                    break;
                case 3143036:
                    if (byAttr.equals("file")) {
                        var5 = 5;
                    }
                    break;
                case 100313435:
                    if (byAttr.equals("image")) {
                        var5 = 6;
                    }
                    break;
                case 1856662182:
                    if (byAttr.equals("outerHTML")) {
                        var5 = 1;
                    }
            }

            switch (var5) {
                case 0:
                    val = xe.html();
                    break;
                case 1:
                    val = xe.outerHtml();
                    break;
                case 2:
                    val = xe.text();
                    break;
                case 3:
                    val = xe.ownText();
                    break;
                case 4:
                    val = xe.tagName();
                case 5:
                case 6:
                    break;
                default:
                    val = xe.attr(byAttr);
            }
        }

        return val;
    }
}
