package com.plantdata.kgcloud.domain.menu.rsp;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Data
public class MenuFavorRsp {
    private String userId;
    private List<Integer> menuId;
    private Date createAt;
    private Date updateAt;
}
