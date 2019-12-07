package com.plantdata.kgcloud.domain.document.rsp;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DirectoryRsp {

    private String text;
    private Integer level;
    private String serialNumber;
    private Integer index;
    private List<DirectoryRsp> children;

    public void setSerialNumber(List<DirectoryRsp> directoryList){

        if(directoryList == null){
            return ;
        }

        DirectoryRsp parent = null;
        Integer count = 1;
        for(DirectoryRsp directoryRsp : directoryList){
            if(directoryRsp.getLevel() < this.level){
                parent = directoryRsp;
                count=1;
            }else if(directoryRsp.getLevel() == this.level){
                count++;
            }
        }

        if(parent == null){
            this.serialNumber = count+"";
            return;
        }
        List<DirectoryRsp> childrens = parent.getChildren();
        if(childrens == null){
            childrens = Lists.newArrayList();
            parent.setChildren(childrens);
        }

        childrens.add(this);

        String parentSerialNum = parent.getSerialNumber();
        this.serialNumber = parentSerialNum+"."+count;
    }

}
