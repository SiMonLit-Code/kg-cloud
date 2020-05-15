package com.plantdata.kgcloud.domain.repo.model;

import com.plantdata.kgcloud.domain.repo.enums.HandleType;
import com.plantdata.kgcloud.domain.repo.model.req.DealDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Function;

/**
 * @author cjw
 * @date 2020/5/15  11:15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryHandler {
    private int id;
    private HandleType handleType;
    private String requestServerName;
    private int rank;
    private Function<DealDTO, DealDTO> d2rFunction;

    public DealDTO handler(DealDTO obj) {
        System.out.println(toString() + ";param" + obj.toString());
        return d2rFunction.apply(obj);
    }

    @Override
    public String toString() {
        return "RepositoryHandler{" +
                "id=" + id +
                ", handleType=" + handleType +
                ", requestServerName='" + requestServerName + '\'' +
                ", rank=" + rank +
                '}';
    }
}
