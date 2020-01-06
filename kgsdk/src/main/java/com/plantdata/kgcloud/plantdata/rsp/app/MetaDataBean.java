package com.plantdata.kgcloud.plantdata.rsp.app;

import com.google.gson.reflect.TypeToken;
import com.hiekn.parser.util.JsonUtils;
import com.plantdata.kgcloud.plantdata.bean.EntityLink;
import com.plantdata.kgcloud.plantdata.constant.MetaDataEnum;
import com.plantdata.kgcloud.plantdata.req.common.Tag;
import com.plantdata.kgcloud.plantdata.rsp.common.AdditionalNull;
import com.plantdata.kgcloud.plantdata.rsp.schema.Additional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MetaDataBean extends HashMap<String, Object> {


    public String getCreationTime() {
        return getOrDefault(MetaDataEnum.creationTime.getValue(), "").toString();
    }

    public Double getScore() {
        String meta_data_3 = getOrDefault(MetaDataEnum.score.getValue(), "0.0").toString();
        try {
            return Double.parseDouble(meta_data_3);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public List<Tag> getTag() {
        String meta_data_4 = JsonUtils.toJson(getOrDefault(MetaDataEnum.tag.getValue(), "[]"));
        try {
            List<Tag> tags = JsonUtils.fromJson(meta_data_4, new TypeToken<List<Tag>>() {
            }.getType());
            if (tags == null) {
                return new ArrayList<>(0);
            }
            return tags;
        } catch (Exception e) {
            return new ArrayList<>(0);
        }
    }

    public String getSource() {
        return getOrDefault(MetaDataEnum.source.getValue(), "").toString();
    }

    public Double getReliability() {
        String meta_data_12 = getOrDefault(MetaDataEnum.reliability.getValue(), "0.0").toString();
        try {
            return Double.parseDouble(meta_data_12);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public String getBatch() {
        return getOrDefault(MetaDataEnum.batch.getValue(), "").toString();
    }


    public Additional getAdditional() {
        String meta_data_14 = JsonUtils.toJson(getOrDefault(MetaDataEnum.additional.getValue(), ""));
        try {
            Additional additional = JsonUtils.fromJson(meta_data_14, new TypeToken<Additional>() {
            }.getType());
            if (additional == null) {
                return AdditionalNull.newAdditionalNull();
            }
            return additional;
        } catch (Exception e) {
            return AdditionalNull.newAdditionalNull();
        }
    }

    public List<Double> getGisCoordinate() {
        String meta_data_15 = JsonUtils.toJson(getOrDefault(MetaDataEnum.gisCoordinate.getValue(), ""));
        try {
            List<Double> additional = JsonUtils.fromJson(meta_data_15, new TypeToken<List<Double>>() {
            }.getType());
            if (additional == null) {
                return new ArrayList<>();
            }
            return additional;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public String getGisAddress() {
        return getOrDefault(MetaDataEnum.gisAddress.getValue(), "").toString();
    }

    public Boolean getOpenGis() {
        String meta_data_17 = getOrDefault(MetaDataEnum.openGis.getValue(), "false").toString();
        try {
            return Boolean.parseBoolean(meta_data_17);
        } catch (Exception e) {
            return false;
        }
    }


    public Set<EntityLink> getEntityLink() {
        String meta_data_18 = JsonUtils.toJson(getOrDefault(MetaDataEnum.entityLink.getValue(), "[]"));
        try {
            return JsonUtils.fromJson(meta_data_18, new TypeToken<Set<EntityLink>>() {
            }.getType());
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    public String getFromTime() {
        return getOrDefault(MetaDataEnum.fromTime.getValue(), "").toString();
    }

    public String getToTime() {
        return getOrDefault(MetaDataEnum.toTime.getValue(), "").toString();
    }


    public String getSourceReason() {
        return getOrDefault(MetaDataEnum.sourceReason.getValue(), "").toString();
    }

}
