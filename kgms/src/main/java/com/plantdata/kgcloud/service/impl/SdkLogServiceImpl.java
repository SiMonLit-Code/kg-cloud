package com.plantdata.kgcloud.service.impl;

import com.plantdata.kgcloud.service.SdkLogService;
import org.springframework.stereotype.Service;

/**
 * describe about this class
 *
 * @author DingHao
 * @date 2019/2/28 11:22
 */
@Service
public class SdkLogServiceImpl implements SdkLogService {

//    @Autowired
//    private SdkLogMapper sdkLogMapper;
//
//    @Autowired
//    private GraphConfService myGraphService;
//
//    @Override
//    public List<SdkLogTopVo> top(SdkLogDTO sdkLogDTO, Integer order) {
//        setInvokeTime(sdkLogDTO);
//        return sdkLogMapper.top(sdkLogDTO, order);
//    }
//
//    @Override
//    public SdkLogGroupUrlVO groupUrl(SdkLogDTO sdkLogDTO ,Integer groupType) {
//        setInvokeTime(sdkLogDTO);
//        List<SdkLogTopVo> sdkLogTopVos = sdkLogMapper.groupUrl(sdkLogDTO, groupType+"");
//        Map<String, List<SdkLogTopVo>> rs = sdkLogTopVos.stream().collect(Collectors.groupingBy(SdkLogTopVo::getTime));
//        List<String> times = Lists.newArrayList(rs.keySet());
//        Collections.sort(times);
//        Map<String, List<SdkLogTopVo>> collect = sdkLogTopVos.stream().collect(Collectors.groupingBy(SdkLogTopVo::getName));
//
//        List<SdkLogGroupUrlVO.VerticalData> vertical = Lists.newArrayList();
//        SdkLogGroupUrlVO sdkLogGroupUrlVO = new SdkLogGroupUrlVO(times,vertical);
//
//        collect.forEach((url,sdkLogTopVoList) -> {
//            SdkLogGroupUrlVO.VerticalData m = new SdkLogGroupUrlVO.VerticalData();
//            m.setName(url);
//            List<Integer> y = Lists.newArrayList();
//            for (String time : times) {
//                int i = 0;
//                for (SdkLogTopVo sdkLogTopVo : sdkLogTopVoList) {
//                    if(sdkLogTopVo.getTime().equals(time)){
//                        y.add(sdkLogTopVo.getValue());
//                        break;
//                    }
//                    i++;
//                }
//                if(i == sdkLogTopVoList.size()){
//                    y.add(0);
//                }
//            }
//            m.setVal(y);
//            vertical.add(m);
//        });
//
//        return sdkLogGroupUrlVO;
//    }
//
//    @Override
//    public List<SdkLogVo> groupKgName(SdkLogDTO sdkLogDTO) {
//        setInvokeTime(sdkLogDTO);
//        return sdkLogMapper.groupKgName(sdkLogDTO);
//    }
//
//    @Override
//    public List<SdkLogVo> groupPage(SdkLogDTO sdkLogDTO) {
//        setInvokeTime(sdkLogDTO);
//        return sdkLogMapper.groupPage(sdkLogDTO);
//    }
//
//    @Override
//    public List<MyGraphBean> allKgName() {
//        return myGraphService.selectAllGraphName();
//    }
//
//    private void  setInvokeTime(SdkLogDTO sdkLogDTO){
//        LocalDate today = LocalDate.now();
//        if(StringUtils.isBlank(sdkLogDTO.getFrom())){
//            sdkLogDTO.setFrom(today.with(TemporalAdjusters.firstDayOfMonth()).toString()+" 00:00:00");
//        }
//        if(StringUtils.isBlank(sdkLogDTO.getTo())){
//            sdkLogDTO.setTo(today.with(TemporalAdjusters.firstDayOfNextMonth()).toString()+" 00:00:00");
//        }
//    }

}
