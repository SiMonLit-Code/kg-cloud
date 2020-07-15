package ai.plantdata.kgcloud.domain.prebuilder.util;

import ai.plantdata.kgcloud.sdk.constant.SortTypeEnum;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-15 11:32
 **/
public class SortUtil {

    public static Sort buildSort(List<String> sorts){

        if(sorts == null || sorts.isEmpty()){
            return Sort.by(Sort.Direction.DESC,"createAt");
        }

        Sort sort = null;
        for(String s : sorts){
            String[] ss = s.split(":");
            if(ss.length != 2){
                continue;
            }

            if(ss[1].toLowerCase().equals(SortTypeEnum.DESC.getName())){
                if(sort == null){
                    sort = Sort.by(Sort.Direction.DESC,ss[0]);
                }else{
                    sort.and(Sort.by(Sort.Direction.DESC,ss[0]));
                }
            }else{
                if(sort == null){
                    sort = Sort.by(Sort.Direction.ASC,ss[0]);
                }else{
                    sort.and(Sort.by(Sort.Direction.ASC,ss[0]));
                }
            }
        }

        if(sort == null){
            sort = Sort.by(Sort.Direction.DESC,"createAt");
        }

        return sort;

    }
}
