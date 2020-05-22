package com.plantdata.kgcloud.domain.repo.model;

import com.plantdata.kgcloud.domain.repo.enums.HandleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.*;

/**
 * @author cjw
 * @date 2020/5/15  11:15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RepositoryHandler {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "rank")
    private int rank;
    @Column(name = "repositoryId")
    private int repositoryId;
    @Column(name = "handleCondition")
    private String handleCondition;
    @Column(name = "handleType")
    private HandleType handleType;
    @Column(name = "requestServerName")
    private String requestServerName;
    @Column(name = "requestUrl")
    private String requestUrl;
    @Column(name = "remark")
    private String remark;
    @Column(name = "request_method")
    private RequestMethod requestMethod;

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
