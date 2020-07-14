package ai.plantdata.kgcloud.plantdata.bean;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ai.plantdata.kgcloud.plantdata.req.common.RelationInfoBean;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BusinessRelationBean {

    private String id;
    private Long from;
    private Long to;
    private Integer attId;
    private String attName;
    private List<String> startTime;
    private List<String> endTime;
    @JsonProperty("nRInfo")
    private List<RelationInfoBean> nRInfo;
    @JsonProperty("oRInfo")
    private List<RelationInfoBean> oRInfo;
    private Integer direction;
    private String creationTime;
    private Map<String, Object> origin;
    private Map<String, Object> style;

    public Map<String, Object> getOrigin() {
        return origin;
    }


    public void addStartTime(String startTime) {
        if (this.startTime == null) {
            this.startTime = new ArrayList<>();
        }
        this.startTime.add(startTime);
    }


    public void addEndTime(String endTime) {
        if (this.endTime == null) {
            this.endTime = new ArrayList<>();
        }
        this.endTime.add(endTime);
    }


    public void addrNumInfo(RelationInfoBean one) {
        if (this.nRInfo == null) {
            this.nRInfo = new ArrayList<>();
        }
        this.nRInfo.add(one);
    }

    public void addrObjectInfo(RelationInfoBean one) {
        if (this.oRInfo == null) {
            this.oRInfo = new ArrayList<>();
        }
        this.oRInfo.add(one);
    }


    public BusinessRelationBean(String id, Long from, Long to, Integer attId) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.attId = attId;
    }

    static class ModelValueSerializer implements ObjectSerializer {
        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
            if (object != null && !"".equals(object.toString())) {
                serializer.write(object);
            }
        }
    }

}
