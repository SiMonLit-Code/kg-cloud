package com.plantdata.kgcloud.domain.repo.model;

import com.plantdata.kgcloud.domain.repo.enums.HandleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Object handler(Object obj) {
        return toString() + ";param" + obj.toString();
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
