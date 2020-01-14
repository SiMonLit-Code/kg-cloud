package com.plantdata.kgcloud.domain.app.bo;

import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.domain.app.dto.StatisticDTO;
import com.plantdata.kgcloud.domain.app.dto.StatisticDateDTO;
import com.plantdata.kgcloud.domain.common.util.RemindDateUtils;
import com.plantdata.kgcloud.domain.edit.util.VeDateUtils;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.AttributeDataTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.statistic.DateTypeReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 10:59
 */
public class GraphAttributeStatisticBO {

    public static List<StatisticDTO> countMerge(List<StatisticDTO> dataList, AttributeDataTypeEnum type, int size) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }

        String min = dataList.get(0).getValue();
        String max = dataList.get(0).getValue();
        for (StatisticDTO resultsBean : dataList) {
            if (countType(max, resultsBean.getValue(), type) < 0) {
                max = resultsBean.getValue();
            }
            if (countType(min, resultsBean.getValue(), type) > 0) {
                min = resultsBean.getValue();
            }
        }
        List<Map<String, Object>> range;
        try {
            range = countRange(min, max, size, type);
        } catch (ParseException e) {
            throw BizException.of(AppErrorCodeEnum.DATE_PARSE_ERROR);
        }
        List<StatisticDTO> newResList = new ArrayList<>();
        range.forEach(s -> {
            StatisticDTO res = new StatisticDTO();
            res.setValue(s.get("min") + " ~ " + s.get("max"));
            res.setTotal(0L);
            res.setEntity(new ArrayList<>());
            res.setRelation(new ArrayList<>());
            newResList.add(res);
        });
        List<Map<String, Object>> finalRange = range;
        dataList.forEach(s -> addCountRange(finalRange, newResList, s, type));
        return newResList;
    }

    private static int countType(String a, String b, AttributeDataTypeEnum type) {
        if (StringUtils.isBlank(a) || StringUtils.isBlank(b)) {
            return 0;
        }
        //整型
        if (AttributeDataTypeEnum.INTEGER.equals(type)) {
            return Integer.valueOf(a).compareTo(Integer.valueOf(b));
            //浮点型
        } else if (AttributeDataTypeEnum.FLOAT.equals(type)) {
            return Double.valueOf(a).compareTo(Double.valueOf(b));
        } else {
            return a.compareTo(b);
        }
    }

    private static List<Map<String, Object>> countRange(String min, String max, Integer size, AttributeDataTypeEnum type) throws ParseException {
        List<Map<String, Object>> list = new ArrayList<>();
        Integer maxInt, minInt, division;
        switch (type) {
            case INTEGER:
                maxInt = Integer.valueOf(max);
                minInt = Integer.valueOf(min);
                division = (maxInt - minInt) / size;
                break;
            case FLOAT:
                maxInt = Double.valueOf(max).intValue() + 1;
                minInt = Double.valueOf(min).intValue();
                division = (maxInt - minInt) / size;
                break;
            default:
                //时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Long minDate = sdf.parse(min).getTime();
                Long maxDate = sdf.parse(max).getTime();
                Long divisionDate = (maxDate - minDate) / size;
                do {
                    Map<String, Object> one = new HashMap<>();
                    one.put("min", sdf.format(new Date(minDate)));
                    one.put("max", sdf.format(new Date((minDate + divisionDate))));
                    list.add(one);
                    minDate = minDate + divisionDate + 1;
                } while (minDate <= maxDate);
                return list;

        }
        do {
            Map<String, Object> one = new HashMap<>();
            one.put("min", minInt);
            one.put("max", minInt + division);
            list.add(one);
            minInt = minInt + division + 1;
        } while (minInt <= maxInt);
        return list;

    }

    private static void addCountRange(List<Map<String, Object>> range, List<StatisticDTO> retRes, StatisticDTO resultsBean, AttributeDataTypeEnum type) {
        for (int i = 0; i < retRes.size(); i++) {
            Map<String, Object> oneRange = range.get(i);
            if (countType(oneRange.get("max").toString(), resultsBean.getValue(), type) >= 0 && countType(oneRange.get("min").toString(), resultsBean.getValue(), type) <= 0) {
                StatisticDTO one = retRes.get(i);
                one.setTotal(one.getTotal() + resultsBean.getTotal());
                if (resultsBean.getEntity() != null) {
                    one.getEntity().addAll(resultsBean.getEntity());
                }
                if (resultsBean.getRelation() != null) {
                    one.getRelation().addAll(resultsBean.getRelation());
                }
                break;
            }
        }


    }

    public static List<StatisticDTO> countMergeDate(List<StatisticDTO> list, DateTypeReq dateTypeBean) {
        if (dateTypeBean == null) {
            return list;
        }
        Date maxTime = null;
        if (dateTypeBean.getType() == null || dateTypeBean.getType() == 0) {
            dateTypeBean.setType(1);
        }

        if (dateTypeBean.get$lte() != null) {
            maxTime = VeDateUtils.strToDateLong(dateTypeBean.get$lte());
        }
        Date minTime = null;
        if (dateTypeBean.get$gte() != null) {
            minTime = VeDateUtils.strToDateLong(dateTypeBean.get$gte());
        }
        Date finalMaxTime = maxTime;
        Date finalMinTime = minTime;
        List<StatisticDateDTO> dateList = list.stream().map(StatisticDateDTO::new).filter(s -> {
            if (s.getDateValue() == null) {
                return false;
            }
            if (finalMaxTime == null && finalMinTime == null) {
                return true;
            }
            if (finalMaxTime != null) {
                if (s.getDateValue().compareTo(finalMaxTime) > 0) {
                    return false;
                }
            }
            if (finalMinTime != null) {
                return s.getDateValue().compareTo(finalMinTime) >= 0;
            }
            return true;
        }).collect(Collectors.toList());
        Map<String, DateRang> dataMaps = new HashMap<>();
        dateList.forEach(s -> {
            Date firstTime = getFirstTime(s.getDateValue(), dateTypeBean.getType());
            Date endTime = getEndTime(s.getDateValue(), dateTypeBean.getType());
            String s1 = VeDateUtils.DateToLongString(firstTime);
            DateRang one;
            if (dataMaps.containsKey(s1)) {
                one = dataMaps.get(s1);
            } else {
                one = new DateRang(firstTime, endTime);
            }
            one.setTotal(one.getTotal() + s.getTotal());
            if (s.getEntity() != null) {
                one.getEntity().addAll(s.getEntity());
            }
            if (s.getRelation() != null) {
                one.getRelation().addAll(s.getRelation());
            }
            dataMaps.put(s1, one);
        });

        return dataMaps.values().stream().peek(s -> {
            switch (dateTypeBean.getType()) {
                case 1:
                    s.setName(VeDateUtils.dateToStrYear(s.min));
                    s.setValue(VeDateUtils.dateToStrYear(s.min));
                    break;
                case 2:
                    s.setName(VeDateUtils.dateToStrQuarter(s.min));
                    s.setValue(VeDateUtils.dateToStrQuarter(s.min));
                    break;
                case 3:
                    s.setName(VeDateUtils.dateToStrMean(s.min));
                    s.setValue(VeDateUtils.dateToStrMean(s.min));
                    break;
                case 4:
                    s.setName(VeDateUtils.dateToStr(s.min));
                    s.setValue(VeDateUtils.dateToStr(s.min));
                    break;
                case 5:
                    s.setName(VeDateUtils.dateToStrLongHH(s.min));
                    s.setValue(VeDateUtils.dateToStrLongHH(s.min));
                    break;
                case 6:
                    s.setName(VeDateUtils.dateToStrLongMM(s.min));
                    s.setValue(VeDateUtils.dateToStrLongMM(s.min));
                    break;
                default:
                    s.setName(VeDateUtils.dateToStrLong(s.min));
                    s.setValue(VeDateUtils.dateToStrLong(s.min));
                    break;
            }
        }).sorted(Comparator.comparing(DateRang::getValue))
                .collect(Collectors.toList());
    }


    private static class DateRang extends StatisticDTO {
        Date max;
        Date min;

        public DateRang(StatisticDTO resultsBean) {
            super(resultsBean);
            setTotal(0L);
            setEntity(new ArrayList<>());
            setRelation(new ArrayList<>());
        }

        public DateRang(Date min, Date max) {
            this.max = max;
            this.min = min;
            setTotal(0L);
            setEntity(new ArrayList<>());
            setRelation(new ArrayList<>());
        }


    }

    public static Date getFirstTime(Date time, Integer type) {
        RemindDateUtils remindDateUtils = new RemindDateUtils();

        if (type == 1) {
            return remindDateUtils.getCurrentYearStartTime(time);
        } else if (type == 2) {
            return remindDateUtils.getCurrentQuarterStartTime(time);
        } else if (type == 3) {
            return remindDateUtils.getCurrentMonthStartTime(time);
        } else if (type == 4) {
            return remindDateUtils.getCurrentDayStartTime(time);
        } else if (type == 5) {
            return remindDateUtils.getCurrentHHStartTime(time);
        } else if (type == 6) {
            return remindDateUtils.getCurrentMMStartTime(time);
        } else {
            return time;
        }

    }

    public static Date getEndTime(Date time, Integer type) {
        RemindDateUtils remindDateUtils = new RemindDateUtils();

        if (type == 1) {
            return remindDateUtils.getCurrentYearEndTime(time);
        } else if (type == 2) {
            return remindDateUtils.getCurrentQuarterEndTime(time);
        } else if (type == 3) {
            return remindDateUtils.getCurrentMonthEndTime(time);
        } else if (type == 4) {
            return remindDateUtils.getCurrentDayEndTime(time);
        } else if (type == 5) {
            return remindDateUtils.getCurrentHHEndTime(time);
        } else if (type == 6) {
            return remindDateUtils.getCurrentMMEndTime(time);
        } else {
            return time;
        }

    }

}
